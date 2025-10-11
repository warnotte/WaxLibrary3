package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADObject;

/**
 * Panneau de debug avancé pour visualiser en temps réel :
 * - État des transformations AffineTransform
 * - Coordonnées souris (monde/vue)
 * - Objets sélectionnés
 * - Statistiques de rendu
 * - Flags système
 */
public class DebugPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final CADWorkshopView targetView;

    // Composants UI
    private JTextArea debugTextArea;
    private JLabel statusLabel;
    private JCheckBox enableDebugShapes;
    private JCheckBox enableAnimation;
    private JButton resetViewButton;
    private JButton toggleAxesButton;

    // Timer pour rafraîchissement
    private Timer refreshTimer;

    public DebugPanel(CADWorkshopView view) {
        this.targetView = view;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 600));
        setBorder(BorderFactory.createTitledBorder("Debug Panel"));

        // Zone de texte pour infos
        debugTextArea = new JTextArea();
        debugTextArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        debugTextArea.setEditable(false);
        debugTextArea.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(debugTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau de contrôles
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        // Status
        statusLabel = new JLabel("Status: Running");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlsPanel.add(statusLabel);

        // Checkboxes
        enableDebugShapes = new JCheckBox("Debug Selection Shapes", false);
        enableDebugShapes.addActionListener(e -> {
            targetView.setEnableSelectionDrawDebug(enableDebugShapes.isSelected());
            targetView.repaint();
        });
        controlsPanel.add(enableDebugShapes);

        enableAnimation = new JCheckBox("Animation", false);
        enableAnimation.addActionListener(e -> {
            targetView.toggleAnimation();
        });
        controlsPanel.add(enableAnimation);

        // Boutons
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        resetViewButton = new JButton("Reset View");
        resetViewButton.addActionListener(e -> {
            targetView.reset_viewport();
        });
        buttonsPanel.add(resetViewButton);

        toggleAxesButton = new JButton("Toggle Axes");
        toggleAxesButton.addActionListener(e -> {
            targetView.setInvertYAxis(!targetView.isInvertYAxis());
            targetView.repaint();
        });
        buttonsPanel.add(toggleAxesButton);

        JButton testModeButton = new JButton("Next Test");
        testModeButton.addActionListener(e -> {
            int next = (targetView.getTestMode().ordinal() + 1) % CADWorkshopView.TestMode.values().length;
            targetView.setTestMode(CADWorkshopView.TestMode.values()[next]);
        });
        buttonsPanel.add(testModeButton);

        JButton clearSelectionButton = new JButton("Clear Select");
        clearSelectionButton.addActionListener(e -> {
            targetView.getContxt().clear_selection(this);
        });
        buttonsPanel.add(clearSelectionButton);

        controlsPanel.add(buttonsPanel);

        add(controlsPanel, BorderLayout.SOUTH);

        // Timer pour rafraîchir les infos
        refreshTimer = new Timer(100, this);
        refreshTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Rafraîchit les informations de debug
        updateDebugInfo();
    }

    /**
     * Met à jour les informations de debug
     */
    private void updateDebugInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("╔════════════════════════════════════╗\n");
        sb.append("║   CAD WORKSHOP - DEBUG PANEL       ║\n");
        sb.append("╠════════════════════════════════════╣\n\n");

        // Informations viewport
        sb.append("VIEWPORT:\n");
        sb.append(String.format("  Zoom: %.3fx\n", targetView.getZoom()));
        sb.append(String.format("  Scroll: (%.1f, %.1f)\n", targetView.getScrollX(), targetView.getScrollY()));
        sb.append(String.format("  Angle: %.1f°\n", targetView.Angle));
        sb.append(String.format("  Size: %dx%d px\n", targetView.getWidth(), targetView.getHeight()));
        sb.append("\n");

        // AffineTransform global
        sb.append("AFFINE TRANSFORM:\n");
        if (targetView.at != null) {
            sb.append(String.format("  ScaleX: %.3f\n", targetView.at.getScaleX()));
            sb.append(String.format("  ScaleY: %.3f\n", targetView.at.getScaleY()));
            sb.append(String.format("  ShearX: %.3f\n", targetView.at.getShearX()));
            sb.append(String.format("  ShearY: %.3f\n", targetView.at.getShearY()));
            sb.append(String.format("  TransX: %.1f\n", targetView.at.getTranslateX()));
            sb.append(String.format("  TransY: %.1f\n", targetView.at.getTranslateY()));
        }
        sb.append("\n");

        // Coordonnées souris
        sb.append("MOUSE:\n");
        sb.append(String.format("  World: (%.2f, %.2f)\n", targetView.MouseX, targetView.MouseY));
        sb.append(String.format("  Delta: (%.2f, %.2f)\n", targetView.MouseDX, targetView.MouseDY));
        sb.append(String.format("  Inside: %b\n", targetView.MOUSEINSIDE));
        sb.append(String.format("  Shift: %b  Ctrl: %b\n", targetView.SHIFT, targetView.CTRL));
        sb.append("\n");

        // Vision rectangle
        if (targetView.getViewRectangleWorldCoordinate() != null) {
            sb.append("VIEW RECTANGLE (world):\n");
            sb.append(String.format("  X: %.1f  Y: %.1f\n",
                targetView.getViewRectangleWorldCoordinate().getX(),
                targetView.getViewRectangleWorldCoordinate().getY()));
            sb.append(String.format("  W: %.1f  H: %.1f\n",
                targetView.getViewRectangleWorldCoordinate().getWidth(),
                targetView.getViewRectangleWorldCoordinate().getHeight()));
            sb.append("\n");
        }

        // Workspace
        sb.append("WORKSPACE:\n");
        sb.append(String.format("  Name: %s\n", targetView.getWorkspace().getName()));
        sb.append(String.format("  Objects: %d\n", targetView.getWorkspace().size()));
        sb.append(String.format("  Machines: %d\n",
            targetView.getWorkspace().getObjectsByType(CADObject.CADObjectType.MACHINE).size()));
        sb.append(String.format("  Dimensions: %d\n",
            targetView.getWorkspace().getObjectsByType(CADObject.CADObjectType.DIMENSION).size()));
        sb.append("\n");

        // Sélection
        sb.append("SELECTION:\n");
        List<?> selection = targetView.getContxt().getSelection();
        sb.append(String.format("  Count: %d\n", selection.size()));

        if (!selection.isEmpty()) {
            for (int i = 0; i < Math.min(3, selection.size()); i++) {
                Object obj = selection.get(i);
                if (obj instanceof CADObject) {
                    CADObject cadObj = (CADObject) obj;
                    sb.append(String.format("  [%d] %s (%.1f, %.1f)\n",
                        i,
                        cadObj.getType().name(),
                        cadObj.getPosition().x,
                        cadObj.getPosition().y));
                }
            }
            if (selection.size() > 3) {
                sb.append(String.format("  ... +%d more\n", selection.size() - 3));
            }
        }
        sb.append("\n");

        // Configuration
        sb.append("CONFIGURATION:\n");
        sb.append(String.format("  InvertX: %b  InvertY: %b\n",
				targetView.isInvertXAxis(), targetView.isInvertYAxis()));
        sb.append(String.format("  Grid: %b  HelpLines: %b\n",
            targetView.config.isDrawGrid(), targetView.config.isDrawHelpLines()));
        sb.append(String.format("  Grid Size: %.2f\n", targetView.config.getGRID_SIZE()));
        sb.append(String.format("  Dynamic Grid: %b\n", targetView.config.isDynamicGridSize()));
        sb.append("\n");

        // État
        sb.append("STATE:\n");
        sb.append(String.format("  Tool: %s\n", targetView.getCurrentTool().getDisplayName()));
        sb.append(String.format("  Test Mode: %s\n", targetView.getTestMode().name()));
        sb.append(String.format("  FPS Display: %b\n", targetView.isDrawFPSInfos()));
        sb.append("\n");

        // Statistiques rendu (si disponibles)
        sb.append("RENDERING:\n");
        sb.append(String.format("  Selectable Shapes: %d\n", targetView.getSelectableObject().size()));
        sb.append("\n");

        sb.append("╚════════════════════════════════════╝\n");

        // Met à jour l'affichage
        debugTextArea.setText(sb.toString());
        debugTextArea.setCaretPosition(0); // Retour en haut

        // Met à jour le statut
        statusLabel.setText(String.format("Status: Running | Zoom: %.2fx | Objects: %d | Selected: %d",
            targetView.getZoom(),
            targetView.getWorkspace().size(),
            selection.size()));
    }

    /**
     * Arrête le timer
     */
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
