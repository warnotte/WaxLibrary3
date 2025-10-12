package io.github.warnotte.waxlib3.W2D2.nodes;

import io.github.warnotte.waxlib3.W2D2.core.Node2D;
import io.github.warnotte.waxlib3.W2D2.core.PickContext;
import io.github.warnotte.waxlib3.W2D2.core.RenderContext;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ArrowDimensionNode2D extends Node2D {
    private double x1, y1, x2, y2; // world/local
    private String label = "";
    private Color color = Color.BLACK;
    private float strokePx = 2f;
    private float headSizePx = 8f;
    private float textOffsetPx = 10f;
    private Font font = new Font("SansSerif", Font.PLAIN, 12);

    public ArrowDimensionNode2D(double x1, double y1, double x2, double y2) { this.x1=x1; this.y1=y1; this.x2=x2; this.y2=y2; }
    public ArrowDimensionNode2D label(String l) { this.label=l; return this; }
    public ArrowDimensionNode2D color(Color c) { this.color=c; return this; }
    public ArrowDimensionNode2D strokePx(float px) { this.strokePx=px; return this; }
    public ArrowDimensionNode2D headSizePx(float px) { this.headSizePx=px; return this; }
    public ArrowDimensionNode2D textOffsetPx(float px) { this.textOffsetPx=px; return this; }
    public ArrowDimensionNode2D font(Font f) { this.font=f; return this; }

    @Override
    public void render(RenderContext ctx) {
        Graphics2D g = ctx.g2d();
        // draw main line in world
        AffineTransform full = ctx.viewAt();
        full.concatenate(worldTransform());
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setTransform(full);
        g2.setColor(color);
        g2.setStroke(new BasicStroke((float)(strokePx * ctx.pixelScale())));
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
        g2.dispose();

        // compute screen endpoints
        Point2D s1 = full.transform(new Point2D.Double(x1, y1), null);
        Point2D s2 = full.transform(new Point2D.Double(x2, y2), null);
        double dx = s2.getX() - s1.getX();
        double dy = s2.getY() - s1.getY();
        double len = Math.hypot(dx, dy);
        if (len < 1e-6) return;
        double ux = dx / len, uy = dy / len;
        double nx = -uy, ny = ux;

        // draw arrowheads and label in screen
        Graphics2D gs = (Graphics2D) g.create();
        gs.setTransform(new AffineTransform());
        gs.setColor(color);
        drawHead(gs, s1.getX(), s1.getY(), ux, uy, headSizePx, strokePx);
        drawHead(gs, s2.getX(), s2.getY(), -ux, -uy, headSizePx, strokePx);
        if (label != null && !label.isEmpty()) {
            double mx = (s1.getX()+s2.getX())*0.5 + nx*textOffsetPx;
            double my = (s1.getY()+s2.getY())*0.5 + ny*textOffsetPx;
            AffineTransform lt = new AffineTransform();
            lt.translate(mx, my);
            lt.rotate(Math.atan2(dy, dx));
            Graphics2D gl = (Graphics2D) gs.create();
            gl.setTransform(lt);
            gl.setFont(font);
            GlyphVector gv = font.createGlyphVector(gl.getFontRenderContext(), label);
            Rectangle2D vb = gv.getVisualBounds();
            double tx = -vb.getX() - vb.getWidth()/2.0;
            double ty = -vb.getY() - vb.getHeight()/2.0;
            gl.drawGlyphVector(gv, (float)tx, (float)ty);
            gl.dispose();
        }
        gs.dispose();
    }

    private void drawHead(Graphics2D g, double x, double y, double ux, double uy, double sizePx, float strokePx) {
        double angle = Math.toRadians(25);
        double cos = Math.cos(angle), sin = Math.sin(angle);
        double rx1 =  ux * cos - uy * sin;
        double ry1 =  ux * sin + uy * cos;
        double rx2 =  ux * cos + uy * sin;
        double ry2 = -ux * sin + uy * cos;
        double x1 = x - sizePx * rx1;
        double y1 = y - sizePx * ry1;
        double x2 = x - sizePx * rx2;
        double y2 = y - sizePx * ry2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(Math.max(1f, strokePx)));
        g2.draw(new Line2D.Double(x, y, x1, y1));
        g2.draw(new Line2D.Double(x, y, x2, y2));
        g2.dispose();
    }

    @Override
    public boolean hitTest(PickContext ctx) {
        AffineTransform full = ctx.viewAt();
        full.concatenate(worldTransform());
        Point2D s1 = full.transform(new Point2D.Double(x1, y1), null);
        Point2D s2 = full.transform(new Point2D.Double(x2, y2), null);
        double d = distancePointToSegment(ctx.screenPt().getX(), ctx.screenPt().getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY());
        return d <= 5.0; // pixels
    }

    @Override
    public boolean hitTestRect(Rectangle2D screenRect, AffineTransform viewAt) {
        AffineTransform full = new AffineTransform(viewAt);
        full.concatenate(worldTransform());
        Point2D s1 = full.transform(new Point2D.Double(x1, y1), null);
        Point2D s2 = full.transform(new Point2D.Double(x2, y2), null);
        if (screenRect.contains(s1) || screenRect.contains(s2)) return true;
        return new Line2D.Double(s1, s2).intersects(screenRect);
    }

    @Override
    public Rectangle2D boundsLocal() {
        double minx=Math.min(x1,x2), maxx=Math.max(x1,x2);
        double miny=Math.min(y1,y2), maxy=Math.max(y1,y2);
        return new Rectangle2D.Double(minx, miny, maxx-minx, maxy-miny);
    }
}

