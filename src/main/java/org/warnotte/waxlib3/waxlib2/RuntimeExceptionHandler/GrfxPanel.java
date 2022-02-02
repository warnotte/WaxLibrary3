
package org.warnotte.waxlib3.waxlib2.RuntimeExceptionHandler;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GrfxPanel extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	private JLabel jLabel = null;
	ImageIcon ic=null;
	ImageIcon ic2=null;
	ImageIcon curr=null;
//	private int	offset;
	Font font=null;  //  @jve:decl-index=0:
	private double	angle;
	
	private float value;

	/**
	 * This is the default constructor
	 */
	public GrfxPanel()
	{
		super();
		initialize();
	}

	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		font = new Font("Impact", 0, 32);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("");
		//jLabel_Image.setText("");
		ic = new ImageIcon(getClass().getResource("/org/warnotte/waxlib2/RuntimeExceptionHandler/RunTimeExceptionDialogImage.jpg"));
		ic2 = new ImageIcon(getClass().getResource("/org/warnotte/waxlib2/RuntimeExceptionHandler/RunTimeExceptionDialogImage2.jpg"));
		jLabel.setIcon(ic);
		curr=ic;
	//	this.setSize(200, 300);
		this.setLayout(new GridBagLayout());
	//	this.setPreferredSize(new Dimension(120, 200));
		this.add(jLabel, gridBagConstraints);
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			
			private MouseEvent	OldMouseEvent;
			
			@Override
			public void mouseDragged(java.awt.event.MouseEvent e)
			{
				
				float dy = (OldMouseEvent.getY()-e.getY())/500f;
				
				value += dy;//(float)e.getY()/(float)getHeight();

				OldMouseEvent = e;
				
			}
			@Override
			public void mouseMoved(java.awt.event.MouseEvent e)
			{
				
			//	value = (float)e.getY()/(float)getHeight();
				
				OldMouseEvent = e;
			}
		});
		this.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mousePressed(java.awt.event.MouseEvent e)
			{
				curr=ic2;
			}
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e)
			{
				curr=ic;
			}
		});
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		value+=0.005f;
		//offset++;
		angle = value / 10;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		

	//	int roffsX = offset % curr.getIconWidth();
	//	int roffsY = offset % curr.getIconHeight();
		// System.err.println("Offset="+offset);
		// System.err.println("ROffset="+roffsX);
	//	float nbrX = this.getWidth() / curr.getIconWidth() + 2;
	//	float nbrY = this.getHeight() / curr.getIconHeight() + 2;
		g2.translate(this.getWidth() / 2, this.getHeight() / 2);
		g2.rotate(Math.toDegrees(angle));

	/*	for (int j = -1; j < 1; j++)
			for (int i = -1; i < 1; i++)
			{

				g2.drawImage(curr.getImage(), roffsX + i * curr.getIconWidth(), roffsY + j * curr.getIconHeight(), this);
			}
		g2.rotate(-Math.toDegrees(angle));
	//	System.err.println(""+(offset%500));
		g2.translate(-this.getWidth() / 2, -this.getHeight() / 2);*/
	/*	if (((offset%500) > 200) && ((offset%500) > 275))
		{
			g2.rotate(Math.toRadians(90));
			g2.setColor(Color.RED);
			g2.setFont(font);
			g2.drawString("Click sur homer pour tourner",5,-5);
			g2.rotate(-Math.toRadians(90));
		}*/
		// System.err.println("Offset="+offset);
	}
}
