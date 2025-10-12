package io.github.warnotte.waxlib3.W2D2.core;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Scene2D {
    private final Group2D root;
    private final Camera2D camera;

    public Scene2D(Group2D root, Camera2D camera) {
        this.root = root;
        this.camera = camera;
    }
    public Group2D root() { return root; }
    public Camera2D camera() { return camera; }

    public void render(Graphics2D g2d, java.awt.Dimension viewport) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform viewAt = camera.buildViewTransform(viewport);
        RenderContext ctx = new RenderContext(g2d, viewAt, camera.pixelScale());
        root.render(ctx);
    }

    public Node2D pick(Point2D screenPt, java.awt.Dimension viewport) {
        AffineTransform viewAt = camera.buildViewTransform(viewport);
        PickContext pctx = new PickContext(screenPt, viewAt, camera.pixelScale());
        return pickRecursive(root, pctx);
    }

    private Node2D pickRecursive(Node2D node, PickContext pctx) {
        if (!node.isVisible()) return null;
        if (node instanceof Group2D) {
            Group2D g = (Group2D) node;
            return g.children().stream().sorted((a,b) -> Integer.compare(b.zIndex(), a.zIndex()))
                .map(n -> pickRecursive(n, pctx))
                .filter(n -> n != null)
                .findFirst().orElse(null);
        }
        return node.hitTest(pctx) ? node : null;
    }
}

