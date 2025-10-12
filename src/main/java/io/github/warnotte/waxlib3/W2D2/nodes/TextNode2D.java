package io.github.warnotte.waxlib3.W2D2.nodes;

import io.github.warnotte.waxlib3.W2D2.core.Node2D;
import io.github.warnotte.waxlib3.W2D2.core.PickContext;
import io.github.warnotte.waxlib3.W2D2.core.RenderContext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TextNode2D extends Node2D {
    public enum AlignX { LEFT, CENTER, RIGHT }
    public enum AlignY { TOP, CENTER, BOTTOM }

    private String text;
    private Font font = new Font("SansSerif", Font.PLAIN, 12);
    private Color color = Color.BLACK;
    private boolean keepPixelSize = false;
    private double angleDeg = 0.0;
    private AlignX alignX = AlignX.LEFT;
    private AlignY alignY = AlignY.CENTER;

    public TextNode2D(String text) { this.text = text; }
    public TextNode2D font(Font f) { this.font = f; return this; }
    public TextNode2D color(Color c) { this.color = c; return this; }
    public TextNode2D keepPixelSize(boolean k) { this.keepPixelSize = k; return this; }
    public TextNode2D angleDeg(double a) { this.angleDeg = a; return this; }
    public TextNode2D align(AlignX ax, AlignY ay) { this.alignX=ax; this.alignY=ay; return this; }

    @Override
    public void render(RenderContext ctx) {
        if (text == null || text.isEmpty()) return;
        Graphics2D g = ctx.g2d();
        AffineTransform old = g.getTransform();
        AffineTransform at = ctx.viewAt();
        at.concatenate(worldTransform());
        if (keepPixelSize) {
            double s = ctx.pixelScale(); if (s != 0) at.scale(s, s);
        }
        if (angleDeg != 0) at.rotate(Math.toRadians(angleDeg));
        g.setTransform(at);
        g.setFont(font);
        g.setColor(color);
        GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(), text);
        Rectangle2D vb = gv.getVisualBounds();
        double tx=0, ty=0;
        switch (alignX) {
            case LEFT:   tx = -vb.getX(); break;
            case CENTER: tx = -vb.getX() - vb.getWidth()/2.0; break;
            case RIGHT:  tx = -vb.getX() - vb.getWidth(); break;
        }
        switch (alignY) {
            case TOP:    ty = -vb.getY(); break;
            case CENTER: ty = -vb.getY() - vb.getHeight()/2.0; break;
            case BOTTOM: ty = -vb.getY() - vb.getHeight(); break;
        }
        g.drawGlyphVector(gv, (float)tx, (float)ty);
        g.setTransform(old);
    }

    @Override
    public boolean hitTest(PickContext ctx) {
        try {
            AffineTransform full = ctx.viewAt();
            full.concatenate(worldTransform());
            if (keepPixelSize) { double s = ctx.pixelScale(); if (s != 0) full.scale(s, s); }
            if (angleDeg != 0) full.rotate(Math.toRadians(angleDeg));
            AffineTransform inv = full.createInverse();
            Point2D local = inv.transform(ctx.screenPt(), null);
            java.awt.Shape outline = outlineShape();
            return outline != null && outline.contains(local);
        } catch (Exception ex) { return false; }
    }

    @Override
    public boolean hitTestRect(Rectangle2D screenRect, AffineTransform viewAt) {
        try {
            AffineTransform full = screenTransform(viewAt);
            java.awt.Shape sb = full.createTransformedShape(outlineShape());
            return sb.intersects(screenRect);
        } catch (Exception ex) { return false; }
    }

    @Override
    public Rectangle2D boundsLocal() {
        java.awt.Shape s = outlineShape();
        return (s!=null) ? s.getBounds2D() : new Rectangle2D.Double();
    }

    @Override
    public java.awt.Shape shapeLocal() { return outlineShape(); }

    private java.awt.Shape outlineShape() {
        if (text == null || text.isEmpty()) return null;
        // Build outline at local origin with alignment applied
        Font f = font;
        GlyphVector gv = f.createGlyphVector(new java.awt.font.FontRenderContext(new AffineTransform(), true, true), text);
        Rectangle2D vb = gv.getVisualBounds();
        double tx=0, ty=0;
        switch (alignX) {
            case LEFT:   tx = -vb.getX(); break;
            case CENTER: tx = -vb.getX() - vb.getWidth()/2.0; break;
            case RIGHT:  tx = -vb.getX() - vb.getWidth(); break;
        }
        switch (alignY) {
            case TOP:    ty = -vb.getY(); break;
            case CENTER: ty = -vb.getY() - vb.getHeight()/2.0; break;
            case BOTTOM: ty = -vb.getY() - vb.getHeight(); break;
        }
        AffineTransform at = new AffineTransform();
        at.translate(tx, ty);
        return at.createTransformedShape(gv.getOutline());
    }

    @Override
    public AffineTransform screenTransform(AffineTransform viewAt) {
        AffineTransform at = new AffineTransform(viewAt);
        at.concatenate(worldTransform());
        if (keepPixelSize) {
            // Derive zoom from viewAt: zoom = sqrt(a^2 + b^2)
            double a = viewAt.getScaleX();
            double b = viewAt.getShearX();
            double zoom = Math.hypot(a, b);
            double s = (zoom == 0) ? 1.0 : (1.0/zoom);
            at.scale(s, s);
        }
        if (angleDeg != 0) at.rotate(Math.toRadians(angleDeg));
        return at;
    }
}
