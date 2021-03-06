package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsCurve;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPath;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPoint;

public abstract class PanelGraphiqueBase<T> extends JPanel implements ComponentListener, KeyListener, MouseListener, MouseMotionListener
{
	/**
     *
     */
	private static final long						serialVersionUID			= -8780048115222887324L;

	protected CurrentSelectionContext				contxt						= null;
	protected ConfigurationGeneral					config						= new ConfigurationGeneral();
	private ConfigurationColor						colorConfig					= new ConfigurationColor();

	//  BufferedImage grid = null; // @jve:decl-index=0:
	protected Random								rand						= new Random();																				// @jve:decl-index=0:
	protected double								accumx;
	protected double								accumy;

	protected boolean								LockScrollY					= false;
	protected boolean								LockScrollX					= false;

	protected double								MouseY;																													// Actual position of mice (with scroll and zoom)
	protected double								MouseX;
	public double									MouseDX						= 0;																							// Actual Displacement during drag (with scroll and zoom);
	public double									MouseDY						= 0;

	protected  boolean								CTRL;
	protected double								lockX						= -1;
	protected double								lockY						= -1;
	public  boolean									SHIFT;
	protected double								oldmousex;
	protected double								oldmousey;
	protected boolean								MOUSEINSIDE;

	protected Point2D								displacement				= null;																						//Deplacement lors du move.

	protected double								ScrollX						= 0;
	protected double								ScrollY						= 0;
	protected double								Zoom						= 10.0d;
	// TODO : Si on change l'angle, plein de truc bug (le scrolling par exemple), la selection etc ....
	protected double								Angle						= 0;
	protected double								ZoomMin						= 0.3f;
	protected double								ZoomMax						= 200f;
	protected boolean 								invertXAxis 			    = false;
	protected boolean 								invertYAxis 			    = true;

	Stroke											drawingStroke_Boundarybox	= new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50, new float[] { 9 }, 0);	//  @jve:decl-index=0:

	Stroke											drawingStroke_Helplines		= new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50, new float[] { 5 }, 0);
	protected AffineTransform						at							= null;
	protected Rectangle								Vision;

	protected ArrayList<SelectionTuple<Shape, ?>>	selectableObject				= new ArrayList<SelectionTuple<Shape, ?>>();

	protected Map<Shape, Object>					map_selectableShapeObject		= new HashMap<>();
	protected Map<Object, Shape>					map_selectableObjectShape		= new HashMap<>();

	private boolean	DisableSelectable;

	public synchronized boolean isDisableSelectable()
	{
		return DisableSelectable;
	}

	public synchronized void setDisableSelectable(boolean disableSelectable)
	{
		DisableSelectable = disableSelectable;
	}

	protected void addToSelectableObject(SelectionTuple<Shape, Object> selectionTuple)
	{
    	if (DisableSelectable==false)
    	{
    		getSelectableObject().add(selectionTuple);
    		map_selectableShapeObject.put(selectionTuple.getShape(), selectionTuple.getUserObject());
    		map_selectableObjectShape.put(selectionTuple.getUserObject(), selectionTuple.getShape());
    		
    	}
	}
	protected void addToSelectableObject(List<SelectionTuple<Shape, Object>> selection_tuple)
	{
		for (Iterator<SelectionTuple<Shape, Object>> iterator = selection_tuple.iterator(); iterator.hasNext();) {
			SelectionTuple<Shape, Object> o=  iterator.next();
			addToSelectableObject(o);
		}
	}
	
	protected void addToSelectableObject(List<Shape> selection_shapes, Object object)
	{
		for (Iterator<Shape> iterator = selection_shapes.iterator(); iterator.hasNext();) {
			Shape shape = iterator.next();
			SelectionTuple<Shape, Object> o = new SelectionTuple<>(shape, object);
			addToSelectableObject(o);
		}
	}
	protected void addToSelectableObject(Shape selection_shapes, Object object)
	{
		addToSelectableObject(new SelectionTuple<Shape, Object>(selection_shapes, object));
	}
	
	
	
	
	BufferedImage				fBugImage				= null;

	protected static boolean DEBUG_VERBOSE = true;
		
	@SuppressWarnings("unused")
	private PanelGraphiqueBase()
	{

	}

	
	public PanelGraphiqueBase(CurrentSelectionContext contxt)
	{
		this.addComponentListener(this);
		
		// Je me demande pourquoi j'ai foutu ces 3 merdeuse ligne, car apr???s le programme
		// n'accepte plus les tabulations. 
	//	KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	//	kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
	//	kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		this.contxt = contxt;
	}

	public PanelGraphiqueBase(CurrentSelectionContext contxt, ConfigurationGeneral config, ConfigurationColor colorConfig)
	{
		this(contxt);
		if (config != null)
			this.config = config;
		if (colorConfig != null)
			this.setColorConfig(colorConfig);
	}
	
	public void componentResized(ComponentEvent e)
	{
		/*System.err.println("Resizing Panel, remaking a bufferedimage");
		myBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);*/
	}
	
	
	public void componentMoved(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	
	public void componentShown(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}


	
	public void componentHidden(ComponentEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	protected void dessineGrandeCroix(Graphics2D g, double x, double y)
	{
		if (config.isDrawHelpLines() == false)
			return;

		if (MOUSEINSIDE == false)
			return;

		g.setStroke(drawingStroke_Helplines);
		g.setColor(Color.BLUE);
		int w = getWidth();
		int h = getHeight();
		g.drawLine(0, (int) y, w, (int) y);
		g.drawLine((int) x, 0, (int) x, h);
		g.setStroke(new BasicStroke(1.0f));

	}

	protected void dessineGrille(Graphics2D g, boolean DrawCentralAxis)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		int w = getWidth();
		int h = getHeight();

		double space = config.getGRID_SIZE() * Zoom;
		double ox = ((ScrollX * Zoom) % (space));
		double oy = ((ScrollY * Zoom) % (space));

		//	if (LockScrollX==1)
		//		ox=0;
		
		

		g.translate(ox, oy);
		
		g.translate(w / 2, h / 2);
		
		g.setColor(getColorConfig().getCOLOR_GRILLE_1().getColor());
		for (double x = 0; x < w / 2 + space; x += space)
		{
		/*	Line2D.Double line1 = new Line2D.Double(x, -(h / 2.0 + space),  x,  (h / 2.0 + space));
			Line2D.Double line2 = new Line2D.Double(-x,-(h / 2.0 + space), -x,  (h / 2 + space));
		*/	g.drawLine((int) x, (int) -(h / 2 + space), (int) x, (int) (h / 2 + space));
			g.drawLine((int) -x, (int) -(h / 2 + space), (int) -x, (int) (h / 2 + space));
		}
		for (double y = 0; y < h / 2 + space; y += space)
		{
			g.drawLine((int) -(w / 2 + space), (int) y, (int) (w / 2 + space), (int) y);
			g.drawLine((int) -(w / 2 + space), (int) -y, (int) (w / 2 + space), (int) -y);
		}
		
		g.translate(-ox, -oy);
		
		g.translate(-w / 2, -h / 2);

		if (DrawCentralAxis == true)
		{
			g.setColor(Color.BLACK);
			int y = (int)((ScrollY * Zoom) + h / 2.0);
			int x = (int)((ScrollX * Zoom) + w / 2.0);
			g.drawLine(0,  y, w, y);
			g.drawLine(x, 0, x, h);
		}

	}

	public void gridc(int w, int h)
	{
		/*
		 * grid = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		 * Graphics2D g = grid.createGraphics();
		 * g.setColor(contxt.getColorConfig
		 * ().getCOLOR_FOND_GRILLE().getColor()); g.fillRect(0, 0, w, h);
		 * g.setColor(contxt.getColorConfig().getCOLOR_GRILLE_1().getColor());
		 * for (int x = 0; x < w; x += contxt.getConfig().getGRID_SIZE())
		 * g.drawLine(x, 0, x, h); for (int y = 0; y < h; y +=
		 * contxt.getConfig().getGRID_SIZE()) g.drawLine(0, y, w, y);
		 * g.setColor(contxt.getColorConfig().getCOLOR_GRILLE_2().getColor());
		 * for (int x = 0; x < w; x += (contxt.getConfig().getGRID_SIZE() * 2))
		 * g.drawLine(x, 0, x, h); for (int y = 0; y < h; y +=
		 * (contxt.getConfig().getGRID_SIZE() * 2)) g.drawLine(0, y, w, y);
		 */
		repaint();
	}

	public BufferedImage getImage()
	{
		BufferedImage myBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D myGraphics2D = myBufferedImage.createGraphics();
		// myGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		// myGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		this.paint(myGraphics2D);
		return ImagetoBufferedImage(myBufferedImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH));

		// return ImageUtilities.ImagetoBufferedImage(createImage(getWidth(),
		// getHeight()));
	}
	
	public static BufferedImage ImagetoBufferedImage(Image image) {
	      if (image instanceof BufferedImage) {
	          return (BufferedImage)image;
	      }
	  
	      // This code ensures that all the pixels in the image are loaded
	      image = new ImageIcon(image).getImage();
	  
	      // Determine if the image has transparent pixels; for this method's
	      // implementation, see e661 Determining If an Image Has Transparent Pixels
	      boolean hasAlpha = true;//hasAlpha(image);
	  
	      // Create a buffered image with a format that's compatible with the screen
	      BufferedImage bimage = null;
	   //   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	      try {
	          // Determine the type of transparency of the new buffered image
	         /* int transparency = Transparency.OPAQUE;
	          if (hasAlpha) {
	              transparency = Transparency.BITMASK;
	          }*/
	  
	          // Create the buffered image
	       //   GraphicsDevice gs = ge.getDefaultScreenDevice();
	    //      GraphicsConfiguration gc = gs.getDefaultConfiguration();
	       //   bimage = gc.createCompatibleImage(
	     //         image.getWidth(null), image.getHeight(null), transparency);
	      } catch (HeadlessException e) {
	          // The system does not have a screen
	      }
	  
	      if (bimage == null) {
	          // Create a buffered image using the default color model
	          int type = BufferedImage.TYPE_INT_RGB;
	          if (hasAlpha) {
	              type = BufferedImage.TYPE_INT_ARGB;
	          }
	      //    ImageIcon ii = new ImageIcon(image);
	          bimage = new BufferedImage( image.getWidth(null), image.getHeight(null), type);
	      }
	  
	      // Copy image to buffered image
	      Graphics g = bimage.createGraphics();
	  
	      // Paint the image onto the buffered image
	      g.drawImage(image, 0, 0, null);
	      g.dispose();
	  
	      return bimage;
	  }

	public void drawArrow(Graphics2D g2d, float xCenter, float yCenter, float x, float y, float stroke)
	{
		double aDir = Math.atan2(xCenter - x, yCenter - y);
		float i1 = (36 * stroke) + (stroke * 2);
		float i2 = (20 * stroke) + stroke; // make the arrow head the same size regardless of the length length
		Path2D tmpPoly = new Path2D.Double();
		tmpPoly.moveTo(x, y); // arrow tip
		tmpPoly.lineTo(x + xCor(i1, aDir + .5) / 2, y + yCor(i1, aDir + .5) / 2);
		tmpPoly.lineTo(x + xCor(i2, aDir) / 2, y + yCor(i2, aDir) / 2);
		tmpPoly.lineTo(x + xCor(i1, aDir - .5) / 2, y + yCor(i1, aDir - .5) / 2);
		tmpPoly.lineTo(x, y); // arrow tip
		g2d.fill(at.createTransformedShape(tmpPoly));

	}

	protected void drawMoveCursor(Graphics2D g, int x, int y)
	{

		Point2D src = new Point2D.Double(x, y);
		Point2D dst = new Point2D.Double(x, y);
		dst = at.transform(src, dst);
		//int xx = (int)dst.getX();
		//int yy = (int)dst.getY();

		g.setColor(new Color(0, 0, 0, 255));

		//	int v = 10;
		if (lockX == -1)
		{
			// 	drawArrow(g, xx+v,yy,xx+contxt.getConfig().getArrow_longueur(),yy, 1.0f);
			// 	drawArrow(g, xx-v,yy,xx-contxt.getConfig().getArrow_longueur(),yy, 1.0f);
		}
		if (lockY == -1)
		{
			//	drawArrow(g, xx,yy+v,xx,yy+contxt.getConfig().getArrow_longueur(), 1.0f);
			// 	drawArrow(g, xx,yy-v,xx,yy-contxt.getConfig().getArrow_longueur(), 1.0f);
		}
		//drawArrow(g, xx,yy,xx-25,yy, 2.0f);
	}

	private static float yCor(float len, double dir)
	{
		return (float) (len * Math.cos(dir));
	}

	private static float xCor(float len, double dir)
	{
		return (float) (len * Math.sin(dir));
	}

	/*
	 * @Override public void repaint() { //if (manager != null) //
	 * manager.fireStgNeedRefresh(new XXXEvent(Refresh_MSG.REPAINT_VIEWS,this));
	 * }
	 */

	public void repaint(boolean b)
	{
		super.repaint();
	}

	protected void dessineCroix(Graphics2D g, float x, float y, float w, float h)
	{

		g.setStroke(new BasicStroke(2.0f));
		Line2D l = new Line2D.Double(x, y, x + w, y + h);
		Shape l2 = at.createTransformedShape(l);
		g.draw(l2);

		l.setLine(x, y, x - w, y + h);
		l2 = at.createTransformedShape(l);
		g.draw(l2);
		l.setLine(x, y, x + w, y - h);
		l2 = at.createTransformedShape(l);
		g.draw(l2);
		l.setLine(x, y, x - w, y - h);
		l2 = at.createTransformedShape(l);
		g.draw(l2);
		g.setStroke(new BasicStroke(1.0f));

		/*
		 * g.drawLine(x, y, x + w, y + h); g.drawLine(x, y, x - w, y + h);
		 * g.drawLine(x, y, x + w, y - h); g.drawLine(x, y, x - w, y - h);
		 */
	}

	protected Vector<?> shiftVector(Vector<?> v)
	{
		Vector<Object> z = new Vector<Object>();
		for (int i = 0; i < v.size(); i++)
		{
			int offset = (i + 1) % v.size();
			z.add(v.get(offset));
		}
		return z;

	}

	/*
	 * protected Node CalculeDeplacementAvecGrille() { double dx = MouseDX;//=
	 * (oldmousex - MouseX);//
	 * /contxt.getConfig().getGRID_SIZE()*contxt.getConfig().getGRID_SIZE();
	 * double dy = MouseDY;//= (oldmousey - MouseY);//
	 * /contxt.getConfig().getGRID_SIZE()*contxt.getConfig().getGRID_SIZE();
	 * accumx += dx; accumy += dy; if (lockY!=-1) accumy=0; if (lockX!=-1)
	 * accumx=0; if (contxt.getConfig().isEnableGridding()==true) { double
	 * deltax = Utils.Griddify(accumx, contxt.getConfig().getGRID_SIZE(),
	 * false); double deltay = Utils.Griddify(accumy,
	 * contxt.getConfig().getGRID_SIZE(), false); if ((Math.abs(deltax) >=
	 * contxt.getConfig().getGRID_SIZE())) { accumx = accumx - deltax; } if
	 * ((Math.abs(deltay) >= contxt.getConfig().getGRID_SIZE())) { accumy =
	 * accumy - deltay; } //if ((Math.abs(accumx) >=
	 * contxt.getConfig().getGRID_SIZE()) || (Math.abs(accumy) >=
	 * contxt.getConfig().getGRID_SIZE())) { if ((Math.abs(deltax) >=
	 * contxt.getConfig().getGRID_SIZE()) || (Math.abs(deltay) >=
	 * contxt.getConfig().getGRID_SIZE())) { // double deltax =
	 * Mmath.round(accumx / contxt.getConfig().getGRID_SIZE())*
	 * contxt.getConfig().getGRID_SIZE(); // double deltay = Math.round(accumy /
	 * contxt.getConfig().getGRID_SIZE())* contxt.getConfig().getGRID_SIZE();
	 * System.err.println("* "+ deltax +","+deltay); // accumx = accumx -
	 * deltax; // accumy = accumy - deltay; // accumx = 0; // accumy = 0; return
	 * new Node(-deltax, -deltay); } } else {
	 * //manager.displacePoints(contxt.getClickedNode(), -accumx, -accumy); Node
	 * p = new Node(-accumx, -accumy); accumx=0; accumy=0; return p; } return
	 * null; }
	 */

	public synchronized double getScrollX()
	{
		return ScrollX;
	}

	public synchronized void setScrollX(double scrollX)
	{
		ScrollX = scrollX;
	}

	public  synchronized double getScrollY()
	{
		return ScrollY;
	}

	public  synchronized void setScrollY(double scrollY)
	{
		ScrollY = scrollY;
	}

	public synchronized double getZoom()
	{
		return Zoom;
	}

	public synchronized void setZoom(double zoom)
	{
		Zoom = zoom;
	}

	public void save(File file) throws IOException
	{
		save(file, "BMP") ;
	}
	
	public void save(File file, String EXTENSIONformat) throws IOException
	{
		BufferedImage tamponSauvegarde = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = tamponSauvegarde.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		this.paint(g);
		ImageIO.write(tamponSauvegarde, EXTENSIONformat, file);
	}

	public void savePDF(File file)/* throws IOException, DocumentException*/
	{
		/*
		 * BufferedImage tamponSauvegarde = new BufferedImage( this.getWidth(),
		 * this.getHeight(), BufferedImage.TYPE_3BYTE_BGR); Graphics g =
		 * tamponSauvegarde.getGraphics(); g.setColor(Color.WHITE);
		 * g.fillRect(0, 0, this.getWidth(), this.getHeight()); this.paint(g);
		 */

/*		Document doc = new Document(PageSize.A0);

		@SuppressWarnings("unused")
		PdfWriter out = null;

		out = PdfWriter.getInstance(doc, new FileOutputStream(file));

		doc.open();

		// iText font != java.awt.Font ! 
		FontFactory.registerDirectories(); // register all fonts in the usual 

		doc.addTitle("EOL Report");
		doc.addCreationDate();
		doc.addCreator("Warnotte R.");
		doc.addProducer();

		Paragraph p = new Paragraph("EOL Report");
		p.setAlignment(1);
		try
		{
			doc.add(p);
		} catch (DocumentException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D img2D = img.createGraphics();
		Dimension dim = this.getSize();
		this.setSize(dim.width, dim.height);

		//	this.print(img2D);
		this.paint(img2D);
		this.setSize(dim.width, dim.height);
		//	float viewWidth = out.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin(); 

		p = new Paragraph();
		com.itextpdf.text.Image itextImg;
		try
		{
			itextImg = com.itextpdf.text.Image.getInstance(img, Color.BLUE, false);
			p.add(itextImg);
			doc.add(p);
		} catch (BadElementException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// restore old LAF 
		//pnl.setUI(old); 

		///// trailing text 
		try
		{
			doc.add(new Paragraph("E O L Support structure"));
		} catch (DocumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doc.close();
*/
	}

	/*
	 * Transforme la coordon???e en bonne coordonn???e (avec zoom et translation)
	 */
	public Point2D convertViewXYtoRealXY(double X, double Y)
	{
		double x = X;
		double y = Y;

		// x = (x-((LockScrollX==false)?0:getWidth()/2))/Zoom-((LockScrollX==false)?0:ScrollX);
		// y = (y-((LockScrollX==false)?0:getWidth()/2))/Zoom-((LockScrollY==false)?0:ScrollY);

		x = (x - getWidth() / 2) / Zoom - ScrollX;

		if (LockScrollX == true)
			x = (X) / Zoom;

		y = (y - getHeight() / 2) / Zoom - ScrollY;

		if (LockScrollY == true)
			y = (Y) / Zoom;

		if (invertYAxis==true) y=-y;
		if (invertXAxis==true) x=-x;
		
		Point2D p = new Point2D.Double(x, y);

		if (Math.abs((Angle % 180) - 5) > 5)
		{
			Path2D p2 = new Path2D.Double();
			p2.moveTo(X, Y);
			if (at != null)
			{
				Shape p3;
				try
				{
					p3 = at.createInverse().createTransformedShape(p2);
					Point2D pp = new Point2D.Double(p3.getBounds2D().getX(), p3.getBounds2D().getY());
					return pp;
				} catch (NoninvertibleTransformException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else
				return p;
		}
		return p;
	}

	/*
	 * protected double RealToWorldCoord(double x, double ScrollX,double width)
	 * { return (x-width/2)/Zoom-ScrollX; } protected double
	 * WorldToRealCoord(double x, double ScrollX, double width) { return
	 * ((x+ScrollX)*Zoom+width/2); }
	 */

	/*
	 * Transforme la coordon???e en bonne coordonn???e (avec zoom et translation)
	 */

	/*
	 * Transforme la coordon???e en bonne coordonn???e (avec zoom et translation)
	 */
	public Point2D convertRealXYToViewXY(double X, double Y)
	{
		double x = X;
		double y = Y;

		// Facteur pour mettre le dessin centr??? 
		x += this.ScrollX;
		y += this.ScrollY;

		x *= this.Zoom;
		y *= this.Zoom;

		if (invertYAxis==true) y=-y;
		if (invertXAxis==true) x=-x;
		
		Point2D p = new Point2D.Double(x, y);

		Rectangle2D p2 = new Rectangle2D.Double(X, Y, 1, 1);
		if (at != null)
		{
			Shape p3;
			//try {
			p3 = at.createTransformedShape(p2);
			Point2D pp = new Point2D.Double(p3.getBounds2D().getX(), p3.getBounds2D().getY());
			return pp;
			/*
			 * } catch (NoninvertibleTransformException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */

		}
		return p;
	}

	protected void dessineBoundingBox(Graphics2D g, Rectangle2D rect, double offset)
	{

		double x = rect.getX();
		double y = rect.getY();
		double w = rect.getWidth();
		double h = rect.getHeight();

		g.setColor(Color.BLACK);
		g.setStroke(drawingStroke_Boundarybox);

		offset /= at.getScaleX();
		offset += 1;

		Rectangle2D l = new Rectangle2D.Double(x - offset, y - offset, (int) (w - x) + offset * 2, (int) (h - y) + offset * 2);
		Shape lM = at.createTransformedShape(l);

		g.draw(lM);

		Point2D ptDst = null;
		ptDst = at.transform(new Point2D.Double(x - offset, y - offset - 0.5f), ptDst);
		//at.transform(pt, ptDst)

		g.drawString("Boundaries", (int) ptDst.getX(), (int) ptDst.getY());
		g.setStroke(new BasicStroke(1));
		Point2D pt = new Point2D.Double((x + w) / 2, (y + h) / 2);
		pt = at.transform(pt, ptDst);
		g.drawString("Center", (int) pt.getX() - 1, (int) pt.getY() - 2);
		g.drawRect((int) pt.getX() - 1, (int) pt.getY() - 1, 2, 2);
		g.setStroke(new BasicStroke(1.0f));
	}

	protected void dessineInfos(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.setFont(new Font("Impact", 0, 16));

		String str = String.format("World Mouse %d, %d\r\n", (int) MouseX, (int) MouseY);
		g.drawString(str, 0, 20);
		
		
		
		// MouseX

	}

	protected void calcule_Rectangle_Vue()
	{

		int MarginToView = 0;
		// Recupere la view actuelle du buffer (x,y,w,h)
		int X = (int) ScrollX;
		int Y = (int) ScrollY;
		
		if (invertYAxis==true) Y=Y*-1;
		if (invertXAxis==true) X=X*-1;

		int ww = this.getWidth();
		int hh = this.getHeight();
		Point2D p = this.convertViewXYtoRealXY(ww, hh);
		Point2D p2 = this.convertViewXYtoRealXY(X, Y);

		if (LockScrollX == true)
			p2.setLocation(0, p2.getY());
		if (LockScrollY == true)
			p2.setLocation(p2.getX(), 0);

		X = (int) p2.getX() + MarginToView;
		Y = (int) p2.getY() + MarginToView;

		int XX = X;
		if (LockScrollY == true)
			XX = 0;

		int YY = Y;
		if (LockScrollX == true)
			YY = 0;

		Vision = new Rectangle(X - 1, Y - 1, (int) p.getX() - XX - MarginToView, (int) p.getY() - YY - MarginToView);
		
	//	System.err.println("Vision = "+Vision);
	}

	public Rectangle Recupere_Rectangle_Vue()
	{
		return Vision;
	}

	@SuppressWarnings("unused")
	private void drawTextHighlight(Graphics2D g2, String title, int size, float opacity)
	{
		g2.setColor(Color.BLACK);
		Composite c = g2.getComposite();
		for (int i = -size; i <= size; i++)
		{
			for (int j = -size; j <= size; j++)
			{
				double distance = (i * i) + (j * j);
				float alpha = opacity;
				if (distance > 0.0d)
					alpha = (float) (1.0f / ((distance * size) * opacity));
				g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
				g2.drawString(title, i + size, j + size);
			}
		}
		g2.setComposite(c);
		g2.setColor(Color.WHITE);
		g2.drawString(title, size, size);
	}

	protected void drawArrowSansLigne(Graphics2D g, double Mx, double My, double theta)
	{

		int ss = 8;
		Polygon p2 = new Polygon();
		p2.addPoint(0, 0);
		p2.addPoint(-ss, -ss);
		p2.addPoint(-ss, ss);

		AffineTransform at2 = new AffineTransform();

		at2.concatenate(at);
		at2.translate(Mx, My);
		at2.scale(1 / at.getScaleX(), 1 / at.getScaleY());
		at2.rotate(theta);
		Shape t = at2.createTransformedShape(p2);
		g.fill(t);

	}

	public double getAngle(Point2D pt1, Point2D pt2)
	{
		double radians = (Math.atan2(pt2.getY() - pt1.getY(), pt2.getX() - pt1.getX()) * 180) / Math.PI;
		double degrees = Math.floor(radians);
		double dd = (degrees % 360 + 360) % 360;
		dd = 360 - dd;
		if (dd == 360)
			dd = 0;
		return dd;
	}

	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y)
	{
		return drawString(g, text, x, y, AlignTexteX.LEFT, AlignTexteY.TOP, false);
	}

	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param centered
	 *            centre le texte au millieu du texte (pour les rotation aussi).
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, AlignTexteX alignX, AlignTexteY alignY)
	{
		return drawString(g, text, x, y, alignX, alignY, false);
	}

	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param centered
	 *            centre le texte au millieu du texte (pour les rotation aussi).
	 * @param txtCantChangeSize
	 *            Le texte va-t-il avoir sa taille modifier par le facteur de
	 *            zoom ?
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, x, y, 0, alignX, alignY, txtCantChangeSize);
	}

	protected Shape drawString(Graphics2D g, String text, double x, double y, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, (float) x, (float) y, 0, alignX, alignY, txtCantChangeSize);
	}

	protected Shape drawString(Graphics2D g, String text, int x, int y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, (float) x, (float) y, ang, alignX, alignY, txtCantChangeSize);
	}
	
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString( g,  text,  x,  y,  ang,  alignX, alignY,  txtCantChangeSize, 1.0f);
	}

	/*
	protected Rectangle2D drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size)
	{
		return drawString(g,  text,  x,  y,  ang,  alignX, alignY,  txtCantChangeSize,  Size, false);
	}*/
	
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size)
	{
		return drawString(g,  text,  x,  y,  ang,  alignX, alignY,  txtCantChangeSize,  Size, false);
	}
	
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround)
	{
		return drawString(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, drawBackGround, Color.white);
	}
	
	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param ang
	 * @param centered centre le texte au millieu du texte (pour les rotation aussi).
	 * @param txtCantChangeSize Le texte va-t-il avoir sa taille modifier par le facteur de zoom ?
	 * @return 
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		// TODO : Gerer le SCALE
		AffineTransform oldat = (AffineTransform) g.getTransform().clone();

		//System.err.println("Focus : "+isFocusOwner());
		float sx = (float) at.getScaleX();
		float sy = (float) at.getScaleY();

		if (txtCantChangeSize == true)
		{
			sx = 1.0f / Size;
			sy = 1.0f / Size;
		}

		if (txtCantChangeSize == true)
		{
			if ((invertXAxis == false) && (invertYAxis == false))
			{
				
			};
			if ((invertXAxis == false) && (invertYAxis == true))
			{
				sy = -sy;
			};
			if ((invertXAxis == true) && (invertYAxis == false))
			{
				sx = -sx;
			};
			if ((invertXAxis == true) && (invertYAxis == true))
			{
				ang += 180;
			};
		}

		
		float recenterx = 0, recentery = 0;
		if ((alignX != AlignTexteX.LEFT) || (alignY != AlignTexteY.BOTTOM))
		{
			float txt_with = g.getFontMetrics().stringWidth(text);
			if (alignX == AlignTexteX.CENTER)
				recenterx = +(txt_with / 2f / sx);
			if (alignX == AlignTexteX.RIGHT)
				recenterx = +(txt_with / sx);
			if (alignY == AlignTexteY.CENTER)
				recentery = -(g.getFontMetrics().getFont().getSize() / 2f / sy);
			if (alignY == AlignTexteY.TOP)
				recentery = -(g.getFontMetrics().getFont().getSize() / sy);
		}

		AffineTransform at2 = new AffineTransform();
		at2.concatenate(at);

		AffineTransform at3 = new AffineTransform();
		
		at3.translate(x, y);
		at3.scale(1f / sx, 1f / sy);
		at3.rotate(Math.toRadians(ang));
		at3.translate(-recenterx * sx, -recentery * sy);

		at2.concatenate(at3);
	
		Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at3.createTransformedShape(rect_texte).getBounds2D();
	
		//g.setTransform(at2);

		Color colorCurrent = g.getColor();

		if (drawBackGround == true)
		{
			Rectangle2D rect_texteM = g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D rect_texte2 = (Rectangle2D) rect_texteM.clone();

			float elargX = 4;
			float elargY = 2;

			if (txtCantChangeSize == true)
			{
				elargX = 3;
				elargY = 1;
			}

			rect_texte2.setRect(rect_texte2.getX() - elargX / 2.0f, rect_texte2.getY() - elargY / 2.0f, rect_texte2.getWidth() + elargX, rect_texte2.getHeight() + elargY);
			Shape shp_rect_texte2 = at2.createTransformedShape(rect_texte2);
			// Dessine un cadre en dessous.
			Stroke old_Stroke = g.getStroke();
			g.setColor(backGroundColor);
			g.fill(shp_rect_texte2);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f));
			g.draw(shp_rect_texte2);
			
			g.setStroke(old_Stroke);
		}
		// Dessine le texte
		g.setColor(colorCurrent);
		
		FontRenderContext frc = g.getFontRenderContext();
		
		GlyphVector gv = g.getFont().createGlyphVector(frc, text);
		
		int length = gv.getNumGlyphs();
		

		
		// Methode rapide mais qui ne permet pas de faire certains truc que l'autre bazard du dessous fait.
//		if (true)
		{
			AffineTransform at4 = new AffineTransform();
			//at4.concatenate(at2);
			
			//at4.scale(1/ sx * Zoom,  1 / sy * Zoom);
			//at4.rotate(Math.toRadians(ang));
			//if (txtCantChangeSize == true)
			{
				
			if ((invertXAxis == false) && (invertYAxis == false))
			{
				at4.scale(1/ sx * Zoom,  1 / sy * Zoom);
			
				//angle = 45;
				at4.rotate(Math.toRadians(ang));
				
			};
			if ((invertXAxis == false) && (invertYAxis == true))
			{
			//	sy = -sy;
				
				at4.scale(1/ sx * Zoom,  1 / -sy * Zoom);
				
			  at4.rotate(Math.toRadians(ang));
				
			};
			if ((invertXAxis == true) && (invertYAxis == false))
			{
			//	sx = -sx;
				
				at4.scale(1/ -sx * Zoom,  1 / sy * Zoom);
				
				at4.rotate(Math.toRadians(ang));
			};
			if ((invertXAxis == true) && (invertYAxis == true))
			{
			//	ang += 180;
				
				at4.scale(1/ sx * Zoom,  1 / sy * Zoom);
				// AJOUT LE 28-09-2021 pour un bug si on fait une rotation de AT
				at4.rotate(Math.toRadians(ang+180));
			};
			
			}

		//	AffineTransform att = AffineTransform.getRotateInstance(Math.toRadians(45));
			
			
		//	System.err.println("Ang = "+(extractAngle(at)));
			
			gv.setGlyphTransform(0, at2);
			
			for (int i = 1; i < length; i++) {
				
				gv.setGlyphTransform(i, at4);
			}
			g.drawGlyphVector(gv, 0, 0);
			
			//g.setTransform(at4);
			//g.drawString(text, 0,0);
		}
//		else
		{
	/*
			for (int i = 0; i < length; i++) {
			//	gv.setGlyphTransform(i, at2);
			//  Point2D p = gv.getGlyphPosition(i);
			  Shape shp = gv.getGlyphOutline(i, 0, 0);
			  shp = at2.createTransformedShape(shp);
			//  g.setColor(Color.YELLOW);
			//  g.fill(shp);
			//  g.setStroke(new BasicStroke(8.0f));
			//  g.setColor(Color.BLUE);
			  g.fill(shp);
			//  g.setStroke(new BasicStroke(1.0f));
			}*/
		}

		//g.setTransform(oldat);
		return rect_texte;
	}
		
	protected Shape drawStringOLD(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size)
	{
		return drawStringOLD(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, false, Color.BLUE);
	}
	
	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param ang
	 * @param centered centre le texte au millieu du texte (pour les rotation aussi).
	 * @param txtCantChangeSize Le texte va-t-il avoir sa taille modifier par le facteur de zoom ?
	 * @return 
	 */
	protected Shape drawStringOLD(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		// TODO : Gerer le SCALE
		AffineTransform old = g.getTransform();

		//System.err.println("Focus : "+isFocusOwner());
		// Cette valeur de scale pourrait sembler bonne, mais si on fait une rotation de AT alors le scale est pas bon.
		float sx = (float) at.getScaleX();
		float sy = (float) at.getScaleY();

		sx = (float)Zoom;
		sy = (float)Zoom;
		
		if (txtCantChangeSize == true)
		{
			sx = 1.0f / Size;
			sy = 1.0f / Size;
		}

	//	if (txtCantChangeSize == true)
		{
			if ((invertXAxis == false) && (invertYAxis == false))
			{
				
			}
			;
			if ((invertXAxis == false) && (invertYAxis == true))
			{
				sy = -sy;
			}
			;
			if ((invertXAxis == true) && (invertYAxis == false))
			{
				sx = -sx;
			}
			;
			if ((invertXAxis == true) && (invertYAxis == true))
			{
				ang += 180;
			}
			;
		}
		

		/*
		 * if (isFocusOwner()==false) { // TODO : Bug si on sort de la fenetre
		 * graphique ... mais si on sort ed la fenetre principale // ca bug
		 * encore plus at2.translate(1, 56); }
		 */

		
		float recenterx = 0, recentery = 0;
		if ((alignX != AlignTexteX.LEFT) || (alignY != AlignTexteY.BOTTOM))
		{
			float txt_with = g.getFontMetrics().stringWidth(text);
			if (alignX == AlignTexteX.CENTER)
				recenterx = +(txt_with / 2f / sx);
			if (alignX == AlignTexteX.RIGHT)
				recenterx = +(txt_with / sx);
			if (alignY == AlignTexteY.CENTER)
				recentery = -(g.getFontMetrics().getFont().getSize() / 2f / sy);
			if (alignY == AlignTexteY.TOP)
				recentery = -(g.getFontMetrics().getFont().getSize() / sy);
		}

		AffineTransform at2 = new AffineTransform();
		at2.concatenate(at);

		AffineTransform at3 = new AffineTransform();
		at3.translate(x, y);
		at3.scale(1f / sx, 1f / sy);
		at3.rotate(Math.toRadians(ang));
		at3.translate(-recenterx * sx, -recentery * sy);

		at2.concatenate(at3);
	
		Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at3.createTransformedShape(rect_texte).getBounds2D();

		
		g.setTransform(at2);

		Color colorCurrent = g.getColor();

		if (drawBackGround == true)
		{
			Rectangle2D rect_texteM = g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D rect_texte2 = (Rectangle2D) rect_texteM.clone();

			float elargX = 4;
			float elargY = 2;

			if (txtCantChangeSize == true)
			{
				elargX = 3;
				elargY = 1;
			}

			rect_texte2.setRect(rect_texte2.getX() - elargX / 2.0f, rect_texte2.getY() - elargY / 2.0f, rect_texte2.getWidth() + elargX, rect_texte2.getHeight() + elargY);
			// Dessine un cadre en dessous.
			Stroke old_Stroke = g.getStroke();
			g.setColor(backGroundColor);
			g.fill(rect_texte2);
			g.setColor(Color.BLACK);
			//if (txtCantChangeSize==true)
			//	g.setStroke(new BasicStroke(0.5f/(float)getZoom()));
			//else
			g.setStroke(new BasicStroke(1.0f));
			g.draw(rect_texte2);
			g.setColor(colorCurrent);
			g.setStroke(old_Stroke);
		}
		// Dessine le texte
		g.drawString(text, 0, 0);

		
	/*Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at2.createTransformedShape(rect_texte).getBounds2D();

		try {
			rect_texte = at.createInverse().createTransformedShape(rect_texte);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		g.setTransform(old);

		/*
		 * Point2D pL= new Point2D.Float(); at.transform(new Point2D.Float(x,
		 * y),pL); g.drawString(text, (int)pL.getX(), (int)pL.getY());
		 */
		return rect_texte;
	}

	/**
	 * D??conne si on rotate AT
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param ang
	 * @param alignX
	 * @param alignY
	 * @param txtCantChangeSize
	 * @param Size
	 * @param drawBackGround
	 * @param backGroundColor
	 * @return
	 */
	protected Shape drawStringOLD_BACKUP(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		// TODO : Gerer le SCALE
		AffineTransform old = g.getTransform();

		//System.err.println("Focus : "+isFocusOwner());
		// Cette valeur de scale pourrait sembler bonne, mais si on fait une rotation de AT alors le scale est pas bon.
		float sx = (float) at.getScaleX();
		float sy = (float) at.getScaleY();

		//sx = (float)Zoom;
		//sy = (float)Zoom;
		
		if (txtCantChangeSize == true)
		{
			sx = 1.0f / Size;
			sy = 1.0f / Size;
		}

		if (txtCantChangeSize == true)
		{
			if ((invertXAxis == false) && (invertYAxis == false))
			{
				
			}
			;
			if ((invertXAxis == false) && (invertYAxis == true))
			{
				sy = -sy;
			}
			;
			if ((invertXAxis == true) && (invertYAxis == false))
			{
				sx = -sx;
			}
			;
			if ((invertXAxis == true) && (invertYAxis == true))
			{
				ang += 180;
			}
			;
		}
		

		/*
		 * if (isFocusOwner()==false) { // TODO : Bug si on sort de la fenetre
		 * graphique ... mais si on sort ed la fenetre principale // ca bug
		 * encore plus at2.translate(1, 56); }
		 */

		
		float recenterx = 0, recentery = 0;
		if ((alignX != AlignTexteX.LEFT) || (alignY != AlignTexteY.BOTTOM))
		{
			float txt_with = g.getFontMetrics().stringWidth(text);
			if (alignX == AlignTexteX.CENTER)
				recenterx = +(txt_with / 2f / sx);
			if (alignX == AlignTexteX.RIGHT)
				recenterx = +(txt_with / sx);
			if (alignY == AlignTexteY.CENTER)
				recentery = -(g.getFontMetrics().getFont().getSize() / 2f / sy);
			if (alignY == AlignTexteY.TOP)
				recentery = -(g.getFontMetrics().getFont().getSize() / sy);
		}

		AffineTransform at2 = new AffineTransform();
		at2.concatenate(at);

		AffineTransform at3 = new AffineTransform();
		at3.translate(x, y);
		at3.scale(1f / sx, 1f / sy);
		at3.rotate(Math.toRadians(ang));
		at3.translate(-recenterx * sx, -recentery * sy);

		at2.concatenate(at3);
	
		Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at3.createTransformedShape(rect_texte).getBounds2D();

		
		g.setTransform(at2);

		Color colorCurrent = g.getColor();

		if (drawBackGround == true)
		{
			Rectangle2D rect_texteM = g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D rect_texte2 = (Rectangle2D) rect_texteM.clone();

			float elargX = 4;
			float elargY = 2;

			if (txtCantChangeSize == true)
			{
				elargX = 3;
				elargY = 1;
			}

			rect_texte2.setRect(rect_texte2.getX() - elargX / 2.0f, rect_texte2.getY() - elargY / 2.0f, rect_texte2.getWidth() + elargX, rect_texte2.getHeight() + elargY);
			// Dessine un cadre en dessous.
			Stroke old_Stroke = g.getStroke();
			g.setColor(backGroundColor);
			g.fill(rect_texte2);
			g.setColor(Color.BLACK);
			//if (txtCantChangeSize==true)
			//	g.setStroke(new BasicStroke(0.5f/(float)getZoom()));
			//else
			g.setStroke(new BasicStroke(1.0f));
			g.draw(rect_texte2);
			g.setColor(colorCurrent);
			g.setStroke(old_Stroke);
		}
		// Dessine le texte
		g.drawString(text, 0, 0);

		
	/*Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		rect_texte = at2.createTransformedShape(rect_texte).getBounds2D();

		try {
			rect_texte = at.createInverse().createTransformedShape(rect_texte);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		g.setTransform(old);

		/*
		 * Point2D pL= new Point2D.Float(); at.transform(new Point2D.Float(x,
		 * y),pL); g.drawString(text, (int)pL.getX(), (int)pL.getY());
		 */
		return rect_texte;
	}

	/**
	 * @param rect_texte
	 * @param f
	 * @param g
	 * @return
	 */
	@SuppressWarnings("unused")
	private Rectangle2D translate(Rectangle2D rect_texteIN, float f, float g)
	{
		Rectangle2D rect_texte = (Rectangle2D) rect_texteIN.clone();
		rect_texte.setRect(rect_texte.getX() + f, rect_texte.getY() + g, rect_texte.getWidth(), rect_texte.getHeight());
		return rect_texte;
	}

	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, boolean enableArrowPt1, boolean enableArrowPt2)
	{
		return drawArrow2(g, pt1, pt2, 1.0f, 1.0f, enableArrowPt1, enableArrowPt2, false);
	}

	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow)
	{
		return drawArrow2(g, pt1, pt2, 1.0f, 1.0f, enableArrowPt1, enableArrowPt2, enableMiddleArrow);
	}
	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow, float scale)
	{
		return drawArrow2(g, pt1, pt2, 1.0f, 1.0f, enableArrowPt1, enableArrowPt2, enableMiddleArrow, scale);
	}
	
	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2)
	{
		return drawArrow2(g, pt1, pt2, scaleArrow1, scaleArrow2, enableArrowPt1, enableArrowPt2,  false);
	}
	
	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2,  boolean enableMiddleArrow)
	{
		return drawArrow2(g, pt1, pt2, scaleArrow1, scaleArrow2, enableArrowPt1, enableArrowPt2,  enableMiddleArrow, 1);
	}
	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2,  boolean enableMiddleArrow, float scale)
	{
		//g.setColor(Color.BLACK);
		Line2D shape = new Line2D.Float(pt1, pt2);
		
		float angle1 = (float) getAngle(pt1, pt2);

		//scale * = 2;
		Path2D tri = new Path2D.Float();
		//	float scale = 1f;
			tri.moveTo(scale * 0.0, scale * -1.0);
			tri.lineTo(scale * -1.0, scale * 1.0);
			tri.lineTo(scale * 0.0, scale * 0.0f);
			tri.lineTo(scale * 1.0, scale * 1.0);
			tri.lineTo(scale * 0.0, scale * -1.0);

			Shape trshape = at.createTransformedShape(shape);
			Shape retour = shape;
			g.draw(trshape);

		AffineTransform at2;

		Color old = g.getColor();

		if (enableArrowPt1 == true)
		{
			at2 = new AffineTransform(at);
			at2.translate(pt1.getX(), pt1.getY());
			at2.scale(scaleArrow1, scaleArrow1);
			at2.rotate(Math.toRadians(-angle1 - 90));
			at2.translate(0, scale);
			
			trshape = at2.createTransformedShape(tri);
			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		if (enableArrowPt2 == true)
		{
			at2 = new AffineTransform(at);
			at2.translate(pt2.getX(), pt2.getY());
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
			at2.translate(0, scale);
			trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}
		
		if (enableMiddleArrow == true)
		{
			at2 = new AffineTransform(at);
			at2.translate((pt2.getX()+pt1.getX())/2, (pt2.getY()+pt1.getY())/2);
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
		//	at2.translate(0, scale);
			trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}
		
		
		g.setColor(old);
		
		return retour;
	}
	
	/**
	 * @param color_tache
	 * @return
	 */
	protected Color invertColorValues(Color color_tache)
	{
		Color textColor = new Color(
				255-color_tache.getRed(),
                255-color_tache.getGreen(),
                255-color_tache.getBlue(),
                color_tache.getAlpha());
		return textColor;
	}

	/**
	 * @param color_tache
	 * @return
	 */
	protected Color invertColorRGB(Color color_tache)
	{
		Color textColor = new Color(
				255-color_tache.getBlue(),
                255-color_tache.getGreen(),
                255-color_tache.getRed(),
                color_tache.getAlpha());
		return textColor;
	}


	
	/**
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	public BufferedImage loadImage(String string) throws IOException
	{
		BufferedImage image = ImageIO.read(new File(string));
		
		BufferedImage convertedImage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		GraphicsDevice gd = ge.getDefaultScreenDevice ();
		GraphicsConfiguration gc = gd.getDefaultConfiguration ();
		convertedImage = gc.createCompatibleImage (image.getWidth (), 
				image.getHeight (), 
				image.getTransparency () );
		Graphics2D g2d = convertedImage.createGraphics ();
		g2d.drawImage ( image, 0, 0, image.getWidth (), image.getHeight (), null );
		g2d.dispose();
		return convertedImage;
	}


	/**
	 * 
	 * @param g
	 * @param img_ship2
	 * @param x
	 * @param y
	 * @param angle
	 * @param scaleMultiplier
	 * @return
	 */
	public Shape drawImage(Graphics2D g, BufferedImage img_ship2, float x, float y, float angle, float scaleMultiplier, boolean zoomIndependantScale)	{
		return drawImage(g, img_ship2, x, y, img_ship2.getWidth(), img_ship2.getHeight(), angle, scaleMultiplier, zoomIndependantScale);
	}


	/**
	 * 
	 * @param g
	 * @param img_ship2
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param angle
	 * @param scaleMultiplier
	 * @return
	 */
	public Shape drawImage(Graphics2D g, BufferedImage img_ship2, float x, float y, float w, float h, float angle, float scaleMultiplier, boolean zoomIndependantScale)
	{
		AffineTransform at2 = new AffineTransform(at);
	
		//zoomIndependantScale=true;
		float img_w = img_ship2.getWidth();
		float img_h = img_ship2.getHeight();
		
		float sX = w/img_w;
		float sY = h/img_h;
		
		at2.translate(x, y);
		if (zoomIndependantScale)
		{
			// Zoom independant scale
			at2.scale(1.0/Zoom, 1.0/Zoom);
		}
		at2.scale(sX, sY);	
		at2.scale(scaleMultiplier, scaleMultiplier);
		at2.rotate(Math.toRadians(angle));	
		at2.translate(-img_w/2, -img_h/2);
		
		// Bordure
		Rectangle2D rect = new Rectangle2D.Double(0, 0, w/sX, h/sY);
		Shape shp = at2.createTransformedShape(rect);
		
		g.drawImage(img_ship2, at2, this);
		return shp;
	}
	
	/**
	 * Deplace la ligne parallement de offsetPixels (positif ou negatif).
	 * @param line
	 * @param offsetPixels
	 * @return
	 */
	public static Line2D.Double createLineParallel(Line2D.Double line, double offsetPixels)
	{
		return View2D_Utils.createLineParallel(line, offsetPixels);
	}
	
	/**
	 * Supprime une distance a un segment (a gauche et a droite).
	 * @param line
	 * @param longueurarabotter
	 * @return
	 */
	public static Line2D.Double createLineLength(Line2D.Double line, double longueurarabotter)
	{
		return View2D_Utils.createLineLength(line, longueurarabotter);
	}
	
	/**
	 * Give middle point
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static Point2D interpolate(Point2D pt1, Point2D pt2)
	{
		return new Point2D.Double((pt1.getX() + pt2.getX()) / 2,(pt1.getY() + pt2.getY()) / 2);
	}
	
	public static Point2D interpolate(double X1, double Y1, double X2, double Y2)
	{
		return new Point2D.Double((X1 + X2) / 2,(Y1 + Y2) / 2);
	}
	
	public static Point2D interpolate(Line2D line)
	{
		return interpolate(line.getP1(),  line.getP2());
	}
	
	public static Point2D interpolate(Line2D line, double delta)
	{
		return interpolate(line.getP1(),  line.getP2(), delta);
	}
	
	public static Point2D interpolate(Point2D pt1, Point2D pt2, double delta)
	{
		double x = pt1.getX() * (1.0 - delta) + pt2.getX() * (delta);
		double y = pt1.getY() * (1.0 - delta) + pt2.getY() * (delta);
		
		return new Point2D.Double(x,y);
	}

	public static synchronized boolean isDEBUG_VERBOSE()
	{
		return DEBUG_VERBOSE;
	}

	public static synchronized void setDEBUG_VERBOSE(boolean dEBUG_VERBOSE)
	{
		DEBUG_VERBOSE = dEBUG_VERBOSE;
	}

	public void setSelectableObject(ArrayList<SelectionTuple<Shape, ?>> selectableObject)
	{
		this.selectableObject = selectableObject;
	}

	public ArrayList<SelectionTuple<Shape, ?>> getSelectableObject()
	{
		return selectableObject;
	}

	public synchronized boolean isInvertXAxis() {
		return invertXAxis;
	}

	public synchronized void setInvertXAxis(boolean invertXAxis) {
		this.invertXAxis = invertXAxis;
	}

	public synchronized boolean isInvertYAxis() {
		return invertYAxis;
	}

	public synchronized void setInvertYAxis(boolean invertYAxis) {
		this.invertYAxis = invertYAxis;
	}

	public ConfigurationColor getColorConfig()
	{
		return colorConfig;
	}

	public void setColorConfig(ConfigurationColor colorConfig)
	{
		this.colorConfig = colorConfig;
		
		
	}
	
	
	/**************************
	 * 
	 * 
	 * Gestion des Nurbs Path
	 * 
	 * 
	 **************************/
	
	/**
	 * @param g
	 * @param curve1
	 * @param drawControlNode 
	 */
	public List<Shape> drawNurbsCurve(Graphics2D g, NurbsCurve curve1, float Thickness, Stroke stroke, Color color)
	{
			Shape shpCurve = at.createTransformedShape(curve1.getShape());
			Stroke oldStroke = g.getStroke();
		    g.setStroke(stroke);

			if (contxt.getSelection().contains(curve1))
				g.setColor(Color.magenta);
			else
				g.setColor(color);
			
			g.draw(shpCurve);
			g.setStroke(oldStroke);
			
			// Pour la selection....
			List<Shape> retour_list = null;
			//double longueur = 0;
			
			retour_list = curve1.getCollisionShapeList( Thickness);
			/*
			for (int j = 0; j < curve1.getLines_rectangles().size(); j++)
			{
				GeneralPath l = curve1.getLines_rectangles().get(j);
				retour_list.add(l);
				//longueur+=curve1.getLength();
			}*/
			
			return retour_list;
	}
	
	protected List<SelectionTuple<Shape, Object>> drawNurbsControlPoints(Graphics2D g, NurbsCurve curve1, Color colorIn, Color colorOut, Color dashedLineColor)
	{
		float dash1[] = {10.0f};
	    BasicStroke dashed =
	    new BasicStroke(2.0f,
	                        BasicStroke.CAP_BUTT,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, dash1, 0.0f);
	    return drawNurbsControlPoints( g,  curve1,  colorIn,  colorOut,  dashedLineColor, dashed);
	}
	
	protected List<SelectionTuple<Shape, Object>> drawNurbsControlPoints(Graphics2D g, NurbsCurve curve1, Color colorIn, Color colorOut, Color dashedLineColor, Stroke dashLineStroke)
	{
		List<SelectionTuple<Shape, Object>> retour_shapes = new ArrayList<>();
		
		if (dashLineStroke!=null)
			g.setStroke(dashLineStroke);
		
		g.setColor(dashedLineColor);

		NurbsPoint pt1 = curve1.getPt1();
		NurbsPoint pt2 = curve1.getPt2();
		NurbsPoint pt3 = curve1.getPt3();
		NurbsPoint pt4 = curve1.getPt4();
		
		Line2D.Double l1 = new Line2D.Double(pt2.getX(), pt2.getY(), pt1.getX(), pt1.getY());
		Line2D.Double l2 = new Line2D.Double(pt3.getX(), pt3.getY(), pt4.getX(), pt4.getY());

		g.draw(at.createTransformedShape(l1));		
		g.draw(at.createTransformedShape(l2));	
		g.setStroke(new BasicStroke(1.0f));
	
		retour_shapes.add(new SelectionTuple<Shape, Object>(drawNurbsControlPoint(g, pt2, colorIn, colorOut), pt2));
		retour_shapes.add(new SelectionTuple<Shape, Object>(drawNurbsControlPoint(g, pt3, colorIn, colorOut), pt3));
		
		return retour_shapes;
	}

	protected Shape drawNurbsPoint(Graphics2D g, NurbsPoint np, Color colorIn, Color colorOut) {
		double scale = 4.0;
		Ellipse2D rect = new Ellipse2D.Double(np.getX()-0.5*scale, np.getY()-0.5*scale, 1.0*scale, 1.0*scale);
		if (contxt.getSelection().contains(np))
			g.setColor(Color.MAGENTA);
		else
			g.setColor(colorIn);
		g.fill(at.createTransformedShape(rect));
		{
			g.setColor(colorOut);
			g.setStroke(new BasicStroke(2.0f));
			g.draw(at.createTransformedShape(rect));
			g.setStroke(new BasicStroke(1.0f));
		}
		return rect;

	}
	
	/**
	 * Draw one control point
	 * @param g
	 * @param np
	 * @return
	 */
	protected Shape drawNurbsControlPoint(Graphics2D g, NurbsPoint np, Color colorIn, Color colorOut) {

		AffineTransform at1 = new AffineTransform();
		at1.translate(np.getX(), np.getY());
		
		double scaleshpControlPoint = 2.0;
		Shape shpControlPoint = new Ellipse2D.Double(-0.5*scaleshpControlPoint, -0.5*scaleshpControlPoint, 1.0*scaleshpControlPoint, +1.0*scaleshpControlPoint);

		 Shape shpPoint_ = at1.createTransformedShape(shpControlPoint);
		if (contxt.getSelection().contains(np))
			g.setColor(Color.magenta);
		else
			g.setColor(Color.white);
		
		g.fill(at.createTransformedShape(shpPoint_));
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2.0f));
		g.draw(at.createTransformedShape(shpPoint_));
		g.setStroke(new BasicStroke(1.0f));
	
		return shpPoint_;

	}
	
	
	/**
	 * TODO : C'est bien joli mais pas faciement customisable cot??? client de la lib...
	 * Dessine un nurbsPath complet (affichage et gestion de la selection comprise).
	 * @param g
	 * @param DrawCurve
	 * @param DrawControlNode
	 * @param DrawNode
	 * @param resizeableWithZoom 
	 */
	public void drawNurbsPath(Graphics2D g, NurbsPath npath, boolean DrawCurve,  boolean DrawControlNode,  boolean DrawNode, boolean resizeableWithZoom)
	{
		
		// TODO : Gestion de resizeableWithZoom
		float Zoom_= (float) Zoom;
		// TODO : Probleme de detection de thickness de ligne, ca risque de bouffer le cpu :(
		if (resizeableWithZoom==true)
			Zoom_= 1f;
		
		// Test 3 : 3 eme methode.
		if (DrawCurve) {
			
			

			float dash1[] = { 2.5f * Zoom_ };
			BasicStroke dashedIn = new BasicStroke((0.5f*Zoom_ ), BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_MITER, (2.5f ), dash1, 0.0f);
			
			Color colorOut = Color.BLACK;
			Color colorIn = Color.WHITE;

			for (int i = 0; i < npath.getCurves().size(); i++) {
				NurbsCurve curve = npath.getCurves().get(i);
				
				float thickness = (curve.getThicknessDetection() *Zoom_);
				BasicStroke dashedOut = new BasicStroke((thickness),
						BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
				List<Shape> selection_shapes = drawNurbsCurve(g, curve, curve.getThicknessDetection()/2.0f, dashedOut, colorOut);
				addToSelectableObject(selection_shapes, curve);
				drawNurbsCurve(g, curve,thickness, dashedIn, colorIn);

			}
			/*
			for (int i = 0; i < npath.getCurves().size(); i++) {
				NurbsCurve curve = npath.getCurves().get(i);
				float thickness = (curve.getThicknessDetection() );
				drawNurbsCurve(g, curve, thickness, dashedIn, colorIn);
			}*/
		}

		if (DrawControlNode)
			for (int i = 0; i < npath.getCurves().size(); i++) {
				NurbsCurve curve = npath.getCurves().get(i);
				List<SelectionTuple<Shape, Object>> ret = drawNurbsControlPoints(g, curve, Color.WHITE, Color.BLACK, Color.BLUE);
				addToSelectableObject(ret);
			}
		if (DrawNode == true) {
			List<NurbsPoint> list_points = npath.getPoints();
			for (Iterator<NurbsPoint> iterator = list_points.iterator(); iterator.hasNext();) {
				NurbsPoint nurbsPoint = iterator.next();
				addToSelectableObject(new SelectionTuple<Shape, Object>(drawNurbsPoint(g, nurbsPoint, Color.RED, Color.BLACK), nurbsPoint));
			}
		}
	}
	
	
	
	/**
	 * Clamp v pour qu'ils soit toujours entre 0 et 1
	 * @param v
	 * @return
	 */
	double clamp(double v)		//////
	{		//////
	  double t = v < 0 ? 0 : v;		//////
	  return t > 1.0 ? 1.0 : t;		//////
	}		//////
	
	/**
	 * 
	 * @param ratio -1 to 1
	 */
	protected Color mapGrayScaleTORGB(float ratio)
	{
		//float Quantize = 8.0f;
		//ratio = ((int)(ratio * Quantize)) / Quantize; 
		
		double red   = clamp(1.5 - Math.abs(2.0 * ratio - 1.0)); //////
		double green = clamp(1.5 - Math.abs(2.0 * ratio));//////
		double blue  = clamp(1.5 - Math.abs(2.0 * ratio + 1.0));		//////
		
		return new Color((int)(255*red), (int)(255*green), (int)(255*blue));
	}
	
	
	
}
