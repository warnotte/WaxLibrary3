package org.warnotte.W2D.PanelGraphique.Nurbs.TrucEugen;
import java.util.ArrayList;
import java.util.List;

public class Spline {
	private List<Vertex> controlPoints = new ArrayList<>();
	private List<Vertex> curve = new ArrayList<>(); 
	final int STEPS = 20;

	public Spline(List<Vertex> controlPoints) {
		this.controlPoints = controlPoints;
	}

	public List<Vertex> getControlPoints() {
		return controlPoints;
	}

	public void setControlPoints(List<Vertex> controlPoints) {
		this.controlPoints = controlPoints;
	}

	public List<Vertex> getCurve() {
		return curve;
	}

	public void setCurve(List<Vertex> curve) {
		this.curve = curve;
	}

	private CubicPolynomial [] calcNaturalCubic(int n, float[] x) {
		float[] gamma = new float[n+1];
		float[] delta = new float[n+1];
		float[] d = new float[n+1];
		int i;

		gamma[0] = 1.0f/2.0f;
		for ( i = 1; i < n; i++) {
			gamma[i] = 1/(4-gamma[i-1]);
		}
		gamma[n] = 1/(2-gamma[n-1]);

		delta[0] = 3*(x[1]-x[0])*gamma[0];
		for ( i = 1; i < n; i++) {
			delta[i] = (3*(x[i+1]-x[i-1])-delta[i-1])*gamma[i];
		}
		delta[n] = (3*(x[n]-x[n-1])-delta[n-1])*gamma[n];

		d[n] = delta[n];
		for ( i = n-1; i >= 0; i--) {
			d[i] = delta[i] - gamma[i]*d[i+1];
		}

		CubicPolynomial [] c = new CubicPolynomial [n];
		for ( i = 0; i < n; i++) {
			c[i] = new CubicPolynomial((float)x[i], d[i], 3*(x[i+1] - x[i]) - 2*d[i] - d[i+1],
					2*(x[i] - x[i+1]) + d[i] + d[i+1]);
		}
		return c;
	}

	public void build() {
		curve.clear();
		int numPoints = controlPoints.size();
		float[] x = new float[numPoints];
		float[] y = new float[numPoints];
		for (int i = 0; i < numPoints; i++) {
			x[i] = controlPoints.get(i).getX();
			y[i] = controlPoints.get(i).getY();
		}
		if (numPoints >= 2) {
			CubicPolynomial [] cx = calcNaturalCubic(numPoints - 1, x);
			CubicPolynomial [] cy = calcNaturalCubic(numPoints - 1, y);     
			for (int i = 0; i < cx.length; i++) {
				for (int j = 0; j <= STEPS; j++) {
					float u = j / (float) STEPS;
					curve.add(new Vertex(cx[i].eval(u), cy[i].eval(u)));
				}
			}	    
		}
	}
	
	public float getLength() {
		if (curve.size() < 2)
			return 0f;
		float length = 0;
		for (int i = 1; i < curve.size(); i++) {
			float x0 = curve.get(i - 1).getX();
			float x1 = curve.get(i).getX();
			float y0 = curve.get(i - 1).getY();
			float y1 = curve.get(i).getY();
			length += Math.sqrt(Math.pow(x1 - x0, 2f) + Math.pow(y1 - y0, 2f));
		}
		return length;
	}
	
	public void insertControlPoint(int v, float ratio) {
		Spline s = new Spline(controlPoints);
		s.build();
		List<Vertex> curve = s.getCurve();
		if (v < 1)
			return;
		Vertex vertex1 = new Vertex(controlPoints.get(v).getX(), controlPoints.get(v).getY()); 
		Vertex vertex0 = new Vertex(controlPoints.get(v - 1).getX(), controlPoints.get(v - 1).getY());		
		float vi1 = 0, vi0 = 0;
		int vi = 0;						
		for (int i = 0; i < curve.size(); i++) {			
			// vertex0 position in curve
			if (round(curve.get(i).getX(), 4) == round(vertex0.getX(), 4) && 
					round(curve.get(i).getY(), 4) == round(vertex0.getY(), 4)){
				vi0 = i;
			}			
			// vertex1 position in curve
			if (round(curve.get(i).getX(), 4) == round(vertex1.getX(),4) && 
					round(curve.get(i).getY(), 4) == round(vertex1.getY(), 4)) {					
				vi1 = i;								
			}						
		}
		vi = (int) (vi0 + (vi1 - vi0) * ratio);				
		Vertex vertex = new Vertex(curve.get(vi).getX(), curve.get(vi).getY());
		controlPoints.add(v, vertex);
		//build();
	}
	
	public static double round(double value, int decimalPlaces) {
		if (decimalPlaces < 0) {
			return value;
		}
		double augmentation = Math.pow(10, decimalPlaces);
		return Math.round(value * augmentation) / augmentation;
	}

	@Override
	public String toString() {
		return "Spline [curve=" + curve + "]";
	}
}