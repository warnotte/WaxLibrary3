package io.github.warnotte.waxlib3.W2D2.demo;

import io.github.warnotte.waxlib3.W2D2.core.*;
import io.github.warnotte.waxlib3.W2D2.nodes.ArrowDimensionNode2D;
import io.github.warnotte.waxlib3.W2D2.nodes.ShapeNode2D;

import javax.swing.*;
import java.awt.*;

public class W2D2_ArrowDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Group2D root = new Group2D();
            Camera2D cam = new Camera2D().setInvertY(true).setZoom(3.5);
            Scene2D scene = new Scene2D(root, cam);
            Panel2D panel = new Panel2D(scene);

            // references
            root.add(new ShapeNode2D().rect(6,6).fill(new Color(0,0,0,100)).translate(-40,0));
            root.add(new ShapeNode2D().rect(6,6).fill(new Color(0,0,0,100)).translate(40,0));

            root.add(new ArrowDimensionNode2D(-40,0, 40,0).label("80.0 u").color(Color.BLACK).strokePx(2f).headSizePx(8f).textOffsetPx(12f).font(new Font("SansSerif", Font.PLAIN, 12)));
            root.add(new ArrowDimensionNode2D(0,-40, 0,40).label("80.0 u").color(new Color(0,100,0)).strokePx(2f).headSizePx(8f).textOffsetPx(12f).font(new Font("SansSerif", Font.PLAIN, 12)));
            root.add(new ArrowDimensionNode2D(-30,-20, 50,30).label("93.4 u").color(new Color(120,0,0)).strokePx(2f).headSizePx(8f).textOffsetPx(12f).font(new Font("SansSerif", Font.PLAIN, 12)));

            JFrame f = new JFrame("W2D2 Arrow Demo");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(panel, BorderLayout.CENTER);
            f.setSize(900, 650);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
