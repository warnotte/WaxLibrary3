package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.GradientSystem;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Gradients prédéfinis populaires
 */
public class GradientPresets {
    
    // Gradient scientifique classique : bleu -> cyan -> vert -> jaune -> rouge
    public final List<GradientPoint> SCIENTIFIC = Arrays.asList(
        new GradientPoint(0.0f, 0f, 0f, 1f),      // Bleu
        new GradientPoint(0.25f, 0f, 1f, 1f),     // Cyan
        new GradientPoint(0.5f, 0f, 1f, 0f),      // Vert
        new GradientPoint(0.75f, 1f, 1f, 0f),     // Jaune
        new GradientPoint(1.0f, 1f, 0f, 0f)       // Rouge
    );
    
    // Gradient simple : bleu -> vert -> rouge
    public final List<GradientPoint> SIMPLE = Arrays.asList(
        new GradientPoint(0.0f, 0f, 0f, 1f),      // Bleu
        new GradientPoint(0.5f, 0f, 1f, 0f),      // Vert
        new GradientPoint(1.0f, 1f, 0f, 0f)       // Rouge
    );
    
    // Gradient thermique : noir -> rouge -> jaune -> blanc
    public final List<GradientPoint> THERMAL = Arrays.asList(
        new GradientPoint(0.0f, 0f, 0f, 0f),      // Noir
        new GradientPoint(0.33f, 1f, 0f, 0f),     // Rouge
        new GradientPoint(0.66f, 1f, 1f, 0f),     // Jaune
        new GradientPoint(1.0f, 1f, 1f, 1f)       // Blanc
    );
    
    // Gradient plasma (style Matplotlib)
    public final List<GradientPoint> PLASMA = Arrays.asList(
        new GradientPoint(0.0f, 0.05f, 0.03f, 0.53f),  // Violet foncé
        new GradientPoint(0.25f, 0.46f, 0.00f, 0.71f), // Violet
        new GradientPoint(0.5f, 0.84f, 0.20f, 0.51f),  // Magenta
        new GradientPoint(0.75f, 0.99f, 0.65f, 0.13f), // Orange
        new GradientPoint(1.0f, 0.94f, 0.98f, 0.65f)   // Jaune clair
    );
    
    // Gradient noir et blanc
    public final List<GradientPoint> GRAYSCALE = Arrays.asList(
        new GradientPoint(0.0f, 0f, 0f, 0f),      // Noir
        new GradientPoint(1.0f, 1f, 1f, 1f)       // Blanc
    );
    
    /**
     * Créer un gradient personnalisé facilement
     */
    public List<GradientPoint> createCustomGradient(Color... colors) {
        List<GradientPoint> points = new java.util.ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            float position = (float) i / (colors.length - 1);
            points.add(new GradientPoint(position, colors[i]));
        }
        return points;
    }
}