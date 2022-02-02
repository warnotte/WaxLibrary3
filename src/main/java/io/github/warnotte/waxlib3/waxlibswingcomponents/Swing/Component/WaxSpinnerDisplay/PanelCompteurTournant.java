package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSpinnerDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Warnotte Renaud 2009
 *
 */
public class PanelCompteurTournant extends JPanel
{
	
	private static final long	serialVersionUID	= 1L;

	private int nbr_chiffre = 5;
	private PanelRoulette[] jPanel = null;
	private final int	taillechiffre;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * 
	 * @param nbrchiffre Nbr de rangées de chiffres
	 * @param taillechiffre Taille en pixel d'un chiffre (et donc du panel qui le represente).
	 * @throws IOException
	 */
	public PanelCompteurTournant(int nbrchiffre, int taillechiffre) throws IOException
	{
		super();
		this.nbr_chiffre=nbrchiffre;
		this.taillechiffre=taillechiffre;
		initialize();
		
		Timer triUpdater = new Timer(25, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		repaint();
    		}
		});
		triUpdater.start();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws IOException 
	 */
	private void initialize() throws IOException
	{
		this.setSize(300, 200);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		jPanel = new PanelRoulette[nbr_chiffre];
		for (int j = 0; j < jPanel.length; j++)
		{
			add(getJPanel(j));
		}
	}
	
	private PanelRoulette getJPanel(int i) throws IOException
	{
		if (jPanel[i] == null)
		{
			jPanel[i] = new PanelRoulette(taillechiffre);
		}
		return jPanel[i];
	}
	
	/**
	 * Valeur a rentrer un string qui sera convertit en int puis reformatter avec un sprintf("%05d");
	 * @param valeur
	 */
	public void setValue(String valeur)
	{
		try
		{
		long vL = (long) Float.parseFloat(""+valeur);
		String chiffre = String.format("%0"+nbr_chiffre+"d", vL);
		for (int i = 0; i < nbr_chiffre; i++)
		{
			
			Long v = (long)Float.parseFloat(""+chiffre.charAt(i));
			jPanel[i].roulette.setValue(v.intValue());
		}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
	}


}
