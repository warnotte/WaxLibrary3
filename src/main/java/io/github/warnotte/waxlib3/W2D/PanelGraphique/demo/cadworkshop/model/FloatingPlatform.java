package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
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
 * Plateforme flottante pour parc solaire flottant.
 *
 * OBJET MÉTIER CONCRET avec dimensions réelles :
 * - longueur, largeur (en mètres)
 * - poids (en kg)
 * - flotabilité (en kg)
 * - tirant d'eau (en m)
 * - franc-bord (en m)
 * - Contient des panneaux solaires (enfants)
 *
 * Inspiré du projet FloatSolarWindFarm_2
 */
public class FloatingPlatform implements CADObject {

    private final String id;
    private String name;
    private Point2D.Double position;  // Position dans le monde (en mètres)
    private double rotation;          // Rotation en degrés

    // Caractéristiques physiques RÉELLES
    private double longueur;      // En mètres
    private double largeur;       // En mètres
    private double poids;         // En kg
    private double flotabilite;   // En kg
    private double tirantEau;     // Profondeur immergée (en m)
    private double francBord;     // Hauteur émergée (en m)

    private Color color;

    // Panneaux solaires sur cette plateforme
    private List<SolarPanel> solarPanels;

    /**
     * Crée une plateforme flottante
     */
    public FloatingPlatform(String name, Point2D.Double position,
                           double longueur, double largeur,
                           double poids, double flotabilite) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.position = position;
        this.longueur = longueur;
        this.largeur = largeur;
        this.poids = poids;
        this.flotabilite = flotabilite;
        this.rotation = 0;
        this.tirantEau = 0.5;  // 50cm par défaut
        this.francBord = 0.3;  // 30cm par défaut
        this.color = new Color(70, 130, 180);  // Steel blue
        this.solarPanels = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CADObjectType getType() {
        return CADObjectType.MACHINE;  // Réutilise le type existant
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
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(
            position.x - longueur/2,
            position.y - largeur/2,
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

        // Change la couleur selon l'état
        Graphics2D g = ctx.getGraphics();
        Color oldColor = g.getColor();

        Color renderColor = color;
        if (isSelected) {
            renderColor = Color.MAGENTA;  // Magenta pour sélectionné
        } else if (isHovered) {
            renderColor = color.brighter();  // Plus clair si survolé
        }
        g.setColor(renderColor);

        // Dessine un rectangle simple pour la plateforme
        // Les 4 coins
        Point2D topLeft = ctx.localToWorld(-longueur/2, -largeur/2, position.x, position.y, rotation);
        Point2D topRight = ctx.localToWorld(longueur/2, -largeur/2, position.x, position.y, rotation);
        Point2D bottomRight = ctx.localToWorld(longueur/2, largeur/2, position.x, position.y, rotation);
        Point2D bottomLeft = ctx.localToWorld(-longueur/2, largeur/2, position.x, position.y, rotation);

        // Dessine les 4 côtés
        ctx.drawArrow(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), false, false);
        ctx.drawArrow(topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), false, false);
        ctx.drawArrow(bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY(), false, false);
        ctx.drawArrow(bottomLeft.getX(), bottomLeft.getY(), topLeft.getX(), topLeft.getY(), false, false);

        // Nom de la plateforme au centre (AVEC txtCantChangeSize=true!)
        String displayName = name;
        if (isSelected) {
            displayName = "★ " + name + " ★";  // Marqueur de sélection
        }
        ctx.drawText(
            displayName,
            position.x, position.y,
            0,
            AlignTexteX.CENTER,
            AlignTexteY.CENTER
        );

        // Dimensions au-dessus
        Point2D topCenter = ctx.localToWorld(0, -largeur/2 - 1.5, position.x, position.y, rotation);
        ctx.drawText(
            String.format("%.1fm × %.1fm", longueur, largeur),
            topCenter.getX(), topCenter.getY(),
            0,
            AlignTexteX.CENTER,
            AlignTexteY.BOTTOM
        );

        // Poids et flotabilité en-dessous
        Point2D bottomCenter = ctx.localToWorld(0, largeur/2 + 1.5, position.x, position.y, rotation);
        ctx.drawText(
            String.format("%.0fkg | Flot:%.0fkg | TE:%.2fm FB:%.2fm",
                poids, flotabilite, tirantEau, francBord),
            bottomCenter.getX(), bottomCenter.getY(),
            0,
            AlignTexteX.CENTER,
            AlignTexteY.TOP
        );

        // Rend tous les panneaux solaires sur cette plateforme
        for (SolarPanel panel : solarPanels) {
            // Calcule la position monde du panneau
            Point2D panelWorld = ctx.localToWorld(
                panel.getPosition().x,
                panel.getPosition().y,
                position.x,
                position.y,
                rotation
            );

            // Rotation totale = rotation plateforme + rotation locale panneau
            double totalRotation = rotation + panel.getRotation();

            // Dessine le panneau
            renderPanel(ctx, panel, panelWorld.getX(), panelWorld.getY(), totalRotation);
        }

        // Restaure la couleur
        g.setColor(oldColor);

        // Ajoute à la sélection
        selectables.add(new SelectionTuple<>(getBounds(), this));

        return selectables;
    }

    /**
     * Rend un panneau solaire à une position monde donnée
     */
    private void renderPanel(RenderContext ctx, SolarPanel panel,
                            double worldX, double worldY, double totalRotation) {
        // Les 4 coins du panneau
        Point2D topLeft = ctx.localToWorld(-panel.getLongueur()/2, -panel.getLargeur()/2, worldX, worldY, totalRotation);
        Point2D topRight = ctx.localToWorld(panel.getLongueur()/2, -panel.getLargeur()/2, worldX, worldY, totalRotation);
        Point2D bottomRight = ctx.localToWorld(panel.getLongueur()/2, panel.getLargeur()/2, worldX, worldY, totalRotation);
        Point2D bottomLeft = ctx.localToWorld(-panel.getLongueur()/2, panel.getLargeur()/2, worldX, worldY, totalRotation);

        // Dessine les 4 côtés
        ctx.drawArrow(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), false, false);
        ctx.drawArrow(topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), false, false);
        ctx.drawArrow(bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY(), false, false);
        ctx.drawArrow(bottomLeft.getX(), bottomLeft.getY(), topLeft.getX(), topLeft.getY(), false, false);

        // Label avec puissance (txtCantChangeSize=true automatique)
        ctx.drawText(
            String.format("%.0fWc", panel.getPuissance()),
            worldX, worldY,
            0,
            AlignTexteX.CENTER,
            AlignTexteY.CENTER
        );
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
        FloatingPlatform copy = new FloatingPlatform(
            this.name + " (copy)",
            new Point2D.Double(position.x + 5, position.y + 5),
            this.longueur,
            this.largeur,
            this.poids,
            this.flotabilite
        );
        copy.rotation = this.rotation;
        copy.tirantEau = this.tirantEau;
        copy.francBord = this.francBord;
        return copy;
    }

    // Gestion des panneaux solaires
    public void addSolarPanel(SolarPanel panel) {
        this.solarPanels.add(panel);
    }

    public List<SolarPanel> getSolarPanels() {
        return solarPanels;
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getPoids() {
        return poids;
    }

    public double getFlotabilite() {
        return flotabilite;
    }

    public double getTirantEau() {
        return tirantEau;
    }

    public void setTirantEau(double tirantEau) {
        this.tirantEau = tirantEau;
    }

    public double getFrancBord() {
        return francBord;
    }

    public void setFrancBord(double francBord) {
        this.francBord = francBord;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
