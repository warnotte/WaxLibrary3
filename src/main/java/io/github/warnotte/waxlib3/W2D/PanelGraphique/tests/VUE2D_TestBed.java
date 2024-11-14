package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

public class VUE2D_TestBed extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
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
	public VUE2D_TestBed(CurrentSelectionContext contxt)
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

		java.awt.Shape shp= new Rectangle2D.Double(10,10,50,50);

		g.draw(at.createTransformedShape(shp));
		
		drawString(g, "Bonjour", 10, 10);
		
		at.rotate(Math.toRadians(0));
		
		Font old_font = g.getFont();
		
		g.setFont(new Font("Impact", Font.BOLD, 18));
	/*	
		drawStringOLD(g, "Hello1", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f);
		drawStringOLD(g, "Hello2", -20, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawStringOLD(g, "Hello3", 20, 0, 0, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f);
	
		
		drawStringOLD(g, "Hello1", -100, -50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawStringOLD(g, "Hello2", -100, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawStringOLD(g, "Hello3", -100, 50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
	*/
		
		drawString(g, "Hello1", 0, 0, 0, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f);
		drawString(g, "Hello2", -20, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawString(g, "Hello3", 20, 0, 0, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f);
	
		
		drawString(g, "Hello1", -100, -50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawString(g, "Hello2", -100, 0, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		drawString(g, "Hello3", -100, 50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
	
		g.setFont(old_font);
		
		if (contxt.getSelection().contains(txt1)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt1 = drawString(g, "Hello1", 10, 20, 0, AlignTexteX.LEFT, AlignTexteY.BOTTOM, false, 1.0f);
		if (contxt.getSelection().contains(txt2)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt2 = drawString(g, "Hello2", 10, 20, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);

		g.setFont(new Font("Impact", Font.PLAIN, 16));
		if (contxt.getSelection().contains(txt3)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt3 = drawString(g, "Hello3", 75, 20, 0, AlignTexteX.LEFT, AlignTexteY.BOTTOM, false, 1.0f);
		if (contxt.getSelection().contains(txt4)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt4 = drawString(g, "Hello4", 75, 20, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);

		g.setFont(old_font);
		if (contxt.getSelection().contains(txt5)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt5 = drawString(g, "Hello5", 20, 30, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f);
		if (contxt.getSelection().contains(txt6)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt6 = drawString(g, "Hello6", 20, 30, 0, AlignTexteX.LEFT, AlignTexteY.BOTTOM, true, 1.0f);
		
		
		if (contxt.getSelection().contains(txt7)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt7 = drawString(g, "Hello7", 40, 50, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 2.0f);
		if (contxt.getSelection().contains(txt8)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt8 = drawString(g, "Hello8", 40, 50, 45, AlignTexteX.LEFT, AlignTexteY.BOTTOM, true, 2.0f);
		
		
		if (contxt.getSelection().contains(txt9)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt9 = drawString(g, "Rotate1", 140, 50, ang, AlignTexteX.CENTER, AlignTexteY.CENTER, true, 1.0f, true);
		if (contxt.getSelection().contains(txt10)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt10 = drawString(g, "Rotate2", 140, 50, ang, AlignTexteX.LEFT, AlignTexteY.BOTTOM, true, 1.0f, true);
		if (contxt.getSelection().contains(txt11)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt11 = drawString(g, "Rotate3", 140, 90, ang, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, true);
		if (contxt.getSelection().contains(txt12)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt12 = drawString(g, "Rotate4", 140, 90, ang, AlignTexteX.LEFT, AlignTexteY.BOTTOM, false, 1.0f, true);
		
		g.setColor(new Color(127,127,0,127));
		g.fill(at.createTransformedShape(shp_txt2));
		
		
		if (contxt.getSelection().contains(txt13)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt13 = drawString(g, "LEFTDOWN", 240, 0, 0, AlignTexteX.LEFT, AlignTexteY.BOTTOM, false, 1.0f, true);
		if (contxt.getSelection().contains(txt14)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt14 = drawString(g, "CENTERDOWN", 240, 10, 0, AlignTexteX.CENTER, AlignTexteY.BOTTOM, false, 1.0f, true);
		if (contxt.getSelection().contains(txt15)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt15 = drawString(g, "RIGHTDOWN", 240, 20, 0, AlignTexteX.RIGHT, AlignTexteY.BOTTOM, false, 1.0f, true);
	
		if (contxt.getSelection().contains(txt16)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt16 = drawString(g, "LEFTCENTER", 300, 0, 0, AlignTexteX.LEFT, AlignTexteY.CENTER, false, 1.0f, true);
		if (contxt.getSelection().contains(txt17)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt17 = drawString(g, "CENTERCENTER", 300, 10, 0, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, true);
		if (contxt.getSelection().contains(txt18)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt18 = drawString(g, "RIGHTCENTER", 300, 20, 0, AlignTexteX.RIGHT, AlignTexteY.CENTER, false, 1.0f, true);
	
		if (contxt.getSelection().contains(txt19)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt19 = drawString(g, "LEFTCENTER", 360, 0, 0, AlignTexteX.LEFT, AlignTexteY.TOP, false, 1.0f, true);
		if (contxt.getSelection().contains(txt20)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt20 = drawString(g, "CENTERCENTER", 360, 10, 0, AlignTexteX.CENTER, AlignTexteY.TOP, false, 1.0f, true);
		if (contxt.getSelection().contains(txt21)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_txt21 = drawString(g, "RIGHTCENTER", 360, 20, 0, AlignTexteX.RIGHT, AlignTexteY.TOP, false, 1.0f, true);
			
		if (contxt.getSelection().contains(arrow1)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_arrow1 = drawArrow2(g, new Point2D.Double(0,-25), new Point2D.Double(50,-20),  1.0f, 1.0f, true, true);
		if (contxt.getSelection().contains(arrow2)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_arrow2 = drawArrow2(g, new Point2D.Double(0,-50), new Point2D.Double(50,-45),  1.0f, 2.0f, true, true);
		if (contxt.getSelection().contains(arrow3)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_arrow3 = drawArrow2(g, new Point2D.Double(0,-75), new Point2D.Double(50,-70),  2.0f, 1.0f, true, true);
		if (contxt.getSelection().contains(arrow4)) g.setColor(Color.MAGENTA); else g.setColor(Color.BLACK);
		Shape shp_arrow4 = drawArrow2(g, new Point2D.Double(0,-100), new Point2D.Double(50,-95), 0.5f, 3.0f, true, true);
		
		
		addToSelectableObject(shp_arrow1, arrow1);
		addToSelectableObject(shp_arrow2, arrow2);
		addToSelectableObject(shp_arrow3, arrow3);
		addToSelectableObject(shp_arrow4, arrow4);
		
		addToSelectableObject(shp_txt1, txt1);
		addToSelectableObject(shp_txt2, txt2);
		addToSelectableObject(shp_txt3, txt3);
		addToSelectableObject(shp_txt4, txt4);
		addToSelectableObject(shp_txt5, txt5);
		addToSelectableObject(shp_txt6, txt6);
		addToSelectableObject(shp_txt7, txt7);
		addToSelectableObject(shp_txt8, txt8);
		
		addToSelectableObject(shp_txt9, txt9);
		addToSelectableObject(shp_txt10, txt10);
		addToSelectableObject(shp_txt11, txt11);
		addToSelectableObject(shp_txt12, txt12);
		addToSelectableObject(shp_txt13, txt13);
		addToSelectableObject(shp_txt14, txt14);
		addToSelectableObject(shp_txt15, txt15);
		addToSelectableObject(shp_txt16, txt16);
		addToSelectableObject(shp_txt17, txt17);
		addToSelectableObject(shp_txt18, txt18);
		addToSelectableObject(shp_txt19, txt19);
		addToSelectableObject(shp_txt20, txt20);
		addToSelectableObject(shp_txt21, txt21);
		
		//setEnableSelectionDrawDebug(true);
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
		VUE2D_TestBed panel1 = new VUE2D_TestBed(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		panel1.invertXAxis = false;
		panel1.invertYAxis = false;
//		panel1.Angle = 45;
		
		JFrame frame2 = new JFrame();
		VUE2D_TestBed panel2 = new VUE2D_TestBed(ctxt2);
		frame2.add(panel2);
		frame2.setSize(640,480);
		frame2.setVisible(true);
		frame2.setTitle("X=false;Y=true");
		panel2.invertXAxis = false;
		panel2.invertYAxis = true;
		
		JFrame frame3 = new JFrame();
		VUE2D_TestBed panel3 = new VUE2D_TestBed(ctxt3);
		frame3.add(panel3);
		frame3.setSize(640,480);
		frame3.setVisible(true);
		frame3.setTitle("X=true;Y=false");
		panel3.invertXAxis = true;
		panel3.invertYAxis = false;
		
		JFrame frame4 = new JFrame();
		VUE2D_TestBed panel4 = new VUE2D_TestBed(ctxt4);
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