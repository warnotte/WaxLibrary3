/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Warnotte Renaud
 *
 */
public class xploSpr extends Entity
{
	//public Vector2D pos = null;
	public List<BufferedImage> list = null;
	int frame = 0;
	long old_time = System.currentTimeMillis();
	
	/**
	 * @param pos
	 * @param list
	 */
	public xploSpr(Vector2D pos, List<BufferedImage> list)
	{
		super(pos, 0);
		this.list = list;
	}
	
	public void draw(Graphics2D g, AffineTransform af)
	{
		Point2D.Float pt = new Point2D.Float((int)position.getX(), (int)position.getY());
		pt = (Float) af.transform(pt, pt);
		BufferedImage image = getCurrentImge();
		g.drawImage(image, null, (int)pt.getX()-image.getWidth()/2, (int) pt.getY()-image.getHeight()/2);
	}

	/**
	 * @return
	 */
	BufferedImage getCurrentImge()
	{
		return list.get(frame%list.size());
	}
	
	@Override
	public boolean isDead()
	{
		if (frame>list.size()-1)
			return true;
		return false;
	}
	
	@Override
	public void update()
	{
		long time = System.currentTimeMillis();
		
		if (time-old_time> 1000/50.0)
		{
			old_time = time;
			frame++;
		}
		
	}

	/* (non-Javadoc)
	 * @see WaxLibrary.W2D.PanelGraphique.tests.ships.Entity#getShape()
	 */
	@Override
	public Shape getShape()
	{
		Rectangle2D.Float pt = new Rectangle2D.Float(
				(int)position.getX()-getCurrentImge().getWidth()/2,
				(int)position.getY()-getCurrentImge().getHeight()/2,
				getCurrentImge().getWidth(),
				getCurrentImge().getHeight());
		return pt;
	}
}
