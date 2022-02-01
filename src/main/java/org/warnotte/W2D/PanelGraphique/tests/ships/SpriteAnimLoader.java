/**
 * 
 */
package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * @author Warnotte Renaud
 *
 */
public class SpriteAnimLoader
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}
	
	/**
	 * 
	 * @param fileName with sprite sheet in one row.
	 * @param w width of one tile
	 * @param h width of one tile
	 * @return
	 * @throws IOException
	 */
	public static List<BufferedImage> readFile(URL fileName, int w, int h) throws IOException
	{
		BufferedImage orig = ImageIO.read(fileName);
		int Width = orig.getWidth();
		int Nbr = Width / w;
		System.err.println(fileName.getFile()+ " - nbr frame = "+Nbr);
		List<BufferedImage> list = new ArrayList<>();
		int x = 0;
		for (int i = 0; i < Nbr; i++)
		{
			list.add(orig.getSubimage(x, 0, w, h));
			x+=w;
		}
		return list;
	}

}
