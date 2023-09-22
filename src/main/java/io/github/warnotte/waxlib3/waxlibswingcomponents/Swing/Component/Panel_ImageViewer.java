package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Panel can show a File or BufferedImage that scale automatically, with zoom and scroll posibilities.
 * @author Warnotte Renaud 2017-2023
 *
 */
public class Panel_ImageViewer extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{

	protected static final Logger Logger = LogManager.getLogger("GUI_FowtSimulation");
	
	public static void main(String args[]) throws IOException {
		
		Panel_ImageViewer panel = new Panel_ImageViewer();
		
		panel.setImage(new File("F:\\git\\COLLFOWT_FOWT\\CollFowt_FOWT\\results\\run_coll_2023-09-22_11-52-31\\run_coll_2023-09-22_11-52-31_Coll_scenario.png"));
		
		JFrame frame = new JFrame();
		frame.setSize(800,600);
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setVisible(true);
		
	}
	
	

	//Manager						manager				= null;
	//GUI_ViewerMPX				parent;

	BufferedImage				image				= null;
	static BufferedImage		nosignal			= null;

	// Preparation pour un Abbas(erie)
	float						userScale			= 1.0f;
	float						userXoffset			= 0.0f;
	float						userYoffset			= 0.0f;
	private int					oldmouseY;
	private int					oldmouseX;

	int boutton_mask = InputEvent.BUTTON2_MASK;
	
	
	/**
	 * 
	 * 
	 */
	private static final long	serialVersionUID	= -7189539482050038170L;

	public Panel_ImageViewer(/*GUI_ViewerMPX parent*/) 
	{
		setBackground(Color.WHITE);

		//this.parent = parent;
		//this.manager = parent.manager;
		
		if (nosignal == null)
			try
			{
				nosignal = ImageIO.read(getClass().getResource("/NoSignal.jpg"));
			} catch (IOException e)
			{
				Logger.fatal(e, e);
				e.printStackTrace();
			}

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

	}
	
	/**
	 * @param file
	 * @throws IOException 
	 */
	public void setImage(File file) 
	{
		try
		{
			image = ImageIO.read(file);
			setImage(image);
		} catch (IOException e)
		{
			Logger.fatal(e, e);
			e.printStackTrace();
		}
		
	}

	public void resetViewPort()
	{
		userScale = 1.0f;
		userXoffset = 0.0f;
		userYoffset = 0.0f;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.BLACK);

		if (image != null)
		{
			drawPictureCentredAndScaled(g2d, image);
		} else
		{
			drawPictureCentredAndScaled(g2d, nosignal);
		}

	}

	/**
	 * @param g2d
	 */
	public void drawPictureCentredAndScaled(Graphics2D g2d, BufferedImage image)
	{
		// Get the required sizes for display and calculations
		int panelWidth = this.getWidth();
		int panelHeight = this.getHeight();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// Get the scale that the image should be resized with
		double scale = userScale * GetScale(panelWidth, panelHeight, imageWidth, imageHeight);

		// Calculate the center position of the panel -- with scale
		double xPos = (panelWidth - (scale * imageWidth)) / 2;
		double yPos = (panelHeight - (scale * imageHeight)) / 2;

		// Locate, scale and draw image
		AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
		at.scale(scale, scale);
		at.translate(userXoffset, userYoffset);
		g2d.drawRenderedImage(image, at);
	}



	/** Calculate the scale required to correctly fit the image into panel */
	private double GetScale(int panelWidth, int panelHeight, int imageWidth, int imageHeight)
	{
		double scale = 1;
		double xScale;
		double yScale;

		// should check that denom != 0 first.
		xScale = (double) panelWidth / imageWidth;
		yScale = (double) panelHeight / imageHeight;
		scale = Math.min(xScale, yScale);
		return scale;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.
	 * MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		
		int modif = e.getModifiers();
		
		
		if ((modif & boutton_mask) == boutton_mask)
		{
			int dx = mouseX - oldmouseX;
			int dy = mouseY - oldmouseY;
	
			oldmouseX = mouseX;
			oldmouseY = mouseY;
	
			if (image != null)
			{
				int panelWidth = this.getWidth();
				int panelHeight = this.getHeight();
				int imageWidth = image.getWidth();
				int imageHeight = image.getHeight();
	
				double scale = userScale * GetScale(panelWidth, panelHeight, imageWidth, imageHeight);
	
				userXoffset += dx / scale;
				userYoffset += dy / scale;
			}
			repaint();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		oldmouseX = mouseX;
		oldmouseY = mouseY;

		repaint();

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		requestFocus();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			System.err.println("Popup");
			JPopupMenu menu = new JPopupMenu("Menu");
			JMenuItem item = new JMenuItem("Reset ViewPort");
			item.addActionListener(em -> {
				resetViewPort();
			});
			menu.add(item);
			menu.show(this, e.getX(), e.getY());
			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_R)
		{
			resetViewPort();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.
	 * MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{

		if (e.getWheelRotation() > 0)
			userScale -= 0.1;
		else
			userScale += 0.1;

		repaint();
	}

}