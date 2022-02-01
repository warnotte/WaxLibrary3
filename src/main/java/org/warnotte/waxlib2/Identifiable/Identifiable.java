package org.warnotte.waxlib2.Identifiable;

import java.io.Serializable;

import org.warnotte.waxlib2.TemplatePropertyMerger.property_mode;
import org.warnotte.waxlib2.TemplatePropertyMerger.Annotations.PROPERTY_interface;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Classe derivable pour certains systeme automatique (obj2gui2 par exemple ou les MINIDB). 
 * @author Renaud Warnotte
 *
 */
public class Identifiable implements Cloneable, Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6050868393960851763L;


	
	@XStreamAsAttribute
	long ID;
	public Identifiable(long ID)
	{
		this.ID=ID;
	}
	
	/**
	 * 
	 */
	public Identifiable()
	{
		// TODO Auto-generated constructor stub
	}

	@PROPERTY_interface(Operation = property_mode.PROPERTY_MERGEABLE, readOnly =true, orderDisplay = -10 )
	public synchronized long getID()
	{
		return ID;
	}

	public synchronized void setID(long iD)
	{
		ID = iD;
	}

	public long getId()
	{
		return ID;
	}

	public void setId(long id)
	{
		this.ID = id;
	}
	
	@Override
	public String toString()
	{
		return "Id="+ID;
	}
	
	@Override
	public Object clone() {
		Identifiable o = null;
    	try {
      		// On récupère l'instance à renvoyer par l'appel de la 
      		// méthode super.clone()
      		o = (Identifiable) super.clone();
    	} catch(CloneNotSupportedException cnse) {
      		// Ne devrait jamais arriver car nous impl�mentons 
      		// l'interface Cloneable
      		cnse.printStackTrace(System.err);
	    }
	    // on renvoie le clone
	    return o;
  	}
}
