package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

/**
 * Créee un bouton d'un certaine couleur offrant la possibilite de changer celle ci (JWColor, mais pas color qui ne contient pas de set)
 * @author wax
 *
 */
public class JColorChooserButton extends JButton implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9158407806747255454L;
	private JWColor col = null;
	
	public JColorChooserButton(Color col1)
	{
		super();
		this.setCol(new JWColor(col1));
		this.setBackground(col1);
		this.addActionListener(this);
	}
	public JColorChooserButton(JWColor col1)
	{
		super();
		this.setCol(col1);
		this.setBackground(new Color(getCol().getRed(),getCol().getGreen(),getCol().getBlue(),255));
		this.addActionListener(this);	
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
	    Color newColor = JColorChooser.showDialog(this, "Choose Background Color", new Color(getCol().getRed(),getCol().getGreen(),getCol().getBlue(),getCol().getAlpha()));
	    if (newColor != null) {
	    	getCol().setColor(newColor); //?????
	    //	parent_panel.fireMyEvent(new MyChangedEvent(this,value));
	    	setBackground(newColor);
	    	
	    } 
	}
	public void setCol(JWColor col)
	{
		this.col = col;
		setBackground(col.getColor());
	}
	public JWColor getCol()
	{
		return col;
	}
	
}
