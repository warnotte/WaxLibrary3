package io.github.warnotte.waxlib3.W2D2.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Group2D extends Node2D {
    private final List<Node2D> children = new ArrayList<>();
    public Group2D add(Node2D n) { if (n!=null) { ((Node2D)n).setParent(this); children.add(n);} return this; }
    public List<Node2D> children() { return children; }

    @Override
    public void render(RenderContext ctx) {
        children.stream().sorted(Comparator.comparingInt(Node2D::zIndex)).forEach(n -> {
            if (n.isVisible()) n.render(ctx);
        });
    }

    @Override
    public boolean hitTest(PickContext ctx) {
        return children.stream().anyMatch(n -> n.hitTest(ctx));
    }

    @Override
    public boolean hitTestRect(java.awt.geom.Rectangle2D screenRect, java.awt.geom.AffineTransform viewAt) {
        return children.stream().anyMatch(n -> n.hitTestRect(screenRect, viewAt));
    }

    @Override
    public java.awt.geom.Rectangle2D boundsLocal() { return null; }
}

