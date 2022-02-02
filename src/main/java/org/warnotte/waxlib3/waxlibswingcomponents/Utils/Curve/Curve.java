package org.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Curve implements Copiable
{
	private ArrayList<Float>  valeurs = null;
	
	boolean Spline = false;
	
	public Curve()
	{
		float valeurs [] = { 0, 1.0f, 0.5f, 1.0f, 0};
		setData(valeurs);
	}
	public Curve(int nbrvaleurs)
	{
		init_tableau_valeurs(nbrvaleurs);
	}

	private synchronized void init_tableau_valeurs(int nbrvaleurs)
	{
		valeurs = new ArrayList<Float>();
		refresh_spline_infos();
	}
	
	public synchronized void setData(int index, float valeur)
	{
		//if (index>valeurs.length) throw new ArrayIndexOutOfBoundsException();
		valeurs.set(index, valeur);
		refresh_spline_infos();
		//valeurs[index]=valeur;
	}
	
	public synchronized void setData(float[] valeurs2)
	{
		valeurs = new ArrayList<Float>();
		for (int i = 0; i < valeurs2.length; i++)
		{
			valeurs.add(valeurs2[i]);
		}
		refresh_spline_infos();
		
	}
	
	public static void main(String args[]) throws Exception
	{
		Curve curve = new Curve(6);
		
		float valeurs [] = { 0, 0.5f, 1.0f, 0.5f, 0};
		curve.setData(valeurs);
	//	curve.setStepSustainFrom(2);
	//	curve.setStepSustainFrom(4);
	
		File f = new File("toto.csv");
		PrintStream ps = new PrintStream(f);
		
		for (int i = 0; i < 100; i++)
		{
			float index = i / 100.0f;
			float interp_value = curve.getValue(index);
			System.err.printf("%2.2f\r\n",interp_value);
			ps.println(index+";"+interp_value);
		}
		ps.flush();
		ps.close();
	}

	/**
	 * Return interpolated value a index.
	 * @param index 0 to 1. (0 is start, 1 is end or more if sustain is activated).
	 * @return
	 * @throws Exception 
	 */
	public synchronized float getValue(float index)
	{
		if (Spline==true)
		{
		/*f (index<0) 
				index=0;
			if (index>=0.9999) 
				index=0.99999f;*/
			
			index*=(valeurs.size()-1);
			
			//for (float xi = (float) (0.0+0); xi < cpt-1; xi+=0.1)
		//	System.err.println("indexM = "+index);
			
			
			return (float) psf.value(index);
		}
		
	//	if (index>valeurs.size()) throw new Exception("Overflow");
		int MemIndex = (int)(index * (valeurs.size()-1));
		float sub = (index - (int)index) * (valeurs.size()-1);
		sub = sub - (int) sub;
		
		
		if (MemIndex<0) return 0; // TODO :  A verifier...
		if (MemIndex>=size()) MemIndex=size()-1;
		
		float value1 = valeurs.get(MemIndex);
		float value2 = 0;
		if (MemIndex>=valeurs.size()-1)
		{
			value2=valeurs.get(valeurs.size()-1);
		}
		else
		{
			value2=valeurs.get(MemIndex+1);
		}
		float val = value1 * (1.0f-sub) + value2 * (sub);
		return val;
	}
	public int size()
	{
		return valeurs.size();
	}
	public float getData(int i)
	{
		return valeurs.get(i);
	}
	public float getLastData()
	{
		return valeurs.get(valeurs.size()-1);
	}
	public void setFirstData(float lastData)
	{
		setData(0, lastData);
		
	}
	public void setLastData(float valeur2) {
		setData(size()-1, valeur2);
	}

	public void addData(int afterindex, float valeur2)
	{
		valeurs.add(afterindex+1, valeur2);
		refresh_spline_infos();
	}
	
	public void addData(float valeur2)
	{
		valeurs.add(valeur2);
		refresh_spline_infos();
	}

	public Float remove(int index)
	{
		if (valeurs.size()>=3)
		{
			Float v = valeurs.remove(index);
			refresh_spline_infos();
			return v;
		}
		return null;
	}
	
	public void copysettings(Copiable to)
	{
		this.valeurs.clear();
		for (int i = 0; i < ((Curve)to).valeurs.size(); i++)
		{
			valeurs.add(((Curve)to).valeurs.get(i));
		}
		refresh_spline_infos();
	}
	public float[] getDatas() {
		float [] fl = new float[valeurs.size()];
		for (int i = 0; i < valeurs.size(); i++) {
			fl[i]=valeurs.get(i);
		}
		return fl;
	}
	
	public void invertX() {
		 Collections.reverse(valeurs);
		 refresh_spline_infos();
	}
	public void invertY() {
		for (int i = 0; i < valeurs.size(); i++) {
			Float f = valeurs.get(i);
			f = 1.0f -f;
			valeurs.set(i, f);
		}
		refresh_spline_infos();
	}
	public void SubdivAndSmooth() {
		
		// TODO Auto-generated method stub
		float [] valeurs2= getDatas();
		float [] valeursN= new float[valeurs2.length*2];
		valeursN[0]=valeurs2[0];
		for (int cpt = 0; cpt < valeursN.length; cpt+=2) {
			valeursN[cpt] = valeurs2[cpt/2];
			valeursN[cpt+1] = valeurs2[cpt/2];
		}
		
		valeursN[valeursN.length-1]=valeurs2[valeurs2.length-1];
		
		for (int cpt = 1; cpt < valeursN.length-1; cpt+=2) {
			float valeur = (valeurs2[(cpt-0)/2]+valeurs2[(cpt+1)/2])/2;
			valeursN[cpt] = valeur;
		}
		setData(valeursN);
		
	}

	PolynomialSplineFunction psf;
	
	public void refresh_spline_infos()
	{
		if (Spline==true)
		{
			SplineInterpolator si = new SplineInterpolator();
			//LinearInterpolator si = new LinearInterpolator();
			
			int cpt = this.size();
			double x [] = new double [cpt];
			double y [] = new double [cpt];
			for (int i = 0; i < x.length; i++)
			{
				x [i]=i;
				y [i]=this.getDatas()[i];
			}

			psf = si.interpolate(x, y);
		}
		
	}
	/**
	 * @return the spline
	 */
	public synchronized boolean isSpline()
	{
		return Spline;
	}
	/**
	 * @param spline the spline to set
	 */
	public synchronized void setSpline(boolean spline)
	{
		Spline = spline;
		refresh_spline_infos();
	}
	
	
	
}
