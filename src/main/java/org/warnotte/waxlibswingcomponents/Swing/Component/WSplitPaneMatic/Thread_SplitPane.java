package org.warnotte.waxlibswingcomponents.Swing.Component.WSplitPaneMatic;
import java.awt.Component;

import javax.swing.JSplitPane;


public class Thread_SplitPane extends Thread
{
	Component Cible;
	Component old_Cible;
	
	Component contentpane;
	double acceleration = 0;
	
	public Thread_SplitPane(Component contentpane,Component Cible)
	{
		acceleration=0;
		this.Cible=Cible;
		this.contentpane=contentpane;
	}
	@Override
	public void run()
	{
		int done_stg = 0;
		
		while (true)
		{
			//Trouver la list des splits panes parent du bon coté
			Component parent = getCible();
			Component old_component = parent;
			acceleration+=acceleration/10;
			done_stg = 0;
			int deep = 1;
			double space = 10;
			synchronized(this)
			{
			do
			{
				if ((parent instanceof WSplitPaneMatic) && (((WSplitPaneMatic)parent).getAutoSplit()==true))
				{
					//System.err.println("Thr::Cible=="+parent);
						
					JSplitPane p1 = (JSplitPane) parent;
					//System.err.print("Parent is "+parent.getName());
					//double div = getValueofShare(p1);
					
					int divider = p1.getDividerLocation();
					int max = p1.getWidth();
					if (p1.getOrientation()==JSplitPane.VERTICAL_SPLIT)
						max = p1.getHeight();
					
					if ((p1.getLeftComponent()==old_component)) 
					{
						//System.err.println(" Left to "+max+" "+divider);
						//p1.setDividerLocation(0.9);
						
						if (divider<max-max/(space)) 
						{
							divider+=acceleration;
						//	System.err.println("+Left to "+max+" "+divider);
							if (divider>max) divider = (int) (max-max/space);
							p1.setDividerLocation(divider);
							done_stg=1;
						}
						 
							
						
					}
					if ((p1.getRightComponent()==old_component))
					{
						//System.err.println(" Right to "+max+" "+divider);
						//p1.setDividerLocation(0.9);
						if (divider>max/(space+deep)) 
						{
							divider-=acceleration;
							//System.err.println("+Right to "+max+" "+divider);
							if (divider<max/(space+deep)) 
								divider = (int) (max/(space+deep));
							p1.setDividerLocation(divider);
							done_stg=1;
						}
					}	
				}
				old_component = parent;
				getContentpane().repaint();
				if (parent==null) break;
				parent=parent.getParent();
				deep++;
				
			
        } while ((parent!=null) || (parent!=getContentpane()));
			}
      	try
		{
      		if (done_stg==0)
      		{
      			System.err.println("J'ai plus rien a faire je method'endors");
      			synchronized(this)
      			{
      				wait();
      				System.err.println("Jme reveille pour faire bouger le monde");
      				
      			}
      		}
			Thread.sleep(2);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	}
	
	public synchronized final Component getCible()
	{
		return Cible;
	}
	/*public synchronized final void setCible(Component cible, double acceleration)
	{
	setCible(cible, 0.5d);
	}*/
	public synchronized final void setCible(Component cible, double acceleration)
	{
		old_Cible=this.Cible;
		Cible = cible;
		this.acceleration=acceleration;
		if (old_Cible!=Cible)
		notify();
		
	}
	public synchronized final Component getContentpane()
	{
		return contentpane;
	}
	public synchronized final void setContentpane(Component contentpane)
	{
		this.contentpane = contentpane;
	}
}
