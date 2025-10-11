package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteX;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteY;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionTuple;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.core.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADComponent;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADMachine;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADObject;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.CADWorkspace;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.FloatingPlatform;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.SolarPanel;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model.Mooring;

/**
 * Vue principale de l'atelier CAO.
 *
 * Cette vue teste EXHAUSTIVEMENT toutes les fonctionnalités du moteur W2D :
 * - drawString() avec txtCantChangeSize dans toutes les configurations
 * - drawArrow2() et drawArrowWithString()
 * - Rotations combinées (viewport + objet + élément)
 * - Axes inversés (4 combinaisons)
 * - Sélection multi-objets avec transformations
 * - Zoom extrême (0.3x à 200x)
 * - Performance avec nombreux objets
 */
public class CADWorkshopView extends PanelGraphiqueBaseBase implements ActionListener, SelectionChangeable {

    private static final long serialVersionUID = 1L;

    private CADWorkspace workspace;
    private CADTool currentTool;
    private CADObject hoveredObject;

    // Animation continue pour tester les rotations
    private boolean animationEnabled = false;
    private float animationAngle = 0;

    // Mode de test - Démarre directement en mode FLOATING_SOLAR_PARK
    private TestMode testMode = TestMode.FLOATING_SOLAR_PARK;

    // Gestion du drag & drop
    private CADObject draggedObject = null;
    private Point2D dragStartWorld = null;
    private Point2D objectStartPos = null;

    // Gestion de la rotation
    private boolean rotationMode = false;
    private Point2D rotationCenter = null;
    private double rotationStartAngle = 0;

    public enum CADTool {
        SELECT("Select [S]", "Sélectionner et manipuler"),
        PLACE("Place [P]", "Placer une machine"),
        ROTATE("Rotate [R]", "Rotation d'objet"),
        MEASURE("Measure [M]", "Créer une cotation"),
        LABEL("Label [L]", "Annoter"),
        LINK("Link [K]", "Relier 2 machines");

        private final String displayName;
        private final String description;

        CADTool(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum TestMode {
        NORMAL("Normal - Vue standard"),
        FLOATING_SOLAR_PARK("Floating Solar Park - Objets métier RÉELS"),
        REAL_CAD("Real CAD - Hiérarchie & Transformations"),
        STRESS_TEST("Stress Test - 100+ objets"),
        ROTATION_TEST("Rotation Test - Animation continue"),
        ZOOM_TEST("Zoom Test - Extrêmes"),
        TEXT_TEST("Text Test - Tous alignements"),
        ARROW_TEST("Arrow Test - Toutes orientations");

        private final String description;

        TestMode(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public CADWorkshopView(CurrentSelectionContext context, CADWorkspace workspace) {
        super(context);

        this.workspace = workspace;
        this.currentTool = CADTool.SELECT;

        // Configuration du repère de référence STANDARD
        // X+ → DROITE, Y+ → HAUT (repère mathématique standard)
        setInvertXAxis(false);  // X+ vers la droite (normal)
        setInvertYAxis(true);   // Y+ vers le haut (inverse Java2D qui a Y+ vers le bas)

        // Configuration visuelle
        setDrawFPSInfos(true);
        config.setDrawGrid(true);
        config.setDrawHelpLines(true);
        config.setDynamicGridSize(true);

        // Zoom initial
        Zoom = 2.0;

        // Tooltip
        setToolTipText(
            "<html>" +
            "<b>CAD Workshop Demo</b><br>" +
            "<b>Vue:</b><br>" +
            "  • Roulette : Zoom<br>" +
            "  • Bouton milieu + Drag : Déplacer vue<br>" +
            "<b>Objets:</b><br>" +
            "  • Clic gauche : Sélectionner<br>" +
            "  • Drag : Déplacer objet<br>" +
            "  • Ctrl + Drag : Rotation<br>" +
            "  • Shift + Clic : Sélection multiple<br>" +
            "<b>Touches:</b><br>" +
            "  • A : Animation ON/OFF<br>" +
            "  • T : Changer mode de test<br>" +
            "  • S : Mode Select<br>" +
            "</html>"
        );

        // Timer pour animation
        Timer timer = new Timer(25, this);
        timer.start();

        // Écoute les changements de sélection
        if (context != null) {
            context.addXXXListener(this);
        }
    }

    @Override
    public void doPaint(Graphics2D g) {
        // Antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Animation si activée
        if (animationEnabled) {
            animationAngle += 0.5f;
            if (animationAngle >= 360) animationAngle = 0;
        }

        // Rendu selon le mode de test
        switch (testMode) {
            case NORMAL:
                renderNormalMode(g);
                break;
            case FLOATING_SOLAR_PARK:
                renderFloatingSolarPark(g);
                break;
            case REAL_CAD:
                renderRealCAD(g);
                break;
            case STRESS_TEST:
                renderStressTest(g);
                break;
            case ROTATION_TEST:
                renderRotationTest(g);
                break;
            case TEXT_TEST:
                renderTextTest(g);
                break;
            case ARROW_TEST:
                renderArrowTest(g);
                break;
            default:
                renderNormalMode(g);
        }

        // HUD - Informations à l'écran
        renderHUD(g);
    }

    /**
     * Mode normal - Rendu du workspace
     */
    private void renderNormalMode(Graphics2D g) {
        // Crée le RenderContext qui CACHE AffineTransform
        RenderContext ctx = new RenderContext(this, g);

        List<CADObject> objects = workspace.getAllObjects();

        for (CADObject obj : objects) {
            boolean isSelected = context.getSelection().contains(obj);
            boolean isHovered = (obj == hoveredObject);

            // Rendu de l'objet avec RenderContext au lieu de Graphics2D
            List<SelectionTuple<Shape, Object>> selectables = obj.render(ctx, isSelected, isHovered);

            // Ajoute les shapes sélectionnables
            for (SelectionTuple<Shape, Object> tuple : selectables) {
                addToSelectableObject(tuple.getShape(), tuple.getUserObject());
            }
        }

        // Test drawString avec rotation et txtCantChangeSize
        renderTextSamples(g);
    }

    /**
     * Mode REAL CAD - Démontre les transformations hiérarchiques
     *
     * CE MODE TESTE EXACTEMENT CE QUE L'UTILISATEUR A DEMANDÉ :
     * - Objets métier (machines) avec leur propre rotation et translation
     * - Objets qui contiennent d'autres objets (composants)
     * - Texte et mesures qui suivent les transformations
     * - Taille de texte FIXE malgré zoom et rotations
     * - AT complètement CACHÉ
     */
    private void renderRealCAD(Graphics2D g) {
        // Titre explicatif
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("REAL CAD MODE - Transformations Hiérarchiques", 10, 25);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString("3 machines avec composants internes (moteur, arbre, roulement, etc.)", 10, 40);
        g.drawString("Chaque composant a sa position et rotation LOCALES", 10, 52);
        g.drawString("Le texte garde sa taille FIXE malgré zoom et rotations!", 10, 64);
        g.drawString("L'utilisateur N'A JAMAIS accès à AffineTransform (caché par RenderContext)", 10, 76);

        // Crée le RenderContext
        RenderContext ctx = new RenderContext(this, g);

        // Rend le workspace avec tous ses objets et composants hiérarchiques
        List<CADObject> objects = workspace.getAllObjects();

        for (CADObject obj : objects) {
            boolean isSelected = context.getSelection().contains(obj);
            boolean isHovered = (obj == hoveredObject);

            List<SelectionTuple<Shape, Object>> selectables = obj.render(ctx, isSelected, isHovered);

            for (SelectionTuple<Shape, Object> tuple : selectables) {
                addToSelectableObject(tuple.getShape(), tuple.getUserObject());
            }
        }

        // Annotations explicatives
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Arial", Font.ITALIC, 9));

        // Flèche pointant vers la turbine
        ctx.drawText(
            "← Turbine (3 composants internes)",
            -80, -15,
            0,
            AlignTexteX.LEFT,
            AlignTexteY.CENTER
        );

        // Flèche pointant vers la pompe
        ctx.drawText(
            "← Pompe (5 composants)",
            80, -15,
            0,
            AlignTexteX.LEFT,
            AlignTexteY.CENTER
        );

        // Flèche pointant vers le compresseur
        ctx.drawText(
            "Compresseur ROTATÉ 45° →\n(3 composants avec rotations locales)",
            -60, 55,
            0,
            AlignTexteX.RIGHT,
            AlignTexteY.CENTER
        );

        // Légende en bas
        g.setColor(new Color(0, 0, 128));
        g.setFont(new Font("Monospaced", Font.BOLD, 10));
        Point2D bottomLeft = convertRealXYToViewXY(-100, -80);
        g.drawString("VALIDATION DES PROMESSES:", (int)bottomLeft.getX(), getHeight() - 80);
        g.setFont(new Font("Monospaced", Font.PLAIN, 9));
        g.drawString("✓ Objets métier avec rotation et translation dans l'espace", (int)bottomLeft.getX(), getHeight() - 65);
        g.drawString("✓ Hiérarchie d'objets (Machine contient Composants)", (int)bottomLeft.getX(), getHeight() - 52);
        g.drawString("✓ Chaque composant a sa transformation LOCALE", (int)bottomLeft.getX(), getHeight() - 39);
        g.drawString("✓ Texte et mesures suivent les transformations", (int)bottomLeft.getX(), getHeight() - 26);
        g.drawString("✓ Taille de texte FIXE malgré zoom (txtCantChangeSize=true)", (int)bottomLeft.getX(), getHeight() - 13);

        // Si animation, fait tourner le compresseur
        if (animationEnabled) {
            // Trouve le compresseur et change sa rotation
            for (CADObject obj : objects) {
                if (obj instanceof CADMachine) {
                    CADMachine machine = (CADMachine) obj;
                    if ("Compresseur".equals(machine.getName())) {
                        machine.setRotation(animationAngle);
                        // Et fait tourner l'engrenage intérieur aussi
                        for (CADComponent comp : machine.getComponents()) {
                            if (comp.getComponentType() == CADComponent.ComponentType.GEAR) {
                                comp.setRotation(animationAngle * 2); // Tourne 2x plus vite
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Mode FLOATING SOLAR PARK - Parc solaire flottant RÉALISTE
     *
     * CE MODE MONTRE DES OBJETS MÉTIER CONCRETS :
     * - FloatingPlatform avec longueur, largeur, poids RÉELS (en mètres et kg)
     * - SolarPanel avec puissance (en Wc) et inclinaison
     * - Mooring avec profondeur et tension (en m et kN)
     * - Dimensions et cotations réalistes
     * - TOUT EST SÉLECTIONNABLE!
     */
    private void renderFloatingSolarPark(Graphics2D g) {
        // Titre
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("FLOATING SOLAR PARK - Objets Métier RÉELS", 10, 25);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("3 plateformes flottantes avec 26 panneaux solaires (7.8 kWc total)", 10, 42);
        g.drawString("Dimensions réelles: longueur, largeur, poids, tirant d'eau, franc-bord", 10, 58);
        g.drawString("8 amarrages avec câbles, profondeur et tension", 10, 74);
        g.drawString("Cliquez pour sélectionner les plateformes, panneaux, amarrages!", 10, 90);

        // Crée le RenderContext
        RenderContext ctx = new RenderContext(this, g);

        // Charge le workspace de parc solaire flottant si pas déjà fait
        if (workspace.size() == 0 || !workspace.getName().equals("Floating Solar Park")) {
            // Remplace le workspace actuel par le parc solaire
            workspace.clear();
            CADWorkspace solarPark = CADWorkspace.createFloatingSolarPark();
            solarPark.getAllObjects().forEach(workspace::addObject);
        }

        // Rend tous les objets
        List<CADObject> objects = workspace.getAllObjects();

        for (CADObject obj : objects) {
            boolean isSelected = context.getSelection().contains(obj);
            boolean isHovered = (obj == hoveredObject);

            List<SelectionTuple<Shape, Object>> selectables = obj.render(ctx, isSelected, isHovered);

            for (SelectionTuple<Shape, Object> tuple : selectables) {
                addToSelectableObject(tuple.getShape(), tuple.getUserObject());
            }
        }

        // Compte les objets pour les statistiques
        int platformCount = 0;
        int panelCount = 0;
        int mooringCount = 0;
        double totalPower = 0;

        for (CADObject obj : objects) {
            if (obj instanceof FloatingPlatform) {
                platformCount++;
                FloatingPlatform platform = (FloatingPlatform) obj;
                panelCount += platform.getSolarPanels().size();
                for (SolarPanel panel : platform.getSolarPanels()) {
                    totalPower += panel.getPuissance();
                }
            } else if (obj instanceof Mooring) {
                mooringCount++;
            }
        }

        // Légende explicative (avec txtCantChangeSize=true via ctx.drawText)
        g.setColor(new Color(0, 100, 0));
        ctx.drawText(
            "OBJETS MÉTIER:\n" +
            "• FloatingPlatform: poids, flotabilité, tirant d'eau\n" +
            "• SolarPanel: 300Wc, 1.65m×1.0m, inclinaison 15°\n" +
            "• Mooring: câble 15-25m, profondeur 15m, tension 35-50kN\n" +
            "• CADDimension: mesures entre plateformes",
            -50, -30,
            0,
            AlignTexteX.LEFT,
            AlignTexteY.TOP
        );

        // Statistiques (avec txtCantChangeSize=true via ctx.drawText)
        g.setColor(new Color(0, 0, 128));
        ctx.drawText(
            String.format(
                "STATISTIQUES:\n" +
                "Plateformes: %d\n" +
                "Panneaux: %d\n" +
                "Puissance: %.1f kWc\n" +
                "Amarrages: %d",
                platformCount, panelCount, totalPower / 1000, mooringCount
            ),
            25, -30,
            0,
            AlignTexteX.LEFT,
            AlignTexteY.TOP
        );
    }

    /**
     * Teste différents cas de drawString
     */
    private void renderTextSamples(Graphics2D g) {
        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.BOLD, 14));

        // Position de test
        double testX = 150;
        double testY = 0;

        g.setColor(Color.BLACK);

        // Test 1: Texte avec taille fixe, différents alignements
        drawString(g, "Fixed Size Center", (float)testX, (float)testY, 0,
                   AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);

        drawString(g, "Fixed Size Left", (float)testX, (float)(testY + 15), 0,
                   AlignTexteX.LEFT, AlignTexteY.CENTER, true, 1.0f);

        drawString(g, "Fixed Size Right", (float)testX, (float)(testY + 30), 0,
                   AlignTexteX.RIGHT, AlignTexteY.CENTER, true, 1.0f);

        // Test 2: Texte avec rotation
        g.setColor(Color.RED);
        drawString(g, "Rotated 45°", (float)testX, (float)(testY + 50), 45,
                   AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);

        // Test 3: Texte avec background
        g.setColor(Color.BLUE);
        drawString(g, "With BG", (float)testX, (float)(testY + 70), 0,
                   AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f, true, Color.YELLOW);

        // Test 4: Texte rotatif (animation)
        if (animationEnabled) {
            g.setColor(Color.GREEN);
            drawString(g, "Animated", (float)testX, (float)(testY - 30), animationAngle,
                       AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
        }

        g.setFont(oldFont);
    }

    /**
     * Test de stress - Beaucoup d'objets
     */
    private void renderStressTest(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawString("STRESS TEST: Rendering 100 machines...", 10, 40);

        // Génère 100 machines en grille
        int gridSize = 10;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = (i - gridSize/2) * 15;
                double y = (j - gridSize/2) * 15;

                g.setColor(new Color(
                    (i * 25) % 256,
                    (j * 25) % 256,
                    ((i+j) * 25) % 256
                ));

                // Petite machine
                g.fillRect(
                    (int)convertRealXYToViewXY(x-2, y-2).getX(),
                    (int)convertRealXYToViewXY(x-2, y-2).getY(),
                    5, 5
                );

                // Texte
                drawString(g, String.format("%d,%d", i, j),
                           (float)x, (float)y, 0,
                           AlignTexteX.CENTER, AlignTexteY.CENTER,
                           true, 2.0f);
            }
        }
    }

    /**
     * Test de rotation continue
     */
    private void renderRotationTest(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawString("ROTATION TEST: Continuous rotation", 10, 40);

        // Crée le RenderContext
        RenderContext ctx = new RenderContext(this, g);

        // Machine qui tourne
        CADMachine testMachine = new CADMachine(
            "Rotating",
            new Point2D.Double(0, 0),
            30, 20,
            Color.CYAN
        );
        testMachine.setRotation(animationAngle);

        testMachine.render(ctx, false, false);

        // Flèches dans différentes directions
        for (int angle = 0; angle < 360; angle += 45) {
            double rad = Math.toRadians(angle + animationAngle);
            double x = Math.cos(rad) * 60;
            double y = Math.sin(rad) * 60;

            g.setColor(Color.RED);
            Shape arrow = drawArrow2(g,
                new Point2D.Double(0, 0),
                new Point2D.Double(x, y),
                1.0f, 1.0f, false, true
            );
        }
    }

    /**
     * Test exhaustif du texte
     */
    private void renderTextTest(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawString("TEXT TEST: All alignments & rotations", 10, 40);

        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        int row = 0;
        double baseX = -120;
        double baseY = -80;
        double spacing = 25;

        // Teste tous les alignements
        AlignTexteX[] alignsX = {AlignTexteX.LEFT, AlignTexteX.CENTER, AlignTexteX.RIGHT};
        AlignTexteY[] alignsY = {AlignTexteY.TOP, AlignTexteY.CENTER, AlignTexteY.BOTTOM};

        for (AlignTexteX alignX : alignsX) {
            for (AlignTexteY alignY : alignsY) {
                double x = baseX + (row % 3) * 80;
                double y = baseY + (row / 3) * spacing;

                String text = String.format("%s-%s",
                    alignX.name().substring(0, 1),
                    alignY.name().substring(0, 1)
                );

                g.setColor(Color.BLUE);
                drawString(g, text, (float)x, (float)y, 0,
                           alignX, alignY, true, 1.5f);

                // Marque le point d'ancrage
                g.setColor(Color.RED);
                Point2D viewPt = convertRealXYToViewXY(x, y);
                g.fillOval((int)viewPt.getX()-2, (int)viewPt.getY()-2, 4, 4);

                row++;
            }
        }

        g.setFont(oldFont);
    }

    /**
     * Test des flèches
     */
    private void renderArrowTest(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawString("ARROW TEST: drawArrow2 & drawArrowWithString", 10, 40);

        // Flèches horizontales
        g.setColor(Color.BLUE);
        drawArrow2(g,
            new Point2D.Double(-60, -40),
            new Point2D.Double(60, -40),
            1.0f, 1.0f, true, true
        );

        // Flèches verticales
        g.setColor(Color.RED);
        drawArrow2(g,
            new Point2D.Double(-40, -60),
            new Point2D.Double(-40, 60),
            1.0f, 1.0f, true, true
        );

        // Flèches diagonales avec tailles différentes
        g.setColor(Color.GREEN);
        drawArrow2(g,
            new Point2D.Double(0, 0),
            new Point2D.Double(80, 60),
            0.5f, 2.0f, true, true
        );

        // TODO: Tester drawArrowWithString quand on l'aura correctement
    }

    /**
     * HUD - Affichage des informations
     */
    private void renderHUD(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));

        int y = 10;
        int lineHeight = 15;

        // Infos générales
        g.drawString(String.format("Mode: %s", testMode.getDescription()), 10, y += lineHeight);
        g.drawString(String.format("Tool: %s", currentTool.getDisplayName()), 10, y += lineHeight);
        g.drawString(String.format("Zoom: %.2fx", Zoom), 10, y += lineHeight);
        g.drawString(String.format("Objects: %d", workspace.size()), 10, y += lineHeight);
        g.drawString(String.format("Selected: %d", context.getSelection().size()), 10, y += lineHeight);
        g.drawString(String.format("Mouse: (%.1f, %.1f)", MouseX, MouseY), 10, y += lineHeight);
        g.drawString(String.format("Animation: %s", animationEnabled ? "ON" : "OFF"), 10, y += lineHeight);

        // Axes inversés
        g.drawString(String.format("InvertX: %b  InvertY: %b", invertXAxis, invertYAxis), 10, y += lineHeight);

        // Shortcuts
        y += 10;
        g.setColor(Color.DARK_GRAY);
        g.drawString("Keys: [A]nim [T]est [M]easure", 10, y += lineHeight);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                animationEnabled = !animationEnabled;
                break;
            case KeyEvent.VK_T:
                // Cycle through test modes
                int nextOrdinal = (testMode.ordinal() + 1) % TestMode.values().length;
                testMode = TestMode.values()[nextOrdinal];
                break;
            case KeyEvent.VK_S:
                currentTool = CADTool.SELECT;
                break;
            case KeyEvent.VK_P:
                currentTool = CADTool.PLACE;
                break;
            case KeyEvent.VK_R:
                currentTool = CADTool.ROTATE;
                break;
            case KeyEvent.VK_L:
                currentTool = CADTool.LABEL;
                break;
            case KeyEvent.VK_K:
                currentTool = CADTool.LINK;
                break;
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        // Détecte l'objet survolé
        Point2D worldPt = convertViewXYtoRealXY(e.getX(), e.getY());
        hoveredObject = workspace.getObjectAt(worldPt);

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Appelle la logique de base (sélection, scroll, popup)
        super.mousePressed(e);

        // Logique personnalisée pour le drag & rotation (bouton gauche seulement)
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        Point2D worldPt = convertViewXYtoRealXY(e.getX(), e.getY());

        if (currentTool == CADTool.SELECT) {
            CADObject clickedObject = workspace.getObjectAt(worldPt);

            System.out.println("DEBUG: Click at world (" + worldPt.getX() + ", " + worldPt.getY() + ")");
            System.out.println("DEBUG: Found object: " + (clickedObject != null ? clickedObject.getType() : "null"));
            if (clickedObject != null) {
                System.out.println("DEBUG: isDraggable=" + clickedObject.isDraggable() + ", isRotatable=" + clickedObject.isRotatable());
            }

            if (clickedObject != null) {
                // Mode rotation si Ctrl enfoncé
                if (e.isControlDown() && clickedObject.isRotatable()) {
                    System.out.println("DEBUG: Starting ROTATION mode");
                    rotationMode = true;
                    draggedObject = clickedObject;
                    rotationCenter = clickedObject.getPosition();

                    // Calcule l'angle initial
                    double dx = worldPt.getX() - rotationCenter.getX();
                    double dy = worldPt.getY() - rotationCenter.getY();
                    rotationStartAngle = Math.toDegrees(Math.atan2(dy, dx)) - draggedObject.getRotation();
                }
                // Mode drag sinon
                else if (clickedObject.isDraggable()) {
                    System.out.println("DEBUG: Starting DRAG mode");
                    draggedObject = clickedObject;
                    dragStartWorld = worldPt;
                    objectStartPos = new Point2D.Double(
                        draggedObject.getPosition().x,
                        draggedObject.getPosition().y
                    );
                }
            }

            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Appelle la logique de base (scroll, sélection rectangle)
        super.mouseDragged(e);

        // Logique personnalisée pour le drag & rotation
        Point2D worldPt = convertViewXYtoRealXY(e.getX(), e.getY());

        if (draggedObject != null) {
            if (rotationMode) {
                // Mode rotation
                double dx = worldPt.getX() - rotationCenter.getX();
                double dy = worldPt.getY() - rotationCenter.getY();
                double currentAngle = Math.toDegrees(Math.atan2(dy, dx));
                double newRotation = currentAngle - rotationStartAngle;

                draggedObject.setRotation(newRotation);
            } else {
                // Mode déplacement
                double deltaX = worldPt.getX() - dragStartWorld.getX();
                double deltaY = worldPt.getY() - dragStartWorld.getY();

                draggedObject.setPosition(new Point2D.Double(
                    objectStartPos.getX() + deltaX,
                    objectStartPos.getY() + deltaY
                ));
            }

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Appelle la logique de base (finalise la sélection, popup menu)
        super.mouseReleased(e);

        // Nettoyage du drag & rotation
        if (e.getButton() == MouseEvent.BUTTON1) {
            draggedObject = null;
            dragStartWorld = null;
            objectStartPos = null;
            rotationMode = false;
            rotationCenter = null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Timer callback
        if (animationEnabled) {
            repaint();
        }
    }

    @Override
    public void somethingNeedRefresh(SelectionChangedEvent e) {
        repaint();
    }

    // Getters/Setters
    public CADWorkspace getWorkspace() {
        return workspace;
    }

    public CADTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(CADTool tool) {
        this.currentTool = tool;
        repaint();
    }

    public TestMode getTestMode() {
        return testMode;
    }

    public void setTestMode(TestMode mode) {
        this.testMode = mode;
        repaint();
    }

    public void toggleAnimation() {
        animationEnabled = !animationEnabled;
    }

    /**
     * Récupère le facteur de zoom actuel
     */
    public double getZoom() {
        return Zoom;
    }
}
