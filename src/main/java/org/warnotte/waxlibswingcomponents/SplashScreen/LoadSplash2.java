package org.warnotte.waxlibswingcomponents.SplashScreen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;



public class LoadSplash2 extends JWindow implements MouseListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6835071641790818321L;

	Thread fadedein;
	//BufferedImage	image;
	ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();  //  @jve:decl-index=0:
	
	BufferedImage	shadow;
	private JPanel	jPanel					= null;								//  @jve:decl-index=0:visual-constraint="342,168"

	Font			font_normal				= new Font("arial", Font.PLAIN, 12);	//  @jve:decl-index=0:
	Font			font_gros				= new Font("impact", Font.PLAIN, 16);
	Color			ProgressBarColor		= new Color(255, 0, 0, 64);
	Color			BackProgressBarColor	= new Color(255, 255, 255, 127);  //  @jve:decl-index=0:

	String			VersionNumber			= "0.4.2";								//  @jve:decl-index=0:
	String			CopyrightNotes			= "0.5.3";								//  @jve:decl-index=0:

	int				load_value				= 0;

	float			multiplier				= 2;
	int				extra					= (int) (14 * multiplier);

	
	public LoadSplash2(URL filename, String version, String copyright)
	{
		this(new URL[]{filename}, version, copyright);
	}
	public LoadSplash2(URL[] filenames, String version, String copyright)
	{
		
		this.VersionNumber=version;
		this.CopyrightNotes=copyright;
		//if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT)) {
		//	AWTUtilities.setWindowOpaque(this, false);
		//}
		this.setAlwaysOnTop(true);
		this.add(getJPanel());
		try
		{
			for (int i = 0; i < filenames.length; i++)
			{
				//File f = new File(filenames[i]);
				//File f = filenames[i].
				//if (f.exists()==false)
				//	System.err.println("LoadSplash2::Cannot load image : "+f);
				//else
				images.add(ImageIO.read(filenames[i]));
			}
			
			// Cree shadow;
			int width = images.get(0).getWidth();
			int height = images.get(0).getHeight();
			shadow = new BufferedImage(width + extra, height + extra, BufferedImage.TYPE_INT_ARGB);
			Graphics g4 = shadow.getGraphics();
			g4.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
			g4.fillRoundRect((int) (6 * multiplier), (int) (6 * multiplier), width, height, (int) (12 * multiplier), (int) (12 * multiplier));

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setSize(images.get(0).getWidth() + extra, images.get(0).getHeight() + extra);
		//this.setLocation(0,0);
		setLocationRelativeTo(null);
		this.addMouseListener(this);
		
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel()
			{
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 4662025606704210648L;

				@Override
				public void paintComponent(Graphics g2)
				{
					Graphics2D g = (Graphics2D) g2;
					g.drawImage(shadow, getBlurOp((int) (7 * multiplier)), 0, 0);
					
					int index_image = (int) (load_value*images.size()/100.0f);
				//	System.err.println("Index image "+index_image);
					g.drawImage(images.get(index_image), 0, 0, this);

					g.setFont(font_normal);
					FontMetrics metrique = g.getFontMetrics();
					int minY = (this.getHeight() - 55);
					//if (Create_Shadow==false) minY += 30;
					int maxX = (this.getWidth() - 50);
					int minX = 10;
					//if (Create_Shadow==false) minX += 10;
					int maxY = 15;
					g.setColor(Color.black);
					g.drawRect(minX, minY, (int) (100 * (float) maxX / 100), maxY);
					g.setColor(BackProgressBarColor);
					g.fillRect(minX + 1, minY + 1, (int) (100 * (float) (maxX - 1) / 100), maxY - 1);
					g.setColor(ProgressBarColor);
					g.fillRect(minX + 1, minY + 1, (int) (load_value * (float) (maxX - 1) / 100), maxY - 1);

					int LabelCenteredX = ((minX + 1) + (int) (100 * (float) (maxX - 1) / 100)) / 2;
					//g.setColor(Color.black);

					g.setColor(Color.BLACK);
					drawTextHighlight(g, metrique, "" + load_value + " %", 12, 1, LabelCenteredX, minY + maxY / 2, true);

					if (VersionNumber != null)
					{
						//	Rectangle2D r2d = g.getFontMetrics().getStringBounds(VersionNumber, g);
						g.setFont(font_gros);
						metrique = g.getFontMetrics();
						g.setColor(Color.WHITE);
						drawTextHighlight(g, metrique, "Ver. " + VersionNumber, 12, 1, minX, minY - 20, false);
						g.setColor(Color.BLACK);
						g.drawString("Ver. " + VersionNumber, minX + 12, minY - 20 + 12);
					}

					if ((CopyrightNotes != null) && (CopyrightNotes.length() != 0))
					{
						//	Rectangle2D r2d = g.getFontMetrics().getStringBounds(VersionNumber, g);
						g.setFont(font_gros);
						metrique = g.getFontMetrics();
						g.setColor(Color.WHITE);
						drawTextHighlight(g, metrique, "" + CopyrightNotes, 12, 1, minX + 80, minY - 20, false);
						g.setColor(Color.BLACK);
						g.drawString("" + CopyrightNotes, minX + 80 + 12, minY - 20 + 12);
					}

				}
			};
		//	jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}

	private void drawTextHighlight(Graphics2D g2, FontMetrics metrique, String title, int size, float opacity, int offsx, int offsy, boolean centered)
	{

		Composite old = g2.getComposite();

		int MX = 0;
		int MY = 0;
		if (centered)
		{

			Rectangle2D r2d = metrique.getStringBounds(title, g2);
			MX = (int) (-r2d.getWidth() / 2);
			MY = (int) (-r2d.getHeight() / 2);
		}

		for (int i = -size; i <= size; i++)
		{
			for (int j = -size; j <= size; j++)
			{
				double distance = i * i + j * j;
				float alpha = opacity;
				if (distance > 0.0d)
				{
					alpha = (float) (1.0f / ((distance * size) * opacity));
				}
				// Ceci pas compatible avec le MAC ???!
				Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g2.setComposite(c);

				g2.drawString(title, i + size + offsx + MX, j + size + offsy + MY);
			}
		}
		g2.setComposite(old);

	}

	private ConvolveOp getBlurOp(int size)
	{
		float[] data = new float[size * size];
		float value = 1 / (float) (size * size);
		for (int i = 0; i < data.length; i++)
		{
			data[i] = value;
		}
		return new ConvolveOp(new Kernel(size, size, data));
	}

	public void set_ProgressBarValue(int i)
	{
		load_value=i;
		repaint();
	}
	
	public static void main(String args[]) throws MalformedURLException
	{
		LoadSplash2 spl=null;
		boolean animated=false;
		if (animated == false)
		{
			spl = new LoadSplash2(LoadSplash2.class.getResource("splash.jpg"), "0.4.2", "Wax78");
		} else
		{
		/*	String[] filenames = new String[25];
			for (int i = 0; i < filenames.length; i++)
				filenames[i] = String.format("\\\\139.165.123.112\\h$\\Upload\\wax\\rendus\\%04d.bmp", i + 60);

			spl = new LoadSplash2(filenames, "0.4.2", "Wax78");*/
		}
		spl.fadeInWindows();
		for (int i = 0; i < 100; i++)
		{
			spl.set_ProgressBarValue(i);
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	//	spl.fadeOutwindows();
		spl.setVisible(false);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount()==2)
		{
			fadeOutwindows();
			setVisible(false);
		}
	}
	public void mouseEntered(MouseEvent e)
	{
	//	System.err.println("Entered");
		
	}
	public void mouseExited(MouseEvent e)
	{
	//	System.err.println("Exited");
		
	}
	public void mousePressed(MouseEvent e)
	{
		
		
	}

	public void mouseReleased(MouseEvent e)
	{
	//	System.err.println("Released");
		
	}

	public void fadeInWindows()
	{
		/*
		boolean isShapingSupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSPARENT);
		@SuppressWarnings("unused")
		boolean isOpacityControlSupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT);
		@SuppressWarnings("unused")
		boolean isTranslucencySupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSLUCENT);
*/
		
		//GraphicsConfiguration translucencyCapableGC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		/*
		if (!translucencyCapableGC.isTranslucencyCapable())
		{
			translucencyCapableGC = null;

			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();

			for (int i = 0; i < devices.length && translucencyCapableGC == null; i++)
			{
				GraphicsConfiguration[] configs = devices[i].getConfigurations();
				for (int j = 0; j < configs.length && translucencyCapableGC == null; j++)
				{
					if (AWTUtilities.isTranslucencyCapable(configs[j]))
					{
						translucencyCapableGC = configs[j];
					}
				}
			}
			if (translucencyCapableGC == null)
			{
				isTranslucencySupported = false;
			}
		}
		
		*/
		//if (isShapingSupported==true)
		{
			fadedein = new Thread()
			{
				@Override
				public void run()
				{
					//AWTUtilities.setWindowOpacity(LoadSplash2.this, 0);
					LoadSplash2.this.setOpacity(0);
					LoadSplash2.this.setVisible(true);
					LoadSplash2.this.setAlwaysOnTop(true);
					//AWTUtilities.setWindowOpaque(MainFrame.this, false);
					for (float i = 0; i <= 1.0f; i+=0.025f)
					{
						LoadSplash2.this.setOpacity(i);
						
						try
						{
							Thread.sleep(1);
						} catch (InterruptedException e)
						{
							LoadSplash2.this.setOpacity( 1);
						//	e.printStackTrace();
							break;
						}
						
					}
					LoadSplash2.this.setAlwaysOnTop(false);		
				}
			};
			fadedein.start();
			
		}

	}
	
	public void fadeOutwindows()
	{
		/*
		boolean isShapingSupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSPARENT);
		@SuppressWarnings("unused")
		boolean isOpacityControlSupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT);
		@SuppressWarnings("unused")
		boolean isTranslucencySupported = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSLUCENT);
*/
		
		
		//GraphicsConfiguration translucencyCapableGC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		/*
		
		if (!translucencyCapableGC.isTranslucencyCapable())
		{
			translucencyCapableGC = null;
			
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();

			for (int i = 0; i < devices.length && translucencyCapableGC == null; i++)
			{
				GraphicsConfiguration[] configs = devices[i].getConfigurations();
				for (int j = 0; j < configs.length && translucencyCapableGC == null; j++)
				{
					if (configs[j].isTranslucencyCapable())
					{
						translucencyCapableGC = configs[j];
					}
				}
			}
			if (translucencyCapableGC == null)
			{
				isTranslucencySupported = false;
			}
		}
		*/
	
		//if (isShapingSupported==true)
		{
		
			if (fadedein!=null)
				fadedein.interrupt();
			
			Thread t = new Thread()
			{
				@Override
				public void run()
				{
					
					
				//	AWTUtilities.setWindowOpaque(MainFrame.this, false);
					for (float i = 0; i < 0.9f; i+=0.05f)
					{
						final float j = i;
						
						
							
						LoadSplash2.this.repaint();
						LoadSplash2.this.setOpacity(0.9f-j);
		//					AWTUtilities.setWindowOpacity(LoadSplash2.this, 0.9f-j);
//	
							
							try
							{
								Thread.sleep(5);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
								

							
						
						//System.err.println("++ "+i);
					}
					LoadSplash2.this.setOpacity(0f);
//					AWTUtilities.setWindowOpacity(LoadSplash2.this, 0f);
					//setVisible(false);
				}
			};
			t.start();
			try
			{
				t.join();
				dispose();
				//System.exit(-1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO : Mais Wax! imbécile ;)
		}
	}
	
}


