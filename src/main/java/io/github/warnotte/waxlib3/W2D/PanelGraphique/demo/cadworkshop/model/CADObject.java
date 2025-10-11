package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.model;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionTuple;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view.RenderContext;

/**
 * Interface de base pour tous les objets CAO.
 * Définit le contrat pour le rendu, la sélection et la manipulation.
 */
public interface CADObject {

    /**
     * Identifiant unique de l'objet
     */
    String getId();

    /**
     * Type d'objet CAO (Machine, Dimension, Label, Link)
     */
    CADObjectType getType();

    /**
     * Position centrale de l'objet dans le monde
     */
    Point2D.Double getPosition();
    void setPosition(Point2D.Double position);

    /**
     * Rotation de l'objet en degrés
     */
    double getRotation();
    void setRotation(double degrees);

    /**
     * Bounding box dans les coordonnées monde
     */
    Rectangle2D getBounds();

    /**
     * Vérifie si un point (coordonnées monde) est dans l'objet
     */
    boolean contains(Point2D point);

    /**
     * Rendu de l'objet
     *
     * IMPORTANT: Utilisez RenderContext au lieu de manipuler Graphics2D directement.
     * Cela cache les détails de AffineTransform et garantit que txtCantChangeSize fonctionne.
     *
     * @param ctx Context de rendu qui encapsule les méthodes W2D
     * @param isSelected Si l'objet est sélectionné
     * @param isHovered Si la souris survole l'objet
     * @return Liste des shapes à ajouter au système de sélection
     */
    List<SelectionTuple<Shape, Object>> render(RenderContext ctx, boolean isSelected, boolean isHovered);

    /**
     * Permet de savoir si l'objet peut être déplacé
     */
    boolean isDraggable();

    /**
     * Permet de savoir si l'objet peut être rotaté
     */
    boolean isRotatable();

    /**
     * Clone l'objet
     */
    CADObject clone();

    /**
     * Types d'objets CAO disponibles
     */
    enum CADObjectType {
        MACHINE("Machine/Equipment"),
        DIMENSION("Dimension/Measure"),
        LABEL("Annotation Label"),
        LINK("Connection Link"),
        GRID("Reference Grid");

        private final String displayName;

        CADObjectType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
