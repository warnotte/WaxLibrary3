package org.warnotte.W2D.PanelGraphique.MarsCurve;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

	/**
	 * Calcul ce qu'il faut pour afficher une courbe de type Mars2000
	 * Attention les Point2D.Double ne doivent pas être modifié tel quels!!!
	 * @author Warnotte Renaud
	 *
	 */
	public class ArcCurveMarsLike
	{
		// Input data
		Point2D.Double pt1; 
		Point2D.Double pt2; 
		
		double radius;
		
		// Computed data
		Point2D.Double pt_upper_left_circle; // For Debug
		Point2D.Double pt_center_of_circle; 
		double angle1;
		double angle2;
		//double length;
		double angleBetween;
	
		
		/*
		 * Curve defined by pt1, pt2 and the radius.
		 */
		public ArcCurveMarsLike(Point2D.Double pt1, Point2D.Double pt2, double radius)
		{
			this.radius=radius;
			this.pt1 = pt1;
			this.pt2 = pt2;
			pt_center_of_circle = new Point2D.Double(0, 0);
			pt_upper_left_circle = new Point2D.Double(0, 0);
			recompute();
		}
		
		
		public static double distance_Arc(Point2D.Double pt1, Point2D.Double pt2, double radius)
		{
			return new ArcCurveMarsLike(pt1, pt2, radius).getLength();
		}
		
		
		/**
		 * @return the angleBetween
		 */
		public synchronized double getAngleBetween()
		{
			return angleBetween;
		}



		/**
		 * @return the pt1
		 */
		public synchronized Point2D.Double getPt1()
		{
			return pt1;
		}



		/**
		 * @param pt1 the pt1 to set
		 */
		public synchronized void setPt1(Point2D.Double pt1)
		{
			this.pt1 = pt1;
			recompute();
		}



		/**
		 * @return the pt2
		 */
		public synchronized Point2D.Double getPt2()
		{
			return pt2;
		}



		/**
		 * @param pt2 the pt2 to set
		 */
		public synchronized void setPt2(Point2D.Double pt2)
		{
			this.pt2 = pt2;
			recompute();
		
		}



		/**
		 * @return the radius
		 */
		public synchronized double getRadius()
		{
			return radius;
		}



		/**
		 * @param radius the radius to set
		 */
		public synchronized void setRadius(double radius)
		{
			this.radius = radius;
			recompute();
		
		}



		/**
		 * @return the pt_upper_left_circle
		 */
		public synchronized Point2D.Double getPt_upper_left_circle()
		{
			return pt_upper_left_circle;
		}



		/**
		 * @return the pt_center_of_circle
		 */
		public synchronized Point2D.Double getPt_center_of_circle()
		{
			return pt_center_of_circle;
		}



		/**
		 * @return the angle1
		 */
		public synchronized double getAngle1()
		{
			return angle1;
		}



		/**
		 * @return the angle2
		 */
		public synchronized double getAngle2()
		{
			return angle2;
		}

		/**
		 * @return the length
		 */
		public synchronized double getLength()
		{
			return 2.0 * Math.PI * radius * (angleBetween / 360f);
		}

		protected void recompute()
		{
			double r0 = radius;
			double r1 = radius;

			// d is the distance between
			double d = Math.sqrt((pt2.x - pt1.x) * (pt2.x - pt1.x) + (pt2.y - pt1.y) * (pt2.y - pt1.y));
			double a = (r0 * r0 - r1 * r1 + d * d) / (2 * d);
			double h = Math.sqrt(r0 * r0 - a * a);
			double x2 = pt1.x + a * (pt2.x - pt1.x) / d;
			double y2 = pt1.y + a * (pt2.y - pt1.y) / d;

			double x3, y3;
			boolean left = true; // TODO : That's seems always TRUE ??? est-ce vrai pour le calcul de la longueur de l'arc ???

			if (left == false)
			{
				x3 = x2 + h * (pt2.y - pt1.y) / d; // also x3=x2-h*(y1-y0)/d
				y3 = y2 - h * (pt2.x - pt1.x) / d; // also y3=y2+h*(x1-x0)/d
			} else
			{
				x3 = x2 - h * (pt2.y - pt1.y) / d; // also x3=x2-h*(y1-y0)/d
				y3 = y2 + h * (pt2.x - pt1.x) / d; // also y3=y2+h*(x1-x0)/d
			}

			pt_center_of_circle.setLocation(x3, y3);


			pt_upper_left_circle.setLocation(pt_center_of_circle.x - radius, pt_center_of_circle.y + radius);

			angle1 = calculateAngle(pt_center_of_circle.x, pt_center_of_circle.y, pt1.x, pt1.y);
			angle2 = calculateAngle(pt_center_of_circle.x, pt_center_of_circle.y, pt2.x, pt2.y);
			
			angleBetween = angleBetween(pt_center_of_circle, pt1, pt2);
			
			
			
			
		}

		protected double angleBetween(Point2D center, Point2D current, Point2D previous) {
			  float v1x = (float) (current.getX() - center.getX()); 
			  float v1y = (float) (current.getY() - center.getY());

			  //need to normalize:
			  float l1 = (float) Math.sqrt(v1x * v1x + v1y * v1y);
			  v1x /= l1;
			  v1y /= l1;

			  float v2x = (float) (previous.getX() - center.getX());
			  float v2y = (float) (previous.getY() - center.getY());

			  //need to normalize:
			  float l2 = (float) Math.sqrt(v2x * v2x + v2y * v2y);
			  v2x /= l2;
			  v2y /= l2;    

			  double rad = Math.acos( v1x * v2x + v1y * v2y );

			  double degres = Math.toDegrees(rad);
			  return degres;
			}
		
		/**
		 * Retourne l'ange entre les 2 vecteur.
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @return
		 */
		protected double calculateAngle(double x1, double y1, double x2, double y2)
		{
		    double angle = -90+Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
		    // Keep angle between 0 and 360
		    angle = angle + Math.ceil( -angle / 360 ) * 360;
	  
		    return angle;
		}
		
		
		// TODO : List discretisée de la courbe (avec facteur de precision) pour l'interaction.
		public List<Shape> getSelectionShape(int subdiv)
		{
			// Draw Stiffeners
			double angleBeetween = getAngleBetween();
			
			Point2D.Double current = getPt1();
			Point2D.Double next=null;
			
			List<Shape> list = new ArrayList<>();
			
			for (int i = 1; i <= subdiv+1; i++)
			{
				double position = angleBeetween * (i/(float)(subdiv+1)) ;
				// Offset it.
				double angle = position-getAngle1();
				
				// Get position
				double Xoncircle = getRadius() * Math.cos(Math.toRadians(angle))+getPt_center_of_circle().x;
				double Yoncircle = getRadius() * Math.sin(Math.toRadians(angle))+getPt_center_of_circle().y;
				
				next = new Point2D.Double(Xoncircle, Yoncircle);
				if (i>=1)
				{
					Line2D.Double line = new Line2D.Double(current, next);
					list.add(line);
				}
				current = next;

			}
			
			Line2D.Double line = new Line2D.Double(next, getPt2());
			list.add(line);
			
			return list;
		}


		/**
		 * @param angle
		 * @return
		 */
		public Point2D.Double getPointOnCurveAtAngle(double angle)
		{
			double Xoncircle = getRadius() * Math.cos(Math.toRadians(angle))+getPt_center_of_circle().x;
			double Yoncircle = getRadius() * Math.sin(Math.toRadians(angle))+getPt_center_of_circle().y;
			return new Point2D.Double(Xoncircle, Yoncircle);
		}


		/**
		 * Get Point position in this curve a position i
		 * @param i
		 * @return
		 */
		public Double getPointOnCurveAtS1D(double i)
		{
			
			double angle = getAngleOnCurveAtS1D(i);
			return getPointOnCurveAtAngle(angle);
		}


		/**
		 * @param i
		 * @return
		 */
		public double getAngleOnCurveAtS1D(double i)
		{
			//i=getLength()-i;
			if (i>getLength())
				System.err.println("You have a problem my friend e64e65de498e7");
			if (i<0)
				System.err.println("You have a problem my friend ???de498e7 "+i);
			
			double ratio = angleBetween * (i/(getLength())) ;
			// Offset it.
			double angle = ratio-getAngle1();
			return angle;
		}
		

		
		
	}
	