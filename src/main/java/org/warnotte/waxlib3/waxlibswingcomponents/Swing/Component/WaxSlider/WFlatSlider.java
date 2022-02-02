package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSlider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WFlatSlider extends javax.swing.JPanel implements ComponentListener, MouseListener
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 2646803907927633567L;
	private final Vector<ChangeListener>	listeners			= new Vector<ChangeListener>();	//  @jve:decl-index=0:
	private final Vector<ActionListener>	listenersA			= new Vector<ActionListener>();	//  @jve:decl-index=0:

	private int								max;
	private int								min;
	private int								value				= 0;							// Sera pour contenir la valeur réel du slider
	private int								old_value;
	private float							Divider				= 1;
	private boolean							EnableAntialiasing	= true;
	Point									mouseClick			= new Point();
	static Image							im					= null;
	int										imgOffs				= 0;
	Font									font				= null;
	String									family				= "Verdana";
	int										style				= Font.PLAIN;
	FontMetrics								fontMetrics			= null;

	String									Label				= "";

	public synchronized String getLabel()
	{
		return Label;
	}

	public synchronized void setLabel(String label)
	{
		Label = label;
	}

	public WFlatSlider() // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
	{
		//im = new ImageIcon("D:\\Workspace\\WaxLib\\WaxLib\\WaxLibrary\\Graphics\\Component\\RotativeSlider\\blurbox.png").getImage();

		this.setName("VolumePannel");
		initComponents();
		value = 0;
		max = 100;
	}

	public WFlatSlider(int max) // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
	{
		this();
		this.max = max;
	}

	public WFlatSlider(int min, int max) // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
	{
		this(max);
		this.min = min;
	}

	public WFlatSlider(int min, int max, int value) // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
	{
		this(max);
		this.min = min;
		this.value = value;
	}

	private void initComponents()
	{

		if (im == null)
		{
			URL url = getClass().getResource("/images/blurbox.png");
			im = new ImageIcon(url).getImage();
		}
		//this.setDoubleBuffered(true);
		this.addComponentListener(this);
		setLayout(new BorderLayout());
		this.setFocusable(true);
		this.requestFocus();
		this.setSize(new Dimension(150, 35));
		this.setMinimumSize(new Dimension(150, 35));
		this.setPreferredSize(new Dimension(150, 35));
		this.addMouseListener(new java.awt.event.MouseAdapter()
		{

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e)
			{
				requestFocus();
			}
		});
		//ToolTipManager.sharedInstance().setInitialDelay(0);

		this.addKeyListener(new java.awt.event.KeyListener()
		{
			public void keyReleased(java.awt.event.KeyEvent e)
			{
				double incvalue = getIncrementValue();
				if (e.getKeyCode() == KeyEvent.VK_HOME)
				{
					value = min;
					imgOffs = 0;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_END)
				{
					value = max;
					imgOffs = 0;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					value += 1;
					if (value > max)
						value = max;
					else
						imgOffs++;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
				{
					value -= 1;
					if (value < min)
						value = min;
					else
						imgOffs--;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
				{
					value -= incvalue;
					if (value < min)
						value = min;
					else
						imgOffs += incvalue;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
				{
					value += incvalue;
					if (value > max)
						value = max;
					else
						imgOffs -= incvalue;
					old_value = value;
					ChangeEvent ch = new ChangeEvent(WFlatSlider.this);
					fireChangeEvent(ch);
					fireActionEvent(new ActionEvent(WFlatSlider.this, 0, "ValueChanged"));
					repaint();
				}
			}

			public void keyTyped(java.awt.event.KeyEvent e)
			{
			}

			public void keyPressed(java.awt.event.KeyEvent e)
			{
			}
		});

		addMouseListener(this);

		addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{

			@Override
			public void mouseDragged(java.awt.event.MouseEvent evt)
			{
				formMouseDragged(evt);
			}

			@Override
			public void mouseMoved(java.awt.event.MouseEvent evt)
			{
				formMouseMoved(evt);
			}

		});
		int size = 12;
		font = new Font(family, style, size);

	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Permet de recuperer la valeur d'un increment (cad entre 2 valeurs)
	 * 
	 * @return
	 */
	private double getIncrementValue()
	{
		float valeur = (float) Math.abs(max - min) / 20;
		valeur = (float) Math.ceil(valeur);
		return (int) valeur;
	}

	////// PAINT ////////////////////////////////////////////////////////////////////////

	@Override
	public void paint(Graphics g1)
	{
		//System.err.println("Repaint();");
		int w = this.getWidth();
		int h = this.getHeight();
		Graphics2D g = (Graphics2D) g1;
		font = new Font(family, style, 1 * h / 3);
		g.setFont(font);
		// Je pense que ceci devrait pas etre fait tout le temps
		if (EnableAntialiasing == true)
		{
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} else
		{
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

		//	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		//	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);

		float WW = ((((float) value - (float) min)) / ((max - min)));
		int ww = (int) (WW * w);

		//	System.err.printf("w=%03d value=%03d ww=%03d WW=%03f\r\n", w,value,ww,WW);

		int tilesize = 15;

		for (int offsx = -tilesize * 2; offsx < w + tilesize * 2; offsx += tilesize * 2)
		{

			float delta = (max - min);
			delta = (value - min) / delta;
			//System.err.println("Delta = " + delta);

			imgOffs = (int) ((delta * w) / 4.0);

			int posx = offsx + (imgOffs % tilesize * 2);

			g.setColor(new Color(238, 238, 238));
			g.fillRect(posx, 0, tilesize, h / 4 * 3 + 1);
			g.setColor(new Color(188, 188, 188));
			g.fillRect(posx + tilesize, 0, tilesize, h / 4 * 3 + 1);
		}

		AffineTransform back = g.getTransform();
		AffineTransform good = g.getTransform();

		if (im != null)
		{
			int imw = im.getWidth(this);
			int imh = im.getHeight(this);
			double dw = (double) imw / (double) w;
			double dh = imh / (((h + 0.1) / 4) * 3);
			good.scale(1 / dw, 1 / dh);
			g.setTransform(good);
			g.drawImage(im, 0, 0, this);
		}

		g.setTransform(back);

		// Dessine la bar de status du fond
		g.setColor(Color.gray);
		g.fillRect(0, h - h / 4 - 2, w, h / 4 + 2);
		g.setColor(Color.WHITE);
		g.fillRect(0, h - h / 4 - 2, ww, h / 4 + 2);

		fontMetrics = g.getFontMetrics();

		g.setColor(Color.BLACK);
		if ((Label != null) && (Label.length() != 0))
		{
			Rectangle2D bounds = fontMetrics.getStringBounds(Label, g);
			g.drawString(Label, (int) (w - w / 2 - bounds.getCenterX()), (h / 2));
		}

		String STRING = "" + getValue();
		font = new Font(family, Font.PLAIN, (int) ((float) h / (float) 4));
		g.setFont(font);
		Rectangle2D bounds = fontMetrics.getStringBounds(STRING, g);
		int V = (int) ((float) 37.5 * h / 32);

		//float H4=(float)bounds.getHeight()/1.25f;

		// Desactive l'antialising cette connerie...
		//    	g.setXORMode(getBackground());

		if (EnableAntialiasing == true)
		{
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} else
		{
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

		g.drawString(STRING, (int) (w / 2 - bounds.getWidth() / 2), (int) (V - bounds.getHeight() / 2));

	}

	@Override
	public Point getToolTipLocation(MouseEvent event)
	{
		return new Point(getWidth() / 2, getHeight() / 2);
	}

	////// COMPONENT LISTENER MANAGEMENT ///////////////////////////////////////////////

	public void componentHidden(ComponentEvent arg0)
	{
		repaint();
	}

	public void componentMoved(ComponentEvent arg0)
	{
		repaint();
	}

	public void componentResized(ComponentEvent arg0)
	{
		//	this.init_buffer_images();
		repaint();
	}

	public void componentShown(ComponentEvent arg0)
	{
		repaint();
	}

	private void formMouseDragged(MouseEvent evt)
	{

		//	float RangeVariable = Math.abs(max-min);
		//System.err.println("RangeVariable == "+RangeVariable);

		int dx = -(mouseClick.x - evt.getX());

		int old_value1 = old_value;
		this.setValue(value + dx);
		if (value != old_value1)
			imgOffs += dx;

		//repaint();
		//System.err.println("imgOffs="+imgOffs);
		mouseClick.x = evt.getX();
		mouseClick.y = evt.getY();
	}

	private void formMouseMoved(MouseEvent evt)
	{
		// TODO Auto-generated method stub
		mouseClick.x = evt.getX();
		mouseClick.y = evt.getY();
		//	System.err.println("Mouvement "+mouseClick);
	}

	////// LISTENER MANAGEMENT ///////////////////////////////////////////////

	/**
	 * DOCUMENT ME!
	 *
	 * @param listener
	 *            DOCUMENT ME!
	 */
	public void addChangeListener(ChangeListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void removeAllChangeListener()
	{
		listeners.removeAll(listeners); // TODO : pas sure
	}

	/*
	 * DOCUMENT ME!
	 * @param event DOCUMENT ME!
	 */
	public void fireChangeEvent(ChangeEvent event)
	{
		int total = listeners.size();

		for (int i = 0; i < total; i++)
		{
			ChangeListener l = listeners.elementAt(i);

			if (l instanceof ChangeListener)
			{
				(l).stateChanged(event);
			}
		}
	}

	/**
	 * * DOCUMENT ME!
	 *
	 * @param listener
	 *            DOCUMENT ME!
	 */
	public void addActionListener(ActionListener listener)
	{
		listenersA.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void removeAllActionListener()
	{
		listenersA.removeAll(listenersA); // TODO : pas sure
	}

	/*
	 * DOCUMENT ME!
	 * @param event DOCUMENT ME!
	 */
	public void fireActionEvent(ActionEvent event)
	{
		int total = listenersA.size();
		for (int i = 0; i < total; i++)
		{
			ActionListener l = listenersA.elementAt(i);

			l.actionPerformed(event);

		}
	}

	////// GET & SET ///////////////////////////////////////////////

	public float getValue()
	{
		return value / getDivider();
	}

	public float getDivider()
	{
		return Divider;
	}

	public void setDivider(float divider)
	{
		Divider = divider;
	}

	public void setValue(int val)
	{
		setValue(val, true);
	}

	public void setValue(int val, boolean sendEvent)
	{
		if (val > max)
			val = max;
		if (val < min)
			val = min;
		//if ((val >= min) && (val <= max))
		{
			this.value = val;
			this.old_value = val;

			if (sendEvent == true)
			{
				//	angle = (int)ConvertValueToAngle(val, min, max, MaximumAngle);
				ChangeEvent ch = new ChangeEvent(this);
				fireChangeEvent(ch);
				fireActionEvent(new ActionEvent(this, 0, "ValueChanged"));
			}
		}
		//	else System.err.println("Overflowing min or max "+value);
		repaint();
	}

	public void setMaximum(int max)
	{
		if (max > this.min)
		{
			this.max = max;
			this.setValue(old_value);
			//	init_buffer_fond(true);
		} else
			System.err.println("Max cannot be lower than Min");
	}

	public void setMinimum(int min)
	{
		if (min < this.max)
		{
			this.min = min;
			this.setValue(old_value);
			//	init_buffer_fond(true);
		} else
			System.err.println("Min cannot be higher than Max");
	}

	public int getMaximum()
	{
		return max;
	}

	public int getMinimum()
	{
		return min;
	}

	public synchronized boolean isEnableAntialiasing()
	{
		return EnableAntialiasing;
	}

	public synchronized void setEnableAntialiasing(boolean enableAntialiasing)
	{
		EnableAntialiasing = enableAntialiasing;
	}

	public void mouseClicked(MouseEvent arg0)
	{
		if (arg0.getClickCount() == 2)
		{
			ShowEditValueBox();
		}

	}

	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Affiche un petit truc pour editer avec un text field la value du slider
	 * precisement.
	 */
	private void ShowEditValueBox()
	{
		String s = (String) JOptionPane.showInputDialog(new JFrame(), "Change value of " + Label, "Change value", JOptionPane.PLAIN_MESSAGE, null, null, "" + getValue());

		// If a string was returned, say so.
		if ((s != null) && (s.length() > 0))
		{

			Double valeur = Double.parseDouble("" + s);
			value = (int) (valeur * Divider);
			setValue(value);
			return;
		}

		// If you're here, the return value was null/empty.
		System.err.println("Valeur non rentrée");

	}

	public static void main(String args[])
	{
		WFlatSlider jw1 = new WFlatSlider();
		jw1.setLabel("Volume");
		WFlatSlider jw2 = new WFlatSlider();
		WFlatSlider jw3 = new WFlatSlider();
		JFrame frame = new JFrame();
		GridLayout gridL = new GridLayout();
		gridL.setColumns(1);
		frame.setLayout(gridL);
		frame.add(jw1);
		frame.add(jw2);
		frame.add(jw3);
		frame.setSize(480, 180);
		frame.setVisible(true);

		WFlatSlider jw1_ = new WFlatSlider();
		jw1_.setMinimum(0);
		jw1_.setMaximum(400);

		jw1_.setLabel("Volume");
		WFlatSlider jw2_ = new WFlatSlider();
		jw2_.setMinimum(0);
		jw2_.setMaximum(100);
		jw2_.setDivider(100);
		WFlatSlider jw3_ = new WFlatSlider();
		JFrame frame_ = new JFrame();
		GridLayout gridL_ = new GridLayout();
		gridL_.setRows(3);
		frame_.setLayout(gridL_);
		frame_.add(jw1_);
		frame_.add(jw2_);
		frame_.add(jw3_);
		frame_.setSize(480, 180);
		frame_.setVisible(true);

	}
}
