package io.github.warnotte.waxlib3.W2D.PanelGraphique.ExperimentalAffineTransformSolution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph.Manager;

public class VUE2D_TestRoseDesVents extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{

	protected static final Logger Logger = LogManager.getLogger(VUE2D_TestRoseDesVents.class);

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4676712333053517713L;
	
	
	double MouseX_start = 0;
	double MouseY_start = 0;
	
	public enum ActionMode{
		SELECTION, MOVE, ROTATE
	}
	
	ActionMode actionMode = ActionMode.SELECTION; // TODO : Mettre un enum a la place genre NONE, DRAGGING, xxxx
	boolean lockMoveAxisX = false;
	boolean lockMoveAxisY = false;

	//JMenuItem menu_item_setShipDraft;
	
	/**
	 * @param contxt
	 */
	public VUE2D_TestRoseDesVents(CurrentSelectionContext contxt)
	{
		super(contxt);
		
		Zoom=0.4;
		
		

	}


	float ang = 0;
	@Override
	public void doPaint(Graphics2D g)
	{
		config.setDrawGrid(false);
		config.setDrawHelpLines(true);
		
		g.setColor(new Color(90,255,90));
		g.fillRect(0, 0, getWidth(), getHeight());
		
	
		
		float size = 60;
		float offsetfromborder= 20;
		
		int w = getWidth();
		int h = getHeight();
		int posX = (int)( w-size/2-offsetfromborder );
		int posY = (int)( h-size/2-offsetfromborder );
		
		AffineTransform at1 = new AffineTransform();
		at1.translate(posX, posY);
		drawRoseDesVents(g, at1, 45, 60);
		
		at1 = new AffineTransform();
		at1.translate(120, 300);
		drawRoseDesVents(g, at1, 45, 60);
		
		at1 = new AffineTransform();
		at1.translate(120, 450);
		drawRoseDesVents(g, at1, 90, 90);
		
		
		at1 = new AffineTransform();
		at1.translate(260, 450);
		at1.rotate(Math.toRadians(45));
		drawRoseDesVents(g, at1, 90, 90);
		
		
		at1 = new AffineTransform();
		at1.translate(120, 600);
		drawRoseDesVents(g, at1, 220, 30);
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(120, 600);
		at1.scale(1, -1);
		drawRoseDesVents(g, at1, 220, 90);
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(420, 600);
		at1.scale(1/Zoom, -1/Zoom);
		drawRoseDesVents(g, at1, 220, 90);
		
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(120, 600+400);
		at1.scale(1, -1);
		
		float angle =270+(float) getAngle(new Point2D.Double(MouseX, MouseY), new Point2D.Double(120, 600+400));
		drawRoseDesVents(g, at1, angle, 90);
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(420, 600+400);
		at1.scale(1/Zoom, -1/Zoom);
		angle =270+(float) getAngle(new Point2D.Double(MouseX, MouseY), new Point2D.Double(420, 600+400));
		drawRoseDesVents(g, at1, angle, 90);
		
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(120, 600-400);
		at1.rotate(Math.toRadians(-45));
		at1.scale(1, -1);
		drawRoseDesVents(g, at1, 220, 90);
		
		
		at1 = new AffineTransform();
		at1.preConcatenate(at);
		at1.translate(420, 600-400);
		at1.rotate(Math.toRadians(-45));
		at1.scale(1/Zoom, -1/Zoom);
		drawRoseDesVents(g, at1, 220, 90);
		
		
		
		g.setColor(Color.black);
		
		g.setColor(Color.BLACK);
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * WaxLibrary.W2D.PanelGraphique.SelectionChangeable#somethingNeedRefresh
	 * (WaxLibrary.W2D.PanelGraphique.SelectionChangedEvent)
	 */
	@Override
	public void somethingNeedRefresh(SelectionChangedEvent e)
	{
		Manager manager = (Manager) context.getManager();
		//logger.info("Something need refresh "+this);
	}

	
	@Override
	public void mousePressed(java.awt.event.MouseEvent e)
    {
		super.mousePressed(e);
		repaint();
    }

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		super.mouseReleased(e);
		repaint();

	}

	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		repaint();
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e)
	{
		super.mouseDragged(e);
		
	
		
		repaint();
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e)
	{
		super.mouseMoved(e);
		
	
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_G)
		{
			MouseX_start=MouseX;
			MouseY_start=MouseY;
			actionMode = ActionMode.MOVE;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_R)
		{
			MouseX_start=MouseX;
			MouseY_start=MouseY;
			actionMode = ActionMode.ROTATE;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_X)
		{
			lockMoveAxisX = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_Y)
		{
			lockMoveAxisY = true;
		}
		
	}

	

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		super.keyReleased(e);
		if ((e.getKeyCode() == KeyEvent.VK_G) && (actionMode==ActionMode.MOVE))
		{
			actionMode = ActionMode.SELECTION;
		}
		if ((e.getKeyCode() == KeyEvent.VK_R) && (actionMode==ActionMode.ROTATE))
		{
			actionMode = ActionMode.SELECTION;
		}
		if (e.getKeyCode() == KeyEvent.VK_X)
		{
			lockMoveAxisX = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_Y)
		{
			lockMoveAxisY = false;
		}
	}

	
	public static void main(String args[]) throws Exception{
		
		CurrentSelectionContext ctxt1 = new CurrentSelectionContext(){
			@Override
			public boolean isFiltred(Class<?> classK)
			{
				return false;
			}
			
		};
		
		JFrame frame1 = new JFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		VUE2D_TestRoseDesVents panel1 = new VUE2D_TestRoseDesVents(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		frame1.setLocation(0, 0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		repaint();
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

	
}