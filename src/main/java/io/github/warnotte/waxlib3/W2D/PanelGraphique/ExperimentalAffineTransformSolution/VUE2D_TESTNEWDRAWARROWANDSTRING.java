package io.github.warnotte.waxlib3.W2D.PanelGraphique.ExperimentalAffineTransformSolution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteX;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteY;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.View2D_Utils;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.VUE2D_TestBed;

/**
 * Ceci devrait permettre de trouver une solution pour les methode drawArrow, DrawString etc qui utilise les coordonée monde et pas ecran. 
 * Hors il faudrait la possibilite d'avoir les 2 sinon je px pas dessiner  une fleche en coordonées ecran pour dessiner des axis X par exemple ou un rose de vents...
 */
public class VUE2D_TESTNEWDRAWARROWANDSTRING extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{

	protected static final Logger		logger				= LogManager.getLogger(VUE2D_TESTNEWDRAWARROWANDSTRING.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -119901807701061793L;

	CurrentSelectionContext		contxt;
	
	float	arrowsize		= 0.15f;
	float	impactarrowsize	= 0.15f;
	
	Point2D.Double measurePt = new Point2D.Double();
	boolean measureMode = false;
	
	float YConsole = 0;
	

	
	/**
	 * @param contxt
	 */
	public VUE2D_TESTNEWDRAWARROWANDSTRING(CurrentSelectionContext contxt)
	{
		super(contxt);
		setDrawFPSInfos(false);

		config.setDrawGrid(false);
		config.setDrawHelpLines(true);
		
		this.contxt = contxt;
		
		Zoom = 2.0;
		
		if (contxt!=null)
			contxt.addXXXListener(this);
		
		setToolTipText(
				"<html>" +
				"Mouse wheel : Zoom<br>" +
				"Middle click+Drag : Translate view<br>" +
				"Right click : Popup menu<br>" +
				"Left Click + Drag : Make selection rectangle<br>"+
				"M + Drag : Measure"+
				"</html>");
		
		/*
		Timer timer = new Timer(25, this);
		timer.start();
		*/
	}
	

	
	@Override
	public void doPaint(Graphics2D g)
	{
		YConsole = 10;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		doSubPaint(g);
		
		if (measureMode==true)
		{
			double distance = measurePt.distance(MouseX, MouseY);
			drawArrowWithString(g, ""+View2D_Utils.arrondir(distance, 2)+"m", 0, measurePt.x, measurePt.y, MouseX, MouseY,  0 , -1);
		}
		
	}
	
	protected void drawConsole(Graphics2D g, String str)
	{
		g.drawString(str, 10, YConsole);
		YConsole +=10;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		
		if (measureMode ==false)
		if (e.getKeyCode() == KeyEvent.VK_M)
		{
			measureMode = true;
			measurePt.setLocation(MouseX, MouseY);
		}
		
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		if (e.getKeyCode() == KeyEvent.VK_M)
		{
			measureMode = false;
			
		}
	}
	
	
	@Override
	public void mouseMoved(java.awt.event.MouseEvent e)
	{
		super.mouseMoved(e);
		
		// Hack sinon le tooltip il fait chier.
		ToolTipManager.sharedInstance().setEnabled(false);
		ToolTipManager.sharedInstance().setEnabled(true);
	}
	
	protected void doSubPaint(Graphics2D g) {
		config.setDrawGrid(false);
		config.setDrawHelpLines(true);
		
		g.setColor(new Color(90,255,90));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		drawRoseDesVents(g, 33f);
		
		drawRoseDesVents(g, new AffineTransform(),10, 33);
		
		Point2D pt1 = new Point2D.Float(100, 0);
		Point2D pt2 = new Point2D.Float(150, 25);
		
		// TODO : Dessiner la flêche...
		drawArrow3(g, pt1, pt2, 1.0f, 1.0f, true, true, false, 1.0f);
		drawArrow3(g, at, pt1, pt2, 1.0f, 1.0f, true, true, false, 1.0f);
		
		drawString3(g, "Guten TAG", 30, 30, 0, AlignTexteX.CENTER, AlignTexteY.CENTER,
				true, 1.0f, false, Color.white, true);
		drawString3(g, "Guten TAG", 30, 40, 0, AlignTexteX.CENTER, AlignTexteY.CENTER,
				false, 1.0f, false, Color.white, true);
		
		drawString3(g, new AffineTransform(), "Guten TAG", 60, 50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER,
				true, 1.0f, false, Color.white, true);
		drawString3(g, new AffineTransform(), "Guten TAG", 60, 70, 0, AlignTexteX.CENTER, AlignTexteY.CENTER,
				false, 1.0f, false, Color.white, true);
		
		
		g.setColor(Color.black);
		drawConsole(g, String.format("XY(Wax) : %.2f,  %.2f",  MouseX, MouseY));
		drawConsole(g, String.format("Selected : %d", contxt.getSelection().size()));
		
		g.setColor(Color.BLACK);
		
		
	}
	
	protected void drawRoseDesVents(Graphics2D g, float angle) {
		AffineTransform atLocal = new AffineTransform();
		
		float offsetfromborder= 20;
		float size = 60;
		int w = getWidth();
		int h = getHeight();
		int posX = (int)( w-size/2-offsetfromborder );
		int posY = (int)( h-size/2-offsetfromborder );
		
		atLocal.translate(posX, posY);
		Area rose = new Area();
		
		Shape ellipse = new Ellipse2D.Float(-size/2, -size/2, size, size);
		Shape line1 = new Line2D.Double(-size/2-4, 0, size/2+4, 0);
		Shape line2 = new Line2D.Double(0, -size/2-4, 0, size/2+4);
		rose.add(new Area(ellipse));
		
		
		g.setColor(Color.black);
		g.draw(atLocal.createTransformedShape(rose));
		
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,0, new float[]{3, 0, 1}, 0);
		g.setStroke(dashed);
		g.draw(atLocal.createTransformedShape(line1)); 
		g.draw(atLocal.createTransformedShape(line2)); 
		g.setStroke(new BasicStroke(1));		
		
		Point2D.Float pt;
		pt = new Point2D.Float(posX, posY);
		
		//g.drawString("0°", (float)pt.getX(), (float)pt.getY());
		drawCenteredString(g, "N 0°", (int)pt.getX(), (int)((pt.getY()- size/2)-offsetfromborder/2));
		drawCenteredString(g, "90°", (int)((pt.getX()+ size/2)+offsetfromborder/2), (int)pt.getY());
		drawCenteredString(g, "180°", (int)pt.getX(), (int)((pt.getY()+ size/2)+offsetfromborder/2));
		drawCenteredString(g, "270°", (int)((pt.getX()- size/2)-offsetfromborder/2), (int)pt.getY());
		
		
		Point2D pt1 = new Point2D.Float(0, 0);
		Point2D pt2 = new Point2D.Float(50, 25);
		
		// TODO : Dessiner la flêche...
		drawArrow3(g, atLocal, pt1, pt2, 1.0f, 1.0f, true, true, false, 1.0f);
		
		
		
		
	}

	
	protected void drawRoseDesVents(Graphics2D g, AffineTransform atParent, double angle, float size) {
		angle -= 90;
		AffineTransform atLocal = new AffineTransform();

		float offsetfromborder = 30;

		atLocal.preConcatenate(atParent);

		Area rose = new Area();

		Shape ellipse = new Ellipse2D.Float(-size / 2, -size / 2, size, size);
		Shape line1 = new Line2D.Double(-size / 2 - 4, 0, size / 2 + 4, 0);
		Shape line2 = new Line2D.Double(0, -size / 2 - 4, 0, size / 2 + 4);
		rose.add(new Area(ellipse));

		g.setColor(Color.black);
		g.draw(atLocal.createTransformedShape(rose));

		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 3, 0, 1 },
				0);
		g.setStroke(dashed);
		g.draw(atLocal.createTransformedShape(line1));
		g.draw(atLocal.createTransformedShape(line2));
		g.setStroke(new BasicStroke(1));
		/*
		 * Point2D.Float pt; pt = new Point2D.Float(0, 0);
		 * 
		 * atParent.transform(pt, pt);
		 * 
		 * //g.drawString("0°", (float)pt.getX(), (float)pt.getY());
		 * drawCenteredString(g, "N 0°", (int)pt.getX(), (int)((pt.getY()-
		 * size/2)-offsetfromborder/2)); drawCenteredString(g, "90°", (int)((pt.getX()+
		 * size/2)+offsetfromborder/2), (int)pt.getY()); drawCenteredString(g, "180°",
		 * (int)pt.getX(), (int)((pt.getY()+ size/2)+offsetfromborder/2));
		 * drawCenteredString(g, "270°", (int)((pt.getX()- size/2)-offsetfromborder/2),
		 * (int)pt.getY());
		 */

		Point2D.Float pt1, pt2, pt3, pt4;
		pt1 = new Point2D.Float(0, (0 - size / 2) - offsetfromborder / 2);
		pt2 = new Point2D.Float((0 + size / 2) + offsetfromborder / 2, 0);
		pt3 = new Point2D.Float(0, (0 + size / 2) + offsetfromborder / 2);
		pt4 = new Point2D.Float(((0 - size / 2) - offsetfromborder / 2), 0);

		atParent.transform(pt1, pt1);
		atParent.transform(pt2, pt2);
		atParent.transform(pt3, pt3);
		atParent.transform(pt4, pt4);

		// g.drawString("0°", (float)pt.getX(), (float)pt.getY());
		drawCenteredString(g, "N 0°", (int) pt1.getX(), (int) pt1.getY());
		drawCenteredString(g, "90°", (int) pt2.getX(), (int) pt2.getY());
		drawCenteredString(g, "180°", (int) pt3.getX(), (int) pt3.getY());
		drawCenteredString(g, "270°", (int) pt4.getX(), (int) pt4.getY());

		pt1 = new Point2D.Float(0, 0);
		pt2 = new Point2D.Float((float) (Math.cos(Math.toRadians(angle)) * size / 2),
				(float) (Math.sin(Math.toRadians(angle)) * size / 2));

		// TODO : Dessiner la flêche...
		drawArrow3(g, atLocal, pt1, pt2, 4.0f, 1.0f, true, false, false, 1.0f);

	}
	
	/***
	 * EXPERIMENTAL CORNER TO PUT IN WAXLIB MAYBE ONEDAY
	 */
	
	protected Shape drawString3(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor, boolean useGlyph)
	{
		/*
		boolean txtCantChangeSize_ = txtCantChangeSize;; 
		boolean invertXAxis_ = invertXAxis;
		boolean invertYAxis_ = invertYAxis;
		
		invertXAxis = true;
		invertYAxis = true;
		txtCantChangeSize = false;
		*/
		Shape shp = drawString3(g, super.at, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, drawBackGround, backGroundColor, useGlyph);
/*
		txtCantChangeSize = txtCantChangeSize_; 
		invertXAxis = invertXAxis_;
		invertYAxis = invertYAxis_;
		*/
		return shp;
	}
	
	protected Shape drawString3(Graphics2D g, AffineTransform at, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor, boolean useGlyph)
	{
		float	sx	= (float) Zoom;
		float	sy	= (float) Zoom;

		if (txtCantChangeSize == true)
		{
			sx = 1.0f / Size;
			sy = 1.0f / Size;
		}

		if ((invertXAxis == false) && (invertYAxis == false))
		{
		}
		;
		if ((invertXAxis == false) && (invertYAxis == true))
		{
			sy = -sy;
		}
		;
		if ((invertXAxis == true) && (invertYAxis == false))
		{
			sx = -sx;
		}
		;
		if ((invertXAxis == true) && (invertYAxis == true))
		{
			sx = -sx;
			sy = -sy;
		};

		float recenterx = 0, recentery = 0;
		if ((alignX != AlignTexteX.LEFT) || (alignY != AlignTexteY.BOTTOM))
		{
			float txt_with = g.getFontMetrics().stringWidth(text);
			if (alignX == AlignTexteX.CENTER)
				recenterx = +(txt_with / 2f / sx);
			if (alignX == AlignTexteX.RIGHT)
				recenterx = +(txt_with / sx);
			if (alignY == AlignTexteY.CENTER)
				recentery = -(g.getFontMetrics().getFont().getSize() / 3f / sy);
			if (alignY == AlignTexteY.TOP)
				recentery = -(g.getFontMetrics().getFont().getSize() / sy);
		}

		AffineTransform at2 = new AffineTransform();
		at2.concatenate(at);

		AffineTransform at3 = new AffineTransform();

		at3.translate(x, y);
		at3.scale(1f / sx, 1f / sy);
		at3.rotate(Math.toRadians(ang));
		at3.translate(-recenterx * sx, -recentery * sy);

		at2.concatenate(at3);

		Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at3.createTransformedShape(rect_texte);//.getBounds2D();

		if (drawBackGround == true)
		{
			Rectangle2D	rect_texteM	= g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D	rect_texte2	= (Rectangle2D) rect_texteM.clone();

			float	elargX	= 4;
			float	elargY	= 2;
			/*
			 * if (txtCantChangeSize == true) { elargX = 3; elargY = 1; }
			 */

			rect_texte2.setRect(rect_texte2.getX() - elargX / 2.0f, rect_texte2.getY() - elargY / 2.0f, rect_texte2.getWidth() + elargX, rect_texte2.getHeight() + elargY);
			Shape shp_rect_texte2 = at2.createTransformedShape(rect_texte2);
			// Dessine un cadre en dessous.
			Stroke old_Stroke = g.getStroke();
			g.setColor(backGroundColor);
			g.fill(shp_rect_texte2);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f));
			g.draw(shp_rect_texte2);

			g.setStroke(old_Stroke);
		}
		// Dessine le texte
		Color colorCurrent = g.getColor();
		g.setColor(colorCurrent);
		FontRenderContext	frc		= g.getFontRenderContext();
		GlyphVector			gv		= g.getFont().createGlyphVector(frc, text);
		int					length	= gv.getNumGlyphs();

		// Methode rapide mais qui ne permet pas de faire certains truc que l'autre bazard du dessous fait.
		if (useGlyph == false)
		{
			AffineTransform oldat = (AffineTransform) g.getTransform().clone();
			g.setTransform(at2);
			g.drawString(text, 0, 0);
			g.setTransform(oldat);
		} else
		{
			// Ca a l'air mieux que le truc au dessus, mais y'a toujours un cas qui marche pas (Porte Busque, vue du dessus, les fleches rouges du bateau.
			frc = g.getFontRenderContext();
			gv = g.getFont().createGlyphVector(frc, text);
			length = gv.getNumGlyphs();
			for (int i = 0; i < length; i++)
			{
				Point2D p = gv.getGlyphPosition(i);
				// JE pense que c'est inutile
				AffineTransform at5 = AffineTransform.getTranslateInstance(0, 0);
				at5.preConcatenate(at2);
				//at.rotate((double) i / (double) (length - 1) * Math.PI / 3);
				Shape	glyph				= gv.getGlyphOutline(i);
				Shape	transformedGlyph	= at2.createTransformedShape(glyph);
				g.fill(transformedGlyph);
			}

		}

		return rect_texte;
	}
	
	
	/**
	 * DrawArrow3 se comporte comme DrawArrow2, cad qu'on place la fleche dans le monde et pas sur l'ecran
	 * @param g
	 * @param pt1
	 * @param pt2
	 * @param scaleArrow1
	 * @param scaleArrow2
	 * @param enableArrowPt1
	 * @param enableArrowPt2
	 * @param enableMiddleArrow
	 * @param scale
	 * @return
	 */
	protected Shape drawArrow3(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow, float scale)
	{
		return drawArrow3(g, super.at, pt1, pt2, scaleArrow1, scaleArrow2, enableArrowPt1, enableArrowPt2, enableMiddleArrow, scale);
	}
	
	/**
	 * Comme drawArrow2, mais on peux lui passer une affinetransform différente de at (celle de super.at, donc celle du "monde")
	 * @param g
	 * @param at3
	 * @param pt1
	 * @param pt2
	 * @param scaleArrow1
	 * @param scaleArrow2
	 * @param enableArrowPt1
	 * @param enableArrowPt2
	 * @param enableMiddleArrow
	 * @param scale
	 * @return
	 */
	protected Shape drawArrow3(Graphics2D g, AffineTransform at3, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow, float scale)
	{
		//g.setColor(Color.BLACK);
		Line2D shape = new Line2D.Float(pt1, pt2);
		float angle1 = (float) getAngle(pt1, pt2);

		//scale * = 2;
		Path2D tri = new Path2D.Float();
		//	float scale = 1f;
		tri.moveTo(scale * 0.0, scale * -1.0);
		tri.lineTo(scale * -1.0, scale * 1.0);
		tri.lineTo(scale * 0.0, scale * 0.0f);
		tri.lineTo(scale * 1.0, scale * 1.0);
		tri.lineTo(scale * 0.0, scale * -1.0);

		Shape	trshape	= at3.createTransformedShape(shape);
		Shape	retour	= shape;
		g.draw(trshape);

		AffineTransform at2;

		Color old = g.getColor();

		if (enableArrowPt1 == true)
		{
			at2 = new AffineTransform(at3);
			at2.translate(pt1.getX(), pt1.getY());
			at2.scale(scaleArrow1, scaleArrow1);
			at2.rotate(Math.toRadians(-angle1 - 90));
			at2.translate(0, scale);

			trshape = at2.createTransformedShape(tri);
			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		if (enableArrowPt2 == true)
		{
			at2 = new AffineTransform(at3);
			at2.translate(pt2.getX(), pt2.getY());
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
			at2.translate(0, scale);
			trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		if (enableMiddleArrow == true)
		{
			at2 = new AffineTransform(at3);
			at2.translate((pt2.getX() + pt1.getX()) / 2, (pt2.getY() + pt1.getY()) / 2);
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
			//	at2.translate(0, scale);
			trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		g.setColor(old);

		return retour;
	}

	/***
	 * END OF - EXPERIMENTAL CORNER TO PUT IN WAXLIB MAYBE ONEDAY
	 */
	

	public void drawCenteredString(Graphics g, String text, int x, int y) {
		 // Cast le Graphics en Graphics2D pour des options plus avancées
	    Graphics2D g2d = (Graphics2D) g;
	    
	    // Obtenir la police et la configuration du texte
	    java.awt.Font font = g2d.getFont();
	    FontMetrics metrics = g2d.getFontMetrics(font);
	    
	    // Calculer la position de début pour que le texte soit centré
	    int textX = x - metrics.stringWidth(text) / 2;
	    int textY = y - metrics.getHeight() / 2 + metrics.getAscent();
	    
	    // Dessiner le texte centré
	    g2d.drawString(text, textX, textY);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void somethingNeedRefresh(SelectionChangedEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]){
		
		CurrentSelectionContext ctxt1 = new CurrentSelectionContext(){
			@Override
			public boolean isFiltred(Class<?> classK)
			{
				return false;
			}
			
		};
		CurrentSelectionContext ctxt2 = new CurrentSelectionContext(){
			@Override
			public boolean isFiltred(Class<?> classK)
			{
				return false;
			}
			
		};
		CurrentSelectionContext ctxt3 = new CurrentSelectionContext(){
			@Override
			public boolean isFiltred(Class<?> classK)
			{
				return false;
			}
			
		};
		CurrentSelectionContext ctxt4 = new CurrentSelectionContext(){
			@Override
			public boolean isFiltred(Class<?> classK)
			{
				return false;
			}
			
		};
		
		JFrame frame1 = new JFrame();
		VUE2D_TESTNEWDRAWARROWANDSTRING panel1 = new VUE2D_TESTNEWDRAWARROWANDSTRING(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		panel1.setInvertXAxis(false);
		panel1.setInvertYAxis(false);
//		panel1.Angle = 45;
		
		JFrame frame2 = new JFrame();
		VUE2D_TESTNEWDRAWARROWANDSTRING panel2 = new VUE2D_TESTNEWDRAWARROWANDSTRING(ctxt2);
		frame2.add(panel2);
		frame2.setSize(640,480);
		frame2.setVisible(true);
		frame2.setTitle("X=false;Y=true");
		panel2.setInvertXAxis(false);
		panel2.setInvertYAxis(true);
		
		JFrame frame3 = new JFrame();
		VUE2D_TESTNEWDRAWARROWANDSTRING panel3 = new VUE2D_TESTNEWDRAWARROWANDSTRING(ctxt3);
		frame3.add(panel3);
		frame3.setSize(640,480);
		frame3.setVisible(true);
		frame3.setTitle("X=true;Y=false");
		panel3.setInvertXAxis(true);
		panel3.setInvertYAxis(false);
		
		JFrame frame4 = new JFrame();
		VUE2D_TESTNEWDRAWARROWANDSTRING panel4 = new VUE2D_TESTNEWDRAWARROWANDSTRING(ctxt4);
		frame4.add(panel4);
		frame4.setSize(640,480);
		frame4.setVisible(true);
		frame4.setTitle("X=true;Y=true");
		panel4.setInvertXAxis(true);
		panel4.setInvertYAxis(true);
		
		frame1.setLocation(0, 0);
		frame2.setLocation(640, 0);
		frame3.setLocation(0, 480);
		frame4.setLocation(640, 480);
		
	}

}