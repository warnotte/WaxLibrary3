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
 * Panneau solaire sur une plateforme flottante.
 *
 * OBJET MÉTIER CONCRET avec:
 * - Dimensions standards de panneau solaire (ex: 1.65m × 1.0m)
 * - Puissance (en Wc - Watt-crête)
 * - Angle d'inclinaison
 * - Position LOCALE par rapport à la plateforme
 */
public class SolarPanel implements CADObject {

    private final String id;
    private String name;
    private Point2D.Double localPosition;  // Position LOCALE sur la plateforme
    private double localRotation;          // Rotation LOCALE (orientation)

    // Caractéristiques techniques RÉELLES
    private double longueur;     // En mètres (standard: 1.65m)
    private double largeur;      // En mètres (standard: 1.0m)
    private double puissance;    // En Watt-crête (ex: 300Wc)
    private double inclinaison;  // Angle d'inclinaison en degrés (ex: 15°)

    private Color color;

    /**
     * Crée un panneau solaire standard (300Wc, 1.65m × 1.0m)
     */
    public SolarPanel(String name, double localX, double localY, double localRotation) {
        this(name, localX, localY, localRotation, 1.65, 1.0, 300, 15);
    }

    /**
     * Crée un panneau solaire avec dimensions custom
     */
    public SolarPanel(String name, double localX, double localY, double localRotation,
                     double longueur, double largeur, double puissance, double inclinaison) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.localPosition = new Point2D.Double(localX, localY);
        this.localRotation = localRotation;
        this.longueur = longueur;
        this.largeur = largeur;
        this.puissance = puissance;
        this.inclinaison = inclinaison;
        this.color = new Color(30, 60, 100);  // Bleu foncé (cellules photovoltaïques)
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
            localPosition.x - longueur/2,
            localPosition.y - largeur/2,
            longueur,
            largeur
        );
    }

    @Override
    public boolean contains(Point2D point) {
        return getBounds().contains(point);
    }

    @Override
    public List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered) {
        List<SelectionTuple<Shape, Object>> selectables = new ArrayList<>();

        Graphics2D g = ctx.getGraphics();
        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();

        // Utilise withLocalTransform pour position + rotation LOCALES
        ctx.withLocalTransform(localPosition.x, localPosition.y, localRotation, () -> {

            // Rectangle du panneau
            Rectangle2D panelRect = new Rectangle2D.Double(
                -longueur/2, -largeur/2, longueur, largeur
            );

            // Couleur selon état
            Color renderColor;
            if (isSelected) {
                renderColor = Color.ORANGE;
            } else if (isHovered) {
                renderColor = color.brighter();
            } else {
                renderColor = color;
            }

            g.setColor(renderColor);
            g.fill(panelRect);

            // Cadre (aluminium)
            g.setColor(Color.GRAY);
            g.setStroke(new BasicStroke(0.05f));
            g.draw(panelRect);

            // Grille des cellules photovoltaïques (6×10 cellules typiques)
            g.setColor(new Color(200, 200, 200, 100));
            int cellsX = 6;
            int cellsY = 10;
            double cellWidth = longueur / cellsX;
            double cellHeight = largeur / cellsY;

            for (int i = 1; i < cellsX; i++) {
                double x = -longueur/2 + i * cellWidth;
                g.drawLine(
                    (int)(x * 100), (int)(-largeur/2 * 100),
                    (int)(x * 100), (int)(largeur/2 * 100)
                );
            }

            for (int j = 1; j < cellsY; j++) {
                double y = -largeur/2 + j * cellHeight;
                g.drawLine(
                    (int)(-longueur/2 * 100), (int)(y * 100),
                    (int)(longueur/2 * 100), (int)(y * 100)
                );
            }

            // Label avec puissance (AVEC txtCantChangeSize=true!)
            ctx.drawText(
                String.format("%dWc", (int)puissance),
                0, 0,
                0,
                AlignTexteX.CENTER,
                AlignTexteY.CENTER
            );

            // Inclinaison en petit
            ctx.drawText(
                String.format("%.0f°", inclinaison),
                longueur/2 - 0.1, -largeur/2 + 0.1,
                0,
                AlignTexteX.RIGHT,
                AlignTexteY.TOP
            );
        });

        // Ajoute à la sélection
        selectables.add(new SelectionTuple<>(getBounds(), this));

        g.setColor(oldColor);
        g.setStroke(oldStroke);

        return selectables;
    }

    @Override
    public boolean isDraggable() {
        return false;  // Les panneaux ne se déplacent pas individuellement
    }

    @Override
    public boolean isRotatable() {
        return false;
    }

    @Override
    public CADObject clone() {
        return new SolarPanel(
            name + " (copy)",
            localPosition.x + 2,
            localPosition.y + 2,
            localRotation,
            longueur,
            largeur,
            puissance,
            inclinaison
        );
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getPuissance() {
        return puissance;
    }

    public double getInclinaison() {
        return inclinaison;
    }

    public void setInclinaison(double inclinaison) {
        this.inclinaison = inclinaison;
    }
}
