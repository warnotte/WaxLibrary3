package org.warnotte.OBJ2GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

import javax.swing.CellRendererPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.warnotte.OBJ2GUI.Events.MyChangedEvent;
import org.warnotte.OBJ2GUI.Events.MyEventListener;
import org.warnotte.OBJ2GUI.Tests.Test;
import org.warnotte.waxlibswingcomponents.Swing.Component.JColorChooserButton;
import org.warnotte.waxlibswingcomponents.Swing.Component.WaxSlider.WFlatSlider;
import org.warnotte.waxlibswingcomponents.Swing.Component.WaxSlider.WRoundSlider;
import org.xml.sax.SAXException;

/**
 * Classe permettant de sauvegarder/charger un XML � partir ou vers un GUI genere par ParseurAnnotations
 * afin de customiser les labels ainsi que les tooltips gener� par le guiifieur et qui sont plutot codeur orient�
 * @author Warnotte Renaud Novembre 2007
 *
 */
public class GUI2XMLLabel {

	
	private static GUI2XMLLabel instance= new GUI2XMLLabel();

	public static GUI2XMLLabel getInstance(){
		return instance;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	final Test objet = new Test();
		
		JFrame frame = new JFrame();
		frame.setSize(320,200);
		frame.setPreferredSize(new Dimension(320,200));
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		
		// Cr�e ces 3 panels sans ajouter la variable change listener qui pompe les ressources si 10000 de changements
		final JWPanel panel = (JWPanel) ParseurAnnotations.CreatePanelFromObject("Main configuration", objet,false);
		
		panel.addMyEventListener(new MyEventListener()
		{
			public void myEventOccurred(MyChangedEvent e)
			{
			//	System.err.println("*** Object has changed make the needed things ...");
			}
		});
		
		
		//obj1.addVariableChangeListener( new ModificationListener(panObj,obj1));
	
		// Ajoute du panel dans la frame
		//frame.add(new JScrollPane(panel),BorderLayout.CENTER);
		frame.add(new JScrollPane(panel),BorderLayout.CENTER);
		JButton button =  new JButton();
		button.setText("Debug");
		button.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				objet.debug();
				panel.fireMyEvent(new MyChangedEvent(this, null));
			}
		});
		
		JButton button2 =  new JButton();
		button2.setText("Refresh Panel");
		button2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				try {
					ParseurAnnotations.Refresh_PanelEditor_For_Object("", panel,objet,panel,false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton button3 =  new JButton();
		button3.setText("Add Object to Vector of Object");
		button3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				objet.addObjets();
				try {
					ParseurAnnotations.Refresh_PanelEditor_For_Object("", panel,objet,panel,false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		frame.add(button,BorderLayout.SOUTH);
		frame.add(button2,BorderLayout.NORTH);
		frame.add(button3,BorderLayout.EAST);
		
		frame.validate();
		frame.pack();
		
		
		GUI2XMLLabel.SaveLabel(panel, "f:\\\\test.xml");
		GUI2XMLLabel.LoadLabel(panel, "f:\\\\test.xml");
	}
	
	HashMap<String, String> labels_map = new HashMap<String, String>();
	HashMap<String, String> tooltips_map = new HashMap<String, String>();
	private final boolean VERBOSE=false;
	static Document document;
	static Element	racine;
	
	public void debug()
	{
		Set<String> key = labels_map.keySet();
		for (int i = 0 ; i < key.size();i++)
		{
			String cle = (String) key.toArray()[i];
			String value = labels_map.get(cle);
			System.err.println(cle+"="+value);
			value = tooltips_map.get(cle);
			System.err.println(cle+"="+value);
		}
	}
	
	public GUI2XMLLabel()
	{
	}
	
	public static boolean LoadLabel(JWPanel panel, String filename) throws IOException, ParserConfigurationException, SAXException
	{
		return getInstance().LoadLabelM(panel, filename);
	}
	public static boolean SaveLabel(JWPanel panel, String filename) throws FileNotFoundException
	{
		return getInstance().SaveLabelM(panel, filename);
	}
	
	/**
	 * Charge un XML et assigne les label et tooltips au panel generer par le generateur de gui
	 * @param panel Le panel dont il faut modifier les labels etc.
	 * @param filename Le fichier XML contenant les Elements.
	 * @throws IOException
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws JDOMException
	 */
	private boolean LoadLabelM(JWPanel panel, String filename) throws IOException, ParserConfigurationException, SAXException
	{
		labels_map.clear();
		tooltips_map.clear();

		
	//	 SAXParserFactory factory = SAXParserFactory.newInstance();
    //     SAXParser saxParser = factory.newSAXParser();
         
	//	SAXBuilder sxb = new SAXBuilder();
		File file = new File(filename);
		if (file.exists()==false)
		{
		    System.err.println("Warning i cannot read "+filename+" so i don't load Labels");
		    return false;
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		document = dBuilder.parse(file);
		racine = document.getDocumentElement();//.getRootElement();
		
		
		NodeList     listConn = racine.getElementsByTagName("element");//racine.getChildren("element"); //$NON-NLS-1$
		
		
		for (int j = 0; j < listConn.getLength(); j++)
		{
			Element courant = (Element)listConn.item(j);
			String cle = courant.getAttribute("name");//.getValue();
			String tooltips = courant.getAttribute("tooltips");//.getValue();
			String label = courant.getAttribute("label");//.getValue();
			if (cle==null)
				System.err.println("Attention une cle est null dans le fichier "+filename);
			labels_map.put(cle, label);
			tooltips_map.put(cle, tooltips);
		}
		/*
		while (i.hasNext())
		{
			Element courant = (Element)i.next();
			String cle = courant.getAttribute("name");//.getValue();
			String tooltips = courant.getAttribute("tooltips");//.getValue();
			String label = courant.getAttribute("label");//.getValue();
			if (cle==null)
				System.err.println("Attention une cle est null dans le fichier "+filename);
			labels_map.put(cle, label);
			tooltips_map.put(cle, tooltips);
		}
		*/
		recursToPanel(panel,0);
		return true;
	}
	
	private boolean SaveLabelM(JWPanel panel, String filename) throws FileNotFoundException
	{
		if (VERBOSE==true)
		System.err.println("Saving panel named "+panel.getName());
		File f = new File(filename);

		PrintStream ps = new PrintStream(f);
		labels_map.clear();
		tooltips_map.clear();
		PanelTorecurs(panel,0);
		ps.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
		ps.println("<Elements>");
		Set<String> key = labels_map.keySet();
		for (int i = 0 ; i < key.size();i++)
		{
			String cle = (String) key.toArray()[i];
			String tips_value = tooltips_map.get(cle);
			String label_value = labels_map.get(cle);
			if (tips_value==null) tips_value="";
			if (tips_value.length()==0) tips_value="";
			if (tips_value.equals("null")) tips_value="";
			ps.println("<element name=\""+cle+"\" label=\""+label_value+"\" tooltips=\""+tips_value+"\"></element>");
		}
		
		ps.println("</Elements>");
		ps.flush();
		ps.close();
		return true;
	}

	private void PanelTorecurs(JComponent cp2, int depth) {
		String tab="";
		for (int i = 0 ;i < depth;i++)
			tab+="\t";
		int nbr_comps = cp2.getComponents().length;
		for (int i = 0 ; i < nbr_comps;i++)
		{
			if (cp2.getComponents()[i] instanceof CellRendererPane) 
				continue;
			JComponent cp = (JComponent) cp2.getComponents()[i];
			if (( cp instanceof JXTaskPane) || ( cp instanceof JXCollapsiblePane))
			{
				if ( cp instanceof JXCollapsiblePane)
				{
					String label = ((JXCollapsiblePane)cp).getParent().getName();
					labels_map.put(label, label);
					label = ((JXCollapsiblePane)cp).getToolTipText();
					tooltips_map.put(label, label);
				}
				else
				{
					String label = ((JXTaskPane)cp).getTitle();
					labels_map.put(cp.getName(), label);
					label = ((JXTaskPane)cp).getToolTipText();
					tooltips_map.put(cp.getName(), label);
				}
				PanelTorecurs(cp, depth+1);
			}
			else
			if (
					(cp instanceof JCheckBox) || 
					(cp instanceof JLabel)||
					(cp instanceof JTextField)||
					(cp instanceof JList)||
					(cp instanceof JComboBox)||
					(cp instanceof JColorChooserButton)||
					(cp instanceof JXDatePicker)||
					(cp instanceof JSlider)||
					(cp instanceof WFlatSlider)||
					(cp instanceof WRoundSlider)||
					(cp instanceof JRadioButton)
					)
			{
				String label = null;
				if (cp instanceof JCheckBox)
					label = ((JCheckBox)cp).getText();
				if (cp instanceof JLabel)
					label = ((JLabel)cp).getText();
				if (cp instanceof JRadioButton)
					label = ((JRadioButton)cp).getText();
				if (label!=null)
				{
					//System.out.println(tab+""+cp.getName()+" == "+label );
					labels_map.put(cp.getName(), label);
					
				}
				if (cp instanceof JLabel == false)
				{
					label = cp.getToolTipText();
					tooltips_map.put(cp.getName(), label);
				}
				//else
				//	System.err.println("No name for component "+cp);
			}
			else
			if (cp instanceof JViewport)
			{
				PanelTorecurs((JComponent)((JViewport)cp).getView(), depth+1);
			}
			else
			if (cp instanceof JScrollPane)
			{
				PanelTorecurs((JComponent)((JScrollPane)cp).getViewport().getView(), depth+1);
			}
			else
			if (cp instanceof JTabbedPane)
			{
				for (int k = 0 ; k < ((JTabbedPane)cp).getTabCount();k++)
				{
					
					//System.out.println(tab+""+((JTabbedPane)cp).getTitleAt(k)+" == "+((JTabbedPane)cp).getTitleAt(k));
					labels_map.put(((JTabbedPane)cp).getName(),((JTabbedPane)cp).getTitleAt(k));
					//labels_map.put(((JTabbedPane)cp).getTitleAt(k),((JTabbedPane)cp).getTitleAt(k));
					
					String label =((JTabbedPane)cp).getToolTipText();
					tooltips_map.put(((JTabbedPane)cp).getTitleAt(k), label);
					PanelTorecurs((JComponent)((JTabbedPane)cp).getComponent(k), depth+1);
				}
			}
			else
				if (cp instanceof JXTaskPaneContainer)
				{
					for (int k = 0 ; k < ((JXTaskPaneContainer)cp).getComponentCount();k++)
					{
						//map.put(((JXTaskPaneContainer)cp).getComponent(k).getName(),((JXTaskPaneContainer)cp).getComponent(k).getName());
						PanelTorecurs((JComponent)((JXTaskPaneContainer)cp).getComponent(k), depth+1);
					}
				}
				else
				if (cp instanceof JPanel)
				{
					PanelTorecurs(cp, depth+1);
				}
			else
			{
				if (VERBOSE==true)
				System.err.println("Non gerer : "+cp.getClass().getName());
			}
			
		}
	}
	
	private void recursToPanel(JComponent cp2, int depth) {
		int nbr_comps = cp2.getComponents().length;
		for (int i = 0 ; i < nbr_comps;i++)
		{
			if ((cp2.getComponents()[i] instanceof CellRendererPane))
				continue;
			JComponent cp = (JComponent) cp2.getComponents()[i];
			if (( cp instanceof JXTaskPane) || ( cp instanceof JXCollapsiblePane))
			{
				if ( cp instanceof JXCollapsiblePane)
				{
					String label = ((JXCollapsiblePane)cp).getParent().getName();
					String valeur = labels_map.get(label);
					if (valeur==null)
						valeur="UNDEFINEDELEMENT_"+cp.getParent().getName();
					((JXTaskPane)((JXCollapsiblePane)cp).getParent()).setTitle(valeur);
				}
				else
				{
					String label = ((JXTaskPane)cp).getName();
					String valeur = labels_map.get(label);
					if (valeur==null)
						valeur="UNDEFINEDELEMENT_"+cp.getName();
					((JXTaskPane)cp).setTitle(valeur);
					
					String value = tooltips_map.get(((JXTaskPane)cp).getName());
					cp.setToolTipText(value);
					
				}
				recursToPanel(cp, depth+1);
			}
			else
			if (
					(cp instanceof JCheckBox) || 
					(cp instanceof JLabel)||
					(cp instanceof JTextField)||
					(cp instanceof JList)||
					(cp instanceof JComboBox)||
					(cp instanceof JColorChooserButton)||
					(cp instanceof JXDatePicker)||
					(cp instanceof JSlider)||
					(cp instanceof WFlatSlider)||
					(cp instanceof WRoundSlider)||
					(cp instanceof JRadioButton)
					)
			{
				String value = labels_map.get(cp.getName());
				
				if (value==null)
					value="UNDEFINEDELEMENT_"+cp.getName();
				if (cp instanceof JCheckBox)
					((JCheckBox)cp).setText(value);
				if (cp instanceof JLabel)
					((JLabel)cp).setText(value);
				if (cp instanceof JRadioButton)
					((JRadioButton)cp).setText(value);
				
				if (cp instanceof JLabel == false)
				{
					value = tooltips_map.get(cp.getName());
					if (value==null)
						value="UNDEFINEDELEMENT_"+cp.getName();
					cp.setToolTipText(value);
				}
			}
			else
			if (cp instanceof JViewport)
			{
				recursToPanel((JComponent)((JViewport)cp).getView(), depth+1);
			}
			else
			if (cp instanceof JScrollPane)
			{
				recursToPanel((JComponent)((JScrollPane)cp).getViewport().getView(), depth+1);
			}
			else
			if (cp instanceof JTabbedPane)
			{
				for (int k = 0 ; k < ((JTabbedPane)cp).getTabCount();k++)
				{
					//System.out.println(tab+""+((JTabbedPane)cp).getTitleAt(k)+" == "+((JTabbedPane)cp).getTitleAt(k));
					String cle = ((JTabbedPane)cp).getTitleAt(k); // ?????
			//		String cle = (String)((JTabbedPane)cp).getComponent(k).getName();
					String value = labels_map.get(cle);
					if (value!=null)
						((JTabbedPane)cp).setTitleAt(k, value);
					else
					{
						value="UNDEFINEDELEMENT_"+cle;
						((JTabbedPane)cp).setTitleAt(k, value);
					}
					value = tooltips_map.get(((JTabbedPane)cp).getTitleAt(k));
					cp.setToolTipText(value);
					
					recursToPanel((JComponent)((JTabbedPane)cp).getComponent(k), depth+1);
				}
			}
			else
				if (cp instanceof JXTaskPaneContainer)
				{
					for (int k = 0 ; k < ((JXTaskPaneContainer)cp).getComponentCount();k++)
					{
						//map.put(((JXTaskPaneContainer)cp).getComponent(k).getName(),((JXTaskPaneContainer)cp).getComponent(k).getName());
						recursToPanel((JComponent)((JXTaskPaneContainer)cp).getComponent(k), depth+1);
					}
				}
				else
				if (cp instanceof JPanel)
				{
					recursToPanel(cp, depth+1);
				}
			else
			{
				if (VERBOSE==true)
				System.err.println("Non gerer : "+cp.getClass().getName());
			}
		}
	}
}
