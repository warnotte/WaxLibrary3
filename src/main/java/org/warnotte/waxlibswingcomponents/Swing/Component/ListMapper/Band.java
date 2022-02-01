package org.warnotte.waxlibswingcomponents.Swing.Component.ListMapper;

public class Band {

    Object filename;
    private float length;
    public Band(Object filename, int length) {
	super();
	this.filename = filename;
	this.length = length;
    }
    
    @Override
	public String toString()
    {
	return filename +" : "+length;
    }

    public synchronized Object getFilename() {
        return filename;
    }

    public synchronized void setFilename(Object filename) {
        this.filename = filename;
    }

    public synchronized float getLength() {
        return length;
    }

    public synchronized void setLength(float length) {
	if (length>=1)
        this.length = length;
    }
    
    
}
