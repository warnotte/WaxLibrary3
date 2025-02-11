package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;


public class VUE2D_Spiro extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4676712333053517713L;
	CurrentSelectionContext		contxt;
	private Manager manager;
	
	
	
	//public enum ScaleTemporalGrid {WEEK, MONTH, SEMESTER, YEAR};
	
	//ConfigurationVue2DSchematique configVue2D = new ConfigurationVue2DSchematique();
	
	//int index_bateau = 0;
	//ScaleTemporalGrid scaleTemporalGrid = ScaleTemporalGrid.MONTH;
	
	
	/**
	 * @param contxt
	 */
	public VUE2D_Spiro(CurrentSelectionContext contxt)
	{
		super(contxt);
		manager = (Manager) contxt.getManager();
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
	
	long last = System.currentTimeMillis();
	long current; 
	float ang = 0;
	@Override
	public void doPaint(Graphics2D g)
	{
		config.setDrawGrid(false);
		current = System.currentTimeMillis();
		float elapsed = (current - last) / 1000.0f;
		last = current;
		
		ang += elapsed * 100;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		
		List<Point2D> points[] = manager.getPoints();
		
		int idx = points.length-1;
		Path2D path = new Path2D.Double();
	
		path.moveTo(points[idx].get(0).getX(), points[idx].get(0).getY());
		for (int i = 1; i < points[idx].size(); i++) {
			path.lineTo(points[idx].get(i).getX(), points[idx].get(i).getY());
		}
		//path.closePath();
		g.setColor(Color.blue);
		g.draw(at.createTransformedShape(path));
		
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
		if (e.getKeyCode() == KeyEvent.VK_R)
			manager.recompute();
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
		VUE2D_Spiro panel1 = new VUE2D_Spiro(ctxt1);
		frame1.add(panel1);
		frame1.setSize(640,480);
		frame1.setVisible(true);
		frame1.setTitle("X=false;Y=false");
		panel1.invertXAxis = false;
		panel1.invertYAxis = false;
//		panel1.Angle = 45;
		
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
	
	
	 
	

}