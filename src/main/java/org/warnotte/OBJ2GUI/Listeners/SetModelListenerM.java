package org.warnotte.OBJ2GUI.Listeners;

import java.util.AbstractSet;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

import org.warnotte.OBJ2GUI.JWPanel;
import org.warnotte.OBJ2GUI.ParseurAnnotations;

/**
 * 
 * @author Warnotte Renaud 2007
 *
 */
public class SetModelListenerM extends BaseListener implements ListSelectionListener {

	JList jtf = null; // La Liste
	AbstractSet<?> elements = null; // Les elements	de la JListe --> Les Cl�es 
	JPanel pane = null; // Le panel qui sera celui a mettre d'un element de la JList
	
	//public ListModelListenerM(JList jtf1, JPanel panel, AbstractList elements)
	public SetModelListenerM(JList jtf1, JPanel panel, AbstractSet<?> elements, JWPanel parent_panel)
	{
		super(parent_panel);
		this.elements = elements;
		this.jtf = jtf1;
		this.pane = panel;
	}
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e)
	{
		// Si le user selectionne un element de la JList alors on rafraichis le panel cible avec l'objet selectionn�e dans la liste en demandant au super generateur
		// Recupere la cl�s selctionn�e
		Object obj = jtf.getSelectedValue();
		//int idx = jtf.getSelectedIndex();
		try
		{
			// Recupere l'objet a partir de la AbstractMap (grace a la cl�)
			ParseurAnnotations.Refresh_PanelEditor_For_Object("",pane,obj,parent_panel, false);
			sendRefresh(jtf);
		} 
		catch (Exception e1)
		{
		e1.printStackTrace();
		}
	}
}
