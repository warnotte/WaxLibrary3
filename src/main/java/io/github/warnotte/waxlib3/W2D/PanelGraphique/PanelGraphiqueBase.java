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
import java.awt.Point;
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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsCurve;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPath;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPoint;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

public abstract class PanelGraphiqueBase<T> extends JPanel implements ComponentListener, KeyListener, MouseListener, MouseMotionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8780048115222887324L;

	protected CurrentSelectionContext	contxt		= null;
	protected ConfigurationGeneral		config		= new ConfigurationGeneral();
	private ConfigurationColor			colorConfig	= new ConfigurationColor();

	protected Random	rand	= new Random();	// @jve:decl-index=0:
	protected double	accumx;
	protected double	accumy;

	protected boolean	LockScrollY	= false;
	protected boolean	LockScrollX	= false;

	protected double	MouseY;			// Actual position of mice (with scroll and zoom)
	protected double	MouseX;
	public double		MouseDX	= 0;	// Actual Displacement during drag (with scroll and zoom);
	public double		MouseDY	= 0;

	protected boolean	CTRL;
	protected double	lockX	= -1;
	protected double	lockY	= -1;
	public boolean		SHIFT;
	protected double	oldmousex;
	protected double	oldmousey;
	protected boolean	MOUSEINSIDE;

	protected Point2D displacement = null; //Deplacement lors du move.

	protected double	ScrollX	= 0;
	protected double	ScrollY	= 0;
	protected double	Zoom	= 10.0d;
	// TODO : Si on change l'angle, plein de truc bug (le scrolling par exemple), la selection etc ....
	protected double	Angle		= 0;
	protected double	ZoomMin		= 0.3f;
	protected double	ZoomMax		= 200f;
	protected boolean	invertXAxis	= false;
	protected boolean	invertYAxis	= true;

	Stroke drawingStroke_Boundarybox = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50, new float[] { 9 }, 0); //  @jve:decl-index=0:

	Stroke						drawingStroke_Helplines	= new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50, new float[] { 5 }, 0);
	protected AffineTransform	at						= null;
	protected Rectangle			Vision;

	protected ArrayList<SelectionTuple<Shape, ?>> selectableObject = new ArrayList<SelectionTuple<Shape, ?>>();

	protected Map<Shape, Object>	map_selectableShapeObject	= new HashMap<>();
	protected Map<Object, Shape>	map_selectableObjectShape	= new HashMap<>();

	private boolean DisableSelectable;

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
		if (DisableSelectable == false)
		{
			getSelectableObject().add(selectionTuple);
			map_selectableShapeObject.put(selectionTuple.getShape(), selectionTuple.getUserObject());
			map_selectableObjectShape.put(selectionTuple.getUserObject(), selectionTuple.getShape());
		}
	}

	protected void addToSelectableObject(List<SelectionTuple<Shape, Object>> selection_tuple)
	{
		for (Iterator<SelectionTuple<Shape, Object>> iterator = selection_tuple.iterator(); iterator.hasNext();)
		{
			SelectionTuple<Shape, Object> o = iterator.next();
			addToSelectableObject(o);
		}
	}

	protected void addToSelectableObject(List<Shape> selection_shapes, Object object)
	{
		for (Iterator<Shape> iterator = selection_shapes.iterator(); iterator.hasNext();)
		{
			Shape							shape	= iterator.next();
			SelectionTuple<Shape, Object>	o		= new SelectionTuple<>(shape, object);
			addToSelectableObject(o);
		}
	}

	protected void addToSelectableObject(Shape selection_shapes, Object object)
	{
		addToSelectableObject(new SelectionTuple<Shape, Object>(selection_shapes, object));
	}

	View2D_Utils v2dUtils = new View2D_Utils();

	BufferedImage fBugImage = null;

	protected static boolean DEBUG_VERBOSE = true;

	@SuppressWarnings("unused")
	private PanelGraphiqueBase()
	{

	}

	public PanelGraphiqueBase(CurrentSelectionContext contxt)
	{
		this.addComponentListener(this);

		// Je me demande pourquoi j'ai foutu ces 3 merdeuse ligne, car apr�s le programme
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
		/*
		 * System.err.println("Resizing Panel, remaking a bufferedimage");
		 * myBufferedImage = new BufferedImage(getWidth(), getHeight(),
		 * BufferedImage.TYPE_INT_RGB);
		 */
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
		int	w	= getWidth();
		int	h	= getHeight();
		g.drawLine(0, (int) y, w, (int) y);
		g.drawLine((int) x, 0, (int) x, h);
		g.setStroke(new BasicStroke(1.0f));

	}

	protected void dessineGrille(Graphics2D g, boolean DrawCentralAxis)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		int	w	= getWidth();
		int	h	= getHeight();

		double	space	= config.getGRID_SIZE() * Zoom;
		double	ox		= ((ScrollX * Zoom) % (space));
		double	oy		= ((ScrollY * Zoom) % (space));

		//	if (LockScrollX==1)
		//		ox=0;

		g.translate(ox, oy);

		g.translate(w / 2, h / 2);

		g.setColor(getColorConfig().getCOLOR_GRILLE_1().getColor());
		for (double x = 0; x < w / 2 + space; x += space)
		{
			/*
			 * Line2D.Double line1 = new Line2D.Double(x, -(h / 2.0 + space), x,
			 * (h / 2.0 + space)); Line2D.Double line2 = new
			 * Line2D.Double(-x,-(h / 2.0 + space), -x, (h / 2 + space));
			 */ g.drawLine((int) x, (int) -(h / 2 + space), (int) x, (int) (h / 2 + space));
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
			int	y	= (int) ((ScrollY * Zoom) + h / 2.0);
			int	x	= (int) ((ScrollX * Zoom) + w / 2.0);
			g.drawLine(0, y, w, y);
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
		BufferedImage	myBufferedImage	= new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D		myGraphics2D	= myBufferedImage.createGraphics();
		// myGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		// myGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		this.paint(myGraphics2D);
		return ImagetoBufferedImage(myBufferedImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH));

		// return ImageUtilities.ImagetoBufferedImage(createImage(getWidth(),
		// getHeight()));
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

	public void drawArrow(Graphics2D g2d, float xCenter, float yCenter, float x, float y, float stroke)
	{
		double	aDir	= Math.atan2(xCenter - x, yCenter - y);
		float	i1		= (36 * stroke) + (stroke * 2);
		float	i2		= (20 * stroke) + stroke;				// make the arrow head the same size regardless of the length length
		Path2D	tmpPoly	= new Path2D.Double();
		tmpPoly.moveTo(x, y); // arrow tip
		tmpPoly.lineTo(x + xCor(i1, aDir + .5) / 2, y + yCor(i1, aDir + .5) / 2);
		tmpPoly.lineTo(x + xCor(i2, aDir) / 2, y + yCor(i2, aDir) / 2);
		tmpPoly.lineTo(x + xCor(i1, aDir - .5) / 2, y + yCor(i1, aDir - .5) / 2);
		tmpPoly.lineTo(x, y); // arrow tip
		g2d.fill(at.createTransformedShape(tmpPoly));

	}

	protected void drawMoveCursor(Graphics2D g, int x, int y)
	{

		Point2D	src	= new Point2D.Double(x, y);
		Point2D	dst	= new Point2D.Double(x, y);
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
		Line2D	l	= new Line2D.Double(x, y, x + w, y + h);
		Shape	l2	= at.createTransformedShape(l);
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

	public synchronized double getScrollY()
	{
		return ScrollY;
	}

	public synchronized void setScrollY(double scrollY)
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

	public void save(File file)
	{
		savePICTURE(file, "PNG");
	}

	public void savePICTURE(File file, String EXTENSIONformat)
	{
		BufferedImage	tamponSauvegarde	= new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics		g					= tamponSauvegarde.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		this.paint(g);
		try
		{
			ImageIO.write(tamponSauvegarde, EXTENSIONformat, file);
		} catch (IOException e)
		{
			e.printStackTrace();
			DialogDivers.Show_dialog(e, "Error exporting PNG file "+file.getName());
		}
	}
	
	
	public void savePDF(File file)
	{
		/*
		Document	document	= new Document(new com.lowagie.text.Rectangle(0, 0, getWidth(), getHeight()));
		PdfWriter	writer;
		try
		{
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			PdfContentByte	cb	= writer.getDirectContent();
			PdfTemplate		tp	= cb.createTemplate(getWidth(), getHeight());
			Graphics2D		g2;
			g2 = tp.createGraphics(getWidth(), getHeight());
			//g2 = new PdfGraphics2D(tp, getWidth(), getHeight());
			//g2 = new PdfGraphics2D(cb, getWidth(), getHeight(), null, false, false, 1.0f);
			//PdfGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper, boolean onlyShapes, boolean convertImagesToJPEG, float quality) {
			this.print(g2);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
			document.close();
			//Desktop.getDesktop().open(new File("exported.pdf"));
		}
		catch (DocumentException | IOException e1)
		{
			e1.printStackTrace();
			DialogDivers.Show_dialog(e1, "Error exporting PDF file "+file.getName());
		}
		*/
	}
	
	
	public void saveSVG(File file) 
	{
		/*
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";

		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(domImpl.createDocument(svgNS, "svg", null));

		// Ask the test to render into the SVG Graphics2D implementation.
		this.paint(svgGenerator);

		// Finally, stream out SVG to the standard output using
		// UTF-8 encoding.
		boolean	useCSS	= true;	// we want to use CSS style attributes
		try
		{
			Writer	out;
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			svgGenerator.stream(out, useCSS);
		} catch (SVGGraphics2DIOException | UnsupportedEncodingException | FileNotFoundException e)
		{
			e.printStackTrace();
			DialogDivers.Show_dialog(e, "Error exporting SVG file "+file.getName());
		}
		*/

	}

	/**
	 * Transforme la coordonée en bonne coordonnée (avec zoom et translation)
	 * @param X
	 * @param Y
	 * @return
	 */
	public Point2D convertViewXYtoRealXY(double X, double Y)
	{
		double	x	= X;
		double	y	= Y;

		// x = (x-((LockScrollX==false)?0:getWidth()/2))/Zoom-((LockScrollX==false)?0:ScrollX);
		// y = (y-((LockScrollX==false)?0:getWidth()/2))/Zoom-((LockScrollY==false)?0:ScrollY);

		x = (x - getWidth() / 2) / Zoom - ScrollX;

		if (LockScrollX == true)
			x = (X) / Zoom;

		y = (y - getHeight() / 2) / Zoom - ScrollY;

		if (LockScrollY == true)
			y = (Y) / Zoom;

		if (invertYAxis == true)
			y = -y;
		if (invertXAxis == true)
			x = -x;

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

	

	/**
	 * Transforme la coordonée en bonne coordonnée (avec zoom et translation)
	 * @param X
	 * @param Y
	 * @return
	 */

	public Point2D convertRealXYToViewXY(double X, double Y)
	{
		double	x	= X;
		double	y	= Y;

		// Facteur pour mettre le dessin centr� 
		x += this.ScrollX;
		y += this.ScrollY;

		x *= this.Zoom;
		y *= this.Zoom;

		if (invertYAxis == true)
			y = -y;
		if (invertXAxis == true)
			x = -x;

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

		double	x	= rect.getX();
		double	y	= rect.getY();
		double	w	= rect.getWidth();
		double	h	= rect.getHeight();

		g.setColor(Color.BLACK);
		g.setStroke(drawingStroke_Boundarybox);

		offset /= at.getScaleX();
		offset += 1;

		Rectangle2D	l	= new Rectangle2D.Double(x - offset, y - offset, (int) (w - x) + offset * 2, (int) (h - y) + offset * 2);
		Shape		lM	= at.createTransformedShape(l);

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
		int	X	= (int) ScrollX;
		int	Y	= (int) ScrollY;

		if (invertYAxis == true)
			Y = Y * -1;
		if (invertXAxis == true)
			X = X * -1;

		int		ww	= this.getWidth();
		int		hh	= this.getHeight();
		Point2D	p	= this.convertViewXYtoRealXY(ww, hh);
		Point2D	p2	= this.convertViewXYtoRealXY(X, Y);

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
				double	distance	= (i * i) + (j * j);
				float	alpha		= opacity;
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

		int		ss	= 8;
		Polygon	p2	= new Polygon();
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
		double	radians	= (Math.atan2(pt2.getY() - pt1.getY(), pt2.getX() - pt1.getX()) * 180) / Math.PI;
		double	degrees	= Math.floor(radians);
		double	dd		= (degrees % 360 + 360) % 360;
		dd = 360 - dd;
		if (dd == 360)
			dd = 0;
		return dd;
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y)
	{
		return drawString(g, text, x, y, AlignTexteX.LEFT, AlignTexteY.TOP, false);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, AlignTexteX alignX, AlignTexteY alignY)
	{
		return drawString(g, text, x, y, alignX, alignY, false);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, x, y, 0, alignX, alignY, txtCantChangeSize);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @return Shape of the text (a rectangle with rotation or not)
	 */

	protected Shape drawString(Graphics2D g, String text, double x, double y, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, (float) x, (float) y, 0, alignX, alignY, txtCantChangeSize);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @return Shape of the text (a rectangle with rotation or not)
	 */

	protected Shape drawString(Graphics2D g, String text, int x, int y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, (float) x, (float) y, ang, alignX, alignY, txtCantChangeSize);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @return Shape of the text (a rectangle with rotation or not)
	 */

	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize)
	{
		return drawString(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, 1.0f);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @param Size
	 *            size of the text
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size)
	{
		return drawString(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, false);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @param Size
	 *            size of the text
	 * @param drawBackGround
	 *            Draw a rectangle below text
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround)
	{
		return drawString(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, drawBackGround, Color.white);
	}

	/**
	 * Draw text using glyphvector
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @param Size
	 *            size of the text
	 * @param drawBackGround
	 *            Draw a rectangle below text
	 * @param backGroundColor
	 *            Color for the drawBackground
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		return drawString(g, text, x, y, ang, alignX, alignY, txtCantChangeSize, Size, drawBackGround, backGroundColor, true);
	}

	/**
	 * Draw a text
	 * 
	 * @param g
	 *            Graphics2D
	 * @param text
	 *            the text itself
	 * @param x
	 *            x position of the text
	 * @param y
	 *            y position of the text
	 * @param ang
	 *            angle of the text
	 * @param alignX
	 *            Alignment X for the text
	 * @param alignY
	 *            Alignment Y for the text
	 * @param txtCantChangeSize
	 *            Text can have fixed size when zooming or not
	 * @param Size
	 *            size of the text
	 * @param drawBackGround
	 *            Draw a rectangle below text
	 * @param backGroundColor
	 *            Color for the drawBackground
	 * @param useGlyph
	 *            Use Glyph method for better text with at and rotation
	 * @return Shape of the text (a rectangle with rotation or not)
	 */
	protected Shape drawString(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor, boolean useGlyph)
	{
		float	sx	= (float) Zoom;
		float	sy	= (float) Zoom;

		if (txtCantChangeSize == true)
		{
			sx = 1.0f / Size;
			sy = 1.0f / Size;
		}

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
			sx = -sx;
			sy = -sy;
		}
		;

		float recenterx = 0, recentery = 0;
		if ((alignX != AlignTexteX.LEFT) || (alignY != AlignTexteY.BOTTOM))
		{
			float txt_with = g.getFontMetrics().stringWidth(text);
			if (alignX == AlignTexteX.CENTER)
				recenterx = +(txt_with / 2f / sx);
			if (alignX == AlignTexteX.RIGHT)
				recenterx = +(txt_with / sx);
			if (alignY == AlignTexteY.CENTER)
				recentery = -(g.getFontMetrics().getFont().getSize() / 3f / sy);
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
		rect_texte = at3.createTransformedShape(rect_texte);//.getBounds2D();

		if (drawBackGround == true)
		{
			Rectangle2D	rect_texteM	= g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D	rect_texte2	= (Rectangle2D) rect_texteM.clone();

			float	elargX	= 4;
			float	elargY	= 2;
			/*
			 * if (txtCantChangeSize == true) { elargX = 3; elargY = 1; }
			 */

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
		Color colorCurrent = g.getColor();
		g.setColor(colorCurrent);
		FontRenderContext	frc		= g.getFontRenderContext();
		GlyphVector			gv		= g.getFont().createGlyphVector(frc, text);
		int					length	= gv.getNumGlyphs();

		// Methode rapide mais qui ne permet pas de faire certains truc que l'autre bazard du dessous fait.
		if (useGlyph == false)
		{
			AffineTransform oldat = (AffineTransform) g.getTransform().clone();
			g.setTransform(at2);
			g.drawString(text, 0, 0);
			g.setTransform(oldat);
		} else
		{
			// Ca a l'air mieux que le truc au dessus, mais y'a toujours un cas qui marche pas (Porte Busque, vue du dessus, les fleches rouges du bateau.
			frc = g.getFontRenderContext();
			gv = g.getFont().createGlyphVector(frc, text);
			length = gv.getNumGlyphs();
			for (int i = 0; i < length; i++)
			{
				Point2D p = gv.getGlyphPosition(i);
				// JE pense que c'est inutile
				AffineTransform at5 = AffineTransform.getTranslateInstance(0, 0);
				at5.preConcatenate(at2);
				//at.rotate((double) i / (double) (length - 1) * Math.PI / 3);
				Shape	glyph				= gv.getGlyphOutline(i);
				Shape	transformedGlyph	= at2.createTransformedShape(glyph);
				g.fill(transformedGlyph);
			}

		}

		return rect_texte;
	}

	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param ang
	 * @param centered
	 *            centre le texte au millieu du texte (pour les rotation aussi).
	 * @param txtCantChangeSize
	 *            Le texte va-t-il avoir sa taille modifier par le facteur de
	 *            zoom ?
	 * @return
	 */
	protected Shape drawString_AFTER_OLD(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		// TODO : Gerer le SCALE 
		AffineTransform oldat = (AffineTransform) g.getTransform().clone();

		// at.getScaleX() fait chier quand on rotate AT
		float	sx	= (float) at.getScaleX();
		float	sy	= (float) at.getScaleY();

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
			Rectangle2D	rect_texteM	= g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D	rect_texte2	= (Rectangle2D) rect_texteM.clone();

			float	elargX	= 4;
			float	elargY	= 2;

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
					at4.scale(1 / sx * Zoom, 1 / sy * Zoom);

					//angle = 45;
					at4.rotate(Math.toRadians(ang));

				}
				;
				if ((invertXAxis == false) && (invertYAxis == true))
				{
					//	sy = -sy;

					at4.scale(1 / sx * Zoom, 1 / -sy * Zoom);

					at4.rotate(Math.toRadians(ang));

				}
				;
				if ((invertXAxis == true) && (invertYAxis == false))
				{
					//	sx = -sx;

					at4.scale(1 / -sx * Zoom, 1 / sy * Zoom);

					at4.rotate(Math.toRadians(ang));
				}
				;
				if ((invertXAxis == true) && (invertYAxis == true))
				{
					//	ang += 180;

					at4.scale(1 / sx * Zoom, 1 / sy * Zoom);
					// AJOUT LE 28-09-2021 pour un bug si on fait une rotation de AT
					at4.rotate(Math.toRadians(ang + 180));
				}
				;

			}

			//	AffineTransform att = AffineTransform.getRotateInstance(Math.toRadians(45));

			//	System.err.println("Ang = "+(extractAngle(at)));

			gv.setGlyphTransform(0, at2);


		
			for (int i = 1; i < length; i++) {


				gv.setGlyphTransform(i, at4);
			}
			g.drawGlyphVector(gv, 0, 0);

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
	 * @param centered
	 *            centre le texte au millieu du texte (pour les rotation aussi).
	 * @param txtCantChangeSize
	 *            Le texte va-t-il avoir sa taille modifier par le facteur de
	 *            zoom ?
	 * @return
	 */
	protected Shape drawStringOLD(Graphics2D g, String text, float x, float y, float ang, AlignTexteX alignX, AlignTexteY alignY, boolean txtCantChangeSize, float Size, boolean drawBackGround, Color backGroundColor)
	{
		// TODO : Gerer le SCALE
		AffineTransform old = g.getTransform();

		//System.err.println("Focus : "+isFocusOwner());
		// Cette valeur de scale pourrait sembler bonne, mais si on fait une rotation de AT alors le scale est pas bon.
		float	sx	= (float) at.getScaleX();
		float	sy	= (float) at.getScaleY();

		sx = (float) Zoom;
		sy = (float) Zoom;

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
			Rectangle2D	rect_texteM	= g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D	rect_texte2	= (Rectangle2D) rect_texteM.clone();

			float	elargX	= 4;
			float	elargY	= 2;

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

		/*
		 * Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		 * rect_texte = at2.createTransformedShape(rect_texte).getBounds2D();
		 * try { rect_texte =
		 * at.createInverse().createTransformedShape(rect_texte); } catch
		 * (NoninvertibleTransformException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		g.setTransform(old);

		/*
		 * Point2D pL= new Point2D.Float(); at.transform(new Point2D.Float(x,
		 * y),pL); g.drawString(text, (int)pL.getX(), (int)pL.getY());
		 */
		return rect_texte;
	}

	/**
	 * Déconne si on rotate AT
	 * 
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
		float	sx	= (float) at.getScaleX();
		float	sy	= (float) at.getScaleY();

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
			Rectangle2D	rect_texteM	= g.getFontMetrics().getStringBounds(text, g);
			Rectangle2D	rect_texte2	= (Rectangle2D) rect_texteM.clone();

			float	elargX	= 4;
			float	elargY	= 2;

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

		/*
		 * Shape rect_texte = g.getFontMetrics().getStringBounds(text, g);
		 * rect_texte = at2.createTransformedShape(rect_texte).getBounds2D();
		 * try { rect_texte =
		 * at.createInverse().createTransformedShape(rect_texte); } catch
		 * (NoninvertibleTransformException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
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
		return drawArrow2(g, pt1, pt2, scaleArrow1, scaleArrow2, enableArrowPt1, enableArrowPt2, false);
	}

	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow)
	{
		return drawArrow2(g, pt1, pt2, scaleArrow1, scaleArrow2, enableArrowPt1, enableArrowPt2, enableMiddleArrow, 1);
	}

	protected Shape drawArrow2(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow, float scale)
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

		Shape	trshape	= at.createTransformedShape(shape);
		Shape	retour	= shape;
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
			at2.translate((pt2.getX() + pt1.getX()) / 2, (pt2.getY() + pt1.getY()) / 2);
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
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offsetX
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetY
	 *            Offset of the arrow on his Y axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 */
	protected Shape drawArrowWithStringOLD(Graphics2D g, String string, float angleBeta, float X1, float Y1, float X2, float Y2, float offsetX, float offsetY, float offsetTexte)
	{
		return drawArrowWithStringOLD(g, string, angleBeta, X1, Y1, X2, Y2, offsetX, offsetY, offsetTexte, 0.1f);
	}

	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offsetX
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetY
	 *            Offset of the arrow on his Y axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 */
	protected Shape drawArrowWithStringOLD(Graphics2D g, String string, double angleBeta, double X1, double Y1, double X2, double Y2, double offsetX, double offsetY, double offsetTexte)
	{
		return drawArrowWithStringOLD(g, string, angleBeta, X1, Y1, X2, Y2, offsetX, offsetY, offsetTexte, 0.1f);
	}

	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offsetX
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetY
	 *            Offset of the arrow on his Y axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 * @param arrowSize
	 *            size of the arrow for the display (default 0.1)
	 */
	protected Shape drawArrowWithStringOLD(Graphics2D g, String string, double angleBeta, double X1, double Y1, double X2, double Y2, double offsetX, double offsetY, double offsetTexte, double arrowSize)
	{
		return drawArrowWithStringOLD(g, string, (float) angleBeta, (float) X1, (float) Y1, (float) X2, (float) Y2, (float) offsetX, (float) offsetY, (float) offsetTexte, (float) arrowSize);
	}

	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offsetX
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetY
	 *            Offset of the arrow on his Y axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 * @param arrowSize
	 *            size of the arrow for the display (default 0.1)
	 */
	protected Shape drawArrowWithStringOLD(Graphics2D g, String string, float angleBeta, float X1, float Y1, float X2, float Y2, float offsetX, float offsetY, float offsetTexte, float arrowSize)
	{
		// TODO : There's obviously an error in offsetX and OffsetY if the arrow is not Horizontal or Vertical
	
		
		// TODO : I'm not sure offsetX and offsetY shouldn't be one and only one variable
		// TODO : put that in graphicsbase
		Vector2D v = getPerpendicularPoint(new Vector2D(X1, Y1), new Vector2D(X2, Y2), offsetTexte);
		//drawStringOLD(g, string, offsetX+ (float)v.getX(),offsetY+(float)v.getY(),  angleBeta, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f);
		// TODO : Bug avec windows si scale a 150%
		//drawStringOLD(g, string, offsetX+ (float)v.getX(),offsetY+(float)v.getY(), angleBeta, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, false, Color.black);
		//drawString44(g, string, offsetX+ (float)v.getX(),offsetY+(float)v.getY(), angleBeta, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, false, Color.black);
		
		
		Shape	shp_str		= drawString(g, string, offsetX + (float) v.getX(), offsetY + (float) v.getY(), angleBeta, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, false, Color.black);
		Shape	shp_arrow	= drawArrow2(g, new Point2D.Double(X1 + offsetX, Y1 + offsetY), new Point2D.Double(X2 + offsetX, Y2 + offsetY), true, true, false, 0.1f);
	
		
		return shp_str;

	}
	
	
	
	
	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offset
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 */
	protected Shape drawArrowWithString(Graphics2D g, String string, float angleBeta, float X1, float Y1, float X2, float Y2, float offset, float offsetTexte)
	{
		return drawArrowWithString(g, string, angleBeta, X1, Y1, X2, Y2, offset, offsetTexte, 0.1f);
	}
	
	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offset
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 */
	protected Shape drawArrowWithString(Graphics2D g, String string, double angleBeta, double X1, double Y1, double X2, double Y2, double offset, double offsetTexte)
	{
		return drawArrowWithString(g, string, angleBeta, X1, Y1, X2, Y2, offset, offsetTexte, 0.2f);
	}
	
	
	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offset
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 * @param arrowSize
	 *            size of the arrow for the display (default 0.2)
	 */
	protected Shape drawArrowWithString(Graphics2D g, String string, float angleBeta, float X1, float Y1, float X2, float Y2, float offset, float offsetTexte, float arrowSize)
	{
		return drawArrowWithString(g, string, (double)angleBeta, (double)X1, (double)Y1, (double)X2, (double)Y2, (double)offset, (double)offsetTexte, (double)arrowSize);
	}
	
	
	
	/**
	 * Fast and easy way to draw an arrow with a text in his middle
	 * 
	 * @param g
	 * @param string
	 *            String to display in middle of the arrow
	 * @param angleBeta
	 *            Angle of the string to display
	 * @param X1
	 *            First point of arrow X position
	 * @param Y1
	 *            First point of arrow Y position
	 * @param X2
	 *            Second point of arrow X position
	 * @param Y2
	 *            Second point of arrow X position
	 * @param offset
	 *            Offset of the arrow on his X axis (default 0) - Permet de
	 *            decaller la fleches pour montrer un objet reel en la décallant
	 *            pour ne pas avoir une superposition
	 * @param offsetTexte
	 *            Offset of the text (0 means text is in the middle of the
	 *            arrow)
	 * @param arrowSize
	 *            size of the arrow for the display (default 0.2)
	 */
	protected Shape drawArrowWithString(Graphics2D g, String string, double angleBeta, double X1, double Y1, double X2, double Y2, double offset, double offsetTexte, double arrowSize)
	{
		Line2D.Double paralelle = createLineParallel(new Line2D.Double(X1, Y1, X2, Y2), -offset);
		Vector2D v = getPerpendicularPoint(new Vector2D(paralelle.x1, paralelle.y1), new Vector2D(paralelle.x2, paralelle.y2), (float) offsetTexte);
		Shape	shp_str		= drawString(g, string, (float) v.getX(),  (float) v.getY(), (float)angleBeta, AlignTexteX.CENTER, AlignTexteY.CENTER, false, 1.0f, false, Color.black);
		Shape	shp_arrow	= drawArrow2(g, new Point2D.Double(paralelle.x1, paralelle.y1), new Point2D.Double(paralelle.x2,  paralelle.y2), true, true, false, (float)arrowSize);
		return shp_str;
	}
	

	/**
	 * Permet de dessiner un axe X Y en bas a gauche de la fenetre
	 * 
	 * @param g
	 * @param arrowLen
	 * @param XaxisLbl
	 * @param YaxisLbl
	 */
	protected void drawAxisInLocalView(Graphics2D g, float arrowLen, String XaxisLbl, String YaxisLbl, float arrowsize)
	{
		drawArrowLocal(g, new Point2D.Double(10, getHeight() - 20), new Point2D.Double(10 + arrowLen, getHeight() - 20), arrowsize, arrowsize, false, true, false, 20.0f);
		g.drawString(XaxisLbl, arrowLen - 10, getHeight() - 25);

		drawArrowLocal(g, new Point2D.Double(10, getHeight() - 20), new Point2D.Double(10, getHeight() - 20 - 10 - arrowLen), arrowsize, arrowsize, false, true, false, 20.0f);
		g.drawString(YaxisLbl, 20, getHeight() - 15 - arrowLen);
	}

	/**
	 * Permet de dessiner un fleche sans transformation de coordonée
	 * 
	 * @param g
	 * @param pt1
	 * @param pt2
	 * @param scaleArrow1
	 * @param scaleArrow2
	 * @param enableArrowPt1
	 * @param enableArrowPt2
	 * @param enableMiddleArrow
	 * @param scale
	 */
	private void drawArrowLocal(Graphics2D g, Point2D pt1, Point2D pt2, float scaleArrow1, float scaleArrow2, boolean enableArrowPt1, boolean enableArrowPt2, boolean enableMiddleArrow, float scale)
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

		g.draw(shape);

		AffineTransform at2;

		Color old = g.getColor();

		if (enableArrowPt1 == true)
		{
			at2 = new AffineTransform();
			at2.translate(pt1.getX(), pt1.getY());
			at2.scale(scaleArrow1, scaleArrow1);
			at2.rotate(Math.toRadians(-angle1 - 90));
			at2.translate(0, scale);

			Shape trshape = at2.createTransformedShape(tri);
			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		if (enableArrowPt2 == true)
		{
			at2 = new AffineTransform();
			at2.translate(pt2.getX(), pt2.getY());
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
			at2.translate(0, scale);
			Shape trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		if (enableMiddleArrow == true)
		{
			at2 = new AffineTransform();
			at2.translate((pt2.getX() + pt1.getX()) / 2, (pt2.getY() + pt1.getY()) / 2);
			at2.scale(scaleArrow2, scaleArrow2);
			at2.rotate(Math.toRadians(-angle1 + 90));
			//	at2.translate(0, scale);
			Shape trshape = at2.createTransformedShape(tri);

			g.fill(trshape);
			g.setColor(g.getColor().darker());
			g.draw(trshape);
			g.setColor(old);
		}

		g.setColor(old);

		return;
	}

	/**
	 * @param color_tache
	 * @return
	 */
	protected Color invertColorValues(Color color_tache)
	{
		Color textColor = new Color(255 - color_tache.getRed(), 255 - color_tache.getGreen(), 255 - color_tache.getBlue(), color_tache.getAlpha());
		return textColor;
	}

	/**
	 * @param color_tache
	 * @return
	 */
	protected Color invertColorRGB(Color color_tache)
	{
		Color textColor = new Color(255 - color_tache.getBlue(), 255 - color_tache.getGreen(), 255 - color_tache.getRed(), color_tache.getAlpha());
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

		BufferedImage			convertedImage	= null;
		GraphicsEnvironment		ge				= GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice			gd				= ge.getDefaultScreenDevice();
		GraphicsConfiguration	gc				= gd.getDefaultConfiguration();
		convertedImage = gc.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
		Graphics2D g2d = convertedImage.createGraphics();
		g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g2d.dispose();
		return convertedImage;
	}

	/**
	 * @param g
	 * @param img_ship2
	 * @param x
	 * @param y
	 * @param angle
	 * @param scaleMultiplier
	 * @return
	 */
	public Shape drawImage(Graphics2D g, BufferedImage img_ship2, float x, float y, float angle, float scaleMultiplier, boolean zoomIndependantScale)
	{
		return drawImage(g, img_ship2, x, y, img_ship2.getWidth(), img_ship2.getHeight(), angle, scaleMultiplier, zoomIndependantScale);
	}

	/**
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
		float	img_w	= img_ship2.getWidth();
		float	img_h	= img_ship2.getHeight();

		float	sX	= w / img_w;
		float	sY	= h / img_h;

		at2.translate(x, y);
		if (zoomIndependantScale)
		{
			// Zoom independant scale
			at2.scale(1.0 / Zoom, 1.0 / Zoom);
		}
		at2.scale(sX, sY);
		at2.scale(scaleMultiplier, scaleMultiplier);
		at2.rotate(Math.toRadians(angle));
		at2.translate(-img_w / 2, -img_h / 2);

		// Bordure
		Rectangle2D	rect	= new Rectangle2D.Double(0, 0, w / sX, h / sY);
		Shape		shp		= at2.createTransformedShape(rect);

		g.drawImage(img_ship2, at2, this);
		return shp;
	}

	/**
	 * Deplace la ligne parallement de offsetPixels (positif ou negatif).
	 * 
	 * @param line
	 * @param offsetPixels
	 * @return
	 */
	public Line2D.Double createLineParallel(Line2D.Double line, double offsetPixels)
	{
		return View2D_Utils.createLineParallel(line, offsetPixels);
	}

	/**
	 * Supprime une distance a un segment (a gauche et a droite).
	 * 
	 * @param line
	 * @param longueurarabotter
	 * @return
	 */
	public Line2D.Double createLineLength(Line2D.Double line, double longueurarabotter)
	{
		return View2D_Utils.createLineLength(line, longueurarabotter);
	}
	
	public Line2D.Double createLineLength(Line2D.Double line, double longueurarabotterGauche, double longueurarabotterDroite)
	{
		return View2D_Utils.createLineLength(line, longueurarabotterGauche, longueurarabotterDroite);
	}

	
	

	

	/**
	 * - Vérifie que les 2 segments ne sont pas cote a cote. (ou l'un touchant l'autre). 
	 * - Vérifie si la pente est la meme. (donc 2 droite parallelle).
	 * - Vérifie ensuite si un des 2 points de l'autre segments est contenu dans le premier
	 * 
	 * @param XA1 point 1 ligne A
	 * @param YA1
	 * @param XB1 point 2 ligne A
	 * @param YB1
	 * @param XA2 point 1 ligne B
	 * @param YA2
	 * @param XB2 point 2 ligne B
	 * @param YB2
	 * @return
	 */
	public boolean isSegmentNeighboor(double XA1, double YA1, double XB1, double YB1, double XA2, double YA2, double XB2, double YB2)
	{
		return View2D_Utils.isSegmentNeighboor(XA1, YA1, XB1, YB1, XA2, YA2, XB2, YB2);

	}
	
	public double [] intersectionDroites(Line2D l1, Line2D l2)
	{
		return View2D_Utils.intersectionDroites(l1, l2);
	}

	/**
	 * Renvoye le point d'intersection entre 2 droites ou NULL s'il n'y en a pas.
	 * @param X1
	 * @param Y1
	 * @param X2
	 * @param Y2
	 * @param X3
	 * @param Y3
	 * @param X4
	 * @param Y4
	 * @return
	 */
	public double [] intersectionDroitesIncluse(double X1, double Y1, double X2, double Y2, double X3, double Y3, double X4, double Y4)
	{
		return View2D_Utils.intersectionDroitesIncluse(X1, Y1, X2, Y2, X3, Y3, X4, Y4);
	}
	
	/**
	 * Test l'intersection exclusive entre 2 droites. 
	 * @param XA1
	 * @param YA1
	 * @param XB1
	 * @param YB1
	 * @param XA2
	 * @param YA2
	 * @param XB2
	 * @param YB2
	 * @return
	 */
	public boolean isIntersectExclusif(double XA1, double YA1, double XB1, double YB1, double XA2, double YA2, double XB2, double YB2)
	{
		return View2D_Utils.isIntersectExclusif(XA1, YA1, XB1, YB1, XA2, YA2, XB2, YB2);

	}

	

	public boolean testIntersectionNonIncluse(double L1X, double L1Y, double L2X, double L2Y,
			double L1Xb, double L1Yb, double L2Xb, double L2Yb)
	{
		return View2D_Utils.testIntersectionNonIncluse(L1X, L1Y, L2X, L2Y, L1Xb, L1Yb, L2Xb, L2Yb);
	}
	
	/**
	 * Verifie que 2 Segments intersectionnent � l'interieur (cad pas au extremit� des segments);
	 * Il faut qu'ils se touchent vraiment, pas qu'ils soient juste coll� l'un � l'autre.
	 *  
	 * @param line1
	 * @param line2
	 * @return
	 */
	public boolean testIntersectionNonIncluse(Line2D line1, Line2D line2)
	{
		return View2D_Utils.testIntersectionNonIncluse(line1, line2);
	}

	/**
	 * Retourne la longueur de chevauchement de 2 lignes (en prenant soin de verifier avant que ca se chevauche bien ...)
	 * @param Xa1
	 * @param Ya1
	 * @param Xb1
	 * @param Yb1
	 * @param Xa2
	 * @param Ya2
	 * @param Xb2
	 * @param Yb2
	 * @return
	 */
	public double getOverlapLength(double Xa1, double Ya1, double Xb1, double Yb1, double Xa2, double Ya2, double Xb2, double Yb2)
	{
		return View2D_Utils.getOverlapLength(Xa1, Ya1, Xb1, Yb1, Xa2, Ya2, Xb2, Yb2);
	}

	public boolean LineContainsPointIncluse(Line2D line1, Point2D p1)
	{
		return View2D_Utils.LineContainsPointIncluse(line1, p1);
	}
	
	/**
	 * METHODS TO CALCULATE THE AREA AND CENTROID OF A POLYGON INSERT THEM INTO
	 * THE CORRESPONDING CLASS
	 **/
	public double SignedPolygonArea(ArrayList<Point> polygon)
	{
		return View2D_Utils.SignedPolygonArea(polygon);
	}

	/* CENTROID */
	public Point2D PolygonCenterOfMass(ArrayList<Point> polygon, int N)
	{
		return View2D_Utils.PolygonCenterOfMass(polygon, N);
	}
	
	
	
	
	
	/**
	 * Compute a point placed perpendicularly at the middle of the segment
	 * composed of A and B, with an offset.
	 * 
	 * @param A
	 *            Point numero 1
	 * @param B
	 *            Point numero 2
	 * @param distance
	 *            distance from the segment
	 * @return
	 */
	protected Vector2D getPerpendicularPoint(Vector2D A, Vector2D B, float distance)
	{
		Vector2D	M			= A.add(B).scalarMultiply(0.5);
		Vector2D	p			= A.subtract(B);
		Vector2D	n			= new Vector2D(-p.getY(), p.getX());
		// TODO : It was an int before ... but why ????
		double			norm_length	=  Math.sqrt((n.getX() * n.getX()) + (n.getY() * n.getY()));
		n = new Vector2D(n.getX() / norm_length, n.getY() / norm_length);
		return (M.add(n.scalarMultiply(distance)));
	}

	/**
	 * Give middle point
	 * 
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static Point2D interpolate(Point2D pt1, Point2D pt2)
	{
		return new Point2D.Double((pt1.getX() + pt2.getX()) / 2, (pt1.getY() + pt2.getY()) / 2);
	}

	public static Point2D interpolate(double X1, double Y1, double X2, double Y2)
	{
		return new Point2D.Double((X1 + X2) / 2, (Y1 + Y2) / 2);
	}

	public static Point2D interpolate(Line2D line)
	{
		return interpolate(line.getP1(), line.getP2());
	}

	public static Point2D interpolate(Line2D line, double delta)
	{
		return interpolate(line.getP1(), line.getP2(), delta);
	}

	public static Point2D interpolate(Point2D pt1, Point2D pt2, double delta)
	{
		double	x	= pt1.getX() * (1.0 - delta) + pt2.getX() * (delta);
		double	y	= pt1.getY() * (1.0 - delta) + pt2.getY() * (delta);

		return new Point2D.Double(x, y);
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

	public synchronized boolean isInvertXAxis()
	{
		return invertXAxis;
	}

	public synchronized void setInvertXAxis(boolean invertXAxis)
	{
		this.invertXAxis = invertXAxis;
	}

	public synchronized boolean isInvertYAxis()
	{
		return invertYAxis;
	}

	public synchronized void setInvertYAxis(boolean invertYAxis)
	{
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
	 * Gestion des Nurbs Path
	 **************************/

	/**
	 * @param g
	 * @param curve1
	 * @param drawControlNode
	 */
	public List<Shape> drawNurbsCurve(Graphics2D g, NurbsCurve curve1, float Thickness, Stroke stroke, Color color)
	{
		Shape	shpCurve	= at.createTransformedShape(curve1.getShape());
		Stroke	oldStroke	= g.getStroke();
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

		retour_list = curve1.getCollisionShapeList(Thickness);
		/*
		 * for (int j = 0; j < curve1.getLines_rectangles().size(); j++) {
		 * GeneralPath l = curve1.getLines_rectangles().get(j);
		 * retour_list.add(l); //longueur+=curve1.getLength(); }
		 */

		return retour_list;
	}

	protected List<SelectionTuple<Shape, Object>> drawNurbsControlPoints(Graphics2D g, NurbsCurve curve1, Color colorIn, Color colorOut, Color dashedLineColor)
	{
		float		dash1[]	= { 10.0f };
		BasicStroke	dashed	= new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		return drawNurbsControlPoints(g, curve1, colorIn, colorOut, dashedLineColor, dashed);
	}

	protected List<SelectionTuple<Shape, Object>> drawNurbsControlPoints(Graphics2D g, NurbsCurve curve1, Color colorIn, Color colorOut, Color dashedLineColor, Stroke dashLineStroke)
	{
		List<SelectionTuple<Shape, Object>> retour_shapes = new ArrayList<>();

		if (dashLineStroke != null)
			g.setStroke(dashLineStroke);

		g.setColor(dashedLineColor);

		NurbsPoint	pt1	= curve1.getPt1();
		NurbsPoint	pt2	= curve1.getPt2();
		NurbsPoint	pt3	= curve1.getPt3();
		NurbsPoint	pt4	= curve1.getPt4();

		Line2D.Double	l1	= new Line2D.Double(pt2.getX(), pt2.getY(), pt1.getX(), pt1.getY());
		Line2D.Double	l2	= new Line2D.Double(pt3.getX(), pt3.getY(), pt4.getX(), pt4.getY());

		g.draw(at.createTransformedShape(l1));
		g.draw(at.createTransformedShape(l2));
		g.setStroke(new BasicStroke(1.0f));

		retour_shapes.add(new SelectionTuple<Shape, Object>(drawNurbsControlPoint(g, pt2, colorIn, colorOut), pt2));
		retour_shapes.add(new SelectionTuple<Shape, Object>(drawNurbsControlPoint(g, pt3, colorIn, colorOut), pt3));

		return retour_shapes;
	}

	protected Shape drawNurbsPoint(Graphics2D g, NurbsPoint np, Color colorIn, Color colorOut)
	{
		double		scale	= 4.0;
		Ellipse2D	rect	= new Ellipse2D.Double(np.getX() - 0.5 * scale, np.getY() - 0.5 * scale, 1.0 * scale, 1.0 * scale);
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
	 * 
	 * @param g
	 * @param np
	 * @return
	 */
	protected Shape drawNurbsControlPoint(Graphics2D g, NurbsPoint np, Color colorIn, Color colorOut)
	{

		AffineTransform at1 = new AffineTransform();
		at1.translate(np.getX(), np.getY());

		double	scaleshpControlPoint	= 2.0;
		Shape	shpControlPoint			= new Ellipse2D.Double(-0.5 * scaleshpControlPoint, -0.5 * scaleshpControlPoint, 1.0 * scaleshpControlPoint, +1.0 * scaleshpControlPoint);

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
	 * TODO : C'est bien joli mais pas faciement customisable cot� client de la
	 * lib... Dessine un nurbsPath complet (affichage et gestion de la selection
	 * comprise).
	 * 
	 * @param g
	 * @param DrawCurve
	 * @param DrawControlNode
	 * @param DrawNode
	 * @param resizeableWithZoom
	 */
	public void drawNurbsPath(Graphics2D g, NurbsPath npath, boolean DrawCurve, boolean DrawControlNode, boolean DrawNode, boolean resizeableWithZoom)
	{

		// TODO : Gestion de resizeableWithZoom
		float Zoom_ = (float) Zoom;
		// TODO : Probleme de detection de thickness de ligne, ca risque de bouffer le cpu :(
		if (resizeableWithZoom == true)
			Zoom_ = 1f;

		// Test 3 : 3 eme methode.
		if (DrawCurve)
		{

			float		dash1[]		= { 2.5f * Zoom_ };
			BasicStroke	dashedIn	= new BasicStroke((0.5f * Zoom_), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, (2.5f), dash1, 0.0f);

			Color	colorOut	= Color.BLACK;
			Color	colorIn		= Color.WHITE;

			for (int i = 0; i < npath.getCurves().size(); i++)
			{
				NurbsCurve curve = npath.getCurves().get(i);

				float		thickness			= (curve.getThicknessDetection() * Zoom_);
				BasicStroke	dashedOut			= new BasicStroke((thickness), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
				List<Shape>	selection_shapes	= drawNurbsCurve(g, curve, curve.getThicknessDetection() / 2.0f, dashedOut, colorOut);
				addToSelectableObject(selection_shapes, curve);
				drawNurbsCurve(g, curve, thickness, dashedIn, colorIn);

			}
			/*
			 * for (int i = 0; i < npath.getCurves().size(); i++) { NurbsCurve
			 * curve = npath.getCurves().get(i); float thickness =
			 * (curve.getThicknessDetection() ); drawNurbsCurve(g, curve,
			 * thickness, dashedIn, colorIn); }
			 */
		}

		if (DrawControlNode)
			for (int i = 0; i < npath.getCurves().size(); i++)
			{
				NurbsCurve							curve	= npath.getCurves().get(i);
				List<SelectionTuple<Shape, Object>>	ret		= drawNurbsControlPoints(g, curve, Color.WHITE, Color.BLACK, Color.BLUE);
				addToSelectableObject(ret);
			}
		if (DrawNode == true)
		{
			List<NurbsPoint> list_points = npath.getPoints();
			for (Iterator<NurbsPoint> iterator = list_points.iterator(); iterator.hasNext();)
			{
				NurbsPoint nurbsPoint = iterator.next();
				addToSelectableObject(new SelectionTuple<Shape, Object>(drawNurbsPoint(g, nurbsPoint, Color.RED, Color.BLACK), nurbsPoint));
			}
		}
	}

	/**
	 * Clamp v pour qu'ils soit toujours entre 0 et 1
	 * 
	 * @param v
	 * @return
	 */
	double clamp(double v) //////
	{ //////
		double t = v < 0 ? 0 : v; //////
		return t > 1.0 ? 1.0 : t; //////

	} //////

	/**
	 * @param ratio
	 *            -1 to 1
	 */
	protected Color mapGrayScaleTORGB(float ratio)
	{
		//float Quantize = 8.0f;
		//ratio = ((int)(ratio * Quantize)) / Quantize; 

		double	red		= clamp(1.5 - Math.abs(2.0 * ratio - 1.0));	//////
		double	green	= clamp(1.5 - Math.abs(2.0 * ratio));		//////
		double	blue	= clamp(1.5 - Math.abs(2.0 * ratio + 1.0));	//////

		return new Color((int) (255 * red), (int) (255 * green), (int) (255 * blue));
	}

}
