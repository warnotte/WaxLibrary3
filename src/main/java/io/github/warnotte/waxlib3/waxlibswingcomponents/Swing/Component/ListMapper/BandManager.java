package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.ListMapper;
import java.util.Vector;


public class BandManager {
    
    Vector<Band> elements = new Vector<Band>();
    int MAX_VALUE = 255;
    
    
    public synchronized Vector<Band> getElements() {
        return elements;
    }

    public synchronized void setElements(Vector<Band> elements) {
        this.elements = elements;
    }

    public void swap_band(int nr1, int nr2)
    {
	Band el1 = elements.get(nr1);
	Band el2 = elements.get(nr2);
	
	elements.remove(nr1);
	elements.add(nr1, el2);
	elements.remove(nr2);
	elements.add(nr2, el1);
	
	
    }
    
    public void ajoute_element(Band element,int size)
    {
	ajoute_element(element);
	resize_element(element, size);
    }
    
    public void ajoute_element(Band element)
    {
	Band last = null;
	if (elements.size()!=0)
	{
	    last = elements.get(elements.size()-1);
	    last.setLength(last.getLength()/2);
	    element.setLength(last.getLength());
	}
	else
	element.setLength(MAX_VALUE);
	elements.add(element);
    }
    
    // TODO : Verification de non depassement des bandes voisines.
    public void resize_element(Band element, int size)
    {
	if (elements.size()==1)
	{
	    System.err.println("There's only one elements; that cannot be resized");
	    return;
	}
	// Calcule la différence à appliquer au prochain ou au precedent.
	float delta = element.getLength() - size; 
	
	int idx = elements.indexOf(element);
	Band neighboor=null;
	// Si au moins premier element et taille > 1
	if (element == elements.lastElement())
	{
	 /*    neighboor = elements.get(idx-1);
	     element.setLength(element.getLength()-delta);
	*/}
	else
	{
	    neighboor = elements.get(idx+1);
	    if (neighboor.getLength()+delta>1)
		if (size>1)
	    {
		    element.setLength(size);
	     neighboor.setLength(neighboor.getLength()+delta);
	     System.err.println("Length = "+neighboor.getLength());
	    }
	}
	
	// Verifier que la taille ne devient pas plus grande que la position du voisin du dessous.
	
	//Band bdessous = elements.get(idx+1);
	//int position = getOffsetFromBand(element);
	//int positiond = getOffsetFromBand(bdessous);
	
	
    }
    
    public void resizeAllwithAverage()
    {
    	int size = MAX_VALUE/elements.size();
    	for (int i = 0 ;  i< elements.size();i++)
    	{
    		Band band = getElements().elementAt(i);
    		resize_element(band, size);
    	}
    }
    
    public void retire_element(Band element)
    {
	int idx = elements.indexOf(element);
	Band neighboor = null;
	if (element==elements.lastElement())
	    neighboor = elements.get(idx-1);
	else
	if (idx >= 0) 
	    neighboor = elements.get(idx+1);
	
	neighboor.setLength(neighboor.getLength()+element.getLength());
	
	elements.remove(element);
    }
    
    public void affiche()
    { 
	System.err.println("Disp");
	for (int i = 0 ; i < elements.size();i++)
	{
	    Band p = elements.get(i);
	    System.err.println(i+" - "+p.filename+" - "+p.getLength());
	}
    }
    private void clear() {
	elements.clear();
    }
    
    public static void main(String[] args) {
	BandManager v = new BandManager();

	Band element1 = new Band("Element1", 0);
	Band element2 = new Band("Element2", 0);
	Band element3 = new Band("Element3", 0);
	Band element4 = new Band("Element4", 0);
	v.ajoute_element(element1);
	v.ajoute_element(element2);
	v.ajoute_element(element3);
	v.ajoute_element(element4);
	
	v.affiche();
	v.resize_element(element4, 50);
	
	v.affiche();
	
	v.retire_element(element2);
	v.affiche();
	
	v.retire_element(element1);
	v.affiche();
	
	v.retire_element(element4);
	v.affiche();
	
	v.clear();
	
	element1 = new Band("Element1",0);
	element2 = new Band("Element2",0);
	v.ajoute_element(element1);
	v.ajoute_element(element2,77);
	v.affiche();
    }
    
    public int getOffsetFromBand(Band b)
    {
	double rY=0;
	for (int i = 0 ; i < elements.size(); i++ )
	{
	    Band band = elements.get(i);
	    if (b==band) return (int)rY;
	    rY += band.getLength();
	    
	}
	return -1;
    }
    
    public Band getBandFromLevel(int realmouseY) {
	if (realmouseY == 0) return elements.get(0);
	int rY = 0;
	Band best = null;
	for (int i = 0 ; i < elements.size(); i++ )
	{
	    Band band = elements.get(i);
	    if (rY >= realmouseY) return best;
	    rY += band.getLength();
	    best=band;
	}
	return best;
    }
    
    public boolean isStartOfBand(int realmouseY, Band band1) {
	int rY = 0;
	for (int i = 0 ; i < elements.size(); i++ )
	{
	    Band band = elements.get(i);
	    if (Math.abs(rY-realmouseY) <=3) return true;
	    rY += band.getLength();
	}
	return false;
    }

    
    public int getBandFromIndex(Band band) {
	return elements.indexOf(band);
	
    }

    
    public Band getNextBand(Band band) {
	int idx =elements.indexOf(band);
	if (idx<elements.size()-1)
	    return elements.get(idx+1);
	return elements.lastElement();
    }

	public void invertBands() {
		
		Vector<Band> bands = new Vector<Band>();
		for (int i = elements.size()-1 ; i >=0 ;i--)
			bands.add(elements.get(i));
		
		elements = bands;
	}

    

}
