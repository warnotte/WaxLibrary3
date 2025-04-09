package io.github.warnotte.waxlib3.core.PropertyChangeManager.experimental;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.warnotte.waxlib3.core.PropertyChangeManager.SuperPropertyChangeEvent;
import io.github.warnotte.waxlib3.core.PropertyChangeManager.TypeOfChangeEvent;

public class DataChangedObserverNouveau implements PropertyChangeListener {

	protected static final Logger	logger			= LogManager.getLogger("DataChangedObserverNouveau");

	//Manager							manager;
	private boolean					isProjectSaved	= true;
	private boolean 				isInUndo = false; 
	
	// TODO : Ceci bug a mort, j'arrive a monté plus haut que 10
	LimitedList<SuperPropertyChangeEvent> history_changes = new LimitedList<>(10);
	
	List<UndoEventListener> undoeventListeners = new ArrayList<>(); 
	
	
	public DataChangedObserverNouveau(/*Manager manager*/) {
		//this.manager = manager;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt1) {
		if (evt1 instanceof SuperPropertyChangeEvent)
		{
			SuperPropertyChangeEvent evt = (SuperPropertyChangeEvent) evt1;
			if (isProjectSaved) {
				isProjectSaved = false;
				logger.info("CHANGED");
			}
			logger.info("DATACHANGED " + evt);
			
			if (isInUndo==false)
			{
				history_changes.push(evt);
				logger.info("Undo size : "+history_changes.size());
			}
		}
		/*
		if (evt1 instanceof AccelerationNeededEvent)
		{
			System.err.println("Acceleration needed");
			if (evt1.getSource() instanceof Flotteur)
		    {
				System.err.println("Flotteur changed");
				manager.ReBuildAccelerationStructure();
				
			}
			
			if (evt1.getSource() instanceof Ilot)
		    {
				System.err.println("Ilot changed");
				manager.ReBuildAccelerationStructure();
			}
		}*/
		
	}

	public boolean isProjectSaved() {
		return isProjectSaved;
	}

	public void setProjectSaved(boolean isProjectSaved) {
		this.isProjectSaved = isProjectSaved;
	}
/*
	@Override
	public void superPropertyChange(SuperPropertyChangeEvent evt) {
		logger.info("DATACHANGE2 " + evt);
		
	}
*/

	public void undo() {
		if (history_changes.size()==0)
		{
			logger.info("Nothing to Undo");
			return;
		}

		SuperPropertyChangeEvent spc = history_changes.pop();
		
		Object sourceObj = spc.getSource();
		Object oldValue = spc.getOldValue();
		String setterName = spc.getPropertyName();
		
		Class<? extends Object> cls = sourceObj.getClass();
		Method	method = null;
		// Parfois on a une methode setValue(int toto), mais a été enregistre sous forme de non primitif
		try {
			
			//boolean isPrimitive = oldValue.getClass().isPrimitive();
			//method = cls.getDeclaredMethod(setterName, oldValue.getClass());
			method = cls.getMethod(setterName, oldValue.getClass());
		} catch (NoSuchMethodException | SecurityException e) {
			try {
					if (spc.getTypeOfEvent() == TypeOfChangeEvent.ADDORREMOVE)
					{
						if (oldValue.getClass()==ArrayList.class)
						{
							method = cls.getDeclaredMethod(setterName, List.class);
						}
					}	
					else
					{
						Class primitiveClass = getPrimitiveType(oldValue.getClass());
						//	method = cls.getMethod(setterName, primitiveClass);
						method = cls.getDeclaredMethod(setterName, primitiveClass);
					}
			} catch (NoSuchMethodException | SecurityException e1) {
				logger.fatal(e, e);
				e1.printStackTrace();
				return;
			}
		}
			
			String methodName = method.getName();
			System.err.println("Method : "+methodName);
			isInUndo = true;
			
			if (spc.getTypeOfEvent() == TypeOfChangeEvent.MODIFYATTRIBUTE)
			{
				try {
					method.invoke(sourceObj, oldValue);
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.fatal(e, e);
					e.printStackTrace();
				}
			}
			if (spc.getTypeOfEvent() == TypeOfChangeEvent.ADDORREMOVE)
			{
				try {
					boolean isAccessible = method.canAccess(sourceObj);
					method.setAccessible(true);
					
					method.invoke(sourceObj, oldValue);
					method.setAccessible(isAccessible);
					
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.fatal(e, e);
					e.printStackTrace();
				}
			}
			
			fireUndoEvent();
			
			isInUndo = false;
		
		
	}

    private void fireUndoEvent() {
    	for (UndoEventListener hl : undoeventListeners)
            hl.undoDone(new UndoEvent(this));
    }

	public static Class<?> getPrimitiveType(Class<?> wrapperClass) {
        if (wrapperClass == Integer.class) {
            return int.class;
        } else if (wrapperClass == Double.class) {
            return double.class;
        } else if (wrapperClass == Boolean.class) {
            return boolean.class;
        } else if (wrapperClass == Character.class) {
            return char.class;
        } else if (wrapperClass == Byte.class) {
            return byte.class;
        } else if (wrapperClass == Short.class) {
            return short.class;
        } else if (wrapperClass == Long.class) {
            return long.class;
        } else if (wrapperClass == Float.class) {
            return float.class;
        }
        return null; // Si ce n'est pas une classe enveloppante pour un type primitif
    }
    
    public void addListener(UndoEventListener toAdd) {
    	undoeventListeners.add(toAdd);
    }
	
}
