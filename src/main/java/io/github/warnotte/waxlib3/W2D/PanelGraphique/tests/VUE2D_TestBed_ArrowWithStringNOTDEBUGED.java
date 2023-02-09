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

public class VUE2D_TestBed_ArrowWithStringNOTDEBUGED extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4676712333053517713L;
	CurrentSelectionContext		contxt;
	
	Object arrow1 = new Object();
	Object arrow2 = new Object();
	Object arrow3 = new Object();
	Object arrow4 = new Object();

	Object txt1 = new Object();
	Object txt2 = new Object();
	Object txt3 = new Object();
	Object txt4 = new Object();
	Object txt5 = new Object();
	Object txt6 = new Object();
	Object txt7 = new Object();
	Object txt8 = new Object();

	Object txt9 = new Object();
	Object txt10 = new Object();
	Object txt11 = new Object();
	Object txt12 = new Object();

	Object txt13 = new Object();
	Object txt14 = new Object();
	Object txt15 = new Object();

	Object txt16 = new Object();
	Object txt17 = new Object();
	Object txt18 = new Object();
	Object txt19 = new Object();
	Object txt20 = new Object();
	Object txt21 = new Object();

	//Manager						manager;
	
	//public enum ScaleTemporalGrid {WEEK, MONTH, SEMESTER, YEAR};
	
	//ConfigurationVue2DSchematique configVue2D = new ConfigurationVue2DSchematique();
	
	//int index_bateau = 0;
	//ScaleTemporalGrid scaleTemporalGrid = ScaleTemporalGrid.MONTH;
	
	/**
	 * @param contxt
	 */
	public VUE2D_TestBed_ArrowWithStringNOTDEBUGED(CurrentSelectionContext contxt)
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
		//ang=0;
	//	Angle=Angle+1.0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		at.rotate(Math.toRadians(0));
		
		setEnableSelectionDrawDebug(true);
		Font old_font = g.getFont();
		
		//g.setFont(new Font("Impact", Font.BOLD, 20));
		g.setColor(Color.BLACK);
		drawStringOLD(g, "OLD1false", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawStringOLD(g, "OLD2false", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawStringOLD(g, "OLD3false", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);

		g.setColor(Color.RED);
		drawStringOLD(g, "OLD1true", 0, -15+75, 45, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
		drawStringOLD(g, "OLD2true", 0, 0+75, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
		drawStringOLD(g, "OLD3true", 0, 15+75, -45, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);

		

		AffineTransform old_at = (AffineTransform) at.clone();
		AffineTransform at2 = new AffineTransform();
		at2.translate(-100, 50);
		at2.rotate(Math.toRadians(0));
		at.concatenate(at2);
		g.setColor(Color.BLACK);
		drawString(g, "NewFalse1", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f, true, Color.gray);
		drawString(g, "NewFalse2", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 2.0f);
		drawString(g, "NewFalse3", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f);
		at = old_at;
		
		
		old_at = (AffineTransform) at.clone();
		at2 = new AffineTransform();
		at2.translate(-100, -50);
		at2.rotate(Math.toRadians(0));
		at.concatenate(at2);
		g.setColor(Color.RED);
		drawString(g, "NewTrue1", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.BOTTOM, true, 1.0f, true, Color.gray);
		drawString(g, "NewTrue2", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
		drawString(g, "NewTure3", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.TOP, true, 1.0f);
		at = old_at;
		
		old_at = (AffineTransform) at.clone();
		at2 = new AffineTransform();
		at2.translate(100, 0);
		at2.rotate(Math.toRadians(0+ang));
		at.concatenate(at2);
		g.setColor(Color.BLACK);
		drawString(g, "NewFalse1", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f, true, Color.gray);
		drawString(g, "NewFalse2", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawString(g, "NewFalse3", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f);
		at = old_at;
		
		
		old_at = (AffineTransform) at.clone();
		at2 = new AffineTransform();
		at2.translate(100, -75);
		at2.rotate(Math.toRadians(0+ang));
		at.concatenate(at2);
		g.setColor(Color.RED);
		drawString(g, "Newtrue1", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.BOTTOM, true, 1.0f, true, Color.gray);
		drawString(g, "Newtrue2", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
		drawString(g, "Newtrue3", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.TOP, true, 1.0f);
		at = old_at;
		
		
		
		old_at = (AffineTransform) at.clone();
		at2 = new AffineTransform();
		at2.translate(10, 0);
		at2.rotate(Math.toRadians(0));
		at.concatenate(at2);
		g.setColor(Color.BLACK);
		drawString(g, "OLDfalse1", 0, -15, 45, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f, true, Color.gray);
		drawString(g, "OLDfalse2", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawString(g, "OLDfalse3", 0, 15, -45, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f);
		
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
		VUE2D_TestBed_ArrowWithStringNOTDEBUGED panel1 = new VUE2D_TestBed_ArrowWithStringNOTDEBUGED(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		panel1.invertXAxis = false;
		panel1.invertYAxis = false;
//		panel1.Angle = 45;
		
		JFrame frame2 = new JFrame();
		VUE2D_TestBed_ArrowWithStringNOTDEBUGED panel2 = new VUE2D_TestBed_ArrowWithStringNOTDEBUGED(ctxt2);
		frame2.add(panel2);
		frame2.setSize(640,480);
		frame2.setVisible(true);
		frame2.setTitle("X=false;Y=true");
		panel2.invertXAxis = false;
		panel2.invertYAxis = true;
		
		JFrame frame3 = new JFrame();
		VUE2D_TestBed_ArrowWithStringNOTDEBUGED panel3 = new VUE2D_TestBed_ArrowWithStringNOTDEBUGED(ctxt3);
		frame3.add(panel3);
		frame3.setSize(640,480);
		frame3.setVisible(true);
		frame3.setTitle("X=true;Y=false");
		panel3.invertXAxis = true;
		panel3.invertYAxis = false;
		
		JFrame frame4 = new JFrame();
		VUE2D_TestBed_ArrowWithStringNOTDEBUGED panel4 = new VUE2D_TestBed_ArrowWithStringNOTDEBUGED(ctxt4);
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