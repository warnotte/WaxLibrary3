package org.warnotte.W2D.PanelGraphique.tests.ships;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import org.warnotte.W2D.PanelGraphique.CurrentSelectionContext;
import org.warnotte.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import org.warnotte.W2D.PanelGraphique.SelectionChangeable;
import org.warnotte.W2D.PanelGraphique.SelectionChangedEvent;

/*
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
*/

public class VUE2D_Ships extends PanelGraphiqueBaseBase implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable,  ActionListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4676712333053517713L;
	CurrentSelectionContext		contxt;
	
	PlayerV player = new PlayerV(new Vector2D(0,0), 0);
	
	List<Missile> list_missiles = new ArrayList<>();
	List<Meteora> list_meteors = new ArrayList<>();
	List<xploSpr> list_explodes = new ArrayList<>();
	
	boolean key_Left = false;
	boolean key_Right = false;
	boolean key_Up = false;
	boolean key_Down = false;
	boolean key_Fire = false;

	List<BufferedImage>[] image_xplo = new ArrayList[3];
	
	int WorldSizeW = 400;
	int WorldSizeH = 200;
	Rectangle2D world = new Rectangle2D.Float(-WorldSizeW/2, -WorldSizeH/2, WorldSizeW, WorldSizeH);
	boolean WorldLoop = true;
	
	/**
	 * @param contxt
	 */
	public VUE2D_Ships(CurrentSelectionContext contxt)
	{
		super(contxt);
		setDrawFPSInfos(true);

		try
		{
			image_xplo[0] = SpriteAnimLoader.readFile(getClass().getResource("Exp_type_A_mini_A.png"), 48, 48);
			image_xplo[1] = SpriteAnimLoader.readFile(getClass().getResource("Exp_type_B_mini_A.png"), 48, 48);
			image_xplo[2] = SpriteAnimLoader.readFile(getClass().getResource("Exp_type_C_mini_A.png"), 48, 48);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		config.setDrawGrid(false);
		config.setDrawHelpLines(false);

		this.contxt = contxt;
		
		Zoom = 1.0;
				
		if (contxt!=null)
			contxt.addXXXListener(this);
		
		setToolTipText(
				"<html>" +
				"Roulette : Zoom<br>" +
				"Bt Millieu+Drag : Deplacer vue<br>" +
				"Bt Droit : Selectionner element<br>" +
				"Bt Droit+Drag : Creer rectangle de selection<br>"+
				"</html>");
		
	
		Timer timer = new Timer(25, this);
		timer.start();
		
		list_meteors.add(new Meteora( new Vector2D(10,10), (float)Math.random()*360f, 32));
		list_meteors.add(new Meteora( new Vector2D(50,50), 45, 32));
			
		getColorConfig().getCOLOR_FOND_GRILLE().setColor(Color.BLACK);

	}
	/*
	 * (non-Javadoc)
	 * @see
	 * WaxLibrary.W2D.PanelGraphique.PanelGraphiqueBaseBase#doPaint(java.awt
	 * .Graphics2D)
	 */
	
	float ang = 0;
	@Override
	public void doPaint(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		Shape world_at = at.createTransformedShape(world);
		g.fill(world_at);
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g.setColor(Color.BLACK);
		g.drawString(String.format("XY : %d,  %d",  (int)MouseX, (int)MouseY), 10,10);
		g.drawString(String.format("Selected : %d", contxt.getSelection().size()), 10,20);
		
		g.setClip(world_at);
		drawGame(g);	
		
	}

	/**
	 * @param shp1
	 * @param world
	 * @return
	 */
	private boolean isInBoundary(Shape shp1, Rectangle2D bounds_world)
	{
		Rectangle2D bounds_objet = shp1.getBounds2D();
		
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2, -WorldSizeH/2, -WorldSizeW/2, WorldSizeH/2)))
			return true;
		if (bounds_objet.intersectsLine(new Line2D.Float(WorldSizeW/2, -WorldSizeH/2, WorldSizeW/2, WorldSizeH/2)))
			return true;
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2, -WorldSizeH/2, WorldSizeW/2, -WorldSizeH/2)))
			return true;
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2, WorldSizeH/2, WorldSizeW/2, WorldSizeH/2)))
			return true;
		
		return false;
	}

	/**
	 * 
	 */
	private void updateControls()
	{
		if (key_Left==true) player.left();
		if (key_Right==true) player.right();
		if (key_Up==true) player.avance();
		if (key_Down==true) player.recule();
		if (key_Fire==true) {
			boolean fired = player.fire();
			if (fired==true)
			{
				// Launch miss
				//System.err.println("Missile launched");
				addMissile(player.position, player.getAngle());
			}
		}
	}

	/**
	 * @param g
	 */
	private void drawGame(Graphics2D g)
	{
		g.setColor(Color.GREEN);
		// Draw MET
		for (Iterator<Meteora> iterator = list_meteors.iterator(); iterator.hasNext();)
		{
			Meteora _meteor = iterator.next();
			Shape shp_meteor = _meteor.getShape();
			
			g.setColor(Color.GREEN);
			g.fill(at.createTransformedShape(shp_meteor));
			g.setColor(Color.BLACK);
			g.draw(at.createTransformedShape(shp_meteor));
			boolean ret = isInBoundary(shp_meteor, world);
			if (ret==true)
			{		
				Shape shape_modifie = creeShapeOverlapped(shp_meteor);
				g.setColor(Color.GREEN);
				g.fill(at.createTransformedShape(shape_modifie));
				g.setColor(Color.BLACK);
				g.draw(at.createTransformedShape(shape_modifie));
			}
		}

		// Draw MIS
		for (Iterator<Missile> iterator = list_missiles.iterator(); iterator.hasNext();)
		{
			Missile type = iterator.next();
			Shape shp_missile = type.getShape();
			g.setColor(Color.BLUE);
			g.fill(at.createTransformedShape(shp_missile));
			g.setColor(Color.BLACK);
			g.draw(at.createTransformedShape(shp_missile));
			
			boolean ret = isInBoundary(shp_missile, world);
			if (ret==true)
			{		
				Shape shape_modifie = creeShapeOverlapped(shp_missile);
				g.setColor(Color.BLUE);
				g.fill(at.createTransformedShape(shape_modifie));
				g.setColor(Color.BLACK);
				g.draw(at.createTransformedShape(shape_modifie));
			}
		}
		
		// Draw Player
		{
			Shape playerShape = player.getShape();
			
			g.setColor(Color.MAGENTA);
			g.fill(at.createTransformedShape(playerShape));
			g.setColor(Color.BLACK);
			g.draw(at.createTransformedShape(playerShape));
			
			boolean ret = isInBoundary(playerShape, world);
			if (ret==true)
			{		
				Shape shape_modifie = creeShapeOverlapped(playerShape);
				
				g.setColor(Color.MAGENTA);
				g.fill(at.createTransformedShape(shape_modifie));
				g.setColor(Color.BLACK);
				g.draw(at.createTransformedShape(shape_modifie));
			}
			
			//if (playerShape.)
		}
		
		for (Iterator<xploSpr> iterator = list_explodes.iterator(); iterator.hasNext();)
		{
			xploSpr spr = iterator.next();
			
			Shape shape = spr.getShape();
			
			Vector2D position = spr.position;
			
			Point2D.Float pt = new Point2D.Float((int)position.getX(), (int)position.getY());
			pt = (Float) at.transform(pt, pt);
			BufferedImage image = spr.getCurrentImge();
		//	g.drawImage(image, null, (int)pt.getX()-image.getWidth()/2, (int) pt.getY()-image.getHeight()/2);
			
			int w = (int) (image.getWidth()*Zoom);
			int h = (int) (image.getHeight()*Zoom);
			
			g.drawImage(image, (int)pt.getX()-w/2, (int) pt.getY()-h/2, w, h,this);
			
			boolean ret = isInBoundary(shape, world);
			if (ret==true)
			{		
				Shape shape_modifie = at.createTransformedShape(creeShapeOverlapped(shape));
	 	 		g.drawImage(image, (int)shape_modifie.getBounds2D().getCenterX()-w/2, (int)shape_modifie.getBounds2D().getCenterY()-h/2, w, h,this);
			}
		}
	}

	/**
	 * @param shp_meteor
	 * @return
	 */
	private Shape creeShapeOverlapped(Shape shp_meteor)
	{
		Rectangle2D bounds_objet = shp_meteor.getBounds2D();
		
		AffineTransform at2 = new AffineTransform();
		// Gauche
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2, -WorldSizeH/2, -WorldSizeW/2,  WorldSizeH/2)))
			at2.translate(WorldSizeW, 0);
		// Droite
		if (bounds_objet.intersectsLine(new Line2D.Float( WorldSizeW/2, -WorldSizeH/2,  WorldSizeW/2,  WorldSizeH/2)))
			at2.translate(-WorldSizeW, 0);
		// Dessus
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2, -WorldSizeH/2,  WorldSizeW/2, -WorldSizeH/2)))
			at2.translate(0, WorldSizeH);
		// Dessous
		if (bounds_objet.intersectsLine(new Line2D.Float(-WorldSizeW/2,  WorldSizeH/2,  WorldSizeW/2,  WorldSizeH/2)))
			at2.translate(0, -WorldSizeH);
						
		Shape shape_modifie = at2.createTransformedShape(shp_meteor);
		return shape_modifie;
	}

	/**
	 * @return
	 */
	private void updateGame()
	{
		// Update la position du joueur.
		player.update();
		correctEntityPosition(player);
		
		// Update position missile et destruction de ceux si s'ils sont mort. 
		for (Iterator<Missile> iterator = list_missiles.iterator(); iterator.hasNext();)
		{
			Missile type = iterator.next();
			type.update();
			correctEntityPosition(type);
			if (type.isDead()==true)
				iterator.remove();
		}
		
		// Update position meteore
		for (Iterator<Meteora> iterator = list_meteors.iterator(); iterator.hasNext();)
		{
			Meteora type = iterator.next();
			type.update();
			correctEntityPosition(type);
		}
		
		// Check les collision meteores versus missille.
		List<Meteora> destroyedmeteor = new ArrayList<>();
		for (Iterator<Missile> iterator_missile = list_missiles.iterator(); iterator_missile.hasNext();)
		{
			Missile type = iterator_missile.next();
			Shape shp_missile = type.getShape();
			boolean flag = false;
			for (Iterator<Meteora> iterator_meteor = list_meteors.iterator(); iterator_meteor.hasNext();)
			{
				Meteora _meteor = iterator_meteor.next();
				if (testIntersection(shp_missile, _meteor.getShape()))
				{
					flag = true;
					iterator_meteor.remove();
					destroyedmeteor.add(_meteor);
					addExplode(_meteor.position);
				}
			}
			if (flag==true)
				iterator_missile.remove();
		}
		
		// Check les collisions meteores versus player.
		for (Iterator<Meteora> iterator = list_meteors.iterator(); iterator.hasNext();)
		{
			Meteora _meteor = iterator.next();
			Shape shp_meteor = _meteor.getShape();
			if (testIntersection(shp_meteor, player.getShape()))
			{
				MakePlayerDead();
				iterator.remove();
				destroyedmeteor.add(_meteor);			
				addExplode(_meteor.position);
			}
		}
		
		// Supprimer les meteores qui ont �t� touch�.
		for (int i = 0; i < destroyedmeteor.size(); i++)
			explodeMeteora(destroyedmeteor.get(i));
		
		// Update les sprites d'explosions.
		for (Iterator<xploSpr> iterator = list_explodes.iterator(); iterator.hasNext();)
		{
			xploSpr spr = iterator.next();
			spr.update();
			if (spr.isDead())
  				iterator.remove();
			correctEntityPosition(spr);
		}
	}
	
	/**
	 * @param type
	 */
	private void correctEntityPosition(Entity type)
	{
		if (WorldLoop==false) 
			return;
		Vector2D position = type.position;
		position = correctPosition(position);
		type.position= position;
	}

	/**
	 * @param position
	 * @return
	 */
	private Vector2D correctPosition(Vector2D position)
	{
		if (position.getX() >=  WorldSizeW/2.0) position = position.add(new Vector2D(-WorldSizeW, 0));else
		if (position.getY() >=  WorldSizeH/2.0) position = position.add(new Vector2D(0, -WorldSizeH));else
		if (position.getX() <= -WorldSizeW/2.0) position = position.add(new Vector2D(WorldSizeW, 0));else
		if (position.getY() <= -WorldSizeH/2.0) position = position.add(new Vector2D(0, WorldSizeH));
		
		return position;
	}

	/**
	 * @param position
	 */
	private void addExplode(Vector2D position)
	{
		int idx = (int)(Math.random()*(image_xplo.length-1));
		list_explodes.add(new xploSpr(position, image_xplo[idx]));
	}
	
	/**
	 * @param position
	 * @param directionVector
	 */
	private void addMissile(Vector2D position, float angle)
	{
		Missile m = new Missile(position, angle);
		list_missiles.add(m);
	}

	/**
	 * @param meteor
	 */
	private void explodeMeteora(Meteora meteor)
	{
		float Sz = meteor.Size;
		if (Sz >= 8.0) 
		{
			Sz /= 2.0;
			for (int i = 0; i < 4; i++)
			{
				list_meteors.add(new Meteora(meteor.position, (float) (Math.random()*360), Sz));
			}
		}
	}

	/**
	 * 
	 */
	private void MakePlayerDead()
	{
		addExplode(player.position);
		player.setPosition(new Vector2D(0,0));
	}

	public static boolean testIntersection(Shape shapeA, Shape shapeB) {
		Area areaA = new Area(shapeA);
		areaA.intersect(new Area(shapeB));
	   return !areaA.isEmpty();
	}

	@Override
	public void somethingNeedRefresh(SelectionChangedEvent e)
	{
		
	}
	
	@Override
	public void mousePressed(java.awt.event.MouseEvent e)
    {
		super.mousePressed(e);
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		super.mouseReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e)
	{
		super.mouseDragged(e);
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e)
	{
		super.mouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			key_Left = true;
		}
		if (e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			key_Right = true;
		}
		if (e.getKeyCode()==KeyEvent.VK_UP)
		{
			key_Up = true;
		}
		if (e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			key_Down = true;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			key_Fire = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			key_Left = false;
		}
		if (e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			key_Right = false;
		}
		if (e.getKeyCode()==KeyEvent.VK_UP)
		{
			key_Up = false;
		}
		if (e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			key_Down = false;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			key_Fire = false;
		}
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				CurrentSelectionContext ctxt1 = new CurrentSelectionContext()
				{
					@Override
					public boolean isFiltred(Class<?> classK)
					{
						return false;
					}
				};
				JFrame frame1 = new JFrame();
				frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				VUE2D_Ships panel1 = new VUE2D_Ships(ctxt1);
				frame1.add(panel1);
				frame1.setSize(640, 480);
				frame1.setVisible(true);
				frame1.setTitle("X=false;Y=false;");
				panel1.invertXAxis = false;
				panel1.invertYAxis = false;
				frame1.setLocation(0, 0);
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateControls();
		updateGame();
		repaint();
	}

}