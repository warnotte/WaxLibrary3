package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Conteneur pour tous les objets CAO de l'atelier.
 * Gère l'ajout, la suppression et la recherche d'objets.
 */
public class CADWorkspace {

    private final List<CADObject> objects;
    private String name;

    public CADWorkspace(String name) {
        this.name = name;
        this.objects = new ArrayList<>();
    }

    /**
     * Ajoute un objet à l'espace de travail
     */
    public void addObject(CADObject object) {
        if (object != null && !objects.contains(object)) {
            objects.add(object);
        }
    }

    /**
     * Supprime un objet
     */
    public void removeObject(CADObject object) {
        objects.remove(object);
    }

    /**
     * Supprime plusieurs objets
     */
    public void removeObjects(List<CADObject> toRemove) {
        objects.removeAll(toRemove);
    }

    /**
     * Récupère tous les objets
     */
    public List<CADObject> getAllObjects() {
        return new ArrayList<>(objects);
    }

    /**
     * Récupère les objets d'un type spécifique
     */
    public List<CADObject> getObjectsByType(CADObject.CADObjectType type) {
        return objects.stream()
                .filter(obj -> obj.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Trouve l'objet à une position donnée
     */
    public CADObject getObjectAt(Point2D point) {
        // Parcourt en ordre inverse pour avoir les objets du dessus en premier
        for (int i = objects.size() - 1; i >= 0; i--) {
            CADObject obj = objects.get(i);
            if (obj.contains(point)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Trouve tous les objets dans un rectangle
     */
    public List<CADObject> getObjectsInArea(java.awt.geom.Rectangle2D area) {
        return objects.stream()
                .filter(obj -> area.intersects(obj.getBounds()))
                .collect(Collectors.toList());
    }

    /**
     * Vide l'espace de travail
     */
    public void clear() {
        objects.clear();
    }

    /**
     * Nombre d'objets
     */
    public int size() {
        return objects.size();
    }

    /**
     * Crée un parc solaire flottant RÉALISTE.
     *
     * DÉMONTRE DES OBJETS MÉTIER CONCRETS :
     * - FloatingPlatform avec dimensions réelles (longueur, largeur, poids)
     * - SolarPanel avec puissance et inclinaison
     * - Mooring (amarrages) avec profondeur et tension
     * - Dimensions et mesures réalistes
     * - Objets sélectionnables et manipulables
     */
    public static CADWorkspace createFloatingSolarPark() {
        CADWorkspace workspace = new CADWorkspace("Floating Solar Park");

        // ═══════════════════════════════════════════════════════════════
        // Plateforme 1 - Grande plateforme (20m × 10m)
        // ═══════════════════════════════════════════════════════════════
        FloatingPlatform platform1 = new FloatingPlatform(
            "Platform-A1",
            new Point2D.Double(-30, 0),
            20.0,  // 20m de long
            10.0,  // 10m de large
            5000,  // 5000 kg
            6000   // 6000 kg de flotabilité
        );
        platform1.setTirantEau(0.6);   // 60cm immergé
        platform1.setFrancBord(0.4);   // 40cm émergé

        // Ajoute 12 panneaux solaires (2 rangées de 6)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                double x = -9.0 + col * 3.0;  // Espacés de 3m
                double y = -3.0 + row * 6.0;  // 2 rangées
                platform1.addSolarPanel(new SolarPanel(
                    String.format("PV-A1-%d%d", row+1, col+1),
                    x, y,
                    0,      // Orientation
                    1.65,   // Longueur standard
                    1.0,    // Largeur standard
                    300,    // 300Wc
                    15      // 15° d'inclinaison
                ));
            }
        }

        workspace.addObject(platform1);

        // ═══════════════════════════════════════════════════════════════
        // Plateforme 2 - Moyenne plateforme (15m × 8m)
        // ═══════════════════════════════════════════════════════════════
        FloatingPlatform platform2 = new FloatingPlatform(
            "Platform-A2",
            new Point2D.Double(30, 0),
            15.0,
            8.0,
            3500,
            4200
        );

        // Ajoute 8 panneaux (2 rangées de 4)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                double x = -6.0 + col * 3.0;
                double y = -2.5 + row * 5.0;
                platform2.addSolarPanel(new SolarPanel(
                    String.format("PV-A2-%d%d", row+1, col+1),
                    x, y,
                    0,
                    1.65, 1.0, 300, 15
                ));
            }
        }

        workspace.addObject(platform2);

        // ═══════════════════════════════════════════════════════════════
        // Plateforme 3 - Plateforme rotatée (12m × 6m, 30°)
        // ═══════════════════════════════════════════════════════════════
        FloatingPlatform platform3 = new FloatingPlatform(
            "Platform-B1",
            new Point2D.Double(0, 35),
            12.0,
            6.0,
            2500,
            3000
        );
        platform3.setRotation(30);  // Rotation de 30°!

        // Ajoute 6 panneaux (1 rangée de 6)
        for (int col = 0; col < 6; col++) {
            double x = -5.0 + col * 2.0;
            platform3.addSolarPanel(new SolarPanel(
                String.format("PV-B1-%d", col+1),
                x, 0,
                0,
                1.65, 1.0, 300, 15
            ));
        }

        workspace.addObject(platform3);

        // ═══════════════════════════════════════════════════════════════
        // Amarrages (Moorings)
        // ═══════════════════════════════════════════════════════════════

        // Amarrages pour Platform-A1
        workspace.addObject(new Mooring(
            "Anchor-A1-NW",
            new Point2D.Double(-45, -15),    // Position ancre
            new Point2D.Double(-40, -5),     // Point d'attache sur plateforme
            25.0,  // 25m de câble
            15.0,  // 15m de profondeur
            50     // 50 kN de tension max
        ));

        workspace.addObject(new Mooring(
            "Anchor-A1-NE",
            new Point2D.Double(-15, -15),
            new Point2D.Double(-20, -5),
            25.0, 15.0, 50
        ));

        workspace.addObject(new Mooring(
            "Anchor-A1-SW",
            new Point2D.Double(-45, 15),
            new Point2D.Double(-40, 5),
            25.0, 15.0, 50
        ));

        workspace.addObject(new Mooring(
            "Anchor-A1-SE",
            new Point2D.Double(-15, 15),
            new Point2D.Double(-20, 5),
            25.0, 15.0, 50
        ));

        // Amarrages pour Platform-A2
        workspace.addObject(new Mooring(
            "Anchor-A2-W",
            new Point2D.Double(15, 0),
            new Point2D.Double(22, 0),
            20.0, 15.0, 40
        ));

        workspace.addObject(new Mooring(
            "Anchor-A2-E",
            new Point2D.Double(45, 0),
            new Point2D.Double(38, 0),
            20.0, 15.0, 40
        ));

        // Amarrages pour Platform-B1 (rotatée)
        workspace.addObject(new Mooring(
            "Anchor-B1-N",
            new Point2D.Double(0, 50),
            new Point2D.Double(0, 41),
            18.0, 15.0, 35
        ));

        workspace.addObject(new Mooring(
            "Anchor-B1-S",
            new Point2D.Double(0, 20),
            new Point2D.Double(0, 29),
            18.0, 15.0, 35
        ));

        // ═══════════════════════════════════════════════════════════════
        // Cotations entre plateformes
        // ═══════════════════════════════════════════════════════════════

        // Distance horizontale Platform-A1 ↔ Platform-A2 (bordures)
        CADDimension dim1 = new CADDimension(
            new Point2D.Double(-20, -8),
            new Point2D.Double(22, -8),
            -3,
            "42.0 m (edge-to-edge)"
        );
        workspace.addObject(dim1);

        // Distance centre à centre Platform-A1 ↔ Platform-A2
        CADDimension dim2 = new CADDimension(
            new Point2D.Double(-30, 0),  // Centre Platform-A1
            new Point2D.Double(30, 0),   // Centre Platform-A2
            10,
            "60.0 m (center-to-center)"
        );
        workspace.addObject(dim2);

        // Distance centre à centre Platform-A1 ↔ Platform-B1
        CADDimension dim3 = new CADDimension(
            new Point2D.Double(-30, 0),  // Centre Platform-A1
            new Point2D.Double(0, 35),   // Centre Platform-B1
            -15,
            "45.3 m"
        );
        workspace.addObject(dim3);

        // Distance centre à centre Platform-A2 ↔ Platform-B1
        CADDimension dim4 = new CADDimension(
            new Point2D.Double(30, 0),   // Centre Platform-A2
            new Point2D.Double(0, 35),   // Centre Platform-B1
            15,
            "46.1 m"
        );
        workspace.addObject(dim4);

        // Largeur de Platform-A1
        CADDimension dim5 = new CADDimension(
            new Point2D.Double(-30, -5),
            new Point2D.Double(-30, 5),
            -5,
            "10.0 m"
        );
        workspace.addObject(dim5);

        // Longueur de Platform-A1
        CADDimension dim6 = new CADDimension(
            new Point2D.Double(-40, 0),
            new Point2D.Double(-20, 0),
            -12,
            "20.0 m"
        );
        workspace.addObject(dim6);

        return workspace;
    }

    /**
     * Crée un workspace de démonstration avec des objets pré-placés.
     *
     * DÉMONTRE LA HIÉRARCHIE D'OBJETS :
     * - Chaque machine contient des composants
     * - Chaque composant a sa position et rotation LOCALES
     * - Les transformations sont composées (monde → machine → composant)
     * - Le texte garde sa taille fixe malgré toutes les transformations
     */
    public static CADWorkspace createDemoWorkspace() {
        CADWorkspace workspace = new CADWorkspace("Demo Workshop");

        // ═══════════════════════════════════════════════════════════════
        // Machine 1 - Turbine avec composants hiérarchiques
        // ═══════════════════════════════════════════════════════════════
        CADMachine turbine = new CADMachine(
            "Turbine",
            new Point2D.Double(-50, 0),
            30,
            20,
            new Color(100, 150, 255)
        );

        // Ajoute un moteur à la turbine (position locale!)
        turbine.addComponent(new CADComponent(
            CADComponent.ComponentType.MOTOR,
            "Motor",
            -10, 0,  // Position LOCALE par rapport au centre de la turbine
            0,       // Rotation locale
            8        // Taille
        ));

        // Ajoute un arbre (shaft) qui sort de la turbine
        turbine.addComponent(new CADComponent(
            CADComponent.ComponentType.SHAFT,
            "Shaft",
            10, 0,   // Position locale
            0,       // Rotation locale
            12       // Taille
        ));

        // Ajoute un roulement au bout de l'arbre
        turbine.addComponent(new CADComponent(
            CADComponent.ComponentType.BEARING,
            "Bearing",
            15, 0,   // Position locale
            0,       // Rotation locale
            6        // Taille
        ));

        workspace.addObject(turbine);

        // ═══════════════════════════════════════════════════════════════
        // Machine 2 - Pompe avec composants
        // ═══════════════════════════════════════════════════════════════
        CADMachine pump = new CADMachine(
            "Pompe",
            new Point2D.Double(50, 0),
            25,
            25,
            new Color(255, 150, 100)
        );

        // Moteur central
        pump.addComponent(new CADComponent(
            CADComponent.ComponentType.MOTOR,
            "Motor",
            0, 0,
            0,
            10
        ));

        // 4 roulements autour du moteur (en croix)
        pump.addComponent(new CADComponent(
            CADComponent.ComponentType.BEARING,
            "B1",
            10, 0,
            0,
            5
        ));
        pump.addComponent(new CADComponent(
            CADComponent.ComponentType.BEARING,
            "B2",
            -10, 0,
            0,
            5
        ));
        pump.addComponent(new CADComponent(
            CADComponent.ComponentType.BEARING,
            "B3",
            0, 10,
            0,
            5
        ));
        pump.addComponent(new CADComponent(
            CADComponent.ComponentType.BEARING,
            "B4",
            0, -10,
            0,
            5
        ));

        workspace.addObject(pump);

        // ═══════════════════════════════════════════════════════════════
        // Machine 3 - Compresseur ROTATÉ avec composants
        // Ceci teste les transformations composées :
        // - La machine est à 45° dans le monde
        // - Les composants ont leurs propres rotations locales
        // - Le texte doit rester lisible et à taille fixe!
        // ═══════════════════════════════════════════════════════════════
        CADMachine compressor = new CADMachine(
            "Compresseur",
            new Point2D.Double(0, 60),
            35,
            18,
            new Color(150, 255, 150)
        );
        compressor.setRotation(45);  // Rotation de la machine entière!

        // Engrenage central qui tourne localement
        compressor.addComponent(new CADComponent(
            CADComponent.ComponentType.GEAR,
            "Gear",
            0, 0,
            30,  // Rotation locale de l'engrenage!
            12
        ));

        // Moteur à gauche
        compressor.addComponent(new CADComponent(
            CADComponent.ComponentType.MOTOR,
            "M1",
            -12, 0,
            0,
            8
        ));

        // Capteur en haut (rotaté localement)
        compressor.addComponent(new CADComponent(
            CADComponent.ComponentType.SENSOR,
            "Sensor",
            0, -8,
            90,  // Rotation locale!
            6
        ));

        workspace.addObject(compressor);

        // ═══════════════════════════════════════════════════════════════
        // Dimensions et cotations
        // ═══════════════════════════════════════════════════════════════

        // Dimension entre Turbine et Pompe
        CADDimension dim1 = new CADDimension(
            new Point2D.Double(-50, 0),
            new Point2D.Double(50, 0),
            -15,
            "100.0 m"
        );
        workspace.addObject(dim1);

        // Dimension verticale
        CADDimension dim2 = new CADDimension(
            new Point2D.Double(-50, 0),
            new Point2D.Double(-50, 60),
            10
        );
        workspace.addObject(dim2);

        // Dimension diagonale vers le compresseur
        CADDimension dim3 = new CADDimension(
            new Point2D.Double(50, 0),
            new Point2D.Double(0, 60),
            5,
            "111.8 m"
        );
        workspace.addObject(dim3);

        return workspace;
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
