package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;


public class View2D_Utils
{

	public static double arrondir(double in, int decimal)
	{
		double v = ((Math.round(in * (Math.pow(10, decimal)))) / (Math.pow(10, decimal)));
		return v;
	}
	
	
	/**
	 * Arrondir un chiffre et le gridiffy
	 * @param in le chiffre
	 * @param gridsize la taille de la grille
	 * @param bypass renvoye simplement le chiffre entr� si mis a vrai.
	 * @return
	 */
	public static double Griddify(double in, double gridsize, boolean bypass)
	{
		if (bypass == true)
			return in;
		return Math.round((in) / (int) (gridsize)) * gridsize;

	}

	
	/**
	 * Permet de borner un chiffre s'il depasse une des 2 bornes (fait un min et max).
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static double constrains(double value, double min, double max)
	{
		if (value <= min) return min;
		if (value >= max) return max;
		return value;
	}
	

	/**
	 * Deplace la ligne parallement de offsetPixels (positif ou negatif).
	 * @param line
	 * @param offsetPixels
	 * @return
	 */
	public static Line2D.Double createLineParallel(Line2D.Double line, double offsetPixels)
	{
		double x1 = line.getX1();
		double x2 = line.getX2();
		double y1 = line.getY1();
		double y2 = line.getY2();
		float L = (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		//	This is the second line
		
		double x1p = x1 + offsetPixels * (y2-y1) / L;
		double x2p = x2 + offsetPixels * (y2-y1) / L;
		double y1p = y1 + offsetPixels * (x1-x2) / L;
		double y2p = y2 + offsetPixels * (x1-x2) / L;
	
		line = new Line2D.Double(x1p, y1p, x2p, y2p);
		return line;
	}
	
	/**
	 * Supprime une distance a un segment (a gauche et a droite).
	 * @param line
	 * @param longueurarabotter
	 * @return
	 */
	public static Line2D.Double createLineLength(Line2D.Double line, double longueurarabotter)
	{
		return createLineLengthWax(line, longueurarabotter);
		//return createLineLengthEugen(line, (float)longueurarabotter);
	}
	
	public static Line2D.Double createLineLength(Line2D.Double line, double delta1, double delta2)
	{
		return createLineLengthWax(line, delta1, delta2);
	}
	
	
	/**
	 * Merci Eugen pour cette méthode :)
	 * @param line
	 * @param delta
	 * @return
	 */
	private static Line2D.Double createLineLengthEugen(Line2D.Double line, double delta)
	{

		double x1 = line.x1;
		double x2 = line.x2;
		double y1 = line.y1;
		double y2 = line.y2;

		double tan = (y2 - y1) / (x2 - x1);
		double alpha = Math.atan(tan);
		int signX = 1, signY = 1;

		if (x2 <= x1 && y2 >= y1)
		{
			signX = -1;
			signY = -1;
		}
		if (x2 <= x1 && y2 <= y1)
		{
			signX = -1;
			signY = -1;
		}
		double mca = Math.cos(alpha);
		double msa = Math.sin(alpha);
		return new Line2D.Double(x1 + signX * delta * mca, y1 + signY * delta * msa, x2 - signX * delta * mca, y2 - signY * delta * msa);
	}
	
	/**
	 * 
	 * @param line
	 * @param delta
	 * @return
	 */
	private static Line2D.Double createLineLengthWax(Line2D.Double line, double delta)
	{
		double x1 = line.x1;
		double x2 = line.x2;
		double y1 = line.y1;
		double y2 = line.y2;
		// Point du millieu 
		double ix1 = (x1 + x2) / 2.0;
		double iy1 = (y1 + y2) / 2.0;
		//Point2D interp = interpolate(line.getP1(), line.getP2());
		// 1) Calcule le scale factor
		// Longueur actuelle
		double L = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// Calcul le scale
		double scale = 1.0 / (L / (L - delta * 2));
		//2) Translation du millieu du segment vers 0, 0 (on garde la position qlq part ou l'offset de deplacement)
		//3) Multiplication des coordon�es par le facteur d'�chelle.
		//4) Translation du millieu du segment vers la position de d�part du millieu de segment.
		x1=(x1-ix1)*scale+ix1;
		x2=(x2-ix1)*scale+ix1;
		y1=(y1-iy1)*scale+iy1;
		y2=(y2-iy1)*scale+iy1;
		return new Line2D.Double(x1, y1, x2, y2);
	}
	
	
	// TODO : Ceci devrait être mis dans WaxLib3
	private static Line2D.Double createLineLengthEugen(Line2D.Double line, double delta1, double delta2)
	{

		double x1 = line.x1;
		double x2 = line.x2;
		double y1 = line.y1;
		double y2 = line.y2;

		double tan = (y2 - y1) / (x2 - x1);
		double alpha = Math.atan(tan);
		int signX = 1, signY = 1;

		if (x2 <= x1 && y2 >= y1)
		{
			signX = -1;
			signY = -1;
		}
		if (x2 <= x1 && y2 <= y1)
		{
			signX = -1;
			signY = -1;
		}
		double mca = Math.cos(alpha);
		double msa = Math.sin(alpha);
		return new Line2D.Double(x1 + signX * delta1 * mca, y1 + signY * delta1 * msa, x2 - signX * delta2 * mca, y2 - signY * delta2 * msa);
	}
	
	// TODO : Ceci devrait être mis dans WaxLib3
	private static Line2D.Double createLineLengthWax(Line2D.Double line, double delta1, double delta2)
	{
		double x1 = line.x1;
		double x2 = line.x2;
		double y1 = line.y1;
		double y2 = line.y2;
		// Point du millieu 
		double ix1 = (x1 + x2) / 2.0;
		double iy1 = (y1 + y2) / 2.0;
		//Point2D interp = interpolate(line.getP1(), line.getP2());
		// 1) Calcule le scale factor
		// Longueur actuelle
		double L = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// Calcul le scale
		double scale1 = 1.0 / (L / (L - delta1 * 2));
		double scale2 = 1.0 / (L / (L - delta2 * 2));
		//2) Translation du millieu du segment vers 0, 0 (on garde la position qlq part ou l'offset de deplacement)
		//3) Multiplication des coordon�es par le facteur d'�chelle.
		//4) Translation du millieu du segment vers la position de d�part du millieu de segment.
		x1=(x1-ix1)*scale1+ix1;
		x2=(x2-ix1)*scale2+ix1;
		y1=(y1-iy1)*scale1+iy1;
		y2=(y2-iy1)*scale2+iy1;
		return new Line2D.Double(x1, y1, x2, y2);
	}
	
	public static double getAngleRadians(Point2D.Double p1, Point2D.Double p2)
	{
		if (p1 == null)
			return Double.NaN;
		if (p2 == null)
			return Double.NaN;

		double radians = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());// * 180) / Math.PI;
		return radians;
	}
	
	

	/**
	 * - Vérifie que les 2 segments ne sont pas cote a cote. (ou l'un touchant l'autre). 
	 * - Vérifie si la pente est la meme. (donc 2 droite parallelle).
	 * - Vérifie ensuite si un des 2 points de l'autre segments est contenu dans le premier
	 * 
	 * @param XA1 point 1 ligne A
	 * @param YA1
	 * @param XB1 point 2 ligne A
	 * @param YB1
	 * @param XA2 point 1 ligne B
	 * @param YA2
	 * @param XB2 point 2 ligne B
	 * @param YB2
	 * @return
	 */
	public static boolean isSegmentNeighboor(double XA1, double YA1, double XB1, double YB1, double XA2, double YA2, double XB2, double YB2)
	{
		double mxa1 = Math.min(XA1, XB1);
		double mxb1 = Math.max(XA1, XB1);
		double mya1 = Math.min(YA1, YB1);
		double myb1 = Math.max(YA1, YB1);
		double mxa2 = Math.min(XA2, XB2);
		double mxb2 = Math.max(XA2, XB2);
		double mya2 = Math.min(YA2, YB2);
		double myb2 = Math.max(YA2, YB2);

		double PenteA = (mxa1 - mxb1) / (mya1 - myb1);
		double PenteB = (mxa2 - mxb2) / (mya2 - myb2);

		if (PenteA != PenteB)
			return false;

		Line2D l1 = new Line2D.Double(mxa1, mya1, mxb1, myb1);
		Line2D l2 = new Line2D.Double(mxa2, mya2, mxb2, myb2);

		if ((mxa1 == mxa2) && (mya1 == mya2))
			return true;
		if ((mxb1 == mxb2) && (myb1 == myb2))
			return true;
		if (l1.intersects(mxa2, mya2, 1, 1))
			return true;
		if (l1.intersects(mxb2, myb2, 1, 1))
			return true;
		if (l2.intersects(mxa1, mya1, 1, 1))
			return true;
		if (l2.intersects(mxb1, myb1, 1, 1))
			return true;
		return false;

	}
	
	public static double [] intersectionDroites(Line2D l1, Line2D l2)
	{
		return intersectionDroitesIncluse(l1.getX1(), l1.getY1(), l1.getX2(), l1.getY2(), l2.getX1(), l2.getY1(), l2.getX2(), l2.getY2());
	}

	/**
	 * Renvoye le point d'intersection entre 2 droites ou NULL s'il n'y en a pas.
	 * @param X1
	 * @param Y1
	 * @param X2
	 * @param Y2
	 * @param X3
	 * @param Y3
	 * @param X4
	 * @param Y4
	 * @return
	 */
	public static double [] intersectionDroitesIncluse(double X1, double Y1, double X2, double Y2, double X3, double Y3, double X4, double Y4)
	{
		if (false)
		{
		  System.err.println("----------Intersection-------------");
		  System.err.println("X1="+X1); System.err.println("Y1="+Y1);
		  System.err.println("X2="+X2); System.err.println("Y2="+Y2);
		  System.err.println("X3="+X3); System.err.println("Y3="+Y3);
		  System.err.println("X4="+X4); System.err.println("Y4="+Y4);
		}

		double MX12 = X1 - X2; // Projection segment 1 sur l'axe X (longueur)
		double MY12 = Y1 - Y2; // Projection segment 1 sur l'axe Y (longueur)
		double MX34 = X3 - X4; // Projection segment 2 sur l'axe X (longueur)
		double MY34 = Y3 - Y4; // Projection segment 2 sur l'axe Y (longueur)
		double PX21 = X2 * Y1; // 
		double PX12 = X1 * Y2;
		double PX43 = X4 * Y3;
		double PX34 = X3 * Y4;

		if (false)
		{
			System.err.println("MX12=" + MX12);
			System.err.println("MY12=" + MY12);
			System.err.println("MX34=" + MX34);
			System.err.println("MY34=" + MY34);
			System.err.println("PX21=" + PX21);
			System.err.println("PX12=" + PX12);
			System.err.println("PX43=" + PX43);
			System.err.println("PX34=" + PX34);
		}
		 
		// Si les pentes sont egales.
	//	if (((X2 - X1) / (Y2 - Y1)) == ((X4 - X3) / (Y4 - Y3)))
	//		return null;
		if (Math.abs(((X2 - X1) / (Y2 - Y1)) - ((X4 - X3) / (Y4 - Y3)))<=0.001)
			return null;

		// if (MX12 == 0 )
		// MX12 = 0.00000000001f; // TODO : Si j'active ca alors y'a un probleme
		// avec certaines collision, si je l'active pas aussi :|

		double X = ((PX34 - PX43) * MX12 - (PX12 - PX21) * MX34) / (MY12 * MX34 - MY34 * MX12);
		if ((MX12 == 0) && (MY34 == 0))
		{
			double Y = (X1 - X3) * ((Y4 - Y3) / (X4 - X3)) + Y3;
			return new double [] {X, Y};
		}
		if (MX12 == 0)
		{
			double Y = (X1 - X3) * ((Y4 - Y3) / (X4 - X3)) + Y3;
			return new double [] {X, Y};
		}
		double Y = X * (MY12 / MX12) + ((PX12 - PX21) / MX12);
		
		if (Double.isNaN(Y))
			return null;
		if (Double.isNaN(X))
			return null;
		if (Y==Double.NEGATIVE_INFINITY)
			return null;
		if (Y==Double.POSITIVE_INFINITY)
			return null;
		if (X==Double.POSITIVE_INFINITY)
			return null;
		if (X==Double.POSITIVE_INFINITY)
				return null;
		return new double [] {X, Y};
	}
	
	/**
	 * Test l'intersection exclusive entre 2 droites. 
	 * @param XA1
	 * @param YA1
	 * @param XB1
	 * @param YB1
	 * @param XA2
	 * @param YA2
	 * @param XB2
	 * @param YB2
	 * @return
	 */
	public static boolean isIntersectExclusif(double XA1, double YA1, double XB1, double YB1, double XA2, double YA2, double XB2, double YB2)
	{
		
		
		boolean v = isSegmentNeighboor(
		 XA1,  YA1,  XB1,  YB1,
		 XA2,  YA2,  XB2,  YB2);
		if (v ==false) return false;

		// regarde si les points se touchent
		double Epsilon = 0.0001d;

		Point2D L1pt1 = new Point2D.Double(XA1, YA1);
		Point2D L1pt2 = new Point2D.Double(XB1, YB1);
		Point2D L2pt1 = new Point2D.Double(XA2, YA2);
		Point2D L2pt2 = new Point2D.Double(XB2, YB2);


		if ((((L1pt1.distance(L2pt1) <= Epsilon) && (L1pt2.distance(L2pt2) > Epsilon)) || ((L1pt1.distance(L2pt2) <= Epsilon) && (L1pt2.distance(L2pt1) > Epsilon))) ||
		(((L1pt2.distance(L2pt1) <= Epsilon) && (L1pt1.distance(L2pt2) > Epsilon)) || ((L1pt2.distance(L2pt2) <= Epsilon) && (L1pt1.distance(L2pt1) > Epsilon)))
		)
			return false;
		return true;

	}

	/**
	 * Interpole des valeur (!!! ca arrondis a 4 decimales).
	 * 
	 * @param pt1
	 * @param pt2
	 * @param rat
	 * @return
	 */
	public static Point2D interpolate(Point2D pt1, Point2D pt2, double rat)
	{
 		Point2D pt = new Point2D.Double(
				arrondir((pt1.getX() * rat) + pt2.getX() * (1f - rat), 4), 
				arrondir((pt1.getY() * rat) + pt2.getY() * (1f - rat), 4));
		return pt;
	}

	public static boolean testIntersectionNonIncluse(double L1X, double L1Y, double L2X, double L2Y,
			double L1Xb, double L1Yb, double L2Xb, double L2Yb)
	{
		return testIntersectionNonIncluse(new Line2D.Double(L1X, L1Y, L2X, L2Y), new Line2D.Double(L1Xb, L1Yb, L2Xb, L2Yb));
	}
	
	/**
	 * Verifie que 2 Segments intersectionnent � l'interieur (cad pas au extremit� des segments);
	 * Il faut qu'ils se touchent vraiment, pas qu'ils soient juste coll� l'un � l'autre.
	 *  
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static boolean testIntersectionNonIncluse(Line2D line1, Line2D line2)
	{
		System.err.println("Not implemented !!!!!");
		/*if (line1.intersectsLine(line2))
		{
			double upsi = 0.00001;
			Node n = intersectionDroites(line1, line2);
			if (n == null)
				return false;
			if (n.distance(line1.getP1()) <= upsi)
				return false;
			if (n.distance(line1.getP2()) <= upsi)
				return false;
			if (n.distance(line2.getP1()) <= upsi)
				return false;
			if (n.distance(line2.getP2()) <= upsi)
				return false;
			return true;
		}*/
		return false;
	}

	/**
	 * Retourne la longueur de chevauchement de 2 lignes (en prenant soin de verifier avant que ca se chevauche bien ...)
	 * @param Xa1
	 * @param Ya1
	 * @param Xb1
	 * @param Yb1
	 * @param Xa2
	 * @param Ya2
	 * @param Xb2
	 * @param Yb2
	 * @return
	 */
	public static double getOverlapLength(double Xa1, double Ya1, double Xb1, double Yb1, double Xa2, double Ya2, double Xb2, double Yb2)
	{
		Line2D line1 = new Line2D.Double(Xa1, Ya1, Xb1, Yb1);
		Line2D line2 = new Line2D.Double(Xa2, Ya2, Xb2, Yb2);
		
		
		
		Vector<Point2D> pt = new Vector<Point2D>();
		
		if (LineContainsPointIncluse(line1, line2.getP1()))
			if (pt.contains(line2.getP1())==false)
			pt.add(line2.getP1());
		if (LineContainsPointIncluse(line1, line2.getP2()))
			if (pt.contains(line2.getP2())==false)
			pt.add(line2.getP2());
		
		if (LineContainsPointIncluse(line2, line1.getP1()))
			if (pt.contains(line1.getP1())==false)
			pt.add(line1.getP1());
		if (LineContainsPointIncluse(line2, line1.getP2()))
			if (pt.contains(line1.getP2())==false)
			pt.add(line1.getP2());
		
		// TODO : Ici jsuis pas sure.
		if (pt.size()<=1)
			return 0;
		
		Point2D pt1 = pt.get(0);
		Point2D pt2 = pt.get(1);
		
		double lengthX = Math.abs(pt1.getX()-pt2.getX());
		double lengthY = Math.abs(pt1.getY()-pt2.getY());
		pt.clear();
		pt=null;
		return Math.sqrt(lengthX*lengthX+lengthY*lengthY);
	}

	public static boolean LineContainsPointIncluse(Line2D line1, Point2D p1)
	{
		return line1.intersectsLine(new Line2D.Double(p1.getX(), p1.getY(), p1.getX(), p1.getY()));
	//	return false;
	}
	
	/**
	 * METHODS TO CALCULATE THE AREA AND CENTROID OF A POLYGON INSERT THEM INTO
	 * THE CORRESPONDING CLASS
	 **/
	public static double SignedPolygonArea(ArrayList<Point> polygon)
	{
	//	Polygon P;
		int i, j;
		double area = 0;
		int N = polygon.size();
		for (i = 0; i < N; i++)
		{
			j = (i + 1) % N;
			area += polygon.get(i).x * polygon.get(j).y;
			area -= polygon.get(i).y * polygon.get(j).x;
		}
		area /= 2.0;

		return (Math.abs(area));
	}

	/* CENTROID */
	public static Point2D PolygonCenterOfMass(ArrayList<Point> polygon, int N)
	{
		float cx = 0, cy = 0;
		float A = (float) SignedPolygonArea(polygon);
		Point2D res = new Point2D.Double();
		int i, j;

		float factor = 0;
		for (i = 0; i < N; i++)
		{
			j = (i + 1) % N;
			factor = (polygon.get(i).x * polygon.get(j).y - polygon.get(j).x * polygon.get(i).y);
			cx += (polygon.get(i).x + polygon.get(j).x) * factor;
			cy += (polygon.get(i).y + polygon.get(j).y) * factor;
		}
		A *= 6.0f;
		factor = 1 / A;
		cx *= factor;
		cy *= factor;
		res.setLocation(cx, cy);
		return res;
	}

}

