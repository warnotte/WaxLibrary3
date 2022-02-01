package org.warnotte.OBJ2GUI.Listeners;

import org.warnotte.OBJ2GUI.JWPanel;
import org.warnotte.OBJ2GUI.Events.MyChangedEvent;

public class BaseListener {
	
	JWPanel parent_panel = null;
	
	/*private BaseListener()
	{
		
	}*/
	public BaseListener(JWPanel parent_panel)
	{
		this.parent_panel= parent_panel;
	}
	
	public void sendRefresh(Object value){
		parent_panel.fireMyEvent(new MyChangedEvent(this,value));
	}
	
}
