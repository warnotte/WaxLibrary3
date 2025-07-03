package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.GradientSystem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Version améliorée de GradientDemo avec chargement d'images
 */
public class GradientDemo extends JFrame {
    private final GradientManager gradientManager;
    private final GradientLegend gradientLegend;
    private GradientPanel gradientPanel;
    private double minValue = -25.5;
    private double maxValue = 78.3;
    
    // Nouvelles variables pour les images
    private BufferedImage loadedImage = null;
    private boolean showImage = false;
    
    public GradientDemo() {
        this.gradientManager = new GradientManager();
        this.gradientLegend = new GradientLegend(gradientManager);
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Démonstration du Système de Gradient - Avec Images !");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // Plus large pour les images
        setLocationRelativeTo(null);
        
        // Panel principal
        gradientPanel = new GradientPanel();
        add(gradientPanel, BorderLayout.CENTER);
        
        // Panel de contrôles
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        // Boutons pour changer de gradient
        JButton scientificBtn = new JButton("Scientifique");
        scientificBtn.addActionListener(e -> {
            gradientManager.setScientificGradient();
            gradientPanel.repaint();
        });
        
        JButton simpleBtn = new JButton("Simple");
        simpleBtn.addActionListener(e -> {
            gradientManager.setSimpleGradient();
            gradientPanel.repaint();
        });
        
        JButton thermalBtn = new JButton("Thermique");
        thermalBtn.addActionListener(e -> {
            gradientManager.setThermalGradient();
            gradientPanel.repaint();
        });
        
        JButton plasmaBtn = new JButton("Plasma");
        plasmaBtn.addActionListener(e -> {
            gradientManager.setPlasmaGradient();
            gradientPanel.repaint();
        });
        
        // NOUVEAU : Bouton pour charger une image
        JButton loadImageBtn = new JButton("📷 Charger Image");
        loadImageBtn.addActionListener(this::loadImageAction);
        
        // NOUVEAU : Bouton pour revenir aux données
        JButton showDataBtn = new JButton("📊 Afficher Données");
        showDataBtn.addActionListener(e -> {
            showImage = false;
            gradientPanel.repaint();
        });
        
        // NOUVEAU : Bouton pour créer une image de test
        JButton createTestBtn = new JButton("🎨 Image Test");
        createTestBtn.addActionListener(this::createTestImageAction);
        
        JButton customBtn = new JButton("Personnalisé");
        customBtn.addActionListener(e -> {
            // Exemple de gradient personnalisé
            List<GradientPoint> custom = gradientManager.getPresets().createCustomGradient(
                Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.RED, Color.BLACK
            );
            gradientManager.setGradient(custom);
            gradientPanel.repaint();
        });

        JButton testBtn = new JButton("Test Artefacts");
        testBtn.addActionListener(e -> {
            // Gradient avec beaucoup de transitions pour révéler les artefacts
            List<GradientPoint> testGradient = Arrays.asList(
                new GradientPoint(0.0f, Color.BLUE),
                new GradientPoint(0.1f, Color.CYAN),
                new GradientPoint(0.2f, Color.GREEN),
                new GradientPoint(0.3f, Color.YELLOW),
                new GradientPoint(0.4f, Color.ORANGE),
                new GradientPoint(0.5f, Color.RED),
                new GradientPoint(0.6f, Color.MAGENTA),
                new GradientPoint(0.7f, Color.PINK),
                new GradientPoint(0.8f, Color.WHITE),
                new GradientPoint(0.9f, Color.GRAY),
                new GradientPoint(1.0f, Color.BLACK)
            );
            gradientManager.setGradient(testGradient);
            gradientPanel.repaint();
        });
        
        panel.add(new JLabel("Gradients:"));
        panel.add(scientificBtn);
        panel.add(simpleBtn);
        panel.add(thermalBtn);
        panel.add(plasmaBtn);
        panel.add(customBtn);        // <- AJOUTER
        panel.add(testBtn);          // <- AJOUTER
        
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Images:"));
        panel.add(loadImageBtn);
        panel.add(createTestBtn);
        panel.add(showDataBtn);
        
        return panel;
    }
    
    /**
     * Action pour charger une image depuis un fichier
     */
    private void loadImageAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Images (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif", "bmp"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                loadedImage = ImageIO.read(selectedFile);
                showImage = true;
                gradientPanel.repaint();
                
                // Mettre à jour la légende pour l'image
                minValue = 0.0;
                maxValue = 255.0;
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors du chargement de l'image: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Action pour créer une image de test mathématique
     */
    private void createTestImageAction(ActionEvent e) {
        // Créer une image de test avec des motifs mathématiques
        int size = 300;
        loadedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = loadedImage.createGraphics();
        
        // Remplir avec un motif mathématique intéressant
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                // Fonction mathématique complexe pour créer un motif
                double nx = (double) x / size;
                double ny = (double) y / size;
                
                double value = Math.sin(nx * Math.PI * 8) * Math.cos(ny * Math.PI * 6) +
                              Math.sin(Math.sqrt(nx*nx + ny*ny) * Math.PI * 10) * 0.5;
                
                // Normaliser entre 0 et 255
                int grayValue = (int) ((value + 2) * 63.75); // Ramène à 0-255
                grayValue = Math.max(0, Math.min(255, grayValue));
                
                Color gray = new Color(grayValue, grayValue, grayValue);
                loadedImage.setRGB(x, y, gray.getRGB());
            }
        }
        
        g.dispose();
        showImage = true;
        minValue = 0.0;
        maxValue = 255.0;
        gradientPanel.repaint();
    }
    
    /**
     * Panel personnalisé pour afficher le gradient et la légende
     */
    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            int width = getWidth();
            int height = getHeight();
            
            if (showImage && loadedImage != null) {
                // Afficher l'image avec le gradient actuel
                drawImageWithGradient(g2d, width, height);
            } else {
                // Afficher les données normales
                drawDataVisualization(g2d, width, height);
            }
            
            // Dessiner la légende
            drawExtendedLegend(g2d, width, height);
            
            g2d.dispose();
        }
        
        /**
         * Dessiner une image en utilisant le gradient pour coloriser
         */
        private void drawImageWithGradient(Graphics2D g2d, int width, int height) {
            // Calculer les dimensions pour centrer l'image
            int maxImageWidth = width - 100;
            int maxImageHeight = height - 200;
            
            double scaleX = (double) maxImageWidth / loadedImage.getWidth();
            double scaleY = (double) maxImageHeight / loadedImage.getHeight();
            double scale = Math.min(scaleX, scaleY);
            
            int scaledWidth = (int) (loadedImage.getWidth() * scale);
            int scaledHeight = (int) (loadedImage.getHeight() * scale);
            
            int startX = (width - scaledWidth) / 2;
            int startY = 50;
            
            // Créer une nouvelle image colorisée
            BufferedImage colorizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            
            for (int x = 0; x < scaledWidth; x++) {
                for (int y = 0; y < scaledHeight; y++) {
                    // Obtenir le pixel original
                    int origX = (int) (x / scale);
                    int origY = (int) (y / scale);
                    
                    if (origX < loadedImage.getWidth() && origY < loadedImage.getHeight()) {
                        Color originalColor = new Color(loadedImage.getRGB(origX, origY));
                        
                        // Convertir en niveau de gris
                        int gray = (int) (originalColor.getRed() * 0.299 + 
                                        originalColor.getGreen() * 0.587 + 
                                        originalColor.getBlue() * 0.114);
                        
                        // Convertir le niveau de gris en gradient (0-1)
                        float gradientValue = gray / 255.0f;
                        
                        // Obtenir la couleur du gradient
                        Color gradientColor = gradientManager.getColorForGradient(gradientValue);
                        colorizedImage.setRGB(x, y, gradientColor.getRGB());
                    }
                }
            }
            
            // Dessiner l'image colorisée
            g2d.drawImage(colorizedImage, startX, startY, null);
            
            // Titre
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String title = "Image colorisée avec le gradient actuel";
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (width - titleWidth) / 2, 30);
            
            // Info sur l'image
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String info = String.format("Taille originale: %dx%d, Affichée: %dx%d", 
                loadedImage.getWidth(), loadedImage.getHeight(), scaledWidth, scaledHeight);
            int infoWidth = g2d.getFontMetrics().stringWidth(info);
            g2d.drawString(info, (width - infoWidth) / 2, startY + scaledHeight + 20);
        }
        
        private void drawDataVisualization(Graphics2D g2d, int width, int height) {
            // Code original de visualisation des données
            int gridSize = 15;
            int cols = (width - 100) / gridSize;
            int rows = (height - 200) / gridSize;
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double x = (double) j / cols;
                    double y = (double) i / rows;
                    double value = Math.sin(x * Math.PI * 4) * Math.cos(y * Math.PI * 3) * 0.5 + 0.5;
                    
                    float gradient = (float) value;
                    Color color = gradientManager.getColorForGradient(gradient);
                    
                    g2d.setColor(color);
                    g2d.fillRect(50 + j * gridSize, 30 + i * gridSize, gridSize - 1, gridSize - 1);
                }
            }
            
            // Titre
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String title = "Visualisation de données avec gradient";
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (width - titleWidth) / 2, 20);
        }
        
        private void drawExtendedLegend(Graphics2D g2d, int width, int height) {
            int margin = 50;
            int legendWidth = width - (2 * margin);
            int legendHeight = 30;
            int legendY = height - 100;
            
            double avgValue = (minValue + maxValue) / 2;
            
            String legendTitle = showImage ? 
                "Niveaux de gris (0=noir, 255=blanc)" : 
                "Température (°C)";
            
            gradientLegend.drawGradientLegend(g2d, margin, legendY, legendWidth, legendHeight, 
                                            minValue, avgValue, maxValue, legendTitle);
            
            // Instructions
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            String instruction = showImage ? 
                "Changez de gradient pour voir l'image sous différentes colorisations !" :
                "Chargez une image pour la voir colorisée avec les gradients";
            g2d.drawString(instruction, margin, legendY - 10);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           /* try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            
            new GradientDemo().setVisible(true);
        });
    }
}