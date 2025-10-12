package io.github.warnotte.waxlib3.W2D2.core;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Node2D {
    private Group2D parent;
    private double tx, ty;
    private double sx = 1.0, sy = 1.0;
    private double rotDeg;
    private double pivotX, pivotY;
    private boolean visible = true;
    private float opacity = 1.0f;
    private int zIndex = 0;

    public Group2D parent() { return parent; }
    void setParent(Group2D p) { this.parent = p; }

    public Node2D translate(double x, double y) { this.tx = x; this.ty = y; return this; }
    public Node2D scale(double sx, double sy) { this.sx = sx; this.sy = sy; return this; }
    public Node2D rotateDeg(double deg) { this.rotDeg = deg; return this; }
    public Node2D pivot(double x, double y) { this.pivotX = x; this.pivotY = y; return this; }
    public Node2D z(int zi) { this.zIndex = zi; return this; }
    public Node2D visible(boolean v) { this.visible = v; return this; }
    public Node2D opacity(float o) { this.opacity = o; return this; }

    public int zIndex() { return zIndex; }
    public boolean isVisible() { return visible; }

    public AffineTransform localTransform() {
        AffineTransform at = new AffineTransform();
        at.translate(tx, ty);
        if (pivotX != 0 || pivotY != 0) at.translate(pivotX, pivotY);
        if (rotDeg != 0) at.rotate(Math.toRadians(rotDeg));
        if (pivotX != 0 || pivotY != 0) at.translate(-pivotX, -pivotY);
        at.scale(sx, sy);
        return at;
    }

    public AffineTransform worldTransform() {
        AffineTransform at = localTransform();
        Group2D p = parent;
        while (p != null) {
            at.preConcatenate(p.localTransform());
            p = p.parent();
        }
        return at;
    }

    public abstract void render(RenderContext ctx);
    public abstract boolean hitTest(PickContext ctx);
    public abstract boolean hitTestRect(java.awt.geom.Rectangle2D screenRect, AffineTransform viewAt);
    public abstract Rectangle2D boundsLocal();
    public java.awt.Shape shapeLocal() { return null; }

    /** Default screen transform used for overlays/pick-rect: viewAt ∘ worldTransform. */
    public AffineTransform screenTransform(AffineTransform viewAt) {
        AffineTransform at = new AffineTransform(viewAt);
        at.concatenate(worldTransform());
        return at;
    }

    protected static double distancePointToSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        if (dx == 0 && dy == 0) return Math.hypot(px - x1, py - y1);
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx*dx + dy*dy);
        t = Math.max(0, Math.min(1, t));
        double projx = x1 + t * dx, projy = y1 + t * dy;
        return Math.hypot(px - projx, py - projy);
    }
}
