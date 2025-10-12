package io.github.warnotte.waxlib3.W2D.fx;

import java.awt.geom.AffineTransform;

/**
 * Camera describing world->screen transform. Independent from Swing panel state,
 * but can be synchronized with it.
 */
public class Camera2D {
    private double zoom = 1.0;         // scale factor (pixels per world unit)
    private double scrollX = 0.0;      // translation in screen space (pixels)
    private double scrollY = 0.0;
    private boolean invertX = false;   // invert axes at view level
    private boolean invertY = true;
    private double rotationDeg = 0.0;  // view rotation in degrees

    public double getZoom() { return zoom; }
    public Camera2D setZoom(double zoom) { this.zoom = zoom; return this; }

    public double getScrollX() { return scrollX; }
    public Camera2D setScrollX(double scrollX) { this.scrollX = scrollX; return this; }

    public double getScrollY() { return scrollY; }
    public Camera2D setScrollY(double scrollY) { this.scrollY = scrollY; return this; }

    public boolean isInvertX() { return invertX; }
    public Camera2D setInvertX(boolean invertX) { this.invertX = invertX; return this; }

    public boolean isInvertY() { return invertY; }
    public Camera2D setInvertY(boolean invertY) { this.invertY = invertY; return this; }

    public double getRotationDeg() { return rotationDeg; }
    public Camera2D setRotationDeg(double rotationDeg) { this.rotationDeg = rotationDeg; return this; }

    /**
     * Returns the world->screen transform. Order: scroll, axis invert, rotate, scale.
     */
    public AffineTransform buildViewTransform() {
        AffineTransform at = new AffineTransform();
        // scroll in screen space
        at.translate(scrollX, scrollY);
        // axis inversion
        double sx = invertX ? -1.0 : 1.0;
        double sy = invertY ? -1.0 : 1.0;
        at.scale(sx, sy);
        // rotation
        if (rotationDeg != 0.0) {
            at.rotate(Math.toRadians(rotationDeg));
        }
        // scale by zoom
        at.scale(zoom, zoom);
        return at;
    }

    /**
     * Reciprocal of zoom (world units per pixel ignoring rotation/inversion).
     */
    public double pixelScale() {
        return zoom == 0 ? 1.0 : (1.0 / zoom);
    }
}

