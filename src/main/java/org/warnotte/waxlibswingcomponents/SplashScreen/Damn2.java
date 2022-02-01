package org.warnotte.waxlibswingcomponents.SplashScreen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;


public class Damn2 extends JWindow
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6835071641790818321L;
	BufferedImage	shadow;
	private JPanel	jPanel					= null;								//  @jve:decl-index=0:visual-constraint="342,168"
	private float	timer;
	private final float	divx;
	private final float	divy;

	
	public Damn2(final int timer1) throws IOException
	{

		this.setBackground(new Color(0,0,0,0));
		this.setOpacity(0.75f);
		//AWTUtilities.setWindowOpaque(this, false);
		//AWTUtilities.setWindowOpacity(this, 0.75f);
		this.setAlwaysOnTop(true);
		this.add(getJPanel());
		
		int c = (int)(Math.random()*3);
		
		if (c==0)shadow = ImageIO.read(new File("F:\\Projets\\WaxLib_Swing_Components\\bin\\WaxLibrary\\SplashScreen\\sk_1.png"));
		if (c==1)shadow = ImageIO.read(new File("F:\\Projets\\WaxLib_Swing_Components\\bin\\WaxLibrary\\SplashScreen\\sk_2.png"));
		if (c==2)shadow = ImageIO.read(new File("F:\\Projets\\WaxLib_Swing_Components\\bin\\WaxLibrary\\SplashScreen\\sk_3.png"));
		
		this.setSize(shadow.getWidth(), shadow.getHeight());
		//this.setLocation(0,0);
		setLocationRelativeTo(null); 
		
		divx = (int) (Math.random()*50);
		divy = (int) (Math.random()*50);
		
					
		
	}

	
	public void evolue()
	{
		timer+=0.1f;
		int x = (int) (160*Math.cos(timer/divx))+320;
		int y = (int) (120*Math.sin(timer/divy))+240;
		setLocation(x,y);
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
				private static final long	serialVersionUID	= -483017911433117556L;

				@Override
				public void paintComponent(Graphics g2)
				{
					Graphics2D g = (Graphics2D) g2;
					g.drawImage(shadow, 0,0, this);
				}
			};
		//	jPanel.setLayout(new GridBagLayout());
		}
		
		return jPanel;
	}

	
	
	public static void main(String args[]) throws IOException
	{
		int nbr = 35;
		Damn2 grille [] = new Damn2[nbr];
		for (int i = 0; i < nbr; i++)
		{
			Damn2 spl = new Damn2(15+i*500);
			grille[i]=spl;
			spl.setVisible(true);
		}
		
		while(true)
		{
			for (int i = 0; i < nbr; i++)
			{
				Damn2 spl = grille[i];
				spl.evolue();
			}
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		

	}


}
