package io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteX;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteY;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;

/**
 * Contexte de rendu qui encapsule les méthodes de dessin du moteur W2D.
 *
 * Cette classe MASQUE les détails de AffineTransform et fournit une API simple
 * pour dessiner du texte et des flèches avec les transformations correctes.
 *
 * L'utilisateur final n'a PAS besoin de manipuler 'at' directement !
 */
public class RenderContext {

    private final CADWorkshopView view;
    private final Graphics2D g;

    public RenderContext(CADWorkshopView view, Graphics2D g) {
        this.view = view;
        this.g = g;
    }

    /**
     * Récupère le facteur de zoom actuel de la vue
     */
    private double getZoom() {
        return view.getZoom();
    }

    /**
     * Dessine du texte à une position donnée (coordonnées monde).
     *
     * IMPORTANT: Le texte garde une TAILLE FIXE malgré le zoom (txtCantChangeSize=true)
     *
     * SOLUTION: Pour que txtCantChangeSize fonctionne, le paramètre Size doit être
     * inversement proportionnel au zoom actuel : Size = 1.0 / Zoom
     *
     * @param text Texte à afficher
     * @param worldX Position X dans le monde
     * @param worldY Position Y dans le monde
     * @param rotation Rotation en degrés (0 = horizontal)
     * @param alignX Alignement horizontal
     * @param alignY Alignement vertical
     * @return Shape du texte (pour sélection)
     */
    public Shape drawText(String text, double worldX, double worldY, double rotation,
                          AlignTexteX alignX, AlignTexteY alignY) {
        // Pour taille constante à l'écran, Size doit être inversement proportionnel au zoom
        double zoom = getZoom();
        float size = (float) (1.0 / zoom);

        return view.drawString(g, text,
            (float) worldX, (float) worldY,
            (float) rotation,
            alignX, alignY,
            true,    // txtCantChangeSize = true
            size     // Size = 1/zoom pour taille écran constante
        );
    }

    /**
     * Dessine du texte simple (centré, pas de rotation)
     */
    public Shape drawText(String text, double worldX, double worldY) {
        return drawText(text, worldX, worldY, 0,
            AlignTexteX.CENTER, AlignTexteY.CENTER);
    }

    /**
     * Dessine du texte avec background (coordonnées monde)
     *
     * IMPORTANT: Le texte garde une TAILLE FIXE malgré le zoom (txtCantChangeSize=true)
     *
     * SOLUTION: Size = 1.0 / zoom pour taille écran constante
     */
    public Shape drawTextWithBackground(String text, double worldX, double worldY,
                                       double rotation, AlignTexteX alignX, AlignTexteY alignY,
                                       Color backgroundColor) {
        // Pour taille constante à l'écran, Size doit être inversement proportionnel au zoom
        double zoom = getZoom();
        float size = (float) (1.0 / zoom);

        return view.drawString(g, text,
            (float) worldX, (float) worldY,
            (float) rotation,
            alignX, alignY,
            true,    // txtCantChangeSize = true
            size,    // Size = 1/zoom pour taille écran constante
            true,    // drawBackground
            backgroundColor
        );
    }

    /**
     * Dessine une flèche entre 2 points (coordonnées monde)
     *
     * IMPORTANT: Les POINTES de flèches gardent une TAILLE FIXE malgré le zoom.
     * La LIGNE se transforme normalement avec les coordonnées monde.
     *
     * SOLUTION: On inverse-scale la taille des pointes basée sur le zoom actuel.
     *
     * @param startX Point de départ X
     * @param startY Point de départ Y
     * @param endX Point d'arrivée X
     * @param endY Point d'arrivée Y
     * @param arrowAtStart Flèche au début?
     * @param arrowAtEnd Flèche à la fin?
     * @return Shape de la flèche
     */
    public Shape drawArrow(double startX, double startY, double endX, double endY,
                          boolean arrowAtStart, boolean arrowAtEnd) {
        // Récupère le facteur de zoom actuel
        double zoom = getZoom();

        // Inverse le zoom pour garder les pointes de flèches à taille écran fixe
        float arrowScale = (float) (1.0 / zoom);

        return view.drawArrow2(g,
            new Point2D.Double(startX, startY),
            new Point2D.Double(endX, endY),
            arrowScale, arrowScale,
            arrowAtStart, arrowAtEnd
        );
    }

    /**
     * Dessine une flèche avec un label au milieu (COTATION)
     *
     * IMPORTANT: Les pointes et le texte gardent une TAILLE FIXE malgré le zoom.
     *
     * SOLUTION: On inverse-scale la taille des pointes basée sur le zoom actuel.
     *
     * @param label Texte de la cotation (ex: "50.5m")
     * @param startX Point de départ X
     * @param startY Point de départ Y
     * @param endX Point d'arrivée X
     * @param endY Point d'arrivée Y
     * @param offset Décalage du texte
     * @param arrowSize Taille des pointes (sera automatiquement ajustée au zoom)
     * @return Shape de la cotation
     */
    public Shape drawDimension(String label,
                              double startX, double startY,
                              double endX, double endY,
                              double offset, double arrowSize) {
        // Récupère le facteur de zoom actuel
        double zoom = getZoom();

        // Inverse le zoom pour garder les pointes à taille écran fixe
        float scaledArrowSize = (float) (arrowSize / zoom);

        return view.drawArrowWithString(g, label,
            0,  // angle beta
            (float) startX, (float) startY,
            (float) endX, (float) endY,
            (float) offset, (float) offset,
            scaledArrowSize
        );
    }

    /**
     * Applique une transformation locale (position + rotation)
     * et exécute le code de rendu dans ce contexte.
     *
     * NE PAS UTILISER - Cette méthode ne fonctionne pas correctement!
     * À la place, dessinez directement dans l'espace monde.
     */
    @Deprecated
    public void withLocalTransform(double worldX, double worldY, double rotation,
                                   Runnable renderer) {
        // Cette méthode est deprecated - ne pas utiliser!
        // Le problème est que Graphics2D ne connaît pas view.at
        renderer.run();
    }

    /**
     * Convertit des coordonnées locales en coordonnées monde
     */
    public Point2D localToWorld(double localX, double localY, double worldX, double worldY, double rotation) {
        double rad = Math.toRadians(rotation);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double rotatedX = localX * cos - localY * sin;
        double rotatedY = localX * sin + localY * cos;

        return new Point2D.Double(worldX + rotatedX, worldY + rotatedY);
    }

    /**
     * Dessine une Shape (en coordonnées monde) en la transformant en coordonnées écran.
     *
     * Cette méthode transforme chaque point de la Shape par convertRealXYToViewXY
     * puis dessine le résultat.
     *
     * @param worldShape Shape en coordonnées monde
     * @param filled true pour remplir, false pour contour uniquement
     */
    public void drawShape(Shape worldShape, boolean filled) {
        // Transforme la Shape monde → écran
        Path2D screenPath = new Path2D.Double();
        PathIterator it = worldShape.getPathIterator(null);
        double[] coords = new double[6];

        while (!it.isDone()) {
            int type = it.currentSegment(coords);

            switch (type) {
                case PathIterator.SEG_MOVETO:
                    Point2D p0 = view.convertRealXYToViewXY(coords[0], coords[1]);
                    screenPath.moveTo(p0.getX(), p0.getY());
                    break;
                case PathIterator.SEG_LINETO:
                    Point2D p1 = view.convertRealXYToViewXY(coords[0], coords[1]);
                    screenPath.lineTo(p1.getX(), p1.getY());
                    break;
                case PathIterator.SEG_QUADTO:
                    Point2D pq1 = view.convertRealXYToViewXY(coords[0], coords[1]);
                    Point2D pq2 = view.convertRealXYToViewXY(coords[2], coords[3]);
                    screenPath.quadTo(pq1.getX(), pq1.getY(), pq2.getX(), pq2.getY());
                    break;
                case PathIterator.SEG_CUBICTO:
                    Point2D pc1 = view.convertRealXYToViewXY(coords[0], coords[1]);
                    Point2D pc2 = view.convertRealXYToViewXY(coords[2], coords[3]);
                    Point2D pc3 = view.convertRealXYToViewXY(coords[4], coords[5]);
                    screenPath.curveTo(pc1.getX(), pc1.getY(),
                                      pc2.getX(), pc2.getY(),
                                      pc3.getX(), pc3.getY());
                    break;
                case PathIterator.SEG_CLOSE:
                    screenPath.closePath();
                    break;
            }
            it.next();
        }

        // Dessine la Shape transformée
        if (filled) {
            g.fill(screenPath);
        } else {
            g.draw(screenPath);
        }
    }

    /**
     * Dessine une Area (en coordonnées monde)
     */
    public void drawArea(Area worldArea, boolean filled) {
        drawShape(worldArea, filled);
    }

    /**
     * Dessine un rectangle (éventuellement rotaté) en utilisant des flèches
     *
     * @param centerX Centre X dans le monde
     * @param centerY Centre Y dans le monde
     * @param width Largeur
     * @param height Hauteur
     * @param rotation Rotation en degrés
     * @param color Couleur
     */
    public void drawRect(double centerX, double centerY, double width, double height,
                        double rotation, Color color) {
        // Calcule les 4 coins en coordonnées monde
        Point2D topLeft = localToWorld(-width/2, -height/2, centerX, centerY, rotation);
        Point2D topRight = localToWorld(width/2, -height/2, centerX, centerY, rotation);
        Point2D bottomRight = localToWorld(width/2, height/2, centerX, centerY, rotation);
        Point2D bottomLeft = localToWorld(-width/2, height/2, centerX, centerY, rotation);

        Color oldColor = g.getColor();
        g.setColor(color);

        // Dessine les 4 côtés avec drawArrow2
        drawArrow(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), false, false);
        drawArrow(topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), false, false);
        drawArrow(bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY(), false, false);
        drawArrow(bottomLeft.getX(), bottomLeft.getY(), topLeft.getX(), topLeft.getY(), false, false);

        g.setColor(oldColor);
    }

    /**
     * Récupère le Graphics2D (UNIQUEMENT pour overlay/console)
     * NE PAS UTILISER pour dessiner des objets métier!
     */
    public Graphics2D getGraphics() {
        return g;
    }

    /**
     * Récupère la vue (pour accès aux utilitaires)
     */
    public CADWorkshopView getView() {
        return view;
    }
}
