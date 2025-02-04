package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.MarsCurve.ArcCurveMarsLike;

public abstract class PanelGraphiqueBaseBase extends PanelGraphiqueBase<Object> implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener
{
	/**
     *
     */
	private static final long	serialVersionUID		= -8780048115222887324L;

//	MainFrame					parent					= null;

	Image						cursorImage				= null;
	Image						cursorAddSegment		= null;
	Image						cursorAddSquaredRegion	= null;
	Image						cursorAddRegion			= null;
	Image						cursorAutoSelect		= null;
	Image						cursorBlockAuto			= null;
/*
	int MBUTTON_SELECTION_MASK = InputEvent.BUTTON1_MASK;
	int MBUTTON_SCROLL_MASK = InputEvent.BUTTON2_MASK;
	int MBUTTON_POPUP_MASK = InputEvent.BUTTON3_MASK;
	*/
	
	protected boolean MouseClicked=false;
	
	// NOTE WAX : Juin 2019 - Si on passe a BUTTON3_DOWN_MASK y'a un bug de selection (voire avec le testBed avec les 4 fenetres)
	protected int MBUTTON_SELECTION_MASK = MouseEvent.BUTTON1;//InputEvent.BUTTON3_MASK;
	protected int MBUTTON_SCROLL_MASK = MouseEvent.BUTTON2;//InputEvent.BUTTON2_MASK;
	protected int MBUTTON_POPUP_MASK = MouseEvent.BUTTON3;//444;//InputEvent.BUTTON4_MASK;
	
	protected boolean MBUTTON_SELECTION_MASK_ENABLED=false;
	protected boolean MBUTTON_SCROLL_MASK_ENABLED=false;
	protected boolean MBUTTON_POPUP_MASK_ENABLED=false;
	

	protected BufferedImage				AxisPicture				= null;

	protected double			rMouseX;
	protected double			rMouseY;

	//  ContxtPopupMenu popupMenu = null;
	protected JPopupMenu					popupMenu				= null;

	// Sert pour compter le temps et les FPS...
	protected long				fps_counter_start_render;
	protected long				fps_counter_stop_render;
	private long				fps_counter_sum_render_time;
	private int					fps_counter_cpt_render_time;

	protected Rectangle2D		selectionArea;

	boolean DrawFPSInfos = true;
	private final double				scrollFactorX			= 5.0;
	private final double				scrollFactorY			= -5.0;

	private boolean enableSelectionDrawDebug = false;
	
	// Permet de faire un zoom la ou le pointeur de la souris se trouver si variable = false sinon centre au millieu de l'"ecran".
    private boolean zoomOnCenterOrMousePointer = false;
    
	
	public PanelGraphiqueBaseBase(CurrentSelectionContext contxt)
	{
		this(contxt, null, null);
	}
	public PanelGraphiqueBaseBase(CurrentSelectionContext contxt, ConfigurationGeneral generalConfig, ConfigurationColor colorConfig)
	{
		super(contxt, generalConfig, colorConfig);

		//this.parent = parent2;
	//	this.manager = contxt.getManager();
		addMouseMotionListener(this);
		addMouseListener(this);
		setBackground(this.getColorConfig().getCOLOR_FOND_GRILLE().getColor());
		setFocusable(true);
		//  requestFocus(true);
		addKeyListener(this);
		setFocusable(true);
		addMouseWheelListener(this);
		requestFocus(true);

		cursorAutoSelect = new ImageIcon("data/images/Cursor/MyCursor_AutoSelect.png").getImage();
		cursorBlockAuto = new ImageIcon("data/images/Cursor/MyCursor_BlockAuto.png").getImage();
		cursorAddSegment = new ImageIcon("data/images/Cursor/MyCursor_AddSegment.png").getImage();
		cursorAddSquaredRegion = new ImageIcon("data/images/Cursor/MyCursor_AddRegionSquare.png").getImage();
		cursorAddRegion = new ImageIcon("data/images/Cursor/MyCursor_AddRegion.png").getImage();
		//     requestFocus(true);

		//  popupMenu = new ContxtPopupMenu(this, contxt);
		popupMenu = new PanelGraphiquePopupMenu(this);

		// Image image = new ImageIcon("data/images/textures/ConstraintViolation.png").getImage();
		// fBugImage = ImageUtilities.ImagetoBufferedImage(image);

	}
	
	public void reinit_fps_counter()
	{
		fps_counter_start_render = 0;
		fps_counter_stop_render = 0;
		fps_counter_sum_render_time = 0;
		fps_counter_cpt_render_time = 0;
	}

	/**
	 * Remise a 1 du zoom et scroll au centre.
	 */
	public void reinit_view()
	{
		ScrollX = 0;
		ScrollY = 0;
		Zoom = 10.0d;
		repaint();
		reinit_fps_counter();
	}
	
	/**
	 * Remise a 1 du zoom et scroll au centre.
	 */
	public void reset_viewport()
	{
		reinit_view();
	}

	
	public void mouseMoved(java.awt.event.MouseEvent e)
	{
		rMouseX = e.getX();
		rMouseY = e.getY();

		Point2D pt = convertViewXYtoRealXY(e.getX(), e.getY());

		MouseDX = MouseX - pt.getX();
		MouseDY = MouseY - pt.getY();

		MouseX = pt.getX();
		MouseY = pt.getY();

	}
	
	public void mouseEntered(MouseEvent e)
	{
		//requestFocus(true); // Comment� sinon les fenetres ouvertes disparaissent quand on revient dans ce panel
		MOUSEINSIDE = true;
		changeMouseCursor();
	}

	
	public void mouseExited(MouseEvent e)
	{
		MOUSEINSIDE = false;
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		//requestFocus(false); // Comment� sinon les fenetres ouvertes disparaissent quand on revient dans ce panel
	}

	public void mousePressed(java.awt.event.MouseEvent e)
	{
		this.requestFocus();
		SHIFT = e.isShiftDown();
		CTRL = e.isControlDown();
		MouseClicked=true;
		// NOTE WAX : Juin 2019 - Si on passe a BUTTON3_DOWN_MASK y'a un bug de selection (voire avec le testBed avec les 4 fenetres)
		//int modif = e.getModifiersEx();
		//if ((modif & MBUTTON_SELECTION_MASK) == MBUTTON_SELECTION_MASK)
		if (e.getButton() == MBUTTON_SELECTION_MASK)
		{
			// Si ni shift ni ctrl n'est mis, alors on vide la selection courante.
			if ((SHIFT == false) && (CTRL == false))
				if (contxt!=null)
				contxt.clear_selection(this);
			selectionArea = new Rectangle2D.Double(MouseX, MouseY, 0, 0);
			
			MBUTTON_SELECTION_MASK_ENABLED=true;
			
		}
		
		if (e.getButton() == MBUTTON_SCROLL_MASK)
			MBUTTON_SCROLL_MASK_ENABLED=true;
		if (e.getButton() == MBUTTON_POPUP_MASK)
			MBUTTON_POPUP_MASK_ENABLED=true;
		

	}

	
	
	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		MouseClicked=false;
		SHIFT = e.isShiftDown();
		CTRL = e.isControlDown();
		// NOTE WAX : Juin 2019 - Si on passe a BUTTON3_DOWN_MASK y'a un bug de selection (voire avec le testBed avec les 4 fenetres)
		int modif = e.getModifiersEx();

		/*int mod = e.getModifiersEx();
		if ((mod & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
		{
			System.err.println("Popup");
		}*/

		// TODO : Leger Hack.
		//if ((modif & MBUTTON_POPUP_MASK) == MBUTTON_POPUP_MASK)
		if (e.getButton() == MBUTTON_POPUP_MASK)
		{
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		displacement = null;
		
		
		if (selectionArea!=null)
		{
			ArrayList<Object> list_selection = RecupereElementsSousSelectionRectangle(CorrigeSelection(selectionArea));
			
			if (contxt!=null)
			contxt.addObjectsToSelection(list_selection, SHIFT, CTRL, this);
			selectionArea=null;
		}
		
		if (e.getButton() == MBUTTON_SELECTION_MASK)		
			MBUTTON_SELECTION_MASK_ENABLED=false;
		if (e.getButton() == MBUTTON_SCROLL_MASK)
			MBUTTON_SCROLL_MASK_ENABLED=false;
		if (e.getButton() == MBUTTON_POPUP_MASK)
			MBUTTON_POPUP_MASK_ENABLED=false;
		

	}
	
	protected ArrayList<Object> RecupereElementsSousSelectionRectangle(Rectangle2D selectionArea) {
		ArrayList<Object> list_selection = new ArrayList<Object>();
		// TODO : Checker tout les truc qui sont dans le rectangle de selection.
		if (selectionArea!=null)
		{
			int mode = 0;
			// Corrige un peu la selection area.
			if ((selectionArea.getWidth()==0) &&  (selectionArea.getHeight()==0))
				mode=1;
	
		for (int i = 0; i < getSelectableObject().size(); i++)
		{
			SelectionTuple<Shape, ?> SelectionTuple = getSelectableObject().get(i);
			Class<?> objClass = SelectionTuple.getUserObject().getClass();
			Shape shape = SelectionTuple.shape;
			if (contxt.isFiltred(objClass)==false)
			if ((mode==0) && (shape.intersects(selectionArea)) ||
			((mode==1) && (shape.contains(new Point2D.Double(selectionArea.getX(), selectionArea.getY())))))
			{
				list_selection.add(SelectionTuple.getUserObject());
			}
		}
		}
		return list_selection;
	}

	
	public void mouseDragged(java.awt.event.MouseEvent e)
	{

		SHIFT = e.isShiftDown();
		CTRL = e.isControlDown();

		rMouseX = e.getX();
		rMouseY = e.getY();

		
		Point2D pt = convertViewXYtoRealXY(e.getX(), e.getY());
		
		
		MouseDX = (MouseX - pt.getX());
		MouseDY = (MouseY - pt.getY());
		
		
		// NOTE WAX : Juin 2019 - Si on passe a BUTTON3_DOWN_MASK y'a un bug de selection (voire avec le testBed avec les 4 fenetres) -> le selectionArea n'était pas corrigé au release (corrigeselectionarea)
		int modif = e.getModifiersEx();

		if (MBUTTON_SCROLL_MASK_ENABLED)
		//if ((modif & MBUTTON_SCROLL_MASK) == MBUTTON_SCROLL_MASK)
		{
			if (LockScrollX == false)
				ScrollX -= MouseDX*((invertXAxis==true)?-1:1);
			if (LockScrollY == false)
				ScrollY -= MouseDY*((invertYAxis==true)?-1:1);

		}
		
		if (MBUTTON_SELECTION_MASK_ENABLED)
		//if ((modif & MBUTTON_SELECTION_MASK) == MBUTTON_SELECTION_MASK)
			if (selectionArea != null)
				selectionArea.setRect(selectionArea.getX(), selectionArea.getY(), MouseX - selectionArea.getX(), MouseY - selectionArea.getY());
			
		Point2D pt2 = convertViewXYtoRealXY(e.getX(), e.getY());
		
		
		MouseX = pt2.getX();
		MouseY = pt2.getY();
		
		

	}

	public void keyReleased(java.awt.event.KeyEvent e)
	{
		CTRL = e.isControlDown();
		SHIFT = e.isShiftDown();

		if (CTRL == true)
		{
	
		} else
		{
			if ((e.getKeyChar() == '+') || (e.getKeyChar() == '-'))
			{
				int dir = -1;
				if (e.getKeyChar() == '+')
					dir = 1;
				
				Zoom += dir;
				Zoom = View2D_Utils.constrains(Zoom, ZoomMin, ZoomMax);
			}

		}
		repaint();
	}

	public void keyPressed(java.awt.event.KeyEvent e)
	{
		CTRL = e.isControlDown();
		SHIFT = e.isShiftDown();

		if (CTRL == true)
		{
			switch (e.getKeyCode())
			{
			/*
			 * case KeyEvent.VK_Z:parent.undo();break; case
			 * KeyEvent.VK_Y:parent.redo();break; case
			 * KeyEvent.VK_C:contxt.Copy();break;
			 */
			}
		} else if (SHIFT == true)
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_INSERT:

					break;
				case KeyEvent.VK_DELETE:
					//contxt.Cut();
					break;
			}
		} else
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
				{
					scroll(0.0, -5);
				}
					break;
				case KeyEvent.VK_DOWN:
				{
					scroll(0.0, 5);
				}
					break;
				case KeyEvent.VK_LEFT:
				{
					scroll(-5, 0.0);
				}
					break;
				case KeyEvent.VK_RIGHT:
				{
					scroll(5, 0.0);
				}
					break;
				case KeyEvent.VK_ESCAPE:
				{
					cancel_current_action();
				}
					break;
				//case KeyEvent.VK_K:contxt.fill();break;
				//case KeyEvent.VK_R:contxt.setCurrentSubMode(SubMode.ROTATE);break;
				case KeyEvent.VK_Z:

					break;
				case KeyEvent.VK_M:

					break;

				case KeyEvent.VK_L:

					repaint();
					break;
			}
		}
		repaint();
	}

	double	tmp_scrollX		= 0;
	double	tmp_scrollY		= 0;
	double	final_scrollX	= 0;
	double	final_scrollY	= 0;

	Thread	t				= null;

	// 
	private void scroll(final double oX, final double oY)
	{
		/*
		 * SwingUtilities.invokeLater(new Runnable() { public void run() {
		 */

		if ((t == null) || (t != null) && (t.isAlive() == false))
		{
			final_scrollX = ScrollX - oX;
			final_scrollY = ScrollY - oY;

			t = new Thread()
			{
				
				@Override
				public void run()
				{
					while (!(Math.abs(final_scrollX - ScrollX) <= 1) || !(Math.abs(final_scrollY - ScrollY) <= 1))
					{

						ScrollX -= (ScrollX - final_scrollX) / 10.0;
						ScrollY -= (ScrollY - final_scrollY) / 10.0;
						repaint();

						try
						{
							Thread.sleep(10);
						} catch (InterruptedException e)
						{
							ScrollX = final_scrollX;
							ScrollY = final_scrollY;
							return;
						}
					}
					ScrollX = final_scrollX;
					ScrollY = final_scrollY;
				}
			};
			t.start();
		} else
		{
			final_scrollX = final_scrollX - oX;
			final_scrollY = final_scrollY - oY;

		}

	}

	@SuppressWarnings("unused")
	private void scrollDown()
	{
		ScrollY += scrollFactorY;
	}

	@SuppressWarnings("unused")
	private void scrollLeft()
	{
		ScrollX += scrollFactorX;
	}

	@SuppressWarnings("unused")
	private void scrollRight()
	{
		ScrollX -= scrollFactorX;
	}

	/**
	 * Annule l'action en cours (deplacement, creation, ...)
	 */
	protected void cancel_current_action()
	{
	}
/*
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int dir = e.getWheelRotation();
		
		float factor = 1;
		if (Zoom<3)
			factor=2f;
		if (Zoom<2.5)
			factor=3f;
		if (Zoom<2)
			factor=5f;
		if (Zoom<1.5)
			factor=7f;
		if (Zoom<1)
			factor=10f;
		if (Zoom<0.5)
			factor=50f;
		
		Zoom -= (double) dir / (double) factor;

		// Arrondis vers le 1 pour avoir 1 (1 pixel = 1 pixel et pas un truc foireux).
		if (Math.abs(1-Zoom) <=0.1) Zoom=1;
		
		if (Zoom < ZoomMin)
			Zoom = ZoomMin;
		if (Zoom > ZoomMax)
			Zoom = ZoomMax;
		repaint();
	}
	
	*/
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.err.println("Neo");
	    int dir = e.getWheelRotation();
	    
	    // Facteur de zoom dynamique pour éviter un zoom trop rapide à faible niveau
	    float factor = 1;
	    if (Zoom < 3) factor = 2f;
	    if (Zoom < 2.5) factor = 3f;
	    if (Zoom < 2) factor = 5f;
	    if (Zoom < 1.5) factor = 7f;
	    if (Zoom < 1) factor = 10f;
	    if (Zoom < 0.5) factor = 50f;

	    Point2D beforeZoom = null;
	    Point2D afterZoom = null;
	    
	    /**
	     * Pour le centrage on peut desactiver
	     */
	    if (zoomOnCenterOrMousePointer==false)
	    	// Position actuelle de la souris en coordonnées réelles AVANT le zoom
	    	beforeZoom = convertViewXYtoRealXY(e.getX(), e.getY());

	    // Appliquer le zoom
	    double oldZoom = Zoom;
	    Zoom -= (double) dir / (double) factor;

	    // Arrondi vers 1 si proche
	    if (Math.abs(1 - Zoom) <= 0.1) Zoom = 1;
	    
	    // Limites
	    if (Zoom < ZoomMin) Zoom = ZoomMin;
	    if (Zoom > ZoomMax) Zoom = ZoomMax;

	    /**
	     * Pour le centrage on peut desactiver
	     */
	    if (zoomOnCenterOrMousePointer==false)
	    	// Position réelle après le zoom
	    	afterZoom = convertViewXYtoRealXY(e.getX(), e.getY());

	    /**
	     * Pour le centrage on peut desactiver
	     */
	    if (zoomOnCenterOrMousePointer==false)
	    {
		    // Ajuster ScrollX et ScrollY pour compenser le déplacement dû au zoom
	    	// Si Invert Axis sont tout les 2 a false
		    ScrollX += (beforeZoom.getX() - afterZoom.getX()) * ((invertXAxis==false)?-1:1);
		    ScrollY += (beforeZoom.getY() - afterZoom.getY()) * ((invertYAxis==false)?-1:1);
	    }

	    repaint();
	}


	public synchronized CurrentSelectionContext getContxt()
	{
		return contxt;
	}

	public synchronized void setContxt(CurrentSelectionContext contxt)
	{
		this.contxt = contxt;
	}

	@Override
	public void paint(Graphics g2)
	{
		super.paint(g2);
		// Supprime la liste des shape/objets dessin�s pour le systeme de selection automatique.
		selectableObject.clear();
		map_selectableShapeObject.clear();
		map_selectableObjectShape.clear();
		
		fps_counter_start_render = System.currentTimeMillis();
		calcule_Rectangle_Vue();

		// Scale, translate, etc
		Graphics2D g = (Graphics2D) g2;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		at = new AffineTransform(); // Faire un new a chaque coup ????
		at.translate(getWidth() / 2.0, getHeight() / 2.0);

		at.scale(Zoom, Zoom);
		at.translate(ScrollX, ScrollY);

		at.scale((invertXAxis == false) ? 1 : -1, (invertYAxis == false) ? 1 : -1); // Reinverse l'image ;)
	
		if (Angle != 0)
		{
			System.err.println("Changing Angle leads you to infernal bugs...");
			at.rotate(Math.toRadians(Angle));
		}


		if (config.isDrawGrid() == true)
			dessineGrille(g, true);
		
		setBackground(this.getColorConfig().getCOLOR_FOND_GRILLE().getColor());
		doPaint((Graphics2D) g2);

		if (enableSelectionDrawDebug)
		{
			if (System.currentTimeMillis()%2000<=1000)
				drawSelectionnableObjetDebug(g);
		}
		
		//	at.rotate(Math.toRadians(-45));

		
		// TODO CECI DOIT BOUGER ....
		dessineSelectionArea(g);

		at.translate(-10, 0);
		
		if (isDrawFPSInfos())
			draw_fps_infos(g);
		
		/*
		// Dessine les selections faites par la user.
		int nbr = contxt.getSelection().size();
		for (int i = 0; i < nbr; i++)
		{	
			Object o = contxt.getSelection().get(i);
			Shape shp =map_selectableObjectShape.get(o);
			if (shp!=null)
			{
				 g.setColor(Color.MAGENTA);
				 
				 shp = at.createTransformedShape(shp);
				 g.draw(shp);
			//	 g.fill(shp);
			}
		}
		*/
		g.setColor(Color.BLACK);
		
		g.dispose();
	}
	
	private void dessineSelectionArea(Graphics2D g)
	{
		if (selectionArea != null)
		{
			Rectangle2D rectM = CorrigeSelection(selectionArea);
			Shape rect = at.createTransformedShape(rectM);
			g.setColor((getColorConfig()).getCOLOR_SELECTION_AREA().getColor());
			g.fill(rect);
			g.setColor((getColorConfig()).getCOLOR_SELECTION_BORDER_AREA().getColor());
			g.draw(rect);
		}
	}
    
	
	public abstract void doPaint(Graphics2D g);

	/**
	 * @param g
	 */
	protected void draw_fps_infos(Graphics2D g)
	{
		fps_counter_stop_render = System.currentTimeMillis();
		long elapsed = fps_counter_stop_render - fps_counter_start_render;
		fps_counter_sum_render_time += elapsed;
		fps_counter_cpt_render_time++;

		long average_time = fps_counter_sum_render_time / fps_counter_cpt_render_time;

		g.setColor(Color.BLACK);
		g.setFont(new Font("Impact", 0, 16));
		int FPS = (int) (1000f / average_time);
		String str = FPS + " fps at " + average_time + " ms";
		g.drawString(str, 200, 20);
	}

	/**
	 * Transforme le rectangle (avec des width qui sont negatif, en width
	 * positif et en reculant le x (meme chose pour y)
	 * 
	 * @param selectionArea
	 * @return
	 */
	protected Rectangle2D CorrigeSelection(Rectangle2D selectionArea)
	{
		double x = selectionArea.getX();
		double y = selectionArea.getY();
		double w = selectionArea.getWidth();
		double h = selectionArea.getHeight();

		if (w < 0)
		{
			x += w;
			w = Math.abs(w);
		}
		if (h < 0)
		{
			y += h;
			h = Math.abs(h);
		}

		return new Rectangle2D.Double(x, y, w, h);
	}

	/**
	 * Transforme le curseur quand il rentre dans les fenetre graphique en
	 * fonction du property_mode
	 */
	private void changeMouseCursor()
	{

		/*
		 * contxt.getCurrentMode(); if (contxt.getCurrentMode()==Mode.SELECTION)
		 * setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); Point
		 * hotspot = new Point(0, 0); contxt.getCurrentMode(); if
		 * (contxt.getCurrentMode()==Mode.ADD_BLOCK_AUTO)
		 * setCursor(getToolkit().createCustomCursor(cursorBlockAuto, hotspot,
		 * "AddAutoBlock")); if (contxt.getCurrentMode()==Mode.AUTO_SELECT)
		 * setCursor(getToolkit().createCustomCursor(cursorAutoSelect, hotspot,
		 * "AutoSelect")); if (contxt.getCurrentMode()==Mode.ADD_BLOCK)
		 * setCursor(getToolkit().createCustomCursor(cursorAddRegion, hotspot,
		 * "AddRegion")); if (contxt.getCurrentMode()==Mode.ADD_BLOCK_SQUARE)
		 * setCursor(getToolkit().createCustomCursor(cursorAddSquaredRegion,
		 * hotspot, "AddRegionSquare")); if
		 * (contxt.getCurrentMode()==Mode.ADD_SEG)
		 * setCursor(getToolkit().createCustomCursor(cursorAddSegment, hotspot,
		 * "AddSegment"));
		 */
	}

	public synchronized double getRMouseX()
	{
		return rMouseX;
	}

	public synchronized void setRMouseX(double mouseX)
	{
		rMouseX = mouseX;
	}

	public synchronized double getRMouseY()
	{
		return rMouseY;
	}

	public synchronized void setRMouseY(double mouseY)
	{
		rMouseY = mouseY;
	}

	/**
	 * @param g
	 * @param p
	 * @param Web_Height
	 * @param Flange_Width
	 * @param x
	 * @param y
	 */
	// TODO : It's used in LBR5 take care ... (need side and direction)
	@Deprecated
	protected void drawRaidisseurTold(Graphics2D g, double angle, double Web_Height, double Flange_Width, double x, double y)
	{
		if (at != null) // faudra supprimer cette condition.
		{
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, (Web_Height));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);
			at2.translate(-Flange_Width / 2, Web_Height);
			at2.rotate(Math.toRadians(0));
			l = new Line2D.Double(0, 0, Flange_Width, 0);
			t = at2.createTransformedShape(l);
			g.draw(t);

		}
	}
	
	/**
	 * 
	 * @param g
	 * @param angle
	 * @param Web_Height
	 * @param Flange_Width
	 * @param x
	 * @param y
	 * @param side 
	 */
	// TODO : Ces 2 variable de merde doivent (DIR ET SIDE) etre calculé dans l'angle du HMStiffener, donc faut revoir les drawraidisseur a la con, faut virer ca et avoir le bon angle
	protected Shape drawRaidisseurT(Graphics2D g, double angle, double Web_Height, double Flange_Width, double x, double y, int side)
	{
		if (at != null) // faudra supprimer cette condition.
		{
			if (side==1)
				Web_Height=-Web_Height;
			
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, (Web_Height));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);
			at2.translate(-Flange_Width / 2, Web_Height);
			at2.rotate(Math.toRadians(0));
			Line2D l2 = new Line2D.Double(0, 0, Flange_Width, 0);
			Shape t2 = at2.createTransformedShape(l2);
			g.draw(t2);
			
			// Cheat !!!!
			at2 = new AffineTransform();
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Shape lll = at2.createTransformedShape(new Line2D.Double(0, 0, 0, (Web_Height)));
			return lll;
		}
		return null;
	}
	
	/**
	 *
	 * @param g
	 * @param angle
	 * @param Web_Height
	 * @param x
	 * @param y
	 * @param side 0 = Left ; 1 = Right
	 * @param Direction 0 = Forward ; 1 = BACKWARD
	 * @param dir 
	 */
	// TODO : Ces 2 variable de merde doivent (DIR ET SIDE) etre calculé dans l'angle du HMStiffener, donc faut revoir les drawraidisseur a la con, faut virer ca et avoir le bon angle
	protected Shape drawRaidisseurB(Graphics2D g, double angle, double Web_Height, double Flange_Width, double x, double y, int side, int dir)
	{
		if (at != null) // faudra supprimer cette condition.
		{
			
			if (side==1)
				Web_Height=-Web_Height;
			if (dir==0)
				Flange_Width = -Flange_Width;
			
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, (Web_Height));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);
			

			
			at2.translate(0, Web_Height);
			at2.rotate(Math.toRadians(0));
			l = new Line2D.Double(0, 0, Flange_Width, 0);
			t = at2.createTransformedShape(l);
			g.draw(t);
			
			l = new Line2D.Double(Flange_Width, 0, 0, -Web_Height/4.0);
			t = at2.createTransformedShape(l);
			g.draw(t);
			
			
			at2 = new AffineTransform();
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Shape lll = at2.createTransformedShape(new Line2D.Double(0, 0, 0, (Web_Height)));
			return lll;

			
			

		}
		return null;
	}

	/**
	 * 
	 * @param g
	 * @param angle
	 * @param Web_Height
	 * @param Flange_Width
	 * @param x
	 * @param y
	 * @param side
	 * @param dir
	 * @return 
	 */
	// TODO : Ces 2 variable de merde doivent (DIR ET SIDE) etre calculé dans l'angle du HMStiffener, donc faut revoir les drawraidisseur a la con, faut virer ca et avoir le bon angle
	protected Shape drawRaidisseurL(Graphics2D g, double angle, double Web_Height, double Flange_Width, double x, double y, int side, int dir)
	{
		if (at != null) // faudra supprimer cette condition.
		{
			if (side==1)
				Web_Height=-Web_Height;
			if (dir==1)
				Flange_Width = -Flange_Width;
			
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, (Web_Height));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);
			

			
			at2.translate(-Flange_Width, Web_Height);
			at2.rotate(Math.toRadians(0));
			l = new Line2D.Double(0, 0, Flange_Width, 0);
			t = at2.createTransformedShape(l);
			g.draw(t);
			
			// Cheat !!!!
			at2 = new AffineTransform();
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Shape lll = at2.createTransformedShape(new Line2D.Double(0, 0, 0, (Web_Height)));
			return lll;


		}
		return null;
	}

	/**
	 * 
	 * @param g
	 * @param angle
	 * @param Web_Height
	 * @param Flange_Width
	 * @param x
	 * @param y
	 * @param side
	 * @param dir
	 */
	// TODO : Ces 2 variable de merde doivent (DIR ET SIDE) etre calculé dans l'angle du HMStiffener, donc faut revoir les drawraidisseur a la con, faut virer ca et avoir le bon angle
	protected Shape drawRaidisseurNULL(Graphics2D g, double angle, double Web_Height, double Flange_Width, double x, double y, int side, int dir)
	{
		if (at != null) // faudra supprimer cette condition.
		{
			if (side==1)
				Web_Height=-Web_Height;
			
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, (Web_Height));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);
			
			at2.translate(-Flange_Width / 2, Web_Height);
			at2.rotate(Math.toRadians(0));
			
			// TODO : De la merde ... a changer...
			l = new Line2D.Double(-0.1, -0.1, 0.2, 0.1);
			t = at2.createTransformedShape(l);
			g.draw(t);
			
			l = new Line2D.Double(-0.1, 0.1, 0.2, -0.1);
			t = at2.createTransformedShape(l);
			g.draw(t);
			
			// Cheat !!!!
			at2 = new AffineTransform();
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Shape lll = at2.createTransformedShape(new Line2D.Double(0, 0, 0, (Web_Height)));
			return lll;


		}
		
		return null;
	}

	
	/**
	 * Dessine une raidisseur avec les vrai dimension et pas un truc foireux comme avant 
	 * @param g
	 * @param angle
	 * @param Web_Height
	 * @param Web_thickness
	 * @param Flange_Width
	 * @param flange_thick
	 * @param x
	 * @param y
	 * @return
	 */
	protected Shape drawRaidisseurT_Real(
			Graphics2D g, double angle, double Web_Height, double Web_thickness, double Flange_Width, double flange_thick,
			double x, double y)
	{
			AffineTransform at2 = new AffineTransform();
			at2.concatenate(this.at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Rectangle2D rectWeb = new Rectangle2D.Double(-Web_thickness/2, 0, Web_thickness, Web_Height);
			g.fill(at2.createTransformedShape(rectWeb));
			
			// -0.005 sinon artefact
			Rectangle2D rectFlange = new Rectangle2D.Double(-Flange_Width/2, Web_Height-0.005, Flange_Width,flange_thick);
			g.fill(at2.createTransformedShape(rectFlange));
			
			at2 = new AffineTransform();
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			
			Area area = new Area(rectWeb);
			area.add(new Area(rectFlange));
			return at2.createTransformedShape(area);
	}

	
	/**
	 * 
	 * @param g
	 * @param angle
	 * @param x
	 * @param y
	 */
	protected void drawRaidisseurLowRes(Graphics2D g, double angle, double x, double y)
	{
		// TODO : Ceci est tout pourri!
		//Web_Height/=100;
		if (at != null) // faudra supprimer cette condition.
		{
			g.setStroke(new BasicStroke(1.0f));

			AffineTransform at2 = new AffineTransform();
			at2.concatenate(at);
			at2.translate(x, y);
			at2.rotate(Math.toRadians(-angle));
			Line2D l = new Line2D.Double(0, 0, 0, Math.round(1));
			Shape t = at2.createTransformedShape(l);
			g.draw(t);

		}

	}
	
	
	/**
	 * // TODO : renommer ça
	 * @param at
	 */
	public void drawArcCurveMarsLike(Graphics2D g, ArcCurveMarsLike curve)
	{
		float startAngle = (float) curve.getAngle2();
		float EndAngle = (float) curve.getAngle1() - startAngle;
		
		if (startAngle < 0)
			startAngle += 360;
		if (EndAngle < 0)
			EndAngle += 360;

		// Faudrait remplacer ca par le truc d'en dessous.
		Arc2D.Double arc = new Arc2D.Double(curve.getPt_upper_left_circle().x, curve.getPt_upper_left_circle().y - curve.getRadius() * 2, curve.getRadius() * 2, curve.getRadius() * 2, startAngle, EndAngle, Arc2D.OPEN);
		g.draw(at.createTransformedShape(arc));
		
	}
	
	/**
	 * Affiche les zone selectionnables tel quels (en orange ou magenta si selectionn�).
	 * @param g
	 */
	public void drawSelectionnableObjetDebug(Graphics2D g)
	{
		g.setStroke(new BasicStroke(4.0f));
		g.setColor(Color.ORANGE);
		for (int i = 0; i < getSelectableObject().size(); i++)
		{
			SelectionTuple<Shape, ?> o = getSelectableObject().get(i);
			Shape shp = o.getShape();
			
			if (getContxt().getSelection().contains(o.userObject))
				g.setColor(Color.magenta);
			else
				g.setColor(Color.ORANGE);
			g.fill(at.createTransformedShape(shp));
			g.setColor(g.getColor().darker().darker().darker().darker().darker().darker().darker().darker());
			g.draw(at.createTransformedShape(shp));
		}
		g.setStroke(new BasicStroke(1.0f));
	}
	
	
	public synchronized boolean isDrawFPSInfos()
	{
		return DrawFPSInfos;
	}
	public synchronized void setDrawFPSInfos(boolean drawFPSInfos)
	{
		DrawFPSInfos = drawFPSInfos;
	}
	

	/**
	 * @return the mBUTTON_SELECTION_MASK
	 */
	public synchronized int getMBUTTON_SELECTION_MASK()
	{
		return MBUTTON_SELECTION_MASK;
	}
	/**
	 * @param mBUTTON_SELECTION_MASK the mBUTTON_SELECTION_MASK to set
	 */
	public synchronized void setMBUTTON_SELECTION_MASK(int mBUTTON_SELECTION_MASK)
	{
		MBUTTON_SELECTION_MASK = mBUTTON_SELECTION_MASK;
	
	}
	/**
	 * @return the mBUTTON_SCROLL_MASK
	 */
	public synchronized int getMBUTTON_SCROLL_MASK()
	{
		return MBUTTON_SCROLL_MASK;
	}
	/**
	 * @param mBUTTON_SCROLL_MASK the mBUTTON_SCROLL_MASK to set
	 */
	public synchronized void setMBUTTON_SCROLL_MASK(int mBUTTON_SCROLL_MASK)
	{
		MBUTTON_SCROLL_MASK = mBUTTON_SCROLL_MASK;
	
	}
	/**
	 * @return the mBUTTON_POPUP_MASK
	 */
	public synchronized int getMBUTTON_POPUP_MASK()
	{
		return MBUTTON_POPUP_MASK;
	}
	/**
	 * @param mBUTTON_POPUP_MASK the mBUTTON_POPUP_MASK to set
	 */
	public synchronized void setMBUTTON_POPUP_MASK(int mBUTTON_POPUP_MASK)
	{
		MBUTTON_POPUP_MASK = mBUTTON_POPUP_MASK;
	
	}
	/**
	 * Affiche ou non le debug pour voire les shape selectionnables.
	 * @return the enableSelectionDrawDebug
	 */
	public synchronized boolean isEnableSelectionDrawDebug()
	{
		return enableSelectionDrawDebug;
	}
	
	/**
	 * Affiche ou non le debug pour voire les shape selectionnables.
	 * @param enableSelectionDrawDebug the enableSelectionDrawDebug to set
	 */
	public synchronized void setEnableSelectionDrawDebug(boolean enableSelectionDrawDebug)
	{
		this.enableSelectionDrawDebug = enableSelectionDrawDebug;
	
	}
	public boolean isZoomOnCenterOrMousePointer() {
		return zoomOnCenterOrMousePointer;
	}
	public void setZoomOnCenterOrMousePointer(boolean zoomOnCenterOrMousePointer) {
		zoomOnCenterOrMousePointer = zoomOnCenterOrMousePointer;
	}
	
	

}
