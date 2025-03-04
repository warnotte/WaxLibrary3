/*
 * PanelRoundBT.java
 *
 * Created on 17 août 2007, 16:35
 */

package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSlider;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Warnotte Reanud 
 * @author Francois Halet
 * 
 */
public class WRoundSlider extends javax.swing.JPanel implements ComponentListener, MouseListener {
    
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5429643182827374717L;

	private int size,h,w; // Va retenir la taille du panel.
	private double angle=0; // 0 a 360 en flottant
	private int value=0; // Sera pour contenir la valeur réel du slider
	private int old_cadran = -1; // Pour determiner dans quel partie du cercle on est (il y'a 4 quadran dans une cercle);
	
	int MaximumAngle = 360;
	int AngleOffset = 0; // Servirait a demarrer ailleurs qu'en position 3 heure mais horrible a faire.
	
	private int max;
	private int min;
	
	private final Vector<ChangeListener> listeners = new Vector<ChangeListener>();  //  @jve:decl-index=0:
	
	private boolean EnableAutoAdjustWhileDrag = true; // Pour ne pas chopper des demi valeurs (et accelerer le rendu graphique)
	private boolean EnableTickString = true; // Pour les labels des graduations
	private boolean EnabeTicks=true; // Pour les graduations
	private boolean EnableMinimalTicksOnly=false; // Pour les graduations minimale, max, max/2, 3/4 max et max/4
	private boolean EnableOverflow = true; // Pour le passage de 0 a 100 ou l'inverse
	private boolean EnableAntialiasing = true;
	private boolean EnableAutoScaleViseur = true; // Pour reduire la taille du pointeur si l'etendue min max augmente
	private boolean EnableLabel;
	
	private int TickDivider = 1; // Pour n'affiche que 1/1 ou X/1 graduations
	private Color ViseurColor=new Color(255,60,0);  //  @jve:decl-index=0:


	private BufferedImage IMG_Fond = null; // Pour l'image du fond (le boutons et les graduations);
	private BufferedImage IMG_Viseur= null;  // Pour l'image du pointeur  //  @jve:decl-index=0:
	private int old_w=-1;
	private int old_h=-1;
	private int old_value;
	private float Divider=1;
	
	public WRoundSlider() // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
    {
		this.setName("VolumePanel");
		initComponents();
        this.addMouseListener(this);
        value = 0;
        max = 100;
    }
  
	public WRoundSlider(int max)  // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
    {
		this();
		this.max = max;
    }
	public WRoundSlider(int min, int max)  // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
    {
		 this(max);
	     this.min=min;
    }
	public WRoundSlider(int min, int max, int value) // Les constructeurs dois etres les ^m que JSlider (+eventuelement des a nous)
    {
		 this(max);
	     this.min=min;
    }
    
	public void init_buffer_images()
	{
		init_buffer_fond(false);
		init_buffer_visee();
	}

	public void init_buffer_fond(boolean force) // Le force ne me plait pas non plus
	{
		int w=this.getWidth();
		int h=this.getHeight();
		// Si on resize la windows comme un porc ca px mettre w ou h a < 0
		if ((w>0) && (h>0))
		if (((w!=old_w) || (h!=old_h)) || (force==true)) // Ceci ne me plait pas bcp
		{
		//	System.err.println("Set buffer ["+w+" , "+h+"]");
		//	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			if (IMG_Fond!=null)
			{
				IMG_Fond.flush();
			}
			IMG_Fond = null; // Prions pour qu'il desinitialise la mémoire ??!
			
			
			IMG_Fond =new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);

			old_w=w;
			old_h=h;
			prepare_image_fond();
			repaint();
		}
	}
	public void init_buffer_visee() 
	{
		int viseur_size = size/7;//(int) (0.05f*w);
        if (viseur_size<=0) viseur_size = 1;
		// Si on resize la windows comme un porc ca px mettre w ou h a < 0
		// System.err.println("Set buffer pointeur ["+viseur_size+" , "+viseur_size+"]");
		// GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		if (IMG_Viseur!=null)
			IMG_Viseur.flush();
		IMG_Viseur = null; // Prions pour qu'il desinitialise la mémoire ??!
		
		IMG_Viseur =new BufferedImage(viseur_size, viseur_size, BufferedImage.TYPE_INT_ARGB);

		//	gc.createCompatibleImage(viseur_size, viseur_size, Transparency.TRANSLUCENT);
		prepare_image_visee();
		repaint();
	}
    private void initComponents() {

    	this.setDoubleBuffered(true);
    	this.addComponentListener(this);
        setLayout(new BorderLayout());
        this.setFocusable(true);
        this.requestFocus();
        this.setSize(new Dimension(150, 150));
        this.setMinimumSize(new Dimension(150, 40));
        this.setPreferredSize(new Dimension(150, 150));
        this.addKeyListener(new java.awt.event.KeyListener() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				double incvalue = getIncrementValue();
				if (e.getKeyCode()==KeyEvent.VK_HOME)
        			
        		{ 
        			angle= 0;
        			value = (int) Math.round(ConvertAngleToValue(angle, min, max, MaximumAngle));
        	    	old_value = value;
        	    	ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        		if (e.getKeyCode()==KeyEvent.VK_END)
        		{ 
        			angle = MaximumAngle;
        			value = (int) Math.round(ConvertAngleToValue(angle, min, max, MaximumAngle));
        	    	old_value = value;
        			ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        		if ((e.getKeyCode()==KeyEvent.VK_RIGHT)|| (e.getKeyCode()==KeyEvent.VK_PLUS)
        				|| (e.getKeyChar()=='+'))
        		{
        			double angle4 = angle+incvalue; 
        			if (angle4<0) angle4=0;
        			if (angle4>MaximumAngle) angle4=MaximumAngle;
        			value = (int) Math.round(ConvertAngleToValue(angle4, min, max, MaximumAngle));
        	    	old_value = value;
        			angle = angle4;
        			ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        		
        		if ((e.getKeyCode()==KeyEvent.VK_LEFT)  || (e.getKeyCode()==KeyEvent.VK_MINUS)
        			|| (e.getKeyChar()=='-'))
        		{
        			double angle4 = angle-incvalue; 
        			if (angle4<0) angle4=0;
        			if (angle4>MaximumAngle) angle4=MaximumAngle;
        	    
        	    	value = (int) Math.round(ConvertAngleToValue(angle4, min, max, MaximumAngle));
        	    	old_value = value;
        			angle = angle4;
        			ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        		if (e.getKeyCode()==KeyEvent.VK_PAGE_DOWN)
        		{
        			double angle4 = angle-(incvalue*(max-min)/10); 
        			if (angle4<0) angle4=0;
        			if (angle4>MaximumAngle) angle4=MaximumAngle;
        	    
        	    	value = (int) Math.round(ConvertAngleToValue(angle4, min, max, MaximumAngle));
        	    	old_value = value;
        			angle = angle4;
        			ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        		if (e.getKeyCode()==KeyEvent.VK_PAGE_UP)
        		{
        			double angle4 = angle+(incvalue*(max-min)/10); 
        			if (angle4<0) angle4=0;
        			if (angle4>MaximumAngle) angle4=MaximumAngle;
        	    
        	    	value = (int) Math.round(ConvertAngleToValue(angle4, min, max, MaximumAngle));
        	    	old_value = value;
        			angle = angle4;
        			ChangeEvent ch = new ChangeEvent(this);
                    fireChangeEvent (ch);
                    repaint();
        		}
        	
        	}
        	public void keyTyped(java.awt.event.KeyEvent e) {
        	}
        	public void keyPressed(java.awt.event.KeyEvent e) {
        	}
        });
        this.addMouseListener(new java.awt.event.MouseAdapter() {
        	
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
        		requestFocus();
        	}
        });
        
        init_buffer_images();
        addMouseListener(new java.awt.event.MouseAdapter() {
            
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
            	//formMousePressed(evt);
            }
            
			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
            	//formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            
			@Override
			public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
    }//

    public void componentHidden(ComponentEvent arg0) {
		repaint();
	}
	public void componentMoved(ComponentEvent arg0) {
		repaint();
	}
	public void componentResized(ComponentEvent arg0) {
		this.init_buffer_images();
		repaint();
	}
	public void componentShown(ComponentEvent arg0) {
		repaint();
	}
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
    	int mousex = evt.getX();
    	int mousey = evt.getY();
        
    	int mx = mousex - size/2;
    	int my = mousey - size/2;
        
    	// Recuperation du cadran
    	int cadran = getCadranSelected(mx, my);
    	
    	
    	setOld_cadran(cadran);
    	
    	
    	angle = getAngleFromXY(mousex, mousey)+AngleOffset;
    	angle = getAngleFromXY2(mousex, mousey);
    	
    	
	
    	value = (int) Math.round(ConvertAngleToValue(angle, min, max, MaximumAngle));
        
    	// Si on vx que le truc se calle au bon endroit (permet de reduire le redessinage aussi)
    	if (isEnableAutoAdjustWhileDrag()==true)
    		angle = (int)ConvertValueToAngle(value, min, max, MaximumAngle);
    	
    	// Verifie que l'on ne tourne pas le bouton de maniere a passer du min au max d'un coup ou l'inverse (cad apsser du min au max ou l'inverse d'un coup sec)
        if (isEnableOverflowCheck()==true)
    	{
    		if ((old_value==max) && (value==min))
    			value=max;
    		if ((old_value==min) && (value==max))
    			value=min;
    		
    	}
    	
    	// Si la valeur a changée on redessine pas ...
    	// Ou qu'on est pas en autodrag
        if ((old_value!=value) || (isEnableAutoAdjustWhileDrag()==false))
        {
        	ChangeEvent ch = new ChangeEvent(this);
            fireChangeEvent (ch);
            repaint();
        }
        
        old_value = value;
        
    }//GEN-LAST:event_formMouseDragged
    
    
    private double getAngleFromXY2(double tempx, double tempy) {
    	 tempx = tempx - size/2;
    	 tempy = tempy - size/2;
         
         // Calcule de l'angle
         double hypo = Math.abs(tempx*tempx) + Math.abs(tempy * tempy);
         hypo = Math.sqrt(hypo);
         double angletmp = Math.acos(tempx/hypo);
         // Si on est dans la partie superieur du cercle alors on fait un petit ajustement
         if(tempy < 0)   angletmp = Math.toRadians(360) - angletmp;
         
         angletmp = Math.toDegrees(angletmp);
         
         angletmp=angletmp+360-AngleOffset;
         angletmp=angletmp%360;
         return angletmp;
	}

	////// CALCULS ET AUTRES SINGERIES /////////////////////////////////////////////
    /**
     * Recupere le cadran selectionné du cercle (sachant que le millieu est en 0,0)
     * @param mx
     * @param my
     * @return 1 = BD; 2 = BG (bas gauche); 3 = HG; 4 = HD (haut droite)
     */
    private int getCadranSelected(int mx, int my) {
    	int cadran = -1;
    	if ((my>=0) && (mx >0))   cadran=1;
    	if ((my>=0) && (mx<=0))   cadran=2;
    	if ((my <0) && (mx <0))   cadran=3;
    	if ((my <0) && (mx>=0))   cadran=4;
    	return cadran;
	}
	/**
	 * Map la valeur de l'angle avec l'etendue Min Max pour un cercle a l'angle_max.
	 * @param angle Angle a mapper
	 * @param min Valeur min du map
	 * @param max Valeur max du map
	 * @param angle_max Angle maximum (generalement 360) du cercle.
	 * @return
	 */
	private static double ConvertAngleToValue(double angle, int min, int max, int angle_max) {
		
	//	System.err.printf("Angle=%f minmax=%d,%d anglemax=%d\r\n", angle,  min,  max,  angle_max);
		
		angle = angle/angle_max; // convertit en 0 to 1;
		
	//	System.err.println("Angle converti en 0 1 = "+angle);
		
		double mix = (max * angle) + (min * (1-angle)); // Obtient la map
	//	System.err.println("Mix = "+mix);
		if (mix>=max)
			mix=max;
		if (mix<=0.0)
			mix=0.0;
		return  mix;
	
	}
	/**
	 * Inverse de ConvertAngleToValue
	 * @param valeur
	 * @param min
	 * @param max
	 * @param angle_max
	 * @return
	 */
	private static double ConvertValueToAngle(double valeur, int min, int max, int angle_max) {
		
		double angle = ((valeur-min))/((max-min)); 
		angle *= angle_max; // remap from 0 to 1 to 0 to 360;
		if (angle>angle_max) angle=angle_max;
		return angle;

	}
	 /**
     * Recuperer l'angle former entre le centre du cercle et le point a l'interieur du cercle
     * @param tempx2
     * @param tempy2
     * @return
     */
	 private double getAngleFromXY(double tempx, double tempy) 
	    {
	    	 tempx = tempx - size/2;
	    	 tempy = tempy - size/2;
	         
	         // Calcule de l'angle
	         double hypo = Math.abs(tempx*tempx) + Math.abs(tempy * tempy);
	         hypo = Math.sqrt(hypo);
	         double angletmp = Math.acos(tempx/hypo);
	         // Si on est dans la partie superieur du cercle alors on fait un petit ajustement
	         if(tempy < 0)   angletmp = Math.toRadians(360) - angletmp;
	         
	         angletmp = Math.toDegrees(angletmp);
	         return angletmp;
		}
    /**
     * Recupere la position en X dans le cercle pour une amplitude donnée
     * @param radangle l'angle en radien
     * @param distFromPeri la distance a partir du centre du cercle
     * @return
     */
    private int getX(double radangle,float distFromPeri)
    {
        return (int)((float)size/(float)2 + Math.cos(radangle)*(((float)size/(float)2)-distFromPeri));
    }
    /**
     * Recupere la position en Y dans le cercle pour une amplitude donnée
     * @param radangle l'angle en radien
     * @param distFromPeri la distance a partir du centre du cercle
     * @return
     */
    private int getY(double radangle,float distFromPeri)
    {
    	return (int)((float)size/(float)2 + Math.sin(radangle)*(((float)size/(float)2)-distFromPeri));
    }
    
    
    ////// PAINT DU DESSIN /////////////////////////////////////////////////////
    
    /**
     * Prepare l'image du fond : le bouton, le ticks, et les textes des ticks
     */
    private void prepare_image_fond()
    {
    	//System.err.println("Draw image fond dans le buffer");
    	if (IMG_Fond==null) return;
    	Graphics2D g = (Graphics2D) IMG_Fond.getGraphics();
    	// Je pense que ceci devrait pas etre fait tout le temps
    	// Ne mets pas d'antialising, ca sera le Paint qui le fera
    	if (EnableAntialiasing==true)
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    	
    	w = this.getWidth();
        h = this.getHeight();
        
        if(h >= w)  size = w;
        else        size = h;
        
       // g.setColor(this.getBackground());
       // g.fillRect(0,0,w,h);
        
        GradientPaint gradient = new GradientPaint(0, 0, Color.GRAY, 1/*w/2*/, h/2, Color.LIGHT_GRAY, true); // true means to repeat pattern
        g.setColor(Color.LIGHT_GRAY);
        g.setPaint(gradient);
        g.fillOval(5,5,size-10,size-10);
        g.setPaint(null);
        g.setColor(Color.BLACK);
        g.drawOval(5,5,size-10,size-10);

        g.setColor(Color.BLACK);
        // Dessine les graduation
        double inc = getIncrementValue();
        //System.out.println("inc: " + inc);
         
        // C'est foireux ici ca me plait pas tout ces chiffres a chipotter pour avoir juste
        int cpt = 0;
        if (isEnableTick())
        {
        	if (isEnableMinimalTicksOnly()==false)
        	{
        		for(double i = 0; i <= MaximumAngle ; i = i+inc)
        		{
        			if (((cpt++%TickDivider)==0))
        				drawCompleteTickGraduation(g, i);
        		}
        		drawCompleteTickGraduation(g, MaximumAngle);
        	}
        	else
        	{
        		drawCompleteTickGraduation(g, 0);
        		drawCompleteTickGraduation(g, MaximumAngle/2);
        		drawCompleteTickGraduation(g, MaximumAngle/4);
        		drawCompleteTickGraduation(g, MaximumAngle/4*3);
        		drawCompleteTickGraduation(g, MaximumAngle);
        	}
        	
        }
       
        
    }
    
    /**
     * Dessine une ligne a l'angle demandé.
     * @param g
     * @param Angle
     */
	private void drawCompleteTickGraduation(Graphics2D g, double Angle)
    {
	Angle+=180;
		
    //	double cosX=Math.cos(Math.toRadians(Angle+AngleOffset));
	//	double sinY=Math.sin(Math.toRadians(Angle+AngleOffset));
	//	double OFFFSYX = 1.970f;
		
        float dist = size/1.00f;  // Mwais ... deja ca c pas normal que ca soit pas juste ...
        dist = (float) (Math.pow(size,1.025)/1.25f); 
        
        int XX = getX(Math.toRadians(Angle+AngleOffset), dist);
        int YY = getY(Math.toRadians(Angle+AngleOffset), dist);
        int XX1 = getX(Math.toRadians(Angle+AngleOffset), dist*1.05f);
        int YY1 = getY(Math.toRadians(Angle+AngleOffset), dist*1.05f);

		g.drawLine(XX,YY ,XX1, YY1);

		
		if (isEnableTickString())
		{
			Angle-=180;
			
	   // 	 cosX=Math.cos(Math.toRadians(Angle+AngleOffset));
		//	 sinY=Math.sin(Math.toRadians(Angle+AngleOffset));
		//	int x = (int)(size/2 + 1.075*cosX*(size/2-10)) ; 
		//	int y = (int)(size/2 + 1.05*sinY*(size/2-0)) ; 
		//	int v = (int) Math.round((((max-min)*(Angle)/MaximumAngle))+min);
			// Dessine les textes des ticks mark.
		//	g.drawString(""+v, x-5, y+5);
		}
	
    }
   /**
     * Prepare l'image du fond : le bouton, le ticks, et les textes des ticks
     */
    private void prepare_image_visee()
    {
    	//System.err.println("Draw image viseur dans le buffer");
    	Graphics2D g = (Graphics2D) IMG_Viseur.getGraphics();
    	// Je pense que ceci devrait pas etre fait tout le temps
    	// Ne mets pas d'antialising, ca sera le Paint qui le fera
    	if (EnableAntialiasing==true)
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    	
    		w = this.getWidth();
    		int viseur_size = size/7;//(int) (0.05f*w);
            
          GradientPaint gradient = new GradientPaint(0, 0, Color.LIGHT_GRAY, viseur_size, viseur_size/2, ViseurColor, true); // true means to repeat pattern
          g.setPaint(gradient);
          
          // Pour dessiner l'ovale
         /* g.fillOval(0,0, viseur_size,viseur_size);
          g.setColor(Color.GRAY);
          g.drawOval(0,0, viseur_size,viseur_size);*/
          
          // Pour le triangle
          Polygon Poly = new Polygon();
          Poly.addPoint(0,0);
          Poly.addPoint(viseur_size,viseur_size/2);
          Poly.addPoint(0,viseur_size);
          g.fillPolygon(Poly);
          g.setColor(Color.BLACK);
          g.drawPolygon(Poly);
          
          
    }
    
    // TODO : Ne serait-il pas plus interessant de dessiner directement le viseur dans le fond?!
    
	@Override
	public void paint(Graphics g1)
    {
    	//System.err.println("Repaint();");
        Graphics2D g = (Graphics2D)g1;
    	// Je pense que ceci devrait pas etre fait tout le temps
        if (EnableAntialiasing==true)
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
        	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        
    	//g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    	//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      
  //      super.paint(g);
        w = this.getWidth();
        h = this.getHeight();
        
        if(h >= w)  size = w;
        else        size = h;
   
        float dist = (float) (Math.pow(size,1.025)*0.75f); 
        // Dessine le beau fond
        
        g.drawImage(IMG_Fond, 0,0, this);
        
        // Calcule la position ou l'on doit afficher le viseur (son centre de gravité)
        int XX = size-getX(Math.toRadians(angle+AngleOffset), dist);
        int YY = size-getY(Math.toRadians(angle+AngleOffset), dist);

      //  g.setColor(Color.RED);
        if (getEnableLabel()==true)
        {
            Font fnt1 = new Font("Console", Font.PLAIN, size/8);
            g.setFont(fnt1);
        	g.drawString(this.getName(),size/5,size/2);
        	g.drawString(""+this.getValue(),size/5+size/4,size/2+size/8);
        }
        
        // TODO : Apparement c'est le Transform qui fait disparaitre le pointeur
        AffineTransform saveAT = g.getTransform();
        AffineTransform transformer = g.getTransform();//new AffineTransform();
        
        transformer.translate(XX,YY);
        transformer.rotate(Math.toRadians(angle+AngleOffset));
        if (isEnableAutoScaleViseur()==true)
        	transformer.scale(1,2.5f/Math.pow(((max-min)),0.15)); // Jouer sur ce parametre la 
        int viseur_size = size/7;//(int) (0.05f*w);
        transformer.translate(-(viseur_size/2),-(viseur_size/2));

        g.setTransform(transformer);
    	g.fillRect(0, 0, 5, 5);
    	g.drawImage(IMG_Viseur, 0, 0, this);
    //	System.err.println("Repaint viseur");
    	g.setTransform( saveAT);
    	
    	g.setColor(Color.BLACK);
    	
    	Rectangle2D rect2D = getFontMetrics(getFont()).getStringBounds(""+value, g);
    	
    	int offsx = (int) (rect2D.getWidth()/2);
     	g.drawString(""+value, size/2-offsx,size/2+6);
    
    	g.translate(size/2-offsx-4,size/2+6-4);
        drawTextHighlight(g, ""+value, 4, 1f);
        g.translate(-(size/2),-(size/2));
    }

    
    ////// LISTENER MANAGEMENT ///////////////////////////////////////////////
  
	/**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener listener)
    {
    	listeners.add (listener);
    }
    /**
     * DOCUMENT ME!
     */
    public void removeAllChangeListener()
    {
        listeners.removeAll(listeners); // TODO : pas sure
    }
    /* DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void fireChangeEvent(ChangeEvent event)
    {
        int total = listeners.size ();

        for (int i = 0; i < total; i++)
        {
        	ChangeListener l = listeners.elementAt(i);

            if (l instanceof ChangeListener)
            {
                (l).stateChanged (event);
            }
        }
    }
    
    ////// GET & SET ///////////////////////////////////////////////
   
    public float getValue()
    {
    	return value/getDivider();
    }
    private float getDivider() {
		return Divider;
	}
    public void setDivider(float divider) {
		Divider=divider;
	}

	public void setValue(int val)
    {
    	this.value = val;
    	this.old_value = val;
    	angle = (int)ConvertValueToAngle(val, min, max, MaximumAngle);
    	repaint();
    }
    public void setMaximum(int max)
    {
    	if (max>this.min)
    	{
    		this.max = max;
    		this.setValue(0/*old_value*/);
    		init_buffer_fond(true);
    		
    	}
    	else
    		System.err.println("Max cannot be lower than Min");
    }
    public void setMinimum(int min)
    {
    	if (min<this.max)
    	{
    		this.min = min;
    		this.setValue(old_value);
    		init_buffer_fond(true);
    	}
    	else
    		System.err.println("Min cannot be higher than Max");
    }
    public int getMaximum()
    {
        return max;
    }
    public int getMinimum()
    {
        return min;
    }
	public synchronized boolean isAutoAdjustWhileDrag() {
		return EnableAutoAdjustWhileDrag;
	}
	public synchronized void setAutoAdjustWhileDrag(boolean autoAdjustWhileDrag) {
		EnableAutoAdjustWhileDrag = autoAdjustWhileDrag;
		repaint();
	}
	public synchronized boolean isEnableAutoAdjustWhileDrag() {
		return EnableAutoAdjustWhileDrag;
	}
	public synchronized void setEnableAutoAdjustWhileDrag(boolean enableAutoAdjustWhileDrag) {
		EnableAutoAdjustWhileDrag = enableAutoAdjustWhileDrag;
		repaint();
	}
	public synchronized boolean isEnableTickString() {
		return EnableTickString;
	}
	/**
	 * Affiche le lable des Ticks
	 * @param enableTickString
	 */
	public synchronized void setEnableTickString(boolean enableTickString) {
		EnableTickString = enableTickString;
		init_buffer_fond(true);
	}
	public synchronized int getTickDivider() {
		return TickDivider;
	}
	/**
	 * Choisis le diviseur pour diminuer le nombre de ticks si on traite une etendue enorme
	 * @param v
	 */
	public synchronized void setTickDivider(int tickDivider) {
		if (tickDivider>0)
		TickDivider = tickDivider;
		init_buffer_fond(true);
	}
	
	public boolean isEnableTick()
	{
		return EnabeTicks;
	}
	
	/**
	 * Dessine ou non les divisions
	 * @param v
	 */
	public void setEnableTick(boolean v)
	{
		this.EnabeTicks=v;
		init_buffer_fond(true);
	}

	/**
	 * Pour savoir si le slider va +- se bloquer au 0; de maniere a ne pas passer du min au max et l'inverse d'un coup sec (mais ca foire)
	 * @return
	 */
	public synchronized final boolean isEnableOverflowCheck()
	{
		return EnableOverflow;
	}

	/**
	 * Pour savoir si le slider va +- se bloquer au 0; de maniere a ne pas passer du min au max et l'inverse d'un coup sec (mais ca foire)
	 * @return
	 */
	public synchronized final void setEnableOverflow(boolean enableOverflow)
	{
		EnableOverflow = enableOverflow;
	}

	public synchronized final boolean isEnableAntialiasing()
	{
		return EnableAntialiasing;
	}

	public synchronized final void setEnableAntialiasing(boolean enableAntialiasing)
	{
		EnableAntialiasing = enableAntialiasing;
		init_buffer_fond(true);
	}
	
	
	/**
	 * Retourne l'angle maximum qui correspond au max (generalement 360 pour un truc normal)
	 * @return 0 a 360
	 */
	public int getMaximumAngle() {
		return MaximumAngle;
	}
	
	/**
	 * Permet de choisir l'angle maximum des Ticks (generalement 360)
	 * @param v 0 a 360
	 */
	public void setMaximumAngle(int v) {
		MaximumAngle=v;
		init_buffer_fond(true);
		setValue(old_value);
		
	}
	public synchronized int getAngleOffset() {
		return AngleOffset;
	}
	public synchronized void setAngleOffset(int angleOffset) {
		AngleOffset = angleOffset;
		init_buffer_fond(true);
		
	}
	
	public synchronized final Color getViseurColor()
	{
		return ViseurColor;
	}

	public synchronized final void setViseurColor(Color viseurColor)
	{
		ViseurColor = viseurColor;
	}

	public synchronized final boolean isEnableAutoScaleViseur()
	{
		return EnableAutoScaleViseur;
	}

	public synchronized final void setEnableAutoScaleViseur(boolean autoScaleViseur)
	{
		EnableAutoScaleViseur = autoScaleViseur;
		repaint();
	}

	public synchronized final boolean isEnableMinimalTicksOnly()
	{
		return EnableMinimalTicksOnly;
	}

	public synchronized final void setEnableMinimalTicksOnly(
			boolean enableMinimalTicksOnly)
	{
		EnableMinimalTicksOnly = enableMinimalTicksOnly;
		init_buffer_fond(true);
		repaint();
	}
	
	/**
	 * Permet de recuperer la valeur d'un increment (cad entre 2 valeurs)
	 * @return
	 */
    private double getIncrementValue() {
    	return (double)MaximumAngle/(double)(max-min);
	}

	public void setEnableLabel(boolean b) {
		EnableLabel = b;
	}
	public boolean getEnableLabel() {
		return EnableLabel;
	}



	public void mouseClicked(MouseEvent arg0)
	{
		if (arg0.getClickCount()==2)
		{
			ShowEditValueBox();
		}
		
	}

	/**
	 * Affiche un petit truc pour editer avec un text field la value du slider precisement.
	 */
	private void ShowEditValueBox()
	{
		String s = (String)JOptionPane.showInputDialog(
                new JFrame(),
                "Change value of "+getName(),
                "Change value",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""+getValue());

//If a string was returned, say so.
if ((s != null) && (s.length() > 0)) {

Double valeur = Double.parseDouble(""+s);
value = (int)(valeur * Divider);
setValue(value);
ChangeEvent ch = new ChangeEvent(this);
fireChangeEvent (ch);
return;
}

//If you're here, the return value was null/empty.
System.err.println("Valeur non rentrée");
		
	}


	
	public void mouseEntered(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseReleased(MouseEvent mouseevent) {
		// TODO Auto-generated method stub
		
	}

	public void setOld_cadran(int old_cadran)
	{
		this.old_cadran = old_cadran;
	}

	public int getOld_cadran()
	{
		return old_cadran;
	}
	
	private void drawTextHighlight(Graphics2D g2, String title,
			int size, float opacity) {
			g2.setColor(Color.BLACK);
			for (int i = -size; i <= size; i++) {
			for (int j = -size; j <= size; j++) {
			double distance = i * i + j * j;
			float alpha = opacity;
			if (distance > 0.0d) {
			alpha = (float) (1.0f /
			((distance * size) * opacity));
			}
			g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
			g2.drawString(title, i + size, j + size);
			}
			}
			}
}
