package io.github.warnotte.waxlib3.W2D2.core;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RenderContext {
    private final Graphics2D g2d;
    private final AffineTransform viewAt; // world->screen
    private final double pixelScale;

    public RenderContext(Graphics2D g2d, AffineTransform viewAt, double pixelScale) {
        this.g2d = g2d;
        this.viewAt = new AffineTransform(viewAt);
        this.pixelScale = pixelScale;
    }
    public Graphics2D g2d() { return g2d; }
    public AffineTransform viewAt() { return new AffineTransform(viewAt); }
    public double pixelScale() { return pixelScale; }
}

