/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Shape;
import java.awt.geom.Path2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Warnotte Renaud
 *
 */
public class PlayerV extends Entity
{
	long lastFireTime = System.currentTimeMillis();
	
	/**
	 * @param position
	 * @param angle
	 */
	public PlayerV(Vector2D position, float angle)
	{
		super(position, angle);
	}
	
	
	@Override
	public void update()
	{
		super.update();
		if (speed>0) speed = speed - 0.05f;
		if (speed<0) speed = speed + 0.05f;
	}
	
	/**
	 * 
	 */
	public void right()
	{
		angleVel++;
		if (angleVel>5.0) angleVel = 5.0f;
	}

	/**
	 * 
	 */
	public void left()
	{
		angleVel--;
		if (angleVel<-5.0) angleVel = -5.0f;
	}

	/**
	 * 
	 */
	public void avance()
	{
		speed+=0.1;
		if (speed>5.0) speed = 5.0f;
		if (angleVel>0) angleVel=angleVel-0.1f;
		if (angleVel<0) angleVel=angleVel+0.1f;
	}

	/**
	 * 
	 */
	public void recule()
	{
		speed-=0.1;
		if (speed<-5.0) speed = -5.0f;	
		if (angleVel>0) angleVel=angleVel-0.1f;
		if (angleVel<0) angleVel=angleVel+0.1f;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Shape getShape()
	{
		float x = 0;// (float) position.getX();
		float y = 0;// (float) position.getY();
		Path2D tmpPoly = new Path2D.Double();
		tmpPoly.moveTo(x - 5 , y + 5); // arrow tip
		tmpPoly.lineTo(x + 5 , y + 5);
		tmpPoly.lineTo(x, y - 5); // arrow tip
		tmpPoly.lineTo(x - 5 , y + 5); // arrow tip
		tmpPoly = (Path2D) applyTransform(tmpPoly);
		return tmpPoly;
	}

	/**
	 * @return the angle
	 */
	public synchronized float getAngle()
	{
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public synchronized void setAngle(float angle)
	{
		this.angle = angle;
	}

	/**
	 * @return the position
	 */
	public synchronized Vector2D getPosition()
	{
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public synchronized void setPosition(Vector2D position)
	{
		this.position = position;
	}
	
	/**
	 * @return
	 */
	public boolean fire()
	{
		long fireTime = System.currentTimeMillis();
		if ((fireTime - lastFireTime) > 1000*0.5)
		{
			lastFireTime = fireTime;
			return true;
		}
		return false;
	}

	@Override
	public boolean isDead()
	{
		return false;
	}
}
