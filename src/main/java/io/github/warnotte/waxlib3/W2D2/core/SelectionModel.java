package io.github.warnotte.waxlib3.W2D2.core;

import java.util.LinkedHashSet;
import java.util.Set;

public class SelectionModel {
    private final Set<Node2D> selected = new LinkedHashSet<>();
    public Set<Node2D> selected() { return selected; }
    public void clear() { selected.clear(); }
    public void select(Node2D n, boolean toggle) {
        if (n == null) return;
        if (toggle && selected.contains(n)) selected.remove(n); else selected.add(n);
    }
}

