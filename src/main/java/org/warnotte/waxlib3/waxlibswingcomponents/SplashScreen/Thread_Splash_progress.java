package  org.warnotte.waxlib3.waxlibswingcomponents.SplashScreen;

import java.util.Random;

public class Thread_Splash_progress extends Thread
{
	public int running=0;
    LoadSplash splash = null;
    int delay_fixe=250;
    int rand_delay=50;
    public Thread_Splash_progress(LoadSplash splash_screen)
    {
        running=1;
        splash = splash_screen;
    }    
    public Thread_Splash_progress(LoadSplash splash_screen, int delay_fixe, int delay_variable)
    {
        running=1;
        splash = splash_screen;
        this.delay_fixe=delay_fixe;
        this.rand_delay=delay_variable;
    }
     
    @Override
	public void run()
    {
      //  Thread thisThread = Thread.currentThread();
      
        Random t =  new Random();
      
        for (int i=1;i<100;i++)
        {
            splash.set_ProgressBarValue(i);
              try {
                  if (running==1)
                Thread.sleep(delay_fixe+   t.nextInt(rand_delay)+i*10);
            } catch (InterruptedException e) {
            //e.printStackTrace();
            }
        }
        Thread.interrupted();
    }
  

}

