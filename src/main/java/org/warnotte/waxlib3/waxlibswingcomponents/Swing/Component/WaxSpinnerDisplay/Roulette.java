package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.WaxSpinnerDisplay;

import java.util.Random;

// TODO : N'utiliser qu'un thread pour toutes les roulettes et pas un thread par roulette c un peu exagerer.
/**
 * 
 * @author Warnotte Renaud 2009
 *
 */
public class Roulette extends Thread
{
	
	float position = 0;
	static Random rand = new Random();
	float onenumberspeed = 1000+rand.nextInt(100); // 1 sec pour tourner la roulette.
	float onestep = 50+rand.nextInt(10); // 50 etaped pour tourner la roulette (avec une pause de onenumberspeed / onestep)
	int desiredValue = 0;
	
	public static void main(String args[])
	{
		Roulette r = new Roulette();
		try
		{
			Thread.sleep(100);
			r.setValue(5);
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public Roulette()
	{
		start();
	}
	
	@Override
	public void run()
	{
		long delaystep = (long) (onenumberspeed/onestep);
		float amountstep =  (float) (1.0/onestep);
		while(true) // héhé
		{
			// Attends que la roulette recoive un nouveau chiffre a afficher.
			synchronized(this)
			{
				try
				{
					System.err.println("Thread waiting");
					wait();
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}	
			}
			
			while(Math.abs(position-desiredValue)>0.001)
			{
				position += amountstep;
				if (position>=10) position = 0;
			//	System.err.println("position == "+position +" desiredvalue == "+desiredValue);
				try
				{
					Thread.sleep(delaystep);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			position = desiredValue;
			System.err.println("Compteur bien position�");
		}
	}
	
	public void setValue(int newValue)
	{
		// Avertir le thread de la roulette qu'il faut qu'il se reveille :)
		desiredValue=newValue;
		synchronized(this)
		{
			notify();
		}
	}
	
} 
