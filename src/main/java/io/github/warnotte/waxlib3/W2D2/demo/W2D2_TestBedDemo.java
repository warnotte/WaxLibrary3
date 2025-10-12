package io.github.warnotte.waxlib3.W2D2.demo;

import io.github.warnotte.waxlib3.W2D2.core.*;
import io.github.warnotte.waxlib3.W2D2.nodes.ShapeNode2D;
import io.github.warnotte.waxlib3.W2D2.nodes.TextNode2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class W2D2_TestBedDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Group2D root = new Group2D();
            Camera2D cam = new Camera2D().setInvertY(true).setZoom(4.0);
            Scene2D scene = new Scene2D(root, cam);
            Panel2D panel = new Panel2D(scene);

            // axes
            root.add(new ShapeNode2D().line(100,0).stroke(new Color(0,0,0,120)).strokePx(1f));
            root.add(new ShapeNode2D().line(-100,0).stroke(new Color(0,0,0,120)).strokePx(1f));
            root.add(new ShapeNode2D().line(0,100).stroke(new Color(0,0,0,120)).strokePx(1f));
            root.add(new ShapeNode2D().line(0,-100).stroke(new Color(0,0,0,120)).strokePx(1f));

            // texts center
            root.add(new TextNode2D("PX").font(new Font("SansSerif", Font.BOLD, 14)).color(Color.DARK_GRAY).keepPixelSize(true).align(TextNode2D.AlignX.CENTER, TextNode2D.AlignY.CENTER));
            root.add(new TextNode2D("WORLD").font(new Font("SansSerif", Font.PLAIN, 12)).color(Color.GRAY).keepPixelSize(false).align(TextNode2D.AlignX.CENTER, TextNode2D.AlignY.TOP).translate(0,-15));
            root.add(new TextNode2D("Rot 30").font(new Font("SansSerif", Font.PLAIN, 12)).color(new Color(0,100,0)).keepPixelSize(true).angleDeg(30).align(TextNode2D.AlignX.CENTER, TextNode2D.AlignY.CENTER).translate(0,20));

            // left group rotating
            Group2D rotating = new Group2D();
            rotating.translate(-60, 0);
            rotating.add(new ShapeNode2D().rect(30,20).fill(new Color(0,120,255,60)).stroke(new Color(0,90,190)).strokePx(2f));
            rotating.add(new ShapeNode2D().rect(22,14).fill(new Color(0,120,255,40)).stroke(new Color(0,90,190)).strokePx(2f).translate(18,0));
            rotating.add(new TextNode2D("Group").font(new Font("SansSerif", Font.PLAIN, 12)).color(new Color(0,90,190)).keepPixelSize(true).align(TextNode2D.AlignX.CENTER, TextNode2D.AlignY.BOTTOM).translate(0,-12));
            root.add(rotating);

            // right shapes
            root.add(new ShapeNode2D().ellipse(30,20).fill(new Color(255,140,0,60)).stroke(new Color(180,100,0)).strokePx(2f).translate(60,0));
            root.add(new ShapeNode2D().line(0,30).stroke(new Color(180,30,0)).strokePx(3f).translate(60,0));

            JFrame f = new JFrame("W2D2 TestBed");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(panel, BorderLayout.CENTER);
            f.setSize(900, 650);
            f.setLocationRelativeTo(null);
            f.setVisible(true);

            Timer timer = new Timer(25, new ActionListener() {
                double a=0;
                @Override public void actionPerformed(ActionEvent e) { a+=0.8; if (a>=360) a=0; rotating.rotateDeg(a); panel.repaint(); }
            });
            timer.start();
        });
    }
}
