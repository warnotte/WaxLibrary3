package io.github.warnotte.waxlib3.core.Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class StripePaintWax
{

	
	public static void main(String args[]){
		
		
	}
	
	public static TexturePaint createDotPaint(Color color, Color color2)
    {
        BufferedImage bufferedimage = new BufferedImage(6, 6, 1);
        bufferedimage.setRGB(0, 0, color2.getRGB());
        bufferedimage.setRGB(1, 0, color2.getRGB());
        bufferedimage.setRGB(0, 1, color2.getRGB());
        bufferedimage.setRGB(1, 1, color.getRGB());
        Graphics g = bufferedimage.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 6, 6);
        g.setColor(color2);
        g.fillRect(3, 3, 4, 4);
        TexturePaint texturepaint = new TexturePaint(bufferedimage, new Rectangle(0, 0, 6, 6));
        return texturepaint;
    }

    public static TexturePaint create45LinePaint(Color foreGroundColor, Paint backGroundColor, int spacing)
    {
    	BufferedImage bufferedimage = new BufferedImage(spacing, spacing, 1);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setPaint(backGroundColor);
        graphics2d.fillRect(0, 0, spacing, spacing);
        graphics2d.setColor(foreGroundColor);
        graphics2d.drawLine(0, 0, spacing, spacing);
        graphics2d.dispose();
        TexturePaint texturepaint = new TexturePaint(bufferedimage, new Rectangle(0, 0, spacing, spacing));
        return texturepaint;
    }

    public static TexturePaint create45LinePaintInvert(Color foreGroundColor, Paint backGroundColor, int spacing)
    {
    	BufferedImage bufferedimage = new BufferedImage(spacing, spacing, 1);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setPaint(backGroundColor);
        graphics2d.fillRect(0, 0, spacing, spacing);
        graphics2d.setColor(foreGroundColor);
        graphics2d.drawLine(spacing-1, 0, 0, spacing-1);
        graphics2d.dispose();
        TexturePaint texturepaint = new TexturePaint(bufferedimage, new Rectangle(0, 0, spacing, spacing));
        return texturepaint;
    }
    
    public static TexturePaint createLinePaint(Color foreGroundColor, Paint backGroundColor, int spacing, boolean vertical)
    {
    	BufferedImage bufferedimage = new BufferedImage(spacing, spacing, 1);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setPaint(backGroundColor);
        graphics2d.fillRect(0, 0, spacing, spacing);
        graphics2d.setColor(foreGroundColor);
        
        if (vertical)
        	graphics2d.drawLine(spacing/2, 0, spacing/2, spacing);
        else
        	graphics2d.drawLine(0, spacing/2, spacing, spacing/2);
        
        graphics2d.dispose();
        TexturePaint texturepaint = new TexturePaint(bufferedimage, new Rectangle(0, 0, spacing, spacing));
        return texturepaint;
    }

}
