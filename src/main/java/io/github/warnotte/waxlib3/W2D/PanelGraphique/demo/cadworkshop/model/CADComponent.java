package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteX;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteY;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionTuple;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view.RenderContext;

/**
 * Composant d'une machine (moteur, arbre, roulement, etc.)
 *
 * Cette classe démontre la hiérarchie d'objets :
 * - Un composant a sa propre position LOCALE par rapport à son parent
 * - Il a sa propre rotation LOCALE
 * - Le texte et les shapes suivent les transformations composées
 *
 * VALIDATION DE :
 * - Transformations hiérarchiques (parent + local)
 * - txtCantChangeSize=true sur texte transformé
 * - RenderContext.withLocalTransform()
 */
public class CADComponent implements CADObject {

    /**
     * Type de composant
     */
    public enum ComponentType {
        MOTOR("Motor", new Color(100, 149, 237)),      // Cornflower blue
        SHAFT("Shaft", new Color(169, 169, 169)),      // Dark gray
        BEARING("Bearing", new Color(255, 215, 0)),    // Gold
        GEAR("Gear", new Color(192, 192, 192)),        // Silver
        SENSOR("Sensor", new Color(50, 205, 50));      // Lime green

        private final String displayName;
        private final Color defaultColor;

        ComponentType(String displayName, Color defaultColor) {
            this.displayName = displayName;
            this.defaultColor = defaultColor;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Color getDefaultColor() {
            return defaultColor;
        }
    }

    private final String id;
    private final ComponentType componentType;
    private String label;
    private Point2D.Double localPosition; // Position LOCALE par rapport au parent
    private double localRotation;         // Rotation LOCALE en degrés
    private double size;
    private Color color;

    /**
     * Crée un composant avec position et rotation LOCALES
     *
     * @param type Type de composant
     * @param label Label du composant
     * @param localX Position X locale (relative au parent)
     * @param localY Position Y locale (relative au parent)
     * @param localRotation Rotation locale en degrés
     * @param size Taille du composant
     */
    public CADComponent(ComponentType type, String label,
                       double localX, double localY,
                       double localRotation, double size) {
        this.id = UUID.randomUUID().toString();
        this.componentType = type;
        this.label = label;
        this.localPosition = new Point2D.Double(localX, localY);
        this.localRotation = localRotation;
        this.size = size;
        this.color = type.getDefaultColor();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CADObjectType getType() {
        return CADObjectType.MACHINE; // Les composants sont aussi des machines
    }

    @Override
    public Point2D.Double getPosition() {
        return localPosition;
    }

    @Override
    public void setPosition(Point2D.Double position) {
        this.localPosition = position;
    }

    @Override
    public double getRotation() {
        return localRotation;
    }

    @Override
    public void setRotation(double degrees) {
        this.localRotation = degrees;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(
            localPosition.x - size/2,
            localPosition.y - size/2,
            size,
            size
        );
    }

    @Override
    public boolean contains(Point2D point) {
        return getBounds().contains(point);
    }

    @Override
    public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered) {
        List<SelectionTuple<Shape, Object>> selectables = new ArrayList<>();

        // Accède au Graphics2D pour les formes
        Graphics2D g = ctx.getGraphics();
        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        // Couleur selon état
        Color renderColor;
        if (isSelected) {
            renderColor = Color.MAGENTA;
        } else if (isHovered) {
            renderColor = color.brighter();
        } else {
			renderColor = color;
		}

		// Utilise withLocalTransform pour appliquer position + rotation LOCALES
        ctx.withLocalTransform(localPosition.x, localPosition.y, localRotation, () -> {

            // Dessine le composant selon son type
            Shape componentShape;

            switch (componentType) {
                case MOTOR:
                    // Rectangle avec cercle dedans
                    Rectangle2D motorBox = new Rectangle2D.Double(-size/2, -size/2, size, size);
                    g.setColor(renderColor);
                    g.fill(motorBox);
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(isSelected ? 2.0f : 1.0f));
                    g.draw(motorBox);

                    Ellipse2D motorCircle = new Ellipse2D.Double(-size/4, -size/4, size/2, size/2);
                    g.draw(motorCircle);
                    componentShape = motorBox;
                    break;

                case SHAFT:
                    // Rectangle long et fin
                    Rectangle2D shaft = new Rectangle2D.Double(-size/2, -size/8, size, size/4);
                    g.setColor(renderColor);
                    g.fill(shaft);
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(isSelected ? 2.0f : 1.0f));
                    g.draw(shaft);
                    componentShape = shaft;
                    break;

                case BEARING:
                    // Deux cercles concentriques
                    Ellipse2D outerCircle = new Ellipse2D.Double(-size/2, -size/2, size, size);
                    Ellipse2D innerCircle = new Ellipse2D.Double(-size/3, -size/3, 2*size/3, 2*size/3);
                    g.setColor(renderColor);
                    g.fill(outerCircle);
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(isSelected ? 2.0f : 1.0f));
                    g.draw(outerCircle);
                    g.draw(innerCircle);
                    componentShape = outerCircle;
                    break;

                case GEAR:
                    // Hexagone simplifié
                    int[] xPoints = new int[6];
                    int[] yPoints = new int[6];
                    for (int i = 0; i < 6; i++) {
                        double angle = i * Math.PI / 3;
                        xPoints[i] = (int)(size/2 * Math.cos(angle));
                        yPoints[i] = (int)(size/2 * Math.sin(angle));
                    }
                    java.awt.Polygon gear = new java.awt.Polygon(xPoints, yPoints, 6);
                    g.setColor(renderColor);
                    g.fill(gear);
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(isSelected ? 2.0f : 1.0f));
                    g.draw(gear);
                    componentShape = gear;
                    break;

                case SENSOR:
                    // Triangle
                    int[] txPoints = {0, (int)(-size/2), (int)(-size/2)};
                    int[] tyPoints = {(int)(-size/2), (int)(size/2), (int)(-size/2)};
                    java.awt.Polygon triangle = new java.awt.Polygon(txPoints, tyPoints, 3);
                    g.setColor(renderColor);
                    g.fill(triangle);
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(isSelected ? 2.0f : 1.0f));
                    g.draw(triangle);
                    componentShape = triangle;
                    break;

                default:
                    componentShape = new Rectangle2D.Double(-size/2, -size/2, size, size);
            }

            // Dessine le label AVEC txtCantChangeSize=true
            // Le texte sera dans le contexte transformé mais gardera sa taille fixe!
            Shape textShape = ctx.drawText(
                label,
                0, size/2 + 3,  // Sous le composant
                0,  // Pas de rotation additionnelle
                AlignTexteX.CENTER,
                AlignTexteY.TOP
            );
        });

        // Ajoute à la sélection (bounds simples)
        selectables.add(new SelectionTuple<>(getBounds(), this));

        // Restaure
        g.setColor(oldColor);
        g.setStroke(oldStroke);

        return selectables;
    }

    @Override
    public boolean isDraggable() {
        return false; // Les composants ne se déplacent pas individuellement
    }

    @Override
    public boolean isRotatable() {
        return false; // Idem pour la rotation
    }

    @Override
    public CADObject clone() {
        return new CADComponent(
            componentType,
            label + " (copy)",
            localPosition.x + 5,
            localPosition.y + 5,
            localRotation,
            size
        );
    }

    // Getters
    public ComponentType getComponentType() {
        return componentType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getSize() {
        return size;
    }
}
