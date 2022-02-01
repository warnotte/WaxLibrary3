package org.warnotte.W2D.PanelGraphique.Nurbs.TrucEugen;

public class Vertex {
	private float x;
	private float y;
	
	public Vertex(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Vertex [x=" + x + ", y=" + y + "]";
	}
}
