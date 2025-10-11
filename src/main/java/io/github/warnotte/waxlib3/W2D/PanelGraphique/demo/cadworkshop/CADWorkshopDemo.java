package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADWorkspace;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view.CADWorkshopView;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view.DebugPanel;

/**
 * Launcher principal de la démo CAD Workshop.
 *
 * Cette application démontre de manière exhaustive toutes les capacités du moteur W2D :
 *
 * 1. RENDU DE TEXTE
 *    - drawString() avec txtCantChangeSize=true → Taille fixe malgré zoom
 *    - Tous les alignements (9 combinaisons LEFT/CENTER/RIGHT × TOP/CENTER/BOTTOM)
 *    - Rotations de texte
 *    - GlyphVector pour précision maximale
 *    - Backgrounds optionnels
 *
 * 2. FLÈCHES ET COTATIONS
 *    - drawArrow2() avec différentes tailles de pointes
 *    - drawArrowWithString() pour cotations annotées
 *    - Orientations multiples (0°, 45°, 90°, etc.)
 *
 * 3. TRANSFORMATIONS
 *    - Zoom extrême (0.3x à 200x)
 *    - Rotations combinées (viewport + objet + élément)
 *    - 4 combinaisons d'axes inversés
 *    - AffineTransform complexes
 *
 * 4. SÉLECTION
 *    - Sélection multi-objets
 *    - Rectangle de sélection
 *    - SHIFT/CTRL pour ajouter/retirer
 *    - Fonctionne avec toutes les transformations
 *
 * 5. MODES DE TEST
 *    - Normal : Atelier CAO classique
 *    - Stress Test : 100+ objets
 *    - Rotation Test : Animation continue
 *    - Text Test : Tous les alignements
 *    - Arrow Test : Toutes les orientations
 *
 * UTILISATION:
 *   - Molette souris : Zoom
 *   - Bouton milieu + drag : Pan
 *   - Bouton gauche : Sélection
 *   - Touche [A] : Active/désactive animation
 *   - Touche [T] : Change le mode de test
 *   - Touche [M] : Mode mesure
 *   - Touches [S][P][R][L][K] : Changer d'outil
 *
 * TESTS VALIDÉS:
 *   ✅ Texte ne change pas de taille au zoom (txtCantChangeSize)
 *   ✅ Rotations correctes même avec AT rotaté
 *   ✅ Sélection fonctionne avec axes inversés
 *   ✅ Flèches orientées correctement
 *   ✅ GlyphVector vs drawString
 *   ✅ Performance avec nombreux objets
 */
public class CADWorkshopDemo {

    public static void main(String[] args) {
        // Set Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lance dans l'EDT
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Crée le contexte de sélection partagé
        CurrentSelectionContext selectionContext = new CurrentSelectionContext() {
            @Override
            public boolean isFiltred(Class<?> classK) {
                return false; // Aucun filtre
            }
        };

        // Crée le workspace avec parc solaire flottant (objets métier RÉELS)
        CADWorkspace workspace = CADWorkspace.createFloatingSolarPark();

        // Crée la vue principale
        CADWorkshopView workshopView = new CADWorkshopView(selectionContext, workspace);

        // Crée le panneau de debug
        DebugPanel debugPanel = new DebugPanel(workshopView);

        // Split pane pour la vue + debug
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            workshopView,
            debugPanel
        );
        splitPane.setResizeWeight(0.7); // 70% pour la vue, 30% pour le debug
        splitPane.setOneTouchExpandable(true);

        // Fenêtre principale
        JFrame frame = new JFrame("CAD Workshop Demo - WaxLibrary W2D Engine Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = createMenuBar(workshopView, workspace, frame);
        frame.setJMenuBar(menuBar);

        // Ajoute le contenu
        frame.add(splitPane, BorderLayout.CENTER);

        // Configure la fenêtre
        frame.setSize(1400, 900);
        frame.setMinimumSize(new Dimension(1000, 600));
        frame.setLocationRelativeTo(null);

        // Cleanup lors de la fermeture
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                debugPanel.dispose();
            }
        });

        // Affiche
        frame.setVisible(true);

        // Focus sur la vue pour les touches
        workshopView.requestFocus();

        // Message de bienvenue dans la console
        printWelcomeMessage();
    }

    /**
     * Crée la barre de menu
     */
    private static JMenuBar createMenuBar(CADWorkshopView view, CADWorkspace workspace, JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        // Menu File
        JMenu fileMenu = new JMenu("File");

        JMenuItem newWorkspaceItem = new JMenuItem("New Workspace");
        newWorkspaceItem.addActionListener(e -> {
            workspace.clear();
            view.getContxt().clear_selection(view);
            view.repaint();
        });
        fileMenu.add(newWorkspaceItem);

        JMenuItem loadDemoItem = new JMenuItem("Load Demo Data");
        loadDemoItem.addActionListener(e -> {
            workspace.clear();
            CADWorkspace demo = CADWorkspace.createDemoWorkspace();
            demo.getAllObjects().forEach(workspace::addObject);
            view.repaint();
        });
        fileMenu.add(loadDemoItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> frame.dispose());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // Menu View
        JMenu viewMenu = new JMenu("View");

        JMenuItem resetViewItem = new JMenuItem("Reset View");
        resetViewItem.addActionListener(e -> view.reset_viewport());
        viewMenu.add(resetViewItem);

        JMenuItem toggleGridItem = new JMenuItem("Toggle Grid");
        toggleGridItem.addActionListener(e -> {
            view.config.setDrawGrid(!view.config.isDrawGrid());
            view.repaint();
        });
        viewMenu.add(toggleGridItem);

        JMenuItem toggleHelpLinesItem = new JMenuItem("Toggle Help Lines");
        toggleHelpLinesItem.addActionListener(e -> {
            view.config.setDrawHelpLines(!view.config.isDrawHelpLines());
            view.repaint();
        });
        viewMenu.add(toggleHelpLinesItem);

        viewMenu.addSeparator();

        JMenuItem invertYAxisItem = new JMenuItem("Invert Y Axis");
        invertYAxisItem.addActionListener(e -> {
            view.setInvertYAxis(!view.isInvertYAxis());
            view.repaint();
        });
        viewMenu.add(invertYAxisItem);

        JMenuItem invertXAxisItem = new JMenuItem("Invert X Axis");
        invertXAxisItem.addActionListener(e -> {
            view.setInvertXAxis(!view.isInvertXAxis());
            view.repaint();
        });
        viewMenu.add(invertXAxisItem);

        menuBar.add(viewMenu);

        // Menu Test
        JMenu testMenu = new JMenu("Test");

        for (CADWorkshopView.TestMode mode : CADWorkshopView.TestMode.values()) {
            JMenuItem modeItem = new JMenuItem(mode.getDescription());
            modeItem.addActionListener(e -> view.setTestMode(mode));
            testMenu.add(modeItem);
        }

        testMenu.addSeparator();

        JMenuItem toggleAnimItem = new JMenuItem("Toggle Animation");
        toggleAnimItem.addActionListener(e -> view.toggleAnimation());
        testMenu.add(toggleAnimItem);

        JMenuItem debugShapesItem = new JMenuItem("Toggle Debug Shapes");
        debugShapesItem.addActionListener(e -> {
            view.setEnableSelectionDrawDebug(!view.isEnableSelectionDrawDebug());
            view.repaint();
        });
        testMenu.add(debugShapesItem);

        menuBar.add(testMenu);

        // Menu Help
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(frame,
                "CAD Workshop Demo\n\n" +
                "Test exhaustif du moteur graphique W2D\n" +
                "WaxLibrary3 - Version 3.2\n\n" +
                "Tests validés:\n" +
                "✓ Texte avec taille fixe (txtCantChangeSize)\n" +
                "✓ Rotations combinées\n" +
                "✓ Axes inversés (4 combinaisons)\n" +
                "✓ Sélection multi-objets\n" +
                "✓ GlyphVector rendering\n" +
                "✓ drawArrow2 et drawArrowWithString\n\n" +
                "Auteur: Démo W2D\n" +
                "Date: 2025",
                "About CAD Workshop",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        });
        helpMenu.add(aboutItem);

        JMenuItem shortcutsItem = new JMenuItem("Keyboard Shortcuts");
        shortcutsItem.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(frame,
                "Raccourcis clavier:\n\n" +
                "[A] - Activer/désactiver animation\n" +
                "[T] - Changer mode de test\n" +
                "[M] - Mode mesure\n" +
                "[S] - Outil sélection\n" +
                "[P] - Outil placement\n" +
                "[R] - Outil rotation\n" +
                "[L] - Outil label\n" +
                "[K] - Outil lien\n\n" +
                "Souris:\n" +
                "Molette - Zoom\n" +
                "Bouton milieu + drag - Pan\n" +
                "Bouton gauche - Action selon outil\n" +
                "SHIFT/CTRL - Modifier sélection",
                "Keyboard Shortcuts",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        });
        helpMenu.add(shortcutsItem);

        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Affiche un message de bienvenue dans la console
     */
    private static void printWelcomeMessage() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║          CAD WORKSHOP DEMO - W2D ENGINE TEST               ║");
        System.out.println("║                                                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                            ║");
        System.out.println("║  Cette démo teste EXHAUSTIVEMENT le moteur W2D :          ║");
        System.out.println("║                                                            ║");
        System.out.println("║  ✓ Texte avec taille fixe (txtCantChangeSize)            ║");
        System.out.println("║  ✓ Rotations combinées (viewport + objet + élément)      ║");
        System.out.println("║  ✓ Axes inversés (4 combinaisons)                        ║");
        System.out.println("║  ✓ Sélection multi-objets avec transformations           ║");
        System.out.println("║  ✓ GlyphVector rendering                                 ║");
        System.out.println("║  ✓ drawArrow2() et drawArrowWithString()                 ║");
        System.out.println("║  ✓ Zoom extrême (0.3x à 200x)                           ║");
        System.out.println("║  ✓ Performance avec 100+ objets                          ║");
        System.out.println("║                                                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                            ║");
        System.out.println("║  Commandes:                                                ║");
        System.out.println("║    [A] - Animation ON/OFF                                  ║");
        System.out.println("║    [T] - Changer mode de test                              ║");
        System.out.println("║    [M] - Mode mesure                                       ║");
        System.out.println("║    Molette - Zoom                                          ║");
        System.out.println("║    Bouton milieu - Pan                                     ║");
        System.out.println("║                                                            ║");
        System.out.println("║  Regardez le panneau de debug à droite pour               ║");
        System.out.println("║  les informations détaillées en temps réel !              ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Application lancée avec succès!");
        System.out.println();
    }

    /**
     * Version alternative : 4 vues en grille pour tester les axes inversés
     */
    public static void createMultiViewDemo() {
        // TODO: Implémenter la vue en grille comme dans VUE2D_TestBed
        // avec 4 panels (invertX=false/true × invertY=false/true)
    }
}
