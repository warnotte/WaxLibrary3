/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Warnotte Renaud
 *
 */
public abstract class Entity implements Updateable
{
	
	public float angle = 0;
	float angleVel = 0;
	public Vector2D position = new Vector2D(0,0);
	float speed = 0.0f;
	
	public abstract Shape getShape();
	public abstract boolean isDead();
	
	public Entity(Vector2D position, float angle)
	{
		this.position=position;
		this.angle=angle;
	}
	
	public void update()
	{
		angle+=angleVel;
		if (angleVel>0) angleVel=angleVel-0.5f;
		if (angleVel<0) angleVel=angleVel+0.5f;
		
		Vector2D dir = getDirectionVector();
		dir = dir.normalize().scalarMultiply(speed);
		position = position.add(dir);
		
		if (Math.abs(speed)<0.05) speed = 0;
		if (Math.abs(angleVel)<0.05) 
			angleVel = 0;
		
	}
	
	/**
	 * @return
	 */
	public Vector2D getDirectionVector()
	{
		float angRad = (float) Math.toRadians(angle);
		Vector2D dir = new Vector2D (Math.cos(angRad), Math.sin(angRad));
		return dir.normalize();
	}
	
	/**
	 * @param shp
	 * @return
	 */
	protected Shape applyTransform(Shape shp)
	{
		AffineTransform at = new AffineTransform();
		at.translate(position.getX(), position.getY());
		at.rotate(Math.toRadians(angle+90));
		return shp =  at.createTransformedShape(shp);
	}
}
