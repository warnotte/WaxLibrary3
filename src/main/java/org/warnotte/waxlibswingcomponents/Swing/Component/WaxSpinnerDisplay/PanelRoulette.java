package org.warnotte.waxlibswingcomponents.Swing.Component.WaxSpinnerDisplay;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Warnotte Renaud 2009
 *
 */
public class PanelRoulette extends JPanel
{

	private static final long	serialVersionUID	= 1L;
	static BufferedImage imagechiffres = null;
	int Size = 160;
	
	Roulette roulette = new Roulette();  //  @jve:decl-index=0:
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		
		JFrame frame = new JFrame();
		frame.setSize(320,200);
		PanelRoulette pr = new PanelRoulette(160);
		frame.add(pr);
		frame.setVisible(true);
	}

	/**
	 * This is the default constructor
	 * @throws IOException 
	 */
	public PanelRoulette(int dimension) throws IOException
	{
		super();
		this.Size=dimension;
		if (imagechiffres==null)
		{
			imagechiffres = ImageIO.read(getClass().getResource("/images/chiffres.jpg"));
			Image k=imagechiffres.getScaledInstance(Size, Size*11, Image.SCALE_AREA_AVERAGING);
			imagechiffres=toBufferedImage(k);
		}
		
		initialize();
		
	}
	
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels; for this method's
	    // implementation, see Determining If an Image Has Transparent Pixels
	    boolean hasAlpha = false;

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		Dimension dim = new Dimension(Size,Size);
		this.setSize(Size, Size);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
	//	int V0 = (int)roulette.position-1;
		float V1 = roulette.position/1.018f; // Petite correction afin de corriger un décalage.
	//	float V1 = roulette.position/1f; // Petite correction afin de corriger un décalage.
	//	float remains = roulette.position - V1;
		g.drawImage(imagechiffres, 0, 0, Size, Size, 0, (int)(0+V1*Size)+Size/8, Size,Size+(int)(0+V1*Size)+Size/8, this);
	}

}
