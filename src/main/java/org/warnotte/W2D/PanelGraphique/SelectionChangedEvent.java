package org.warnotte.W2D.PanelGraphique;

public class SelectionChangedEvent {
	
    public static enum Refresh_MSG {
    	PUSH_UNDO_OPERATION,
    	POP_UNDO_OPERATION,
    	REPAINT_VIEWS, 
    	MODEL_CHANGED,
    	SELECTION_CHANGED, 
    	INFORMATIONS_CHANGED, 
    	//X_CHANGED, 
    	//PRE_SELECT_FILTER_CHANGED,
    	//SHOW_COST_WINDOW
    	
    	};
    
    public Refresh_MSG message;
    private Object source=null; // Wich object
	
    public SelectionChangedEvent(Refresh_MSG message, Object source_from_the_event) {
	super();
	this.message = message;
	this.setSource(source_from_the_event);
    }
    
    @Deprecated
 	public void setObj(Object obj)
 	{
 		this.source = obj;
 	}

 	/**
 	 * Get The source from the event
 	 * @return
 	 */
 	@Deprecated
 	public Object getObj()
 	{
 		return source;
 	}
 	
 	
    
 	public void setSource(Object obj)
 	{
 		this.source = obj;
 	}

 	/**
 	 * Get The source from the event
 	 * @return
 	 */
 	public Object getSource()
 	{
 		return source;
 	}
 	
 	
	
}
