package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper.Renderer.DefaultMapRenderer;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    

    private static final long serialVersionUID = 1L;
    private BandManager bandManager = null;
    private int oldrealmouseY;
    private Band SelectedBand;  //  @jve:decl-index=0:
    private DefaultMapRenderer mapRenderer = new DefaultMapRenderer();  //  @jve:decl-index=0:
    
    public synchronized DefaultMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public synchronized void setMapRenderer(DefaultMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame();
	
	BandManager bandManager = new BandManager();
	Band element1 = new Band("Element1", 0);
	//Band element2 = new Band("Element2", 0);
	//Band element3 = new Band("Element3", 0);
	//Band element4 = new Band("Element4", 0);
	bandManager.ajoute_element(element1);
	/*bandManager.ajoute_element(element2);
	bandManager.ajoute_element(element3);
	bandManager.ajoute_element(element4);*/
	
	JPanel panel = new MapPanel(bandManager);
	frame.add(panel);
	frame.setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public MapPanel(BandManager bm) {
	super();
	this.setBandManager(bm);
	initialize();
	
    }
    
    public MapPanel() {
	this(new BandManager());
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(300, 200);
	this.setFocusable(true);
	this.requestFocus();
	this.addMouseMotionListener(this);
	this.addMouseListener(this);
	this.addKeyListener(this);
	
    }
    
    
    Font fnt_labels = new Font("Impact", Font.PLAIN, 24);
    private Band ClickedBand;
    private int pressedmouseY;
    //private final boolean LUXURY = true;
    
    @Override
	public void paint(Graphics g)
    {
	int h = this.getHeight();
	int w = this.getWidth();
	int BandH = this.getBandManager().MAX_VALUE;
	float ratio = (float)BandH / (float)h;
	Graphics2D g2 = (Graphics2D)g;
	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // Allez :) on n'est plus en < 1995 qd meme ; Par contre ca bouffe :)
	
	int nbr_elements = getBandManager().elements.size();
	//Random rand = new Random();
	
	int y = 0;
	for (int i = 0 ; i < nbr_elements;i++)
	{
	    Band band = getBandManager().elements.get(i);
	    
	    float thisBandH = band.getLength()/ratio;
	    
	    mapRenderer.paint(g2, band, 0, y, w, thisBandH);
	    
	    
	    
	    
	    g2.setFont(fnt_labels);
	    g2.setStroke(new BasicStroke(2));
	    g2.setColor(Color.black);
	    g2.drawLine(0, y, w, (y));
	    
	    if (ClickedBand==band)
	    {
		g2.setColor(new Color(255,255,255,64));
	    	g2.fillRect(0, y, w, (int) (y+thisBandH));
	    }
	    
	    FontMetrics fm = g.getFontMetrics();
	    String str = band.toString();
	    Rectangle2D area = fm.getStringBounds(str, g);
	    
	    
	    
	    /*if (LUXURY==false)
	    {
		if (ClickedBand==band)
		    g2.setColor(Color.WHITE);
		else
		    g2.setColor(Color.BLACK);
		g2.drawString(""+str, (int)(w/2-area.getWidth()/2),(int)(y+thisBandH/2 + area.getHeight()/2));
	    }
	    else*/
	    {
		if (ClickedBand==band)
		    drawTextHighlight(g2, ""+str,3,(int)(w/2-area.getWidth()/2),(int)(y+thisBandH/2 + area.getHeight()/2), 1.0f,Color.BLACK);
		else
		    drawTextHighlight(g2, ""+str,3,(int)(w/2-area.getWidth()/2),(int)(y+thisBandH/2 + area.getHeight()/2), 1.0f,Color.WHITE);
		if (ClickedBand==band)
		g2.setColor(Color.BLACK);
		else
		    g2.setColor(Color.WHITE);
		g2.drawString(""+str, (int)(w/2-area.getWidth()/2),(int)(y+thisBandH/2 + area.getHeight()/2));
	}
	    //g2.setStroke(new BasicStroke(1));
	    y+=thisBandH;
	}
    }

    private void drawTextHighlight(Graphics2D g2, String title,
		int size, int x, int y, float opacity, Color color) {
		
		for (int i = -size; i <= size; i++) {
		for (int j = -size; j <= size; j++) {
		double distance = i * i + j * j;
		float alpha = opacity;
		if (distance > 0.0d) {
		alpha = (float) (1.0f /
			
		((distance * size) * opacity));
		}
		Composite c = g2.getComposite();
		g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
		g2.drawString(title, i + x, j + y);
		g2.setComposite(c);
		}
		}
		}
    
    
    public void mouseClicked(MouseEvent mouseevent) {
	// TODO Auto-generated method stub
	
    }

    
    public void mouseEntered(MouseEvent mouseevent) {
	this.requestFocus(true);
	
    }

    
    public void mouseExited(MouseEvent mouseevent) {
	// TODO Auto-generated method stub
	
    }

    
    public void mousePressed(MouseEvent e) {
	int h = this.getHeight();
	int BandH = this.getBandManager().MAX_VALUE;
	float ratio = (float)BandH / (float)h;
	int mouseY = e.getY();
	int realmouseY = (int)(mouseY * ratio);
	
	Band b = getBandManager().getBandFromLevel(realmouseY);
	boolean border = getBandManager().isStartOfBand(realmouseY, b);
	if (border==true)
	    SelectedBand = getBandManager().getBandFromLevel(realmouseY-1);
	else
	SelectedBand = b;
	//System.err.println("Bande cliquée = "+SelectedBand);
	oldrealmouseY = realmouseY;
	pressedmouseY = realmouseY;
	ClickedBand = SelectedBand;
    }

    
    public void mouseReleased(MouseEvent e) {
	int h = this.getHeight();
	int BandH = this.getBandManager().MAX_VALUE;
	float ratio = (float)BandH / (float)h;
	int mouseY = e.getY();
	int realmouseY = (int)(mouseY * ratio);
	
	getBandManager().affiche();
	
	if (e.getButton()==2)
	{
	    int offset = getBandManager().getOffsetFromBand(SelectedBand);
	    // Prendre l'offset de la bande courante.
	    // Soustraire l'offset a la position de la souris
	    int delta = realmouseY - offset+2;
	    getBandManager().ajoute_element(new Band("New Band", 0), delta);
	    System.err.println("New size will be "+delta);
	    getBandManager().resize_element(SelectedBand, delta);
	    //SelectedBand.setLength(delta);
	    int idx = getBandManager().elements.indexOf(SelectedBand);
	    System.err.println("Will swap "+idx +" -> "+(getBandManager().elements.size()-1));
	    if (getBandManager().elements.size()>2)
	    getBandManager().swap_band(idx+1, getBandManager().elements.size()-1);
	    
	}
	ClickedBand = SelectedBand;
	SelectedBand=null;
	repaint();
	
    }

    public synchronized Band getClickedBand() {
        return ClickedBand;
    }

    public synchronized void setClickedBand(Band clickedBand) {
        ClickedBand = clickedBand;
    }

    
    public void mouseDragged(MouseEvent e) {
	int h = this.getHeight();
	int BandH = this.getBandManager().MAX_VALUE;
	float ratio = (float)BandH / (float)h;
	int mouseY = e.getY();
	int realmouseY = (int)(mouseY * ratio);
	int delta = (oldrealmouseY - realmouseY);
	//System.err.println("Delta = " + delta);
	 
	if (SelectedBand!=null)
	{
	    
		int modif = e.getModifiersEx();
	    if ((modif & InputEvent.BUTTON1_DOWN_MASK ) == InputEvent.BUTTON1_DOWN_MASK ) 
	    {
		System.err.println("Resize band "+SelectedBand);
		getBandManager().resize_element(SelectedBand, (int)SelectedBand.getLength()-delta);
	    }
	    if ((modif & InputEvent.BUTTON3_DOWN_MASK ) == InputEvent.BUTTON3_DOWN_MASK) 
	    {
		delta = pressedmouseY - realmouseY;
		if (Math.abs(delta) >=25 )
		{
		    int value = -1;
		    if (delta < 0) value =1;
		    System.err.println("Swap band "+SelectedBand+ " val = "+value);
		    if (getBandManager().getBandFromIndex(SelectedBand)+value!=-1)
		    if ((getBandManager().getBandFromIndex(SelectedBand)+value)<getBandManager().getElements().size())
		    getBandManager().swap_band(getBandManager().getBandFromIndex(SelectedBand), getBandManager().getBandFromIndex(SelectedBand)+value);
		    pressedmouseY = realmouseY;
		}
	    }
	    repaint();
	}
	else
	System.err.println("No Band selected");
	
	oldrealmouseY = realmouseY;
    }
    
    
    public void mouseMoved(java.awt.event.MouseEvent e) {
	int h = this.getHeight();
//	int w = this.getWidth();
	int BandH = this.getBandManager().MAX_VALUE;
	float ratio = (float)BandH / (float)h;
	int mouseY = e.getY();
	int realmouseY = (int)(mouseY * ratio);
	System.err.println("Real position "+realmouseY);
	
	Band band = getBandManager().getBandFromLevel(realmouseY);
	if (band!=null)
	    System.err.println("Band selected "+band);
	
	boolean border = getBandManager().isStartOfBand(realmouseY, band);
	//System.err.println("Border = "+ border);
	if (border==true)
	    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	else
	    setCursor(Cursor.getDefaultCursor());
	
	oldrealmouseY = realmouseY;
	
    }

    
    public void keyPressed(KeyEvent keyevent) {
	// TODO Auto-generated method stub
	
    }

    
    public void keyReleased(KeyEvent keyevent) {
	
	Band band=ClickedBand;//getBandManager().getBandFromLevel(oldrealmouseY);
	if (band!=null)
	{
		if (keyevent.getKeyChar()=='a')
		    getBandManager().resizeAllwithAverage();
		if (keyevent.getKeyChar()=='+')
		if (keyevent.getKeyChar()=='+')
			getBandManager().resize_element(band, (int)band.getLength()+1);
		if (keyevent.getKeyChar()=='-')
			getBandManager().resize_element(band, (int)band.getLength()-1);
		if (keyevent.getKeyChar()=='i')
			getBandManager().invertBands();
	
	    if (keyevent.getKeyChar()=='o')
		getBandManager().swap_band(getBandManager().getBandFromIndex(band), getBandManager().getBandFromIndex(band)+1);
	    if (keyevent.getKeyChar()=='p')
			getBandManager().swap_band(getBandManager().getBandFromIndex(band), getBandManager().getBandFromIndex(band)-1);
	    if (keyevent.getKeyChar()==KeyEvent.VK_DELETE)
			getBandManager().retire_element(ClickedBand);
			
	repaint();
	}
	
    }

    
    public void keyTyped(KeyEvent keyevent) {
	// TODO Auto-generated method stub
	
    }

    public void setBandManager(BandManager bandManager) {
	this.bandManager = bandManager;
    }

    public BandManager getBandManager() {
	return bandManager;
    }

}
