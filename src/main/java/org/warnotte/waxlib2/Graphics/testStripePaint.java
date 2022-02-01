/**
 * 
 */
package org.warnotte.waxlib2.Graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Warnotte Renaud
 *
 */
public class testStripePaint extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121853748463179624L;
	private final JPanel	contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					testStripePaint frame = new testStripePaint();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testStripePaint()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2319613125185839448L;

			@Override
			public void paint(Graphics g)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.BLUE);
				g2d.fillRect(10, 10, 50, 20);
				
				//int w = ChartStyle.h;
				
				//StripePaint2 sp1 = new StripePaint2(0, 3, 1f);
				
				
				Paint sp1 = StripePaintWax.create45LinePaint(Color.YELLOW, Color.BLUE, 8);
				Paint sp2 = StripePaintWax.create45LinePaintInvert(Color.YELLOW, Color.BLUE, 8);
				Paint sp3 = StripePaintWax.create45LinePaintInvert(Color.YELLOW, sp1, 8);
				
				g2d.setPaint(sp1);
				g2d.fillRect(10, 30, 50, 20);
				
				g2d.setPaint(sp2);
				g2d.fillRect(10, 60, 50, 20);
				
				g2d.setPaint(sp3);
				g2d.fillRect(10, 90, 50, 20);
				
				Paint sp4 = StripePaintWax.createLinePaint(Color.YELLOW, Color.BLUE, 4, false);
				Paint sp5 = StripePaintWax.createLinePaint(Color.YELLOW, Color.BLUE, 4, true);
				Paint sp6 = StripePaintWax.createLinePaint(Color.YELLOW, sp4, 4, true);
				
				g2d.setPaint(sp4);
				g2d.fillRect(70, 30, 50, 20);
				
				g2d.setPaint(sp5);
				g2d.fillRect(70, 60, 50, 20);
				
				g2d.setPaint(sp6);
				g2d.fillRect(70, 90, 50, 20);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
