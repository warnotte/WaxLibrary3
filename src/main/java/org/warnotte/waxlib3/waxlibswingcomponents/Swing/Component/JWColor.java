package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.Color;

public class JWColor implements Cloneable {
	int Red,Green,Blue,Alpha;
	
	public JWColor(int r,int g,int b)
	{
	    this.setColor(r,g,b,255);
	}
	public JWColor(int r,int g,int b,int a)
	{
	    this.setColor(r,g,b,a);
	}
	
	public JWColor(Color col1) {
		setColor(col1);
	}
	
	public Color getColor()
	{
		return new Color(Red,Green,Blue,Alpha);
	}

	/**
	 * Prends un string et fait une couleur
	 * @param string R@G@B@A
	 */
	public JWColor(String string) {
		String spl [] = string.split("@");
		int r = Integer.parseInt(spl[0]);
		int g = Integer.parseInt(spl[1]);
		int b = Integer.parseInt(spl[2]);
		int a = Integer.parseInt(spl[3]);
		this.Red=r;
		this.Green=g;
		this.Blue=b;
		this.Alpha=a;
	}

	public JWColor(int i) {
		// TODO Auto-generated constructor stub
	}

	public void setColor(int r,int g,int b,int a)
	{
		this.Red=r;
		this.Green=g;
		this.Blue=b;
		this.Alpha=a;
	}

	public synchronized int getAlpha() {
		return Alpha;
	}
	public synchronized void setAlpha(int alpha) {
		Alpha = alpha;
	}
	public synchronized int getBlue() {
		return Blue;
	}
	public synchronized void setBlue(int blue) {
		Blue = blue;
	}
	public synchronized int getGreen() {
		return Green;
	}
	public synchronized void setGreen(int green) {
		Green = green;
	}
	public synchronized int getRed() {
		return Red;
	}
	public synchronized void setRed(int red) {
		Red = red;
	}

	@Override
	public String toString()
	{
		return Red+"@"+Green+"@"+Blue+"@"+Alpha;
	}
	public void setColor(Color newColor) {

		this.Red=newColor.getRed();
		this.Green=newColor.getGreen();
		this.Blue=newColor.getBlue();
		this.Alpha=newColor.getAlpha();
		
		
	}

	public Color getMixedColor(JWColor edi_color0, float f) {
		if (f>1.0f) 
		{
			System.err.println("Error");
			f=1.0f;
		}

		Color z = new Color(
				((Red*f+edi_color0.Red*(1-f))/255),
				((Green*f+edi_color0.Green*(1-f))/255),
				((Blue*f+edi_color0.Blue*(1-f))/255),
				((Alpha*f+edi_color0.Alpha*(1-f))/255));
			
		return z;
	}
	
	public float[] getColorArrayFloat() {
	    return new float [] {(float)Red/(float)255,(float)Green/(float)255,(float)Blue/(float)255,(float)Alpha/(float)255};
	}
	public int[] getColorArrayInt() {
	    return new int [] {Red,Green,Blue,Alpha};
	}
	
	@Override
	public Object clone()
	{
		Object o = null;
		try
		{
			// On récupère l'instance à renvoyer par l'appel de la
			// méthode super.clone()
			o = super.clone();
			
		} catch (CloneNotSupportedException cnse)
		{
			// Ne devrait jamais arriver car nous impl�mentons
			// l'interface Cloneable
		//Logs.getLogger().fatal(cnse);
			cnse.printStackTrace(System.err);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// on renvoie le clone
		return o;
	}

}


