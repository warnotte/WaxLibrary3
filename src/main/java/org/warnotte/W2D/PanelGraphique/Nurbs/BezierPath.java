/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.Nurbs;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Warnotte Renaud
 *
 */
public class BezierPath
{
	List<NurbsPoint> points = new ArrayList<NurbsPoint>();
	
	public List<Line2D.Double> getCurveLines(int NbrSegments)
	{
		ArrayList<Line2D.Double> retour = new ArrayList<>();
		if (points.size()<3) return retour;
		double step = 1.0 / NbrSegments;
		NurbsPoint current = Bez(0, points);
		for (double t = step; t <= 1; t += step)
		{
			NurbsPoint next = Bez(t, points);
			Line2D.Double line = new Line2D.Double(current.x, current.y, next.x, next.y);
			retour.add(line);
			current = next;
		}
		return retour;
	}


	/**
	 * @return
	 */
	public List<Line2D.Double> getLines()
	{
		List<Line2D.Double> ret = new ArrayList<>();
		
		NurbsPoint first = points.get(0);
		for (int i = 1; i < points.size(); i++)
		{
			NurbsPoint second = points.get(i);
			ret.add(new Line2D.Double(first.x, first.y, second.x, second.y));
			first = second;
		}
		return ret;
	}



	public NurbsPoint Bez(double t, List<NurbsPoint> points)
	{
		if (points.size() == 1 /* || t < 0 || t > 1 */)
		{
			return new NurbsPoint(points.get(0).x, points.get(0).y);
		}
		List<NurbsPoint> New = new ArrayList<>();
		for (int i = 0; i < points.size() - 1; i++)
		{
			NurbsPoint np = new NurbsPoint();
			np.x= ((1 - t) * points.get(i).x) + (t * points.get(i+1).x);
			np.y= ((1 - t) * points.get(i).y) + (t * points.get(i+1).y);
			New.add(np);
		}
		return Bez(t, New);
	}
	
	public NurbsPoint interpolate(double t)
	{
		return Bez(t, points);
		
	}
	
	/**
	 * @return the points
	 */
	public synchronized List<NurbsPoint> getPoints()
	{
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public synchronized void setPoints(List<NurbsPoint> points)
	{
		this.points = points;
	
	}


	public void addPoint(NurbsPoint neonp) {
		points.add(neonp);
	}



}
