/**
 * 
 */
package io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector2d;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.View2D_Utils;

/**
 * @author Warnotte Renaud
 *
 */
public class NurbsCurve 
{

	// Nurbs testing...
	NurbsPoint pt1 = new NurbsPoint(0, 0);
	NurbsPoint pt2 = new NurbsPoint(10, 0);
	NurbsPoint pt3 = new NurbsPoint(15, 15);
	NurbsPoint pt4 = new NurbsPoint(20, 15);
	
	float thicknessDetection = 8.0f;
	
	/**
	 * @param pt1
	 * @param pt2
	 * @param pt3
	 * @param pt4
	 */
	protected NurbsCurve(NurbsPoint pt1, NurbsPoint pt2, NurbsPoint pt3, NurbsPoint pt4)
	{
		super();
		this.pt1 = pt1;
		this.pt2 = pt2;
		this.pt3 = pt3;
		this.pt4 = pt4;
		update();
	}

	public NurbsCurve()
	{
		update();
	}
	
	/**
	 * Add a curve, with control point automatically computed
	 * @param np1
	 * @param np2
	 */
	public NurbsCurve(NurbsPoint np1, NurbsPoint np4) {
		
		double dst = new Vector2d(Math.abs(np1.x-np4.x), Math.abs(np1.y-np4.y)).length();
		
		Line2D.Double line = new Line2D.Double(np1.x, np1.y, np4.x, np4.y);
		
		pt1 = np1;
		
		line = View2D_Utils.createLineLength(line, dst/10.0f);
		
		pt2 = vnc3(line.getP1());
		pt3 = vnc3(line.getP2());
		
		pt4 = np4;
	}

	/**
	 * @return the pt1
	 */
	public synchronized NurbsPoint getPt1()
	{
		return pt1;
	}
	/**
	 * @param pt1 the pt1 to set
	 */
	public synchronized void setPt1(NurbsPoint pt1)
	{
		this.pt1 = pt1;
		update();
	}
	/**
	 * @return the pt2
	 */
	public synchronized NurbsPoint getPt2()
	{
		return pt2;
	}
	/**
	 * @param pt2 the pt2 to set
	 */
	public synchronized void setPt2(NurbsPoint pt2)
	{
		this.pt2 = pt2;
		update();
	}
	/**
	 * @return the pt3
	 */
	public synchronized NurbsPoint getPt3()
	{
		return pt3;
	}
	/**
	 * @param pt3 the pt3 to set
	 */
	public synchronized void setPt3(NurbsPoint pt3)
	{
		this.pt3 = pt3;
		update();
	}
	/**
	 * @return the pt4
	 */
	public synchronized NurbsPoint getPt4()
	{
		return pt4;
	}
	/**
	 * @param pt4 the pt4 to set
	 */
	public synchronized void setPt4(NurbsPoint pt4)
	{
		this.pt4 = pt4;
		update();
	}
	
	List<Line2D.Double> lines = new ArrayList<>();
	//Rectangle2D bounds = new Rectangle2D.Double(0, 0, 0, 0);
	List<Shape> lines_rectangles = new ArrayList<>();
	
	
	
	/**
	 * @return the lines_rectangles
	 */
	public synchronized List<Shape> getLines_rectangles()
	{
		return lines_rectangles;
	}

	/**
	 * @param lines_rectangles the lines_rectangles to set
	 */
	public synchronized void setLines_rectangles(List<Shape> lines_rectangles)
	{
		this.lines_rectangles = lines_rectangles;
	
	}

	
	public void update()
	{
		lines.clear();
		lines.addAll(getBez(pt1, pt2, pt3, pt4, 32));
		lines_rectangles = getCollisionShapeList(getThicknessDetection());
	}
	/**
	 * 
	 */
	public List<Shape> getCollisionShapeList(float thickness)
	{
		/*
		double minX = Math.min(Math.min(Math.min(pt1.getX(), pt2.getX()), pt3.getX()), pt4.getX()); 
		double minY = Math.min(Math.min(Math.min(pt1.getY(), pt2.getY()), pt3.getY()), pt4.getY()); 
		double maxX = Math.max(Math.max(Math.max(pt1.getX(), pt2.getX()), pt3.getX()), pt4.getX()); 
		double maxY = Math.max(Math.max(Math.max(pt1.getY(), pt2.getY()), pt3.getY()), pt4.getY()); 
		//bounds.setRect(minX, minY, maxX-minX, maxY-minY);
		*/
		List<Shape> retour = new ArrayList<>();
		
		Iterator<java.awt.geom.Line2D.Double> iterator2 = lines.iterator();
		while(iterator2.hasNext())
		{
			Line2D.Double double1 = iterator2.next();
			GeneralPath polyline = generateRectangleFromLine(double1, thickness);
			retour.add(polyline);
			//g.draw(at.createTransformedShape(double1));
		}
		
		return retour;
		
	}
	
	public Shape getShape()
	{
		update();
		Path2D path = new Path2D.Double();
		path.moveTo(pt1.getX(), pt1.getY());
		path.curveTo(
				pt2.getX(), pt2.getY(), 
				pt3.getX(), pt3.getY(), 
				pt4.getX(), pt4.getY());
		return path;
	}
	
	

	/**
	 * @param double1
	 * @return
	 */
	/*private GeneralPath generateRectangleFromLine(Line2D.Double double1)
	{
		return generateRectangleFromLine(double1, getThicknessDetection()/2.0f);
	}*/
	
	public static GeneralPath generateRectangleFromLine(Line2D.Double double1, double thicknessDetection)
	{
		// On crée des rectangles pour la selectionnabilités.
		double line_scale = thicknessDetection;
		Line2D.Double lineSELECT1 = View2D_Utils.createLineParallel(double1, -line_scale);
		Line2D.Double lineSELECT2 = View2D_Utils.createLineParallel(double1, line_scale);
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
		polyline.moveTo (lineSELECT1.getX1(), lineSELECT1.getY1());
		polyline.lineTo (lineSELECT1.getX2(), lineSELECT1.getY2());
		polyline.lineTo (lineSELECT2.getX2(), lineSELECT2.getY2());
		polyline.lineTo (lineSELECT2.getX1(), lineSELECT2.getY1());
		polyline.closePath();
		return polyline;
	}

	/**
	 * @param d
	 * @return
	 */
	public NurbsPoint interpolate(double d)
	{
		NurbsPoint posBateau = vnc3(CalculateBezierPoint(
				d,
				cnv(pt1),
				cnv(pt2),
				cnv(pt3),
				cnv(pt4)));
		return posBateau;
	}

	/**
	 * @return the thicknessDetection
	 */
	public synchronized float getThicknessDetection()
	{
		return thicknessDetection;
	}

	/**
	 * @param thicknessDetection the thicknessDetection to set
	 */
	public synchronized void setThicknessDetection(float thicknessDetection)
	{
		this.thicknessDetection = thicknessDetection;
		update();
	
	}

	/**
	 * @param i
	 * @param j
	 */
	public void translate(double dx, double dy)
	{
		pt1.x+=dx;
		pt2.x+=dx;
		pt3.x+=dx;
		pt4.x+=dx;
		pt1.y+=dy;
		pt2.y+=dy;
		pt3.y+=dy;
		pt4.y+=dy;
		
	}
	
	
	protected static Vector2d CalculateBezierPoint(double t, Vector2d p0, Vector2d p1, Vector2d p2, Vector2d p3) {
		double u = 1.0 - t;

		double tt = t * t;
		double uu = u * u;
		double uuu = uu * u;
		double ttt = tt * t;

		Vector2d p = new Vector2d(p0.x, p0.y);
		p.scale(uuu); // first term
		p1.scale(3.0 * uu * t);
		p.add(p1);
		p2.scale(3.0 * u * tt);
		p.add(p2);
		p3.scale(ttt);
		p.add(p3);
		// Vector3 p = uuu * p0; //first term
		// p += (3 * uu * t) * p1; //second term
		// p += (3 * u * tt) * p2; //third term
		// p += ttt * p3; //fourth term

		return p;
	}

	
	protected static List<Line2D.Double> getBez(Point2D p0, Point2D p1, Point2D p2, Point2D p3, int SEGMENT_COUNT) {

		Vector2d q0 = CalculateBezierPoint(0, cnv(p0), cnv(p1), cnv(p2), cnv(p3));

		List<Line2D.Double> lines = new ArrayList<>();
		for (int i = 1; i <= SEGMENT_COUNT; i++) {
			double t = i / (double) SEGMENT_COUNT;
			Vector2d q1 = CalculateBezierPoint(t, cnv(p0), cnv(p1), cnv(p2), cnv(p3));
			Line2D.Double line = new Line2D.Double(q0.x, q0.y, q1.x, q1.y);
			//g.draw(line);
			lines.add(line);
			q0 = q1;
		}
		return lines;
	}

	/**
	 * Semble merdé
	 * @param controlPoints
	 * @return
	 */
	protected static List<Point2D.Double> GetDrawingPoints(List<Point2D> controlPoints) {

		int SEGMENTS_PER_CURVE = 8;

		List<Point2D.Double> drawingPoints = new ArrayList<>();
		for (int i = 0; i < controlPoints.size() - 3; i += 3) {
			Vector2d p0 = cnv(controlPoints.get(i + 0));
			Vector2d p1 = cnv(controlPoints.get(i + 1));
			Vector2d p2 = cnv(controlPoints.get(i + 2));
			Vector2d p3 = cnv(controlPoints.get(i + 3));

			if (i == 0) // Only do this for the first endpoint.
						// When i != 0, this coincides with the end
						// point of the previous segment
			{
				drawingPoints.add(vnc3(CalculateBezierPoint(0, p0, p1, p2, p3)));
			}

			for (int j = 1; j <= SEGMENTS_PER_CURVE; j++) {
				float t = j / (float) SEGMENTS_PER_CURVE;
				drawingPoints.add(vnc3(CalculateBezierPoint(t, p0, p1, p2, p3)));
			}
		}

		return drawingPoints;
	}

	public static NurbsPoint vnc3(Vector2d calculateBezierPoint) {
		return new NurbsPoint(calculateBezierPoint.x, calculateBezierPoint.y);
	}

	public static Vector2d cnv(Point2D p) {
		return new Vector2d(p.getX(), p.getY());
	}
	
	private NurbsPoint vnc3(Point2D p1) {
		return new NurbsPoint(p1.getX(), p1.getY());
	}

	public double getLength() {
		
		double sum=0;
		for (int i = 0; i < lines.size(); i++) {
			Line2D.Double line = lines.get(i);
			sum += new Vector2d(Math.abs(line.x1-line.getX2()), Math.abs(line.y1-line.y2)).length();
		}
		
		return sum;
	}

	
}
