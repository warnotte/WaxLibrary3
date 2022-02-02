package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper.Renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper.Band;

public class DefaultMapRenderer implements DefaultMapRendererInterface {
    Random rand = new Random();
    public void paint(Graphics2D g2, Band band, int x, int y, int w, float h) {
	rand.setSeed(band.hashCode());
	g2.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
	g2.fillRect(0, y, w, (int) (y+h));
    }
}
