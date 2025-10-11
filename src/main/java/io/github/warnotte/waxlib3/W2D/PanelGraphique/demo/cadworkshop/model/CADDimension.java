package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionTuple;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view.RenderContext;

/**
 * Représente une cotation/dimension avec flèches et mesure.
 *
 * Teste spécifiquement :
 * - drawArrowWithString() dans toutes les orientations
 * - Texte de mesure avec taille fixe
 * - Flèches aux 2 extrémités
 * - Calcul automatique de distance
 */
public class CADDimension implements CADObject {

    private final String id;
    private Point2D.Double start;
    private Point2D.Double end;
    private double offset; // Décalage perpendiculaire à la ligne
    private String customLabel; // Label personnalisé (null = auto-calcul)
    private Color color;

    public CADDimension(Point2D.Double start, Point2D.Double end, double offset) {
        this.id = UUID.randomUUID().toString();
        this.start = start;
        this.end = end;
        this.offset = offset;
        this.customLabel = null;
        this.color = Color.BLUE;
    }

    public CADDimension(Point2D.Double start, Point2D.Double end, double offset, String label) {
        this(start, end, offset);
        this.customLabel = label;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CADObjectType getType() {
        return CADObjectType.DIMENSION;
    }

    @Override
    public Point2D.Double getPosition() {
        // Position = point milieu
        return new Point2D.Double(
            (start.x + end.x) / 2.0,
            (start.y + end.y) / 2.0
        );
    }

    @Override
    public void setPosition(Point2D.Double position) {
        // Déplace toute la dimension
        double dx = position.x - getPosition().x;
        double dy = position.y - getPosition().y;
        start.x += dx;
        start.y += dy;
        end.x += dx;
        end.y += dy;
    }

    @Override
    public double getRotation() {
        // Angle de la ligne
        return Math.toDegrees(Math.atan2(end.y - start.y, end.x - start.x));
    }

    @Override
    public void setRotation(double degrees) {
        // Les dimensions ne se rotent pas directement
    }

    @Override
    public Rectangle2D getBounds() {
        double minX = Math.min(start.x, end.x);
        double minY = Math.min(start.y, end.y);
        double maxX = Math.max(start.x, end.x);
        double maxY = Math.max(start.y, end.y);

        // Ajoute une marge pour le texte et les flèches
        double margin = 5;
        return new Rectangle2D.Double(
            minX - margin,
            minY - margin,
            (maxX - minX) + 2*margin,
            (maxY - minY) + 2*margin
        );
    }

    @Override
    public boolean contains(Point2D point) {
        return getBounds().contains(point);
    }

    @Override
    public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered) {
        List<SelectionTuple<Shape, Object>> selectables = new ArrayList<>();

        var g = ctx.getGraphics();
        Color oldColor = g.getColor();

        // Calcule la distance
        double distance = start.distance(end);

        // Label
        String label;
        if (customLabel != null) {
            label = customLabel;
        } else {
            label = String.format("%.1f m", distance);
        }

        // Couleur
        Color renderColor;
        if (isSelected) {
            renderColor = Color.MAGENTA;
        } else if (isHovered) {
            renderColor = color.brighter();
        } else {
            renderColor = color;
        }
        g.setColor(renderColor);

        // Utilise drawDimension de RenderContext qui appelle drawArrowWithString!
        // Cela garantit que le texte garde une taille fixe malgré le zoom
        Shape dimShape = ctx.drawDimension(
            label,
            start.x, start.y,
            end.x, end.y,
            offset,   // Décalage du texte
            8.0       // Taille des flèches
        );

        // Ajoute à la sélection
        if (dimShape != null) {
            selectables.add(new SelectionTuple<>(dimShape, this));
        } else {
            // Fallback si drawDimension retourne null
            Rectangle2D selectionBox = new Rectangle2D.Double(
                getPosition().x - 10,
                getPosition().y - 5,
                20,
                10
            );
            selectables.add(new SelectionTuple<>(selectionBox, this));
        }

        g.setColor(oldColor);
        return selectables;
    }

    /**
     * Calcule un point avec offset perpendiculaire
     */
    private Point2D.Double calculateOffsetPoint(Point2D.Double from, Point2D.Double to, double offset) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        double perpAngle = angle + Math.PI / 2;

        return new Point2D.Double(
            from.x + Math.cos(perpAngle) * offset,
            from.y + Math.sin(perpAngle) * offset
        );
    }

    @Override
    public boolean isDraggable() {
        return false;  // Les dimensions ne sont pas déplaçables (objets de mesure, pas métier)
    }

    @Override
    public boolean isRotatable() {
        return false;
    }

    @Override
    public CADObject clone() {
        return new CADDimension(
            new Point2D.Double(start.x, start.y),
            new Point2D.Double(end.x, end.y),
            offset,
            customLabel
        );
    }

    // Getters/Setters
    public Point2D.Double getStart() {
        return start;
    }

    public void setStart(Point2D.Double start) {
        this.start = start;
    }

    public Point2D.Double getEnd() {
        return end;
    }

    public void setEnd(Point2D.Double end) {
        this.end = end;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }

    public double getDistance() {
        return start.distance(end);
    }
}
