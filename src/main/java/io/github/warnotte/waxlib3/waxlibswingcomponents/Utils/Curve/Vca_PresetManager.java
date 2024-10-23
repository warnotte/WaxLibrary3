package io.github.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.ArrayTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

/**
 * 
 * @author Warnotte Renaud.
 * NOTE : La synchronization est un peu bizarre ...
 */
public class Vca_PresetManager {

 
	
	static String MatFile = "config"+File.separator+"Vca_FactoryPresets.xml";
	//static String MatFile = "C:\\Users\\warnotte\\git\\WaxLibrary3\\src\\main\\java\\io\\github\\warnotte\\waxlib3\\waxlibswingcomponents\\Utils\\Curve\\Vca_FactoryPresets.xml";
    static private Vector<Vca_Preset> vca_presets = new Vector<Vca_Preset>();
    
    public Vca_PresetManager() throws Exception
    {
    	/*Vca_Preset vp = new Vca_Preset(new float []{0,1,4}, "fat");
    	weld_costs.add(vp);
    	Save();*/
    	Load();
    }
       
    public static synchronized Vector<Vca_Preset> getVca_presets() {
		return vca_presets;
	}

	public static synchronized void setVca_presets(Vector<Vca_Preset> vca_presets) {
		Vca_PresetManager.vca_presets = vca_presets;
	}

	/*public static Vca_Preset getWeld_Cost()
    {
		return null;
    }*/
    
    /**
     * Sauve dans le fichier XML
     * @throws IOException 
     */
    public void Save() throws IOException
    {
	// Partie XML
    	File f = new File(MatFile);
    	FileOutputStream fos = new FileOutputStream(f);
    	XStream xstream = new XStream(new DomDriver());
    	xstream.toXML(vca_presets, fos);
    	
    	fos.flush();
    	fos.close();
	
    }
    
    /**
     * Charge la liste a partir du fichier XML
     * @throws Exception 
     */
    private void Load() throws Exception
    {
    	LoadThread();
	}
    
    @SuppressWarnings("unchecked")
	private void LoadThread() throws IOException
    {
    	// Charge
		//Logs.getLogger().info("WeldProcessManager::Loading WeldProcess file "+MatFile);
		XStream xstream = new XStream(new DomDriver());
		
		xstream.addPermission(NoTypePermission.NONE);
		// allow some basics
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.addPermission(ArrayTypePermission.ARRAYS);
		xstream.allowTypeHierarchy(Collection.class);
		// allow any type from the same package
		xstream.allowTypesByWildcard(new String[] {
		    "io.github.warnotte.**"
		});
		
		File f = new File(MatFile);
		FileInputStream fos = new FileInputStream(f);
		xstream.alias("Curve.Vca_Preset", Vca_Preset.class);
		xstream.alias("org.warnotte.Utils.Curve.Vca_Preset", Vca_Preset.class);
		vca_presets = (Vector<Vca_Preset>) xstream.fromXML(fos);
		fos.close();
		
	/*	try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	//	Logs.getLogger().info("WeldProcessManager::Loaded "+Vca_Presets.size()+" Vca_Preset(s).");
	}

   /* @SuppressWarnings("unused")
	static private synchronized Vector<Vca_Preset> getVca_Preset() {
        return Vca_Presets;
    }

    static private synchronized void setVca_Preset(Vector<Vca_Preset> materials1) {
        Vca_Presets = materials1;
    }
    */
    public static void main(String []args) throws Exception
    {
	//	@SuppressWarnings("unused")
	//	Logs l = new Logs("TestWeldProcessManager");
	//	Logs.getLogger().info("1");
	//	@SuppressWarnings("unused")
	//	Vca_PresetManager m = new Vca_PresetManager();
	//	Logs.getLogger().info("2");
	/*	
	//	System.err.println(""+m.getVca_Preset(0, 5, 4, 3, 'C', TypeCost.PREPARATION));
		System.err.println("A "+WeldProcessManager.getVca_Preset(1, 1, 0, 7, 'F', TypeCost.WELDING));
		System.err.println("B "+WeldProcessManager.getVca_Preset(1, 1, 0, 7, 'F', TypeCost.PREPARATION));
		*/ 
	//	Logs.getLogger().info("3");
		
	
		
	
    }

	public void addPreset(String named, float[] values) {
		
		Vca_Preset preset = new Vca_Preset(values, named);
		vca_presets.add(preset);
	}
}
