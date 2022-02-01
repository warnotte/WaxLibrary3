package org.warnotte.waxlib2.TemplatePropertyMerger;

import java.lang.reflect.Method;

public class ResultatMerge {

	Class<?> Ownerclass;
	Method nom;
    Object value;
    boolean equals=true;
    //boolean hasSet=false;
    Method hasSet=null;
  //  String parentName="";
  //  boolean isCollection = false;
    
    public ResultatMerge(Class<?> Ownerclass, Method nom, Object value, boolean equals, Method hasSet) {
	super();
	this.Ownerclass = Ownerclass;
	this.equals = equals;
	this.nom = nom;
	this.value = value;
	this.hasSet = hasSet;
//	this.parentName=parentName;
    }
    
    @Override
	public String toString()
    {
	return nom.getName()+" | Val=="+ value+ " |  ==? "+ equals+ " | hasSet?"+hasSet;
    }
    public synchronized Method getNom() {
        return nom;
    }
    public synchronized void setNom(Method nom) {
        this.nom = nom;
    }
    public synchronized Object getValue() {
        return value;
    }
    public synchronized void setValue(Object value) {
        this.value = value;
    }
    public synchronized boolean isEquals() {
        return equals;
    }
    public synchronized void setEquals(boolean equals) {
        this.equals = equals;
    }
    public synchronized Method getHasSet() {
        return hasSet;
    }


    public synchronized void setHasSet(Method hasSet) {
        this.hasSet = hasSet;
    }
/*
	public synchronized String getParentName() {
		return parentName;
	}

	public synchronized void setParentName(String parentName) {
		this.parentName = parentName;
	}
    */

	public synchronized Class<?> getOwnerclass()
	{
		return Ownerclass;
	}

	public synchronized void setOwnerclass(Class<?> ownerclass)
	{
		Ownerclass = ownerclass;
	
	}
    
}
