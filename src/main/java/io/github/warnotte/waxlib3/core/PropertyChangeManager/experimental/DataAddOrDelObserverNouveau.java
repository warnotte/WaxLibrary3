package io.github.warnotte.waxlib3.core.PropertyChangeManager.experimental;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataAddOrDelObserverNouveau implements PropertyChangeListener {
	/*
	protected static final Logger	logger			= LogManager.getLogger("DataChangedObserverNouveau");

	Manager							manager;
	//private boolean					isProjectSaved	= true;

	public DataAddOrDelObserverNouveau(Manager manager) {
		this.manager = manager;
	}
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.err.println("ADD OR DeL");
		
		System.err.println(evt);
		
	}
}
