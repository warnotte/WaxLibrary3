package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;


/**
 * Retiens la selection
 * @author Warnotte Renaud.
 *
 */
public abstract class CurrentSelectionContext
{
	private Object			manager			= null;

	public List<Object>		selection		= new ArrayList<Object>();

	// Envoye des message de changements de selection.
	EventListenerList		xxxListeners	= new EventListenerList();
	
	public CurrentSelectionContext()
	{
		
	}
	
	public CurrentSelectionContext(Object manager)
	{
		super();
		this.setManager(manager);
	}
	
	public synchronized List<?> getSelection()
	{
		return selection;
	}
	
	/**
	 * Retourne tout les type de classes qui sont selectionnés
	 * @return
	 */
	public synchronized List<Class<?>> getSelectionClass()
	{
		List<Class<?>> cls = new ArrayList<Class<?>> ();
		for (int i = 0; i < selection.size(); i++)
		{
			Class<?> cl = selection.get(i).getClass();
			if (cls.contains(cl)==false)
				cls.add(cl);
		}
		return cls;
	}
	/*
	public synchronized List<?> getSelection(Class<?> filterClass)
	{
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < selection.size(); i++)
		{
			Object o = selection.get(i);
			if (filterClass.isInstance(o))
				list.add(o);
		}
		return list;
	}*/
	
	public synchronized <U> List<U> getSelection(Class<U> filterClass)
    {
        List<U> list = new ArrayList<>();
        for (Object o : selection) {
            if (filterClass.isInstance(o))
                list.add(filterClass.cast(o));
        }
        return list;
    }

		
	public synchronized void setSelection(List<Object> selection, Object fromSource)
	{
		this.selection = selection;
		fireStgNeedRefresh(new SelectionChangedEvent(SelectionChangedEvent.Refresh_MSG.SELECTION_CHANGED, fromSource));
	}

	/**
	 * Ajoute/Supprime les elements a la selection courante.
	 * @param To
	 * @param From
	 * @param AddOrRemove
	 */
	protected synchronized void addToSelectionIfnotExists(List<Object> To, List<Object> From, boolean AddOrRemove) {
	    
		addToSelectionIfnotExists(To, From, AddOrRemove, true);
		
	}
	protected synchronized void addToSelectionIfnotExists(List<Object> To, List<Object> From, boolean AddOrRemove, boolean fireEvent) {
	    
		addIfNotExists(To, From, AddOrRemove);
		
	    // if (fireEvent==true)
	    //manager.fireStgNeedRefresh(new XXXEvent(Refresh_MSG.SELECTION_CHANGED, this));
	}
	
	/**
	 * 
	 * @param To
	 * @param From
	 * @param AddOrRemove
	 */
	private void addIfNotExists(List<Object> To,List<Object> From, boolean AddOrRemove) {
	if (To == null) {
	    //Logs.getLogger().error("Vous avez refiler un truc null a addIfNotExists.");
	    System.err.println("Vous avez refiler un truc null a addIfNotExists.");
	    return;
	}
	
	if (AddOrRemove)
	{
		// ClickedNode.addAll(selectedNode);
		for (int k = 0; k < To.size(); k++) {
		    for (int i = 0; i < From.size(); i++) {
			if (To.get(k).equals(From.get(i)) == true) {
			    From.remove(i);
			    i--;
			    continue;
			}
		    }
		}
		 To.addAll(From);
	}
	else
	{
	   for (int i = 0; i < From.size(); i++) {
		   To.remove(From.get(i));
	}
	}

    }

	List<Object> v_tmp = new ArrayList<Object>();
	public synchronized void addObjectToSelection(Object objetclicked, boolean SHIFT,
	    boolean CTRL, Object fromSource, boolean FireEvent) {
	    if ((SHIFT==false) && (CTRL==false))
	    {
	    	selection.clear();
	    }
	    boolean AddOrRemove = (CTRL == true) ? false : true;
	    v_tmp.clear();
	    v_tmp.add(objetclicked);
	    addToSelectionIfnotExists(selection, v_tmp, AddOrRemove, FireEvent);
	    if (FireEvent==true)
	    fireStgNeedRefresh(new SelectionChangedEvent(SelectionChangedEvent.Refresh_MSG.SELECTION_CHANGED, fromSource));
	}

	public void addObjectsToSelection(List<?> listSelection, boolean sHIFT, boolean cTRL, Object fromSource)
	{
		if (listSelection.size()==0) return;
		Object o = listSelection.get(0);
		addObjectToSelection(o, sHIFT, cTRL, fromSource, false);
		for (int i = 1; i < listSelection.size(); i++)
		{
			o = listSelection.get(i);
			addObjectToSelection(o, true, cTRL, fromSource, false);
		}
		fireStgNeedRefresh(new SelectionChangedEvent(SelectionChangedEvent.Refresh_MSG.SELECTION_CHANGED, this));
	}
	
	public void clear_selection(Object fromSource)
	{
		selection.clear();
		fireStgNeedRefresh(new SelectionChangedEvent(SelectionChangedEvent.Refresh_MSG.SELECTION_CHANGED, fromSource));
		
	}

	public void setManager(Object manager)
	{
		this.manager = manager;
	}
	
	public Object getManager()
	{
		return manager;
	}

	public void addXXXListener(SelectionChangeable listener)
	{
		xxxListeners.add(SelectionChangeable.class, listener);
	}

	public void removeXXXListener(SelectionChangeable listener)
	{
		xxxListeners.remove(SelectionChangeable.class, listener);
	}

	public void fireStgNeedRefresh(SelectionChangedEvent xxxEvent)
	{
		Object[] listeners = xxxListeners.getListenerList();
		// loop through each listener and pass on the event if needed
		int numListeners = listeners.length;
		for (int i = 0; i < numListeners; i += 2)
		{
			if (listeners[i] == SelectionChangeable.class)
			{
				// pass the event to the listeners event dispatch method
				((SelectionChangeable) listeners[i + 1]).somethingNeedRefresh(xxxEvent);
			}
		}
	}

	public abstract boolean isFiltred(Class<?> classK);
	
}
