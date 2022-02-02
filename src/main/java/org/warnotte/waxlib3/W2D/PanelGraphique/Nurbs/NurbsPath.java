package org.warnotte.waxlib3.W2D.PanelGraphique.Nurbs;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class NurbsPath {

	List<NurbsPoint> points = new ArrayList<>();
	List<NurbsCurve> curves = new ArrayList<>();
	
	public void addPoint(Point2D.Double pos)
	{
		//points.add(new NurbsPoint(pos.x, pos.y));
		points.add((NurbsPoint) pos);
		return;
	}
	
	public NurbsCurve addCurve(NurbsPoint np1, NurbsPoint np2)
	{
		NurbsCurve n = new NurbsCurve(np1, np2);
		curves.add(n);
		return n ;
	}
	
	public double getLength()
	{
		double len = 0;
		for (int i = 0; i < curves.size(); i++) {
			len +=curves.get(i).getLength();
		}
		
		return len;
	}

	public List<NurbsPoint> getPoints() {
		return points;
	}

	public void setPoints(List<NurbsPoint> points) {
		this.points = points;
	}

	public List<NurbsCurve> getCurves() {
		return curves;
	}

	public void setCurves(List<NurbsCurve> curves) {
		this.curves = curves;
	}
	
	
	
}