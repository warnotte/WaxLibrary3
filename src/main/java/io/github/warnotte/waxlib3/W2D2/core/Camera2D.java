package io.github.warnotte.waxlib3.W2D2.core;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;

public class Camera2D {
    private double zoom = 1.0;
    private double scrollX = 0.0;
    private double scrollY = 0.0;
    // Axes inversion intentionally not supported in W2D2 baseline to reduce complexity
    private double rotationDeg = 0.0;

    public double getZoom() { return zoom; }
    public Camera2D setZoom(double zoom) { this.zoom = zoom; return this; }
    public Camera2D zoomBy(double factor) { this.zoom *= factor; return this; }
    public double getScrollX() { return scrollX; }
    public double getScrollY() { return scrollY; }
    public Camera2D setScroll(double x, double y) { this.scrollX = x; this.scrollY = y; return this; }
    public Camera2D addScroll(double dx, double dy) { this.scrollX += dx; this.scrollY += dy; return this; }
    public boolean isInvertX() { return false; }
    public Camera2D setInvertX(boolean v) { return this; }
    public boolean isInvertY() { return false; }
    public Camera2D setInvertY(boolean v) { return this; }
    public double getRotationDeg() { return rotationDeg; }
    public Camera2D setRotationDeg(double rotationDeg) { this.rotationDeg = rotationDeg; return this; }

    public double pixelScale() { return zoom == 0 ? 1.0 : (1.0 / zoom); }

    public AffineTransform buildViewTransform(Dimension viewport) {
        // Canonical W2D2 order without axis inversion:
        // center -> scale(zoom) -> rotate(angle) -> translate(scroll)
        AffineTransform at = new AffineTransform();
        at.translate(viewport.width / 2.0, viewport.height / 2.0);
        at.scale(zoom, zoom);
        if (rotationDeg != 0.0) at.rotate(Math.toRadians(rotationDeg));
        at.translate(scrollX, scrollY);
        return at;
    }
}
