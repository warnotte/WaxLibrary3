package org.warnotte.W2D.PanelGraphique.Nurbs.TrucEugen;

public class CubicPolynomial {
	private float a, b, c, d;

	public CubicPolynomial (float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public float eval(float u) {
		return (((d*u) + c)*u + b)*u + a;
	}
}
