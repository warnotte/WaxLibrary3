package org.warnotte.waxlibswingcomponents.Swing.Component.CurveEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.warnotte.waxlibswingcomponents.Utils.Curve.Curve;



public class CurvePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{

	private static final long	serialVersionUID	= 1L;
	private Curve curve = new Curve();  //  @jve:decl-index=0:
	private int	SelectedIndex;
	private int	oldSelectedIndex;
	CurvePanel parent=null;
	
	PopupMenu_VCA popupMenu = null;
	
	/**
	 * This is the default constructor
	 * @throws Exception 
	 */
	public CurvePanel() throws Exception
	{
		this(new Curve());
		
	}
	
	public CurvePanel(Curve crv) throws Exception
	{
		super();
		this.curve=crv;
		initialize();
	}

	public CurvePanel(Curve curve2, CurvePanel parent) throws Exception {
		this(curve2);
		this.parent=parent;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws Exception 
	 */
	private void initialize() throws Exception
	{
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		popupMenu=new PopupMenu_VCA(this,curve);
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		
		int w = getWidth();
		int h = getHeight();
		
		g.fillRect(0,0, w, h);
		
		w = getWidth()-20;
		h = getHeight()-20;
		
		g.translate(10,10);
		
		int div = curve.size();
		float spc = w / (div-1);
		
		g.setColor(Color.gray);
		g.drawRect(0,0, w, h);
		
		// Dessine la grille
		{
			g.setColor(Color.gray);
			for (int i = 0; i < 10; i++)
			{
				int y = (int)(i* ((float)h / (float)10));
				g.drawLine(0, y, w, y);
				g.drawString(""+(10-i)/10f, 10, y-2);
			}
		}
		
	
		// TODO : Ici j'ai fait un melange a la con avec getDate et getValue().... faut utiliser getValue(0..1)
		if (curve.isSpline()==false)
		{
			// Dessine la courbe
			{
				g.setColor(Color.yellow);
				for (int i = 0; i < div-1; i++)
				{
					g.setColor(Color.yellow);
					int x1 = (int)(spc*i);
					int y1 = h-(int)(curve.getData(i)*h);
					int x2 = (int)(spc*(i+1));
					int y2 = h-(int)(curve.getData(i+1)*h);
					g.drawLine(x1, y1, x2, y2);
	
					if (i==SelectedIndex)
					{
						g.setColor(Color.GREEN);
						g.fillRect(x1-8, y1-8, 16, 16);
					}
				}
			}
		}
		else
		{
			int x1 = (int)(spc*0);
			
			int y1 = h-(int)(curve.getValue(0)*h);
			//int cpt = curve.size();
			
			for (float xi = 0; xi <= 1; xi+=0.01)
			{
				g.setColor(Color.yellow);
				int x2 = (int) (xi*w);//(cpt-1)*(int)(spc*(xi));
				int y2 = h-(int)(curve.getValue(xi)*h);
				g.drawLine(x1, y1, x2, y2);
				x1=x2;
				y1=y2;
				
			}
			
		
		}
		
	/*g.setColor (Color.blue);
	      //g.setStroke (s);
	      final QuadCurve2D.Float q = new QuadCurve2D.Float ();
	      float[] qc = curve.getDatas();
	      double [] qc2 = new double[qc.length];
	      for (int i = 0; i < qc2.length; i++) {
			qc2[i]=qc[i];
		}
	      q.setCurve (qc2,0);
	      ((Graphics2D)g).draw (q);
		
		*/
		// Dessine les points verts.
		{
			for (int i = 0; i < div; i++)
			{
				g.setColor(Color.yellow);
				int x1 = (int)(spc*i);
				int y1 = h-(int)(curve.getData(i)*h);
				
				if (i==SelectedIndex)
				{
					int sz = 16;
					g.setColor(Color.RED);
					g.fillRect(x1-sz/2, y1-sz/2, sz, sz);
				}
				else
				{
					int sz = 8;
					g.setColor(Color.green);
					g.fillRect(x1-sz/2, y1-sz/2, sz, sz);
				
				}
			}
		}
		
		
		g.setColor(Color.white);
		g.drawString("NbrPoints="+curve.size(), 20, 25);
		
		if (parent!=null) parent.repaint();
	}
	
	
	/*public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		
		int w = getWidth();
		int h = getHeight();
		
		g.fillRect(0,0, w, h);
		
		w = getWidth()-20;
		h = getHeight()-20;
		
		g.translate(10,10);
		
		int div = curve.size();
		float spc = w / (div-1);
		
		g.setColor(Color.gray);
		g.drawRect(0,0, w, h);
		
		// Dessine la grille
		{
			g.setColor(Color.gray);
			for (int i = 0; i < 10; i++)
			{
				int y = (int)((float)i* ((float)h / (float)10));
				g.drawLine(0, y, w, y);
				g.drawString(""+(10-i)/10f, 10, y-2);
			}
		}
		
	
		// Dessine la courbe
		{
			
			g.setColor(Color.yellow);
			for (int i = 0; i < div-1; i++)
			{
				g.setColor(Color.yellow);
				int x1 = (int)(spc*i);
				int y1 = h-(int)(curve.getData(i)*h);
				int x2 = (int)(spc*(i+1));
				int y2 = h-(int)(curve.getData(i+1)*h);
				g.drawLine(x1, y1, x2, y2);
				
				if (i==SelectedIndex)
				{
					g.setColor(Color.GREEN);
					g.fillRect(x1-8, y1-8, 16, 16);
				}
			}
		}
		
		// Dessine les points verts.
		{
			for (int i = 0; i < div; i++)
			{
				g.setColor(Color.yellow);
				int x1 = (int)(spc*i);
				int y1 = h-(int)(curve.getData(i)*h);
				
				if (i==SelectedIndex)
				{
					int sz = 16;
					g.setColor(Color.RED);
					g.fillRect(x1-sz/2, y1-sz/2, sz, sz);
				}
				else
				{
					int sz = 8;
					g.setColor(Color.green);
					g.fillRect(x1-sz/2, y1-sz/2, sz, sz);
				
				}
			}
		}
		
		
		g.setColor(Color.white);
		g.drawString("NbrPoints="+curve.size(), 20, 25);
		
		if (parent!=null) parent.repaint();
	}*/

	
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton()==MouseEvent.BUTTON3)
		{
			
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
			/*
			float valeur = 0.75f;
			curve.addData(SelectedIndex, valeur);
			//curve.addData( valeur);
			repaint();*/
		}
		
		if (e.getButton()==MouseEvent.BUTTON2)
		{
			for (int i = 0; i < 100; i++)
			{
				float index = i / 100.0f;
				float interp_value;
				try
				{
					interp_value = curve.getValue(index);
					System.err.printf("%2.2f\r\n",interp_value);
				} 
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	
	public void mouseDragged(MouseEvent e)
	{
		float valeur = curve.getData(SelectedIndex);
		int y = e.getY();
		valeur = 1.0f - (float) y / (float) getHeight();
		valeur = constrains(valeur, 0.0f, 1.0f);
		//System.err.println("Set Value to " + valeur);
		curve.setData(SelectedIndex, valeur);
		repaint();
		
	}
	
	public static float constrains(float value, float min, float max)
	{
		if (value>=max) value=max;
		if (value<=min) value=min;
		return value;
	}

	
	public void mouseMoved(MouseEvent e)
	{
		int x = e.getX();
		
		
		SelectedIndex = getIndexByMouseX(x);
		if (oldSelectedIndex!=SelectedIndex)
		{
		System.err.println(""+SelectedIndex);
		repaint();
		
		oldSelectedIndex=SelectedIndex;
		}
		requestFocus();
	}
	
	private int getIndexByMouseX(int x)
	{
		int w = getWidth();
		int div = curve.size();
		float spc = w / (div-1);
		
		int SelectedIndex = ((int)((x+spc/2) / spc)  );
		return SelectedIndex;
	}

	
	//
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_INSERT)
		{
			float valeur = curve.getData(SelectedIndex);
			curve.addData(SelectedIndex, valeur);
			repaint();
			
		}
		if (e.getKeyCode()==KeyEvent.VK_DELETE)
		{
			if (SelectedIndex!=-1)
			{
				curve.remove(SelectedIndex);
				repaint();
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		
	}

	public void keyTyped(KeyEvent e)
	{
	
	}

}
