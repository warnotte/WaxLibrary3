package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


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
}

