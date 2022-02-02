package io.github.warnotte.waxlib3.W2D.PanelGraphique;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import io.github.warnotte.waxlib3.OBJ2GUI.JWPanel;
import io.github.warnotte.waxlib3.OBJ2GUI.ParseurAnnotations;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_CLASS;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_FIELD_TYPE;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JWColor;


@GUI_CLASS(type=GUI_CLASS.Type.BoxLayout, BoxLayout_property=GUI_CLASS.Type_BoxLayout.Y)
public class ConfigurationColor {
    
	private static String XMLSaveFile = "data/ColorsConfig.xml";
	
	@GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SELECTION_AREA = new JWColor(255,0,0,64);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SELECTION_BORDER_AREA = new JWColor(0,0,0,255);
   
	@GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_FOND_GRILLE = new JWColor(255, 255, 255);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_GRILLE_1 = new JWColor(220, 220, 220);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_GRILLE_2 = new JWColor(180, 180, 180);
  /*  @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_MOUSEONSOMETHING = new JWColor(Color.RED);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SELECTION = new JWColor(Color.MAGENTA);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_COLLISION = new JWColor(Color.red);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_COLLISION_NO_COST = new JWColor(Color.GREEN);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_COLLISION_BORDER = new JWColor(Color.black);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_TRANCHESEPARATOR_SELECTED = new JWColor(Color.RED);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_TRANCHESEPARATOR_UNSELECTED = new JWColor(Color.GREEN);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SEGMENT = new JWColor(Color.BLUE);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_NODE = new JWColor(Color.GREEN);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_NODE_BORDER = new JWColor(Color.BLACK);
    public synchronized JWColor getCOLOR_COLLISION_NO_COST()
	{
		return COLOR_COLLISION_NO_COST;
	}
	public synchronized void setCOLOR_COLLISION_NO_COST(JWColor color_collision_no_cost)
	{
		COLOR_COLLISION_NO_COST = color_collision_no_cost;
	}

	@GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SELECTION_AREA = new JWColor(255,0,0,64);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_ANGLE_VIEW_1 = new JWColor(64,255,64,127);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_ANGLE_VIEW_2 = new JWColor(255,64,64,127);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SELECTION_BORDER_AREA = new JWColor(0,0,0,255);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_SEGMENT_POLYGON = new JWColor(127, 127, 127,255);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_COLLISION_BLOCKTOBLOCK = new JWColor(255, 0, 255,255);
    @GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.COLOR)
    JWColor COLOR_MOUSEONZONE = new JWColor(255, 0, 255,127);
    */
    
   /* 
    public synchronized void setCOLOR_MOUSEONZONE(JWColor color_mouseonzone)
	{
		COLOR_MOUSEONZONE = color_mouseonzone;
	}
	public synchronized JWColor getCOLOR_COLLISION_BLOCKTOBLOCK() {
        return COLOR_COLLISION_BLOCKTOBLOCK;
    }
    public synchronized void setCOLOR_COLLISION_BLOCKTOBLOCK(
    	JWColor color_collision_blocktoblock) {
        COLOR_COLLISION_BLOCKTOBLOCK = color_collision_blocktoblock;
    }
    public synchronized JWColor getCOLOR_SEGMENT_POLYGON() {
        return COLOR_SEGMENT_POLYGON;
    }
    public synchronized void setCOLOR_SEGMENT_POLYGON(JWColor color_segment_polygon) {
        COLOR_SEGMENT_POLYGON = color_segment_polygon;
    }
    public synchronized JWColor getCOLOR_SELECTION_BORDER_AREA() {
        return COLOR_SELECTION_BORDER_AREA;
    }
    public synchronized void setCOLOR_SELECTION_BORDER_AREA(
    	JWColor color_selection_border_area) {
        COLOR_SELECTION_BORDER_AREA = color_selection_border_area;
    }
    public synchronized JWColor getCOLOR_ANGLE_VIEW_1() {
        return COLOR_ANGLE_VIEW_1;
    }
    public synchronized void setCOLOR_ANGLE_VIEW_1(JWColor color_angle_view_1) {
        COLOR_ANGLE_VIEW_1 = color_angle_view_1;
    }
    public synchronized JWColor getCOLOR_ANGLE_VIEW_2() {
        return COLOR_ANGLE_VIEW_2;
    }
    public synchronized void setCOLOR_ANGLE_VIEW_2(JWColor color_angle_view_2) {
        COLOR_ANGLE_VIEW_2 = color_angle_view_2;
    }
    public synchronized JWColor getCOLOR_SELECTION_AREA() {
        return COLOR_SELECTION_AREA;
    }
    public synchronized void setCOLOR_SELECTION_AREA(JWColor color_selection_area) {
        COLOR_SELECTION_AREA = color_selection_area;
    }*/
    public synchronized JWColor getCOLOR_FOND_GRILLE() {
        return COLOR_FOND_GRILLE;
    }
    public synchronized void setCOLOR_FOND_GRILLE(JWColor color_fond_grille) {
        COLOR_FOND_GRILLE = color_fond_grille;
    }
    public synchronized JWColor getCOLOR_GRILLE_1() {
        return COLOR_GRILLE_1;
    }
    public synchronized void setCOLOR_GRILLE_1(JWColor color_grille_1) {
        COLOR_GRILLE_1 = color_grille_1;
    }
    public synchronized JWColor getCOLOR_GRILLE_2() {
        return COLOR_GRILLE_2;
    }
    public synchronized void setCOLOR_GRILLE_2(JWColor color_grille_2) {
        COLOR_GRILLE_2 = color_grille_2;
    }
   /* public synchronized JWColor getCOLOR_DROITE_SELECTION() {
        return COLOR_DROITE_SELECTION;
    }
    public synchronized void setCOLOR_DROITE_SELECTION(
    	JWColor color_droite_selection) {
        COLOR_DROITE_SELECTION = color_droite_selection;
    }
    public synchronized JWColor getCOLOR_DROITE() {
        return COLOR_DROITE;
    }
    public synchronized void setCOLOR_DROITE(JWColor color_droite) {
        COLOR_DROITE = color_droite;
    }*/
  /*  public synchronized JWColor getCOLOR_MOUSEONSOMETHING() {
        return COLOR_MOUSEONSOMETHING;
    }
    public synchronized void setCOLOR_MOUSEONSOMETHING(JWColor color_node_selection) {
	COLOR_MOUSEONSOMETHING = color_node_selection;
    }
    public synchronized JWColor getCOLOR_COLLISION() {
        return COLOR_COLLISION;
    }
    public synchronized void setCOLOR_COLLISION(JWColor color_collision) {
        COLOR_COLLISION = color_collision;
    }
    public synchronized JWColor getCOLOR_COLLISION_BORDER() {
        return COLOR_COLLISION_BORDER;
    }
    public synchronized void setCOLOR_COLLISION_BORDER(
    	JWColor color_collision_border) {
        COLOR_COLLISION_BORDER = color_collision_border;
    }
    public synchronized JWColor getCOLOR_SEGMENT() {
        return COLOR_SEGMENT;
    }
    public synchronized void setCOLOR_SEGMENT(JWColor color_segment) {
        COLOR_SEGMENT = color_segment;
    }
    public synchronized JWColor getCOLOR_NODE() {
        return COLOR_NODE;
    }
    public synchronized void setCOLOR_NODE(JWColor color_node_segment) {
        COLOR_NODE = color_node_segment;
    }
    public synchronized JWColor getCOLOR_NODE_BORDER() {
        return COLOR_NODE_BORDER;
    }
    public synchronized void setCOLOR_NODE_BORDER(
    	JWColor color_node_segment_border) {
        COLOR_NODE_BORDER = color_node_segment_border;
    }
    
    public synchronized JWColor getCOLOR_TRANCHESEPARATOR_SELECTED() {
        return COLOR_TRANCHESEPARATOR_SELECTED;
    }
    public synchronized void setCOLOR_TRANCHESEPARATOR_SELECTED(
    	JWColor trancheseparator_selected) {
        COLOR_TRANCHESEPARATOR_SELECTED = trancheseparator_selected;
    }
    public synchronized JWColor getCOLOR_TRANCHESEPARATOR_UNSELECTED() {
        return COLOR_TRANCHESEPARATOR_UNSELECTED;
    }
    public synchronized void setCOLOR_TRANCHESEPARATOR_UNSELECTED(
    	JWColor trancheseparator_unselected) {
        COLOR_TRANCHESEPARATOR_UNSELECTED = trancheseparator_unselected;
    }
    
    public synchronized JWColor getCOLOR_SELECTION() {
        return COLOR_SELECTION;
    }
    
    public synchronized void setCOLOR_SELECTION(JWColor color_selection) {
        COLOR_SELECTION = color_selection;
    }
    */
    
    public static void main(String []args) throws IOException
    {
	//ConfigurationColor c = new ConfigurationColor();
	//c.save();
	//ColorConfiguration.load(c);
	
    }

    
    public static ConfigurationColor load() throws IOException {
    	XStream xstream = new XStream(new DomDriver());
  	  File f = new File(XMLSaveFile);//DialogDivers.LoadDialog("xml", MainFrame.this);
  	  
          // Redirection du fichier c:/temp/article.xml vers un flux
          // d'entr�e fichier
          FileInputStream fis = new FileInputStream(f);
          
          try {
              // D�s�rialisation du fichier c:/temp/article.xml vers un nouvel
              // objet article
              ConfigurationColor c = (ConfigurationColor) xstream.fromXML(fis);

              return c;
          } finally {
              // On s'assure de fermer le flux quoi qu'il arrive
              fis.close();
          }
  }
    
    protected static void save(ConfigurationColor c) {
    	try {
        	
    		   // String f =DialogDivers.SaveDialog("xml", MainFrame.this);
    		    String f = XMLSaveFile;
    	    	if (f!=null)
    	    	{
    		        	//FileOutputStream fos = new FileOutputStream(f);
    	    		PrintStream ps = new PrintStream(new File(f));
    		        	// Instanciation de la classe XStream		
    			        XStream xstream = new XStream(new DomDriver());
        				// Convertion du contenu de l'objet article en XML
        				String xml = xstream.toXML(c);
        				// Affichage de la conversion XML
        				ps.print(xml);
        				ps.flush();
        				ps.close();
        				
    	        	}
    		    } catch (Exception e1) {
    		    	//Logs.getLogger().fatal(e1);
    		    	DialogDivers.Show_dialog(e1, "");
    			e1.printStackTrace();
    		    }
    }
    
    
    public synchronized JWColor getCOLOR_SELECTION_AREA()
	{
		return COLOR_SELECTION_AREA;
	}
	public synchronized JWColor getCOLOR_SELECTION_BORDER_AREA()
	{
		return COLOR_SELECTION_BORDER_AREA;
	}
	public synchronized void setCOLOR_SELECTION_AREA(JWColor cOLORSELECTIONAREA)
	{
		COLOR_SELECTION_AREA = cOLORSELECTIONAREA;
	}
	public synchronized void setCOLOR_SELECTION_BORDER_AREA(JWColor cOLORSELECTIONBORDERAREA)
	{
		COLOR_SELECTION_BORDER_AREA = cOLORSELECTIONBORDERAREA;
	}
	public void popupConfig() throws Exception
    {
 	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	frame.setSize(480,350);
	frame.setPreferredSize(new Dimension(480,350));
	frame.setVisible(true);
	frame.setLayout(new BorderLayout());
	// Cr�e ces 3 panels sans ajouter la variable change listener qui pompe les ressources si 10000 de changements
	final ConfigurationColor cc = this;
	final JWPanel panel = (JWPanel) ParseurAnnotations.CreatePanelFromObject("Configuration des couleurs", cc,false);
	frame.add(new JScrollPane(panel),BorderLayout.CENTER);
	JButton button = new JButton();
	button.setText("Save");
	button.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		ConfigurationColor.save(cc);
	    }
	});
	
	frame.add(button,BorderLayout.SOUTH);
	frame.validate();
	frame.pack();
    }
	/**/
	public synchronized static String getXMLSaveFile()
	{
		return XMLSaveFile;
	}
	public synchronized static void setXMLSaveFile(String xMLSaveFile)
	{
		XMLSaveFile = xMLSaveFile;
	
	}
}
