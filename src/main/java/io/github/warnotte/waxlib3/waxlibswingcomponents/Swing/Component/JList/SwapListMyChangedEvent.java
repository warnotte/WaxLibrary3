package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList;

import java.util.EventObject;

//Declare the event. It must extend EventObject.
public class SwapListMyChangedEvent extends EventObject {
    /**
	 * 
	 */
	private static final long	serialVersionUID	= -8942475752564844776L;
	Object OrignalComponent = null;
    public SwapListMyChangedEvent(Object source, Object value) {
	super(source);
	this.OrignalComponent=value;
        
    }
    public synchronized Object getOrignalComponent() {
        return OrignalComponent;
    }
    public synchronized void setOrignalComponent(Object orignalComponent) {
        OrignalComponent = orignalComponent;
    }
}