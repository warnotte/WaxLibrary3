package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;


/**
 * Cette classe va prendre en parametre un Vecteur et l'afficher sous forme de liste.
 * Cette liste peut ensuite etre triée avec la souris ou le clavier et l'ordre du vecteur va changer.
 * 
 * @author wax
 *
 */
public class JSwapList3<T> extends JList<T>
{
	/**
     * 
     */
	private static final long	serialVersionUID	= -1827796423054098231L;
	public SwapListModel<T>		model_ordres		= null;
	private int					idxselected;

	public JSwapList3(List<T> liste)
	{
		this(new SwapListModel<T>(liste));
		model_ordres = (SwapListModel<T>) getModel();
	}

	public JSwapList3(SwapListModel<T> model_ordres2)
	{
		model_ordres = model_ordres2;
		setModel(model_ordres);
		addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(java.awt.event.MouseEvent e)
			{

				Rectangle r = getCellBounds(0, 0);
				if (r != null)
				{
					int underpos = (int) (e.getY() / r.getHeight());
					if (underpos != idxselected)
					{
						System.err.println(idxselected + "->" + underpos);
						if (underpos - idxselected > 0)
							swap_graphique_ordre(-1, underpos);
						else
							swap_graphique_ordre(+1, underpos);
						idxselected = underpos;

					}
				}
			}
		});

		addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mousePressed(java.awt.event.MouseEvent e)
			{
				idxselected = getSelectedIndex();
			}
		});
	}
	
	public void setModel(List<T> liste)
	{
		model_ordres = new SwapListModel<T>(liste);
		setModel(model_ordres);
	}

	protected void swap_graphique_ordre(int dir, int underpos)
	{

		if ((underpos + dir >= model_ordres.getSize()) || (underpos + dir < 0))
			return;
		if (underpos >= model_ordres.getSize())
			return;
		if (underpos < 0)
			return;
		T selected = model_ordres.get(underpos);
		model_ordres.remove(underpos);
		model_ordres.add(underpos + dir, selected);
		fireMyEvent(new SwapListMyChangedEvent(this, null));

	}

	// This methods allows classes to register for MyEvents
	public void addMyEventListener(SwapListMyEventListener listener)
	{
		// System.err.println(this.getName()+" Adding a listener "+listener);
		listenerList.add(SwapListMyEventListener.class, listener);
	}

	// This methods allows classes to unregister for MyEvents
	public void removeMyEventListener(SwapListMyEventListener listener)
	{
		listenerList.remove(SwapListMyEventListener.class, listener);
	}

	// This private class is used to fire MyEvents
	public void fireMyEvent(SwapListMyChangedEvent evt)
	{
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2)
		{
			if (listeners[i] == SwapListMyEventListener.class)
			{
				// System.err.println("Transmit the event"
				// +((MyEventListener)listeners[i+1]));
				((SwapListMyEventListener) listeners[i + 1]).myEventOccurred(evt);
			}
		}
	}

	// Transmet au panel parent ?!
	public void myEventOccurred(SwapListMyChangedEvent evt)
	{
		// System.err.println("Received an event" +this.getName());
		fireMyEvent(evt);
	}

	public static void main(String args[])
	{
		ArrayList<Integer> list_1 = new ArrayList<>();
		list_1.add(1);
		list_1.add(8);
		list_1.add(5);
		list_1.add(4);
		list_1.add(2);
		list_1.add(7);
		Vector<Integer> list_2 = new Vector<>();
		list_2.add(2);
		list_2.add(4);
		list_2.add(3);
		list_2.add(8);
		list_2.add(7);
		list_2.add(16);
		
		Vector<Number> list_3 = new Vector<>();
		list_3.add(2);
		list_3.add(4.4);
		list_3.add(3.33);
		list_3.add(8.78);
		list_3.add(7);
		list_3.add(16);
		
		JSwapList3<Integer> js1 = new JSwapList3<Integer>(list_1);
		JSwapList3<Integer> js2 = new JSwapList3<Integer>(list_2);
		JSwapList3<Number> js3 = new JSwapList3<Number>(list_3);
		
		js3.addMyEventListener(e -> {
			System.err.println("Event : "+e);
		});
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.getContentPane().add(js1);
		frame.getContentPane().add(js2);
		frame.getContentPane().add(js3);
		frame.pack();
		frame.doLayout();
		frame.setVisible(true);
	}
}
