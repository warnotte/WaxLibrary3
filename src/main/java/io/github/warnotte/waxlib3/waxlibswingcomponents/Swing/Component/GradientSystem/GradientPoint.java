package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.GradientSystem;
import java.awt.Color;

/**
 * Point de contrôle pour définir un gradient
 */
public class GradientPoint {
    public final float position; // Entre 0 et 1
    public final Color color;
    
    public GradientPoint(float position, Color color) {
        this.position = Math.max(0f, Math.min(1f, position));
        this.color = color;
    }
    
    public GradientPoint(float position, int r, int g, int b) {
        this(position, new Color(r, g, b));
    }
    
    public GradientPoint(float position, float r, float g, float b) {
        this(position, new Color(r, g, b, 1f));
    }
}