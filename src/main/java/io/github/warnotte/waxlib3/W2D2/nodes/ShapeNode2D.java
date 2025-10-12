package io.github.warnotte.waxlib3.W2D2.nodes;

import io.github.warnotte.waxlib3.W2D2.core.Node2D;
import io.github.warnotte.waxlib3.W2D2.core.PickContext;
import io.github.warnotte.waxlib3.W2D2.core.RenderContext;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ShapeNode2D extends Node2D {
    public enum Kind { RECT, ELLIPSE, LINE }
    private Kind kind = Kind.RECT;
    private double w = 40, h = 30; // rect/ellipse
    private double x2 = 40, y2 = 0; // line
    private Color fill = new Color(0,0,0,0);
    private Color stroke = Color.BLUE;
    private float strokePx = 2f;
    private boolean keepStrokePixel = true;

    public ShapeNode2D rect(double w, double h) { this.kind=Kind.RECT; this.w=w; this.h=h; return this; }
    public ShapeNode2D ellipse(double w, double h) { this.kind=Kind.ELLIPSE; this.w=w; this.h=h; return this; }
    public ShapeNode2D line(double x2, double y2) { this.kind=Kind.LINE; this.x2=x2; this.y2=y2; return this; }
    public ShapeNode2D fill(Color c) { this.fill=c; return this; }
    public ShapeNode2D stroke(Color c) { this.stroke=c; return this; }
    public ShapeNode2D strokePx(float px) { this.strokePx=px; return this; }
    public ShapeNode2D keepStrokePixel(boolean k) { this.keepStrokePixel=k; return this; }

    @Override
    public void render(RenderContext ctx) {
        Graphics2D g = ctx.g2d();
        AffineTransform old = g.getTransform();
        AffineTransform at = ctx.viewAt();
        at.concatenate(worldTransform());
        g.setTransform(at);
        Shape shp = buildLocalShape();
        if (fill != null && fill.getAlpha() > 0) { g.setColor(fill); g.fill(shp); }
        if (stroke != null) {
            float sw = keepStrokePixel ? (float)(strokePx * ctx.pixelScale()) : strokePx;
            g.setStroke(new BasicStroke(sw)); g.setColor(stroke); g.draw(shp);
        }
        g.setTransform(old);
    }

    private Shape buildLocalShape() {
        switch (kind) {
            case RECT: return new Rectangle2D.Double(-w/2, -h/2, w, h);
            case ELLIPSE: return new Ellipse2D.Double(-w/2, -h/2, w, h);
            case LINE: return new Line2D.Double(0,0,x2,y2);
            default: return new Rectangle2D.Double(-w/2, -h/2, w, h);
        }
    }

    @Override
    public boolean hitTest(PickContext ctx) {
        try {
            AffineTransform full = ctx.viewAt();
            full.concatenate(worldTransform());
            AffineTransform inv = full.createInverse();
            Point2D local = inv.transform(ctx.screenPt(), null);
            if (kind == Kind.LINE) {
                double tolW = 5.0 * ctx.pixelScale();
                return distancePointToSegment(local.getX(), local.getY(), 0,0, x2, y2) <= tolW;
            }
            return buildLocalShape().contains(local);
        } catch (Exception ex) { return false; }
    }

    @Override
    public boolean hitTestRect(Rectangle2D screenRect, AffineTransform viewAt) {
        try {
            AffineTransform full = new AffineTransform(viewAt);
            full.concatenate(worldTransform());
            if (kind == Kind.LINE) {
                Point2D s1 = full.transform(new Point2D.Double(0,0), null);
                Point2D s2 = full.transform(new Point2D.Double(x2,y2), null);
                if (screenRect.contains(s1) || screenRect.contains(s2)) return true;
                return new Line2D.Double(s1, s2).intersects(screenRect);
            }
            Shape sb = full.createTransformedShape(buildLocalShape());
            return sb.intersects(screenRect);
        } catch (Exception ex) { return false; }
    }

    @Override
    public Rectangle2D boundsLocal() {
        switch (kind) {
            case RECT:
            case ELLIPSE:
                return new Rectangle2D.Double(-w/2, -h/2, w, h);
            case LINE:
                double minx = Math.min(0, x2), maxx = Math.max(0, x2);
                double miny = Math.min(0, y2), maxy = Math.max(0, y2);
                return new Rectangle2D.Double(minx, miny, maxx-minx, maxy-miny);
        }
        return null;
    }

    @Override
    public Shape shapeLocal() {
        return buildLocalShape();
    }
}
