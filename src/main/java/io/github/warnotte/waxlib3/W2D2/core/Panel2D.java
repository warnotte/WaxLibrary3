package io.github.warnotte.waxlib3.W2D2.core;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Panel2D extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Scene2D scene;
    private final SelectionModel selection = new SelectionModel();
    private double gridBase = 10.0; // world units between grid lines at zoom=1
    private Point lastMouse;
    private boolean panning = false;
    private Rectangle2D selRectScreen = null;
    private Point selStart;

    public Panel2D(Scene2D scene) {
        this.scene = scene;
        setBackground(Color.WHITE);
        setFocusable(true);
        installInput();
    }

    private void installInput() {
        addMouseWheelListener((MouseWheelEvent e) -> {
            // Zoom around mouse with exact scroll solve (no axis inversion)
            java.awt.Dimension vp = getSize();
            java.awt.geom.AffineTransform before = scene.camera().buildViewTransform(vp);
            java.awt.geom.Point2D screenPt = new java.awt.geom.Point2D.Double(e.getX(), e.getY());
            java.awt.geom.AffineTransform invBefore;
            try { invBefore = before.createInverse(); } catch (Exception ex) { invBefore = new java.awt.geom.AffineTransform(); }
            java.awt.geom.Point2D worldAnchor = invBefore.transform(screenPt, null);

            double factor = Math.pow(1.1, -e.getWheelRotation());
            scene.camera().zoomBy(factor);

            // Solve scroll so that anchor maps to cursor: scroll = R^-1 * ((screen-center)/zoom) - world
            double angle = Math.toRadians(scene.camera().getRotationDeg());
            double cos = Math.cos(angle), sin = Math.sin(angle);
            double cx = vp.width / 2.0, cy = vp.height / 2.0;
            double zx = (screenPt.getX() - cx) / scene.camera().getZoom();
            double zy = (screenPt.getY() - cy) / scene.camera().getZoom();
            double rx =  cos * zx + sin * zy;
            double ry = -sin * zx + cos * zy;
            double scrollX = rx - worldAnchor.getX();
            double scrollY = ry - worldAnchor.getY();
            scene.camera().setScroll(scrollX, scrollY);
            repaint();
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                lastMouse = e.getPoint();
                if (e.getButton() == MouseEvent.BUTTON2) {
                    panning = true;
                } else if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) {
                    selStart = e.getPoint();
                    selRectScreen = new Rectangle2D.Double(selStart.x, selStart.y, 0, 0);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    Node2D hit = scene.pick(new Point2D.Double(e.getX(), e.getY()), getSize());
                    if (!e.isControlDown() && !e.isShiftDown()) selection.clear();
                    selection.select(hit, e.isControlDown());
                }
                repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (panning) panning = false;
                if (selRectScreen != null) {
                    if (!e.isControlDown()) selection.clear();
                    pickRect(selRectScreen);
                    selRectScreen = null; selStart = null;
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (panning && lastMouse != null) {
                    // Incremental pan: convert screen delta to world delta via inverse of (scale*rotate)
                    double dx = e.getX() - lastMouse.x;
                    double dy = e.getY() - lastMouse.y;
                    double z = scene.camera().getZoom();
                    double angle = Math.toRadians(scene.camera().getRotationDeg());
                    double c = Math.cos(angle), s = Math.sin(angle);
                    double zx = dx / z;
                    double zy = dy / z;
                    // Apply inverse rotation R(-angle): [c s; -s c]
                    double rx =  c * zx + s * zy;
                    double ry = -s * zx + c * zy;
                    scene.camera().addScroll(rx, ry);
                    lastMouse = e.getPoint();
                } else if (selRectScreen != null && selStart != null) {
                    int x = Math.min(selStart.x, e.getX());
                    int y = Math.min(selStart.y, e.getY());
                    int w = Math.abs(e.getX() - selStart.x);
                    int h = Math.abs(e.getY() - selStart.y);
                    selRectScreen.setRect(x, y, w, h);
                }
                repaint();
            }
        });
    }

    private void drawGrid(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        AffineTransform viewAt = scene.camera().buildViewTransform(getSize());
        AffineTransform inv;
        try { inv = viewAt.createInverse(); } catch (Exception e) { inv = new AffineTransform(); }
        // Determine visible world bounds via inverse transform
        Point2D w00 = inv.transform(new Point2D.Double(0,0), null);
        Point2D w10 = inv.transform(new Point2D.Double(getWidth(),0), null);
        Point2D w01 = inv.transform(new Point2D.Double(0,getHeight()), null);
        Point2D w11 = inv.transform(new Point2D.Double(getWidth(),getHeight()), null);
        double minWx = Math.min(Math.min(w00.getX(), w10.getX()), Math.min(w01.getX(), w11.getX()));
        double maxWx = Math.max(Math.max(w00.getX(), w10.getX()), Math.max(w01.getX(), w11.getX()));
        double minWy = Math.min(Math.min(w00.getY(), w10.getY()), Math.min(w01.getY(), w11.getY()));
        double maxWy = Math.max(Math.max(w00.getY(), w10.getY()), Math.max(w01.getY(), w11.getY()));

        double zoom = scene.camera().getZoom();
        double stepWorld = gridBase;
        double stepPx = stepWorld * zoom;
        while (stepPx < 20) { stepWorld *= 2; stepPx *= 2; }
        while (stepPx > 60) { stepWorld /= 2; stepPx /= 2; }

        int iStartX = (int)Math.floor(minWx / stepWorld) - 1;
        int iEndX   = (int)Math.ceil (maxWx / stepWorld) + 1;
        int iStartY = (int)Math.floor(minWy / stepWorld) - 1;
        int iEndY   = (int)Math.ceil (maxWy / stepWorld) + 1;

        g2.setColor(new Color(230,230,230));
        for (int ix = iStartX; ix <= iEndX; ix++) {
            double xw = ix * stepWorld;
            Point2D p0 = viewAt.transform(new Point2D.Double(xw, minWy-10), null);
            Point2D p1 = viewAt.transform(new Point2D.Double(xw, maxWy+10), null);
            g2.drawLine((int)Math.round(p0.getX()), (int)Math.round(p0.getY()), (int)Math.round(p1.getX()), (int)Math.round(p1.getY()));
        }
        for (int iy = iStartY; iy <= iEndY; iy++) {
            double yw = iy * stepWorld;
            Point2D p0 = viewAt.transform(new Point2D.Double(minWx-10, yw), null);
            Point2D p1 = viewAt.transform(new Point2D.Double(maxWx+10, yw), null);
            g2.drawLine((int)Math.round(p0.getX()), (int)Math.round(p0.getY()), (int)Math.round(p1.getX()), (int)Math.round(p1.getY()));
        }
        // axes
        g2.setColor(new Color(200,200,200));
        Point2D X0 = viewAt.transform(new Point2D.Double(minWx-10, 0), null);
        Point2D X1 = viewAt.transform(new Point2D.Double(maxWx+10, 0), null);
        Point2D Y0 = viewAt.transform(new Point2D.Double(0, minWy-10), null);
        Point2D Y1 = viewAt.transform(new Point2D.Double(0, maxWy+10), null);
        g2.drawLine((int)Math.round(X0.getX()), (int)Math.round(X0.getY()), (int)Math.round(X1.getX()), (int)Math.round(X1.getY()));
        g2.drawLine((int)Math.round(Y0.getX()), (int)Math.round(Y0.getY()), (int)Math.round(Y1.getX()), (int)Math.round(Y1.getY()));
    }

    private void pickRect(Rectangle2D screenRect) {
        AffineTransform viewAt = scene.camera().buildViewTransform(getSize());
        pickRectRecursive(scene.root(), screenRect, viewAt);
    }
    private void pickRectRecursive(Node2D node, Rectangle2D screenRect, AffineTransform viewAt) {
        if (!node.isVisible()) return;
        if (node instanceof Group2D) {
            for (Node2D n : ((Group2D)node).children()) pickRectRecursive(n, screenRect, viewAt);
        } else {
            if (node.hitTestRect(screenRect, viewAt)) selection.select(node, true);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawGrid(g2);
        scene.render(g2, getSize());
        // selection rect
        if (selRectScreen != null) {
            Color area = new Color(0,120,255,60);
            Color border = new Color(0,120,255,180);
            Color old = g2.getColor();
            g2.setColor(area); g2.fill(selRectScreen);
            g2.setColor(border); g2.draw(selRectScreen);
            g2.setColor(old);
        }
        // selection overlay (prefer outline shape if available)
        g2.setTransform(new AffineTransform());
        for (Node2D n : selection.selected()) {
            AffineTransform viewAt = scene.camera().buildViewTransform(getSize());
            java.awt.Shape local = n.shapeLocal();
            java.awt.Shape show = (local != null) ? local : n.boundsLocal();
            if (show == null) continue;
            AffineTransform full = n.screenTransform(viewAt);
            java.awt.Shape sb = full.createTransformedShape(show);
            Color old = g2.getColor();
            g2.setColor(new Color(255,0,255,180));
            g2.draw(sb);
            g2.setColor(old);
        }
    }
}
