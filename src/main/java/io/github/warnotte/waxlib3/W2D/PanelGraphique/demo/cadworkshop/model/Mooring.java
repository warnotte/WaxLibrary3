package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
 * Point d'amarrage (ancrage) pour plateforme flottante.
 *
 * OBJET MÉTIER CONCRET avec:
 * - Position d'ancrage
 * - Longueur de câble (en m)
 * - Profondeur d'ancrage (en m)
 * - Force de traction maximale (en kN)
 * - Point d'attache sur la plateforme
 */
public class Mooring implements CADObject {

    private final String id;
    private String name;
    private Point2D.Double anchorPosition;   // Position de l'ancre au fond
    private Point2D.Double attachPoint;      // Point d'attache sur la plateforme

    // Caractéristiques techniques
    private double cableLenght;          // Longueur du câble (en m)
    private double depth;                // Profondeur d'ancrage (en m)
    private double maxTension;           // Force max (en kN)

    private Color color;

    public Mooring(String name, Point2D.Double anchorPosition, Point2D.Double attachPoint,
                   double cableLength, double depth, double maxTension) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.anchorPosition = anchorPosition;
        this.attachPoint = attachPoint;
        this.cableLenght = cableLength;
        this.depth = depth;
        this.maxTension = maxTension;
        this.color = new Color(139, 69, 19);  // Saddle brown (chaîne rouillée)
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CADObjectType getType() {
        return CADObjectType.LINK;
    }

    @Override
    public Point2D.Double getPosition() {
        return anchorPosition;
    }

    @Override
    public void setPosition(Point2D.Double position) {
        this.anchorPosition = position;
    }

    @Override
    public double getRotation() {
        return 0;
    }

    @Override
    public void setRotation(double degrees) {
        // Les amarrages ne tournent pas
    }

    @Override
    public Rectangle2D getBounds() {
        double minX = Math.min(anchorPosition.x, attachPoint.x) - 1;
        double minY = Math.min(anchorPosition.y, attachPoint.y) - 1;
        double maxX = Math.max(anchorPosition.x, attachPoint.x) + 1;
        double maxY = Math.max(anchorPosition.y, attachPoint.y) + 1;

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean contains(Point2D point) {
        // Vérifie si le point est proche du câble
        Line2D cable = new Line2D.Double(anchorPosition, attachPoint);
        return cable.ptSegDist(point) < 0.5;  // 50cm de tolérance
    }

    @Override
    public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered) {
        List<SelectionTuple<Shape, Object>> selectables = new ArrayList<>();

        // Dessine le câble (utilise uniquement drawArrow de RenderContext)
        ctx.drawArrow(
            anchorPosition.x, anchorPosition.y,
            attachPoint.x, attachPoint.y,
            false, false
        );

        // Label au milieu du câble
        double midX = (anchorPosition.x + attachPoint.x) / 2;
        double midY = (anchorPosition.y + attachPoint.y) / 2;

        ctx.drawText(
            String.format("%s\n%.0fm, %.0fkN", name, cableLenght, maxTension),
            midX, midY,
            0,
            AlignTexteX.CENTER,
            AlignTexteY.CENTER
        );

        // Profondeur à l'ancre (avec symbole)
        ctx.drawText(
            String.format("⚓ %.0fm", depth),
            anchorPosition.x, anchorPosition.y - 1.5,
            0,
            AlignTexteX.CENTER,
            AlignTexteY.BOTTOM
        );

        // Point d'attache (petit symbole)
        ctx.drawText(
            "●",
            attachPoint.x, attachPoint.y,
            0,
            AlignTexteX.CENTER,
            AlignTexteY.CENTER
        );

        // Ajoute à la sélection
        selectables.add(new SelectionTuple<>(getBounds(), this));

        return selectables;
    }

    @Override
    public boolean isDraggable() {
        return false;  // Les amarrages ne sont pas déplaçables (pour l'instant)
    }

    @Override
    public boolean isRotatable() {
        return false;
    }

    @Override
    public CADObject clone() {
        return new Mooring(
            name + " (copy)",
            new Point2D.Double(anchorPosition.x + 5, anchorPosition.y + 5),
            new Point2D.Double(attachPoint.x + 5, attachPoint.y + 5),
            cableLenght,
            depth,
            maxTension
        );
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public Point2D.Double getAnchorPosition() {
        return anchorPosition;
    }

    public Point2D.Double getAttachPoint() {
        return attachPoint;
    }

    public void setAttachPoint(Point2D.Double attachPoint) {
        this.attachPoint = attachPoint;
    }

    public double getCableLength() {
        return cableLenght;
    }

    public double getDepth() {
        return depth;
    }

    public double getMaxTension() {
        return maxTension;
    }
}
