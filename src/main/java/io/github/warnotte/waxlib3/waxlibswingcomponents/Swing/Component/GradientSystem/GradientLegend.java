package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.GradientSystem;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Gestionnaire de légende de gradient
 */
public class GradientLegend {
    private final GradientManager gradientManager;
    
    public GradientLegend(GradientManager gradientManager) {
        this.gradientManager = gradientManager;
    }
    
    /**
     * Affiche une légende de gradient avec les valeurs min, moyenne et max
     */
    public void drawGradientLegend(Graphics2D g2d, int x, int y, int width, int height, 
                                  double minValue, double avgValue, double maxValue, String title) {
        
        Graphics2D g = (Graphics2D) g2d.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Dessiner le titre si fourni
        int titleOffset = 0;
        if (title != null && !title.isEmpty()) {
            Font titleFont = new Font("Arial", Font.BOLD, 12);
            g.setFont(titleFont);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g.drawString(title, x + (width - titleWidth) / 2, y);
            titleOffset = fm.getHeight() + 5;
        }
        
        int legendY = y + titleOffset;
        
        // Dessiner la barre de gradient SANS artefacts
        drawGradientBar(g, x, legendY, width, height);
        
        // Dessiner le cadre
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.0f));
        g.drawRect(x, legendY, width, height);
        
        // Dessiner les étiquettes
        drawGradientLabels(g, x, legendY, width, height, minValue, avgValue, maxValue);
        
        g.dispose();
    }
    
    /**
     * Version corrigée qui utilise LinearGradientPaint (sans artefacts)
     */
    private void drawGradientBar(Graphics2D g, int x, int y, int width, int height) {
        // Récupérer le gradient actuel du manager
        List<GradientPoint> currentGradient = getCurrentGradient();
        
        // Créer les arrays pour LinearGradientPaint
        float[] fractions = new float[currentGradient.size()];
        Color[] colors = new Color[currentGradient.size()];
        
        for (int i = 0; i < currentGradient.size(); i++) {
            fractions[i] = currentGradient.get(i).position;
            colors[i] = currentGradient.get(i).color;
        }
        
        // Créer et appliquer le LinearGradientPaint
        LinearGradientPaint gradientPaint = new LinearGradientPaint(
            x, y,           // Point de début
            x + width, y,   // Point de fin (horizontal)
            fractions,      // Positions des couleurs (0.0 à 1.0)
            colors          // Couleurs correspondantes
        );
        
        // Sauvegarder le Paint actuel et appliquer le nouveau
        Paint oldPaint = g.getPaint();
        g.setPaint(gradientPaint);
        g.fillRect(x, y, width, height);
        g.setPaint(oldPaint); // Restaurer
    }
    
    private List<GradientPoint> getCurrentGradient() {
        return gradientManager.getCurrentGradient();
    }
    
    private void drawGradientLabels(Graphics2D g, int x, int y, int width, int height, 
                                   double minValue, double avgValue, double maxValue) {
        
        Font labelFont = new Font("Arial", Font.PLAIN, 10);
        g.setFont(labelFont);
        FontMetrics fm = g.getFontMetrics();
        
        int tickHeight = 5;
        int textOffset = 15;
        
        String minText = formatValue(minValue);
        String avgText = formatValue(avgValue);
        String maxText = formatValue(maxValue);
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.0f));
        
        // Valeur minimale
        int minX = x;
        g.drawLine(minX, y + height, minX, y + height + tickHeight);
        int minTextWidth = fm.stringWidth(minText);
        g.drawString(minText, minX - minTextWidth/2, y + height + textOffset);
        
        // Valeur moyenne
        int avgX = x + width / 2;
        g.drawLine(avgX, y + height, avgX, y + height + tickHeight);
        int avgTextWidth = fm.stringWidth(avgText);
        g.drawString(avgText, avgX - avgTextWidth/2, y + height + textOffset);
        
        // Valeur maximale
        int maxX = x + width;
        g.drawLine(maxX, y + height, maxX, y + height + tickHeight);
        int maxTextWidth = fm.stringWidth(maxText);
        g.drawString(maxText, maxX - maxTextWidth/2, y + height + textOffset);
    }
    
    private String formatValue(double value) {
        if (Math.abs(value) >= 1000000) {
            return String.format("%.2e", value);
        } else if (Math.abs(value) >= 1) {
            return String.format("%.2f", value);
        } else {
            return String.format("%.4f", value);
        }
    }
}