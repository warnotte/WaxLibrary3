package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper.Renderer;

import java.awt.Graphics2D;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper.Band;

public interface DefaultMapRendererInterface {

    public void paint(Graphics2D g2, Band band, int x, int y, int w, float h);
}
