/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.warnotte.waxlib3.waxlib2.Graphics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * A class that simplifies a few common image operations, in particular creating
 * a BufferedImage from an image file, and using MediaTracker to wait until an
 * image or several images are done loading. From tutorial on learning Java2D at
 * http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html 1998 Marty Hall,
 * http://www.apl.jhu.edu/~hall/java/ Update : Warnotte Renaud 2006
 */

public class ImageUtilities
{

	/**
	 * Permet de r�cup�rer une ImageIcon d'une taille bien pr�cise (avec un
	 * SCALE_SMOOTH)
	 * 
	 * @param Le
	 *            nom du fichier image
	 * @param Taille
	 *            W de l'image
	 * @param Taille
	 *            H de l'image
	 * @return
	 */
	public static ImageIcon getImageIconResized(String imageIcon, int sx, int sy)
	{
		ImageIcon img = null;
		img = new ImageIcon(imageIcon);
		if (img.getIconHeight() == -1)
			img = new ImageIcon("Images/Icons/DefaultIcon.gif");
		img = new ImageIcon(img.getImage().getScaledInstance(sx, sy, Image.SCALE_SMOOTH));
		return img;
	}

	/**
	 * Create Image from a file, then turn that into a BufferedImage.
	 */

	public static BufferedImage getBufferedImage(String imageFile, Component c)
	{
		Image image = c.getToolkit().getImage(imageFile);
		waitForImage(image, c);
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(c), image.getHeight(c), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return (bufferedImage);
	}

	public static BufferedImage ImagetoBufferedImage(Image image)
	{
		if (image instanceof BufferedImage)
		{
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent Pixels
		boolean hasAlpha = true;//hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		//   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try
		{
			// Determine the type of transparency of the new buffered image
			/*
			 * int transparency = Transparency.OPAQUE; if (hasAlpha) {
			 * transparency = Transparency.BITMASK; }
			 */

			// Create the buffered image
			//   GraphicsDevice gs = ge.getDefaultScreenDevice();
			//      GraphicsConfiguration gc = gs.getDefaultConfiguration();
			//   bimage = gc.createCompatibleImage(
			//         image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e)
		{
			// The system does not have a screen
		}

		if (bimage == null)
		{
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha)
			{
				type = BufferedImage.TYPE_INT_ARGB;
			}
			//    ImageIcon ii = new ImageIcon(image);
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
	 * Take an Image associated with a file, and wait until it is done loading.
	 * Just a simple application of MediaTracker. If you are loading multiple
	 * images, don't use this consecutive times; instead use the version that
	 * takes an array of images.
	 */

	public static boolean waitForImage(Image image, Component c)
	{
		MediaTracker tracker = new MediaTracker(c);
		tracker.addImage(image, 0);
		try
		{
			tracker.waitForAll();
		} catch (InterruptedException ie)
		{
		}
		return (!tracker.isErrorAny());
	}

	/**
	 * Take some Images associated with files, and wait until they are done
	 * loading. Just a simple application of MediaTracker.
	 */

	public static boolean waitForImages(Image[] images, Component c)
	{
		MediaTracker tracker = new MediaTracker(c);
		for (int i = 0; i < images.length; i++)
			tracker.addImage(images[i], 0);
		try
		{
			tracker.waitForAll();
		} catch (InterruptedException ie)
		{
		}
		return (!tracker.isErrorAny());
	}

	public static BufferedImage getBufferedImage(URL url, Component c)
	{
		Image image = c.getToolkit().getImage(url);
		waitForImage(image, c);
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(c), image.getHeight(c), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return (bufferedImage);
	}

}