package io.github.warnotte.waxlib3.W2D2.core;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class PickContext {
    private final Point2D screenPt; // in pixels
    private final AffineTransform viewAt; // world->screen
    private final double pixelScale;

    public PickContext(Point2D screenPt, AffineTransform viewAt, double pixelScale) {
        this.screenPt = (Point2D) screenPt.clone();
        this.viewAt = new AffineTransform(viewAt);
        this.pixelScale = pixelScale;
    }
    public Point2D screenPt() { return (Point2D) screenPt.clone(); }
    public AffineTransform viewAt() { return new AffineTransform(viewAt); }
    public double pixelScale() { return pixelScale; }
}

