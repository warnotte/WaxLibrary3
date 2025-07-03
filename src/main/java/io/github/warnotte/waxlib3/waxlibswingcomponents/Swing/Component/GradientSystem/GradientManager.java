package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.GradientSystem;
import java.awt.Color;
import java.util.List;

/**
 * Gestionnaire de gradient avec interpolation
 */
public class GradientManager {
    private List<GradientPoint> currentGradient;
    private final GradientPresets presets;
    
    public GradientManager() {
        this.presets = new GradientPresets();
        this.currentGradient = presets.SCIENTIFIC;
    }
    
    /**
     * Obtenir une couleur du gradient actuel
     */
    public Color getColorForGradient(float gradient) {
        return getColorFromGradient(gradient, currentGradient);
    }
    
    /**
     * Obtenir une couleur d'un gradient spécifique
     */
    public Color getColorFromGradient(float gradient, List<GradientPoint> gradientPoints) {
        // Assurer que le gradient est entre 0 et 1
        gradient = Math.max(0f, Math.min(1f, gradient));
        
        // Cas spéciaux
        if (gradientPoints.isEmpty()) return Color.BLACK;
        if (gradientPoints.size() == 1) return gradientPoints.get(0).color;
        
        // Trouver les deux points de contrôle encadrant notre position
        GradientPoint before = gradientPoints.get(0);
        GradientPoint after = gradientPoints.get(gradientPoints.size() - 1);
        
        for (int i = 0; i < gradientPoints.size() - 1; i++) {
            GradientPoint current = gradientPoints.get(i);
            GradientPoint next = gradientPoints.get(i + 1);
            
            if (gradient >= current.position && gradient <= next.position) {
                before = current;
                after = next;
                break;
            }
        }
        
        // Interpolation linéaire entre les deux couleurs
        return interpolateColor(before, after, gradient);
    }
    
    /**
     * Interpolation linéaire entre deux points de gradient
     */
    private Color interpolateColor(GradientPoint before, GradientPoint after, float gradient) {
        if (before.position == after.position) {
            return before.color;
        }
        
        // Calculer le facteur d'interpolation local
        float t = (gradient - before.position) / (after.position - before.position);
        t = Math.max(0f, Math.min(1f, t));
        
        // Interpoler chaque composante
        float r = lerp(before.color.getRed() / 255f, after.color.getRed() / 255f, t);
        float g = lerp(before.color.getGreen() / 255f, after.color.getGreen() / 255f, t);
        float b = lerp(before.color.getBlue() / 255f, after.color.getBlue() / 255f, t);
        float a = lerp(before.color.getAlpha() / 255f, after.color.getAlpha() / 255f, t);
        
        return new Color(r, g, b, a);
    }
    
    /**
     * Interpolation linéaire simple
     */
    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }
    
    // Méthodes pour changer le gradient
    public void setGradient(List<GradientPoint> gradient) {
        this.currentGradient = gradient;
    }
    
    public void setScientificGradient() {
        setGradient(presets.SCIENTIFIC);
    }
    
    public void setSimpleGradient() {
        setGradient(presets.SIMPLE);
    }
    
    public void setThermalGradient() {
        setGradient(presets.THERMAL);
    }
    
    public void setPlasmaGradient() {
        setGradient(presets.PLASMA);
    }
    
    public void setGrayscaleGradient() {
        setGradient(presets.GRAYSCALE);
    }
    
    public GradientPresets getPresets() {
        return presets;
    }
    
    /**
     * Obtenir le gradient actuel
     */
    public List<GradientPoint> getCurrentGradient() {
        return currentGradient;
    }
}