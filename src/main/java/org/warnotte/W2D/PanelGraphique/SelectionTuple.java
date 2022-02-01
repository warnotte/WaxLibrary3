package org.warnotte.W2D.PanelGraphique;

public class SelectionTuple<S1, S2>
{
	public S1 shape;
	public S2 userObject;
	public SelectionTuple(S1 shape, S2 userObject)
	{
		super();
		this.shape = shape;
		this.userObject = userObject;
	}
	public synchronized S1 getShape()
	{
		return shape;
	}
	public synchronized S2 getUserObject()
	{
		return userObject;
	}
	public synchronized void setShape(S1 shape)
	{
		this.shape = shape;
	}
	public synchronized void setUserObject(S2 userObject)
	{
		this.userObject = userObject;
	}
	@Override
	public String toString()
	{
		return "Tuple [shape=" + shape + ", userObject=" + userObject + "]";
	}
	
}
