package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.Timer;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteX;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.AlignTexteY;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;

/*
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
*/

public class VUE2D_TestBed_AffineTransforms extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4676712333053517713L;
	CurrentSelectionContext		contxt;
	
	Object arrow1 = new Object();
	Object arrow2 = new Object();
	

	//Manager						manager;
	
	//public enum ScaleTemporalGrid {WEEK, MONTH, SEMESTER, YEAR};
	
	//ConfigurationVue2DSchematique configVue2D = new ConfigurationVue2DSchematique();
	
	//int index_bateau = 0;
	//ScaleTemporalGrid scaleTemporalGrid = ScaleTemporalGrid.MONTH;
	
	/**
	 * @param contxt
	 */
	public VUE2D_TestBed_AffineTransforms(CurrentSelectionContext contxt)
	{
		super(contxt);
		setDrawFPSInfos(false);

		config.setDrawGrid(true);
		config.setDrawHelpLines(true);

		this.contxt = contxt;
		
		Zoom = 2.0;
		
		
		
		if (contxt!=null)
			contxt.addXXXListener(this);
	
	//	ScrollX = -168;
	//	ScrollY = -55.5;

	//	ZoomMin = 0.05;
	//	ZoomMax = 2.5;
		
		setToolTipText(
				"<html>" +
				"Roulette : Zoom<br>" +
				"Bt Millieu+Drag : Deplacer vue<br>" +
				"Bt Droit : Selectionner element<br>" +
				"Bt Droit+Drag : Creer rectangle de selection<br>"+
				"</html>");
		
	
		Timer timer = new Timer(25, this);
		timer.start();
	

	}
	/*
	 * (non-Javadoc)
	 * @see
	 * WaxLibrary.W2D.PanelGraphique.PanelGraphiqueBaseBase#doPaint(java.awt
	 * .Graphics2D)
	 */
	
	float ang = 0;
	@Override
	public void doPaint(Graphics2D g)
	{
		
		ang += 0.5f;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		at.rotate(Math.toRadians(0));
		
		setEnableSelectionDrawDebug(true);
		Font old_font = g.getFont();
		
		// Method 1
		 
		Rectangle2D shape_one = new Rectangle2D.Double(0, 0, 10, 10);
		
		// Deplacé le centre de gravité ou l'on veut dans le cas ou on a pas centrer l'objet au départ.
		AffineTransform at_cog = new AffineTransform();
		at_cog.translate(-5, -5);
		// Effectue une rotation autour du COG
		AffineTransform at_cog2 = new AffineTransform();
		at_cog2.translate(-10, 0);
		at_cog2.rotate(Math.toRadians(ang));
		
		//AffineTransform at_door = new AffineTransform();
		//at_door.translate(-10, 0);
		g.setColor(Color.BLACK);
		if (contxt.getSelection().contains(arrow1))
			g.setColor(Color.MAGENTA);
		g.fill(at.createTransformedShape((at_cog2.createTransformedShape(at_cog.createTransformedShape(shape_one)))));
		
		addToSelectableObject((at_cog2.createTransformedShape(at_cog.createTransformedShape(shape_one))), arrow1);
		
		
		// Method 2 - J'aime moins, plus chiant avec le addToSelectableObject
		Path2D shp_ship = new Path2D.Double();
		shp_ship.moveTo(0, 0);
		shp_ship.lineTo(0, 10);
		shp_ship.lineTo(10, 10);
		shp_ship.lineTo(10, 0);
		shp_ship.closePath();
		
		AffineTransform at_cogB = new AffineTransform();
		at_cogB.translate(-5, -5);
		//shp_ship.transform(at_cog);
		Shape shp_ship1 = at_cogB.createTransformedShape(shp_ship);

		AffineTransform old_at = (AffineTransform) at.clone();
		// Position dans l'espace et rotation
		AffineTransform at2 = new AffineTransform();
		at2.translate(10, 0);
		at2.rotate(Math.toRadians(ang));
		//at2.concatenate(at_cog);
		at.concatenate(at2);
		g.setColor(Color.BLACK);
		if (contxt.getSelection().contains(arrow2))
			g.setColor(Color.MAGENTA);
		g.fill(at.createTransformedShape(shp_ship1));
		
		addToSelectableObject(at2.createTransformedShape(shp_ship1), arrow2);
		
		at = old_at;
		
		
		
		g.setFont(old_font);
		

		g.setColor(Color.BLACK);
		
		g.drawString(String.format("XY : %d,  %d",  (int)MouseX, (int)MouseY), 10,10);
		g.drawString(String.format("Selected : %d", contxt.getSelection().size()), 10,20);
		
		
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
		// TODO Auto-generated method stub

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
		
	}

	

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
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
		VUE2D_TestBed_AffineTransforms panel1 = new VUE2D_TestBed_AffineTransforms(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		panel1.invertXAxis = false;
		panel1.invertYAxis = false;
//		panel1.Angle = 45;
		
		JFrame frame2 = new JFrame();
		VUE2D_TestBed_AffineTransforms panel2 = new VUE2D_TestBed_AffineTransforms(ctxt2);
		frame2.add(panel2);
		frame2.setSize(640,480);
		frame2.setVisible(true);
		frame2.setTitle("X=false;Y=true");
		panel2.invertXAxis = false;
		panel2.invertYAxis = true;
		
		JFrame frame3 = new JFrame();
		VUE2D_TestBed_AffineTransforms panel3 = new VUE2D_TestBed_AffineTransforms(ctxt3);
		frame3.add(panel3);
		frame3.setSize(640,480);
		frame3.setVisible(true);
		frame3.setTitle("X=true;Y=false");
		panel3.invertXAxis = true;
		panel3.invertYAxis = false;
		
		JFrame frame4 = new JFrame();
		VUE2D_TestBed_AffineTransforms panel4 = new VUE2D_TestBed_AffineTransforms(ctxt4);
		frame4.add(panel4);
		frame4.setSize(640,480);
		frame4.setVisible(true);
		frame4.setTitle("X=true;Y=true");
		panel4.invertXAxis = true;
		panel4.invertYAxis = true;
		
		frame1.setLocation(0, 0);
		frame2.setLocation(640, 0);
		frame3.setLocation(0, 480);
		frame4.setLocation(640, 480);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		repaint();
		
	}
	
	
	 
	

}