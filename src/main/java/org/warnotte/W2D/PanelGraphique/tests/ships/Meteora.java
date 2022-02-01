/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Warnotte Renaud
 *
 */
public class Meteora extends Entity
{

	float Size = 8.0f;
	/**
	 * @param position
	 * @param angle
	 */
	public Meteora(Vector2D position, float angle, float Size)
	{
		super(position, angle);
		speed = 0.5f;
		this.Size = Size;
	}

	/* (non-Javadoc)
	 * @see WaxLibrary.W2D.PanelGraphique.tests.ships.Entity#getShape()
	 */
	@Override
	public Shape getShape()
	{
		Shape shp = new Ellipse2D.Float(-Size/2.0f, -Size/2.0f, Size, Size);
		return applyTransform(shp);
		
	}

	/* (non-Javadoc)
	 * @see WaxLibrary.W2D.PanelGraphique.tests.ships.Entity#isDead()
	 */
	@Override
	public boolean isDead()
	{
		return false;
	}

}
