/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Warnotte Renaud
 *
 */
public class Missile extends Entity
{


	long startLife = System.currentTimeMillis();
	
	/**
	 * @param position
	 * @param angle
	 */
	public Missile(Vector2D position, float angle)
	{
		super(position, angle);
		speed = 5.0f;
	}

	@Override
	public boolean isDead()
	{
		long actLife = System.currentTimeMillis();
		if ((actLife - startLife) > 2000)
			return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see WaxLibrary.W2D.PanelGraphique.tests.ships.Entity#getShape()
	 */
	@Override
	public Shape getShape()
	{
		Shape shp = new Rectangle2D.Double(-1,-1,2,2);
		
		shp = applyTransform(shp);
		
		
		return shp;
	}



	/* (non-Javadoc)
	 * @see WaxLibrary.W2D.PanelGraphique.tests.ships.Entity#update()
	 */
	@Override
	public void update()
	{
		super.update();
		
		//if (speed>0) speed=speed-0.05f;
		//if (speed<0) speed=speed+0.05f;
	}

}
