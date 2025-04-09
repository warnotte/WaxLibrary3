package io.github.warnotte.waxlib3.core.PropertyChangeManager;

import java.beans.PropertyChangeEvent;

public class SuperPropertyChangeEvent extends PropertyChangeEvent {

	
	TypeOfChangeEvent typeOfEvent = TypeOfChangeEvent.MODIFYATTRIBUTE;;
	
	public SuperPropertyChangeEvent(TypeOfChangeEvent type, Object source, String setterMethodName, Object oldValue, Object newValue) {
		super(source, setterMethodName, oldValue, newValue);
		typeOfEvent=type;
	}
	public SuperPropertyChangeEvent(TypeOfChangeEvent type, Object source, Object oldValue, Object newValue) {
		super(source, determineSetterMethod(), oldValue, newValue);
		typeOfEvent=type;
	}
	
	public TypeOfChangeEvent getTypeOfEvent() {
		return typeOfEvent;
	}
	public void setTypeOfEvent(TypeOfChangeEvent typeOfEvent) {
		this.typeOfEvent = typeOfEvent;
	}

	@Override
	public String toString() {
		return "SuperPropertyChangeEvent [typeOfEvent=" + typeOfEvent + "]["+super.toString()+"]";
	}

	static String determineSetterMethod()
	{
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		return stackTrace[2].getMethodName();
	}
	
}
