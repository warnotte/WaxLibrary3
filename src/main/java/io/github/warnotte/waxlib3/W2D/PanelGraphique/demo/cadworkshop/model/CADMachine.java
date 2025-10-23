package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
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
 * Représente une machine ou un équipement dans l'atelier CAO.
 *
 * DÉMONTRE LES TRANSFORMATIONS HIÉRARCHIQUES :
 * - La machine a sa position et rotation dans le monde
 * - Elle contient des composants (moteur, arbre, roulement, etc.)
 * - Chaque composant a sa position et rotation LOCALES
 * - Le texte garde une taille fixe malgré zoom et rotations (txtCantChangeSize=true)
 * - AT est COMPLÈTEMENT CACHÉ grâce à RenderContext
 *
 * Fonctionnalités testées :
 * - Rotation de l'objet avec texte qui suit
 * - Texte à taille fixe malgré le zoom (txtCantChangeSize=true)
 * - Hiérarchie d'objets (Machine contient Components)
 * - Transformations composées (monde → machine → composant)
 * - Rendu correct dans toutes les combinaisons d'axes inversés
 */
public class CADMachine implements CADObject {

    private final String id;
    private String name;
    private Point2D.Double position;
    private double rotation; // en degrés
    private double width;
    private double height;
    private Color color;

    // Composants de la machine (hiérarchie!)
    private List<CADComponent> components;

    // Simule une rotation 3D pour affichage
    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    public CADMachine(String name, Point2D.Double position, double width, double height, Color color) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.position = position;
        this.width = width;
        this.height = height;
        this.color = color;
        this.rotation = 0;
        this.components = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CADObjectType getType() {
        return CADObjectType.MACHINE;
    }

    @Override
    public Point2D.Double getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(double degrees) {
        this.rotation = degrees;
        // Simule une rotation 3D
        this.rotationZ = degrees;
    }

    @Override
    public Rectangle2D getBounds() {
        // Bounds simplifié (sans rotation)
        return new Rectangle2D.Double(
            position.x - width/2,
            position.y - height/2,
            width,
            height
        );
    }

    @Override
    public boolean contains(Point2D point) {
        // TODO: Implémenter avec rotation
        return getBounds().contains(point);
    }

    @Override
    public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered) {
        List<SelectionTuple<Shape, Object>> selectables = new ArrayList<>();

        // Accède au Graphics2D UNIQUEMENT pour les formes géométriques
        Graphics2D g = ctx.getGraphics();
        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        // Utilise withLocalTransform pour appliquer la transformation de la machine
        // Cela CACHE l'AffineTransform à l'utilisateur!
        ctx.withLocalTransform(position.x, position.y, rotation, () -> {

            // Crée le rectangle de la machine (coordonnées LOCALES!)
            Rectangle2D machineRect = new Rectangle2D.Double(
                -width/2, -height/2, width, height
            );

            // Dessine le corps de la machine
            Color renderColor;
            if (isSelected) {
                renderColor = Color.MAGENTA;
            } else if (isHovered) {
                renderColor = color.brighter();
            } else {
                renderColor = color;
            }

            g.setColor(renderColor);
            g.fill(machineRect);

            // Dessine le contour
            g.setColor(isSelected ? Color.MAGENTA : Color.BLACK);
            g.setStroke(new BasicStroke(isSelected ? 3.0f : 2.0f));
            g.draw(machineRect);

            // Dessine le nom de la machine au centre
            // IMPORTANT: Utilise ctx.drawText() avec txtCantChangeSize=true!
            // Le texte garde sa taille FIXE malgré zoom et rotations
            ctx.drawText(
                name,
                0, 0,  // Centre de la machine (coordonnées locales!)
                0,     // Pas de rotation additionnelle
                AlignTexteX.CENTER,
                AlignTexteY.CENTER
            );

            // Dessine les informations de rotation sous le nom
            String rotInfo = String.format("RotZ: %.0f°", rotationZ);
            ctx.drawText(
                rotInfo,
                0, height/2 + 5,  // Sous la machine
                0,
                AlignTexteX.CENTER,
                AlignTexteY.TOP
            );

            // Dessine les dimensions aux coins
            ctx.drawText(
                String.format("%.1f", width),
                0, -height/2 - 3,  // Au-dessus
                0,
                AlignTexteX.CENTER,
                AlignTexteY.BOTTOM
            );

            // Rend tous les composants enfants
            // Chaque composant aura SA transformation locale en plus de celle de la machine!
            for (CADComponent comp : components) {
                List<SelectionTuple<Shape, Object>> compSelectables =
                    comp.render(ctx, false, false);
                selectables.addAll(compSelectables);
            }
        });

        // Ajoute la machine à la sélection (bounds dans le monde)
        selectables.add(new SelectionTuple<>(getBounds(), this));

        // Restaure l'état graphique
        g.setColor(oldColor);
        g.setStroke(oldStroke);

        return selectables;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public boolean isRotatable() {
        return true;
    }

    @Override
    public CADObject clone() {
        CADMachine copy = new CADMachine(
            this.name + " (copy)",
            new Point2D.Double(position.x + 10, position.y + 10),
            this.width,
            this.height,
            this.color
        );
        copy.rotation = this.rotation;
        return copy;
    }

    // Getters/Setters supplémentaires
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void incrementRotationZ(double delta) {
        this.rotationZ += delta;
        this.rotation = this.rotationZ;
    }

    /**
     * Ajoute un composant à la machine
     */
    public void addComponent(CADComponent component) {
        this.components.add(component);
    }

    /**
     * Récupère la liste des composants
     */
    public List<CADComponent> getComponents() {
        return components;
    }

    /**
     * Supprime tous les composants
     */
    public void clearComponents() {
        this.components.clear();
    }
}
