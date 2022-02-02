package io.github.warnotte.waxlib3.waxlib2.TemplatePropertyMerger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.warnotte.waxlib3.waxlib2.TemplatePropertyMerger.Annotations.PROPERTY_interface;



/**
 * Permet de créer a partir d'une liste d'objet de meme type un objet qui aura ses variables
 * initialisée uniquement si elles sont communes à tout les objets. 
 * (ou de faire une somme, une moyenne, ...)
 * @author Warnotte Renaud
 *
 */
public class TemplatePropertyMergerV2
{
	protected static final Logger Logger = LogManager.getLogger("TemplatePropertyMergerV2");
	
	public static double DefaultValuefornumber = 2147483647d;
	
	public static boolean PRINT_DEBUG = false;
	
	static Map<Class<?>, List<editablemethod>> map_annotated_sorted = new HashMap<>();
	static Map<Class<?>, List<editablemethod>> map_AllMethod_sorted = new HashMap<>();
	
	private static List<editablemethod> FindAnnotatedMethodsUnBuffered(Class<?> ObjectClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		List<editablemethod> listretour = new ArrayList<editablemethod>();
	    Method []  methods = ObjectClass.getMethods();
	    // Parcourcir tout les GET avec une annotation PROPERTY_MERGEABLE
	    for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			//String Get_method_name = m.getName();
			PROPERTY_interface annotation = m.getAnnotation(PROPERTY_interface.class);
			if (annotation == null)
				 continue;
				
			editablemethod c = new editablemethod();
			c.method=m;
			c.mode = annotation.Operation();
			listretour.add(c);
	    }
	    
	    RetrieEditableMethod(listretour);
		return listretour;
	}
	
	private static List<editablemethod> FindAllMethodsUnBuffered(Class<?> ObjectClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		List<editablemethod> listretour = new ArrayList<editablemethod>();
	    Method []  methods = ObjectClass.getMethods();
	    // Parcourcir tout les GET avec une annotation PROPERTY_MERGEABLE
	    for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			//String Get_method_name = m.getName();
			
			if (m.getReturnType()==void.class)
				continue;
			if (m.getName().contains("equals"))
				continue;
			if (m.getName().contains("Class"))
				continue;
			if (m.getName().contains("hashCode"))
				continue;
			if (m.getName().contains("toString"))
				continue;
			
			editablemethod c = new editablemethod();
			c.method=m;
			
			//String Get_method_name = m.getName();
			PROPERTY_interface annotation = m.getAnnotation(PROPERTY_interface.class);
			if (annotation != null)
				c.mode = annotation.Operation();
			else
				c.mode = property_mode.PROPERTY_MERGEABLE; // annotation.getOperation();
			listretour.add(c);
	    }
	    RetrieEditableMethod(listretour);
		return listretour;
		
	}
	
	protected static List<editablemethod> FindAnnotatedMethods(Class<?> ObjectClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		if (map_annotated_sorted.get(ObjectClass)==null)
		{
			List<editablemethod> ret = FindAnnotatedMethodsUnBuffered(ObjectClass);
			map_annotated_sorted.put(ObjectClass, ret);			
		}
		return map_annotated_sorted.get(ObjectClass);
	}
	
	protected static List<editablemethod> FindAllMethods(Class<?> ObjectClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		if (map_AllMethod_sorted.get(ObjectClass)==null)
		{
			List<editablemethod> ret = FindAllMethodsUnBuffered(ObjectClass);
			map_AllMethod_sorted.put(ObjectClass, ret);			
		}
		return map_AllMethod_sorted.get(ObjectClass);
	}

	/**
	 * Retrie la liste des methods en regardant si orderDisplay est pr�sent dans l'eventuelle annotations d'une m�thode
	 * Permet d'avoir toujours la liste dans le m�me ordre (getMethods() ne fournissant aucune garantie quand a l'ordre de retour).
	 * @param dudus
	 */
	private static void RetrieEditableMethod(List<editablemethod> dudus)
	{
		  java.util.Collections.sort(dudus, new Comparator<Object>()
			{
				public int compare(Object o1, Object o2)
				{
					editablemethod oo1 = (editablemethod) o1;
					editablemethod oo2 = (editablemethod) o2;
					
					PROPERTY_interface or1 = oo1.method.getAnnotation(PROPERTY_interface.class);
					PROPERTY_interface or2 = oo2.method.getAnnotation(PROPERTY_interface.class);
					
					// nulls last
	                if (or1 != null && or2 != null) {
	                    return or1.orderDisplay() - or2.orderDisplay();
	                } else
	                if (or1 != null && or2 == null) {
	                    return -1;
	                } else
	                if (or1 == null && or2 != null) {
	                    return 1;
	                }
	                return 0;
					
					
				}
			});
			
	}
	
	public static List<?> MergeCollection(List<?> pans) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		return TemplatePropertyMergerV2.MergeCollection(pans, true);
	}
	
	/**
	 * 
	 * @param parentName Eventually a parent name in order to keep track of parent and son (object with object inside)
	 * @param pans List of selected element of the same types
	 * @param findOnlyAnnotatedMethod Will check only for method with annotation or will take all methods and make a merge of values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	
	public static List<?> MergeCollection(List<?> pans, boolean findOnlyAnnotatedMethod) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		List<Object> retour = new ArrayList<Object>();
	    // Etape 1.
	    // Recherche des get/set
		if (pans==null) return  retour;
		if (pans.size()==0) return  retour;
	    if (pans.get(0)==null) return  retour;
	    Class<?> ObjectClass=pans.get(0).getClass();
	    
	    List<editablemethod> nomMethodesAnnotee = null;
	    
	    if (findOnlyAnnotatedMethod)
	    	nomMethodesAnnotee = FindAnnotatedMethods(ObjectClass);
	    else
	    	nomMethodesAnnotee = FindAllMethods(ObjectClass);
	    
	    // Parcourcir tout les GET avec une annotation PROPERTY_MERGEABLE
	    for (int i = 0; i < nomMethodesAnnotee.size(); i++) {
		    editablemethod d = nomMethodesAnnotee.get(i);
		    
			try {
				ResultatMerge rm;
				rm = recupereValeurMergeeApartirDeLaListe(pans, ObjectClass, d);
				retour.add(rm);
			} catch (Exception e) {
				//Logs.getLogger().fatal(e,e);
				Logger.fatal(e, e);
				e.printStackTrace();
			}
			
		}
	    return retour;
	}


	private static ResultatMerge recupereValeurMergeeApartirDeLaListe(List<?> ListObjects, Class<?> ObjectClass, editablemethod d) throws Exception {
		Method m = d.method;
		String Get_method_name = m.getName();

		int offs = 3;
		if ((m.getReturnType() == boolean.class) || (m.getReturnType() == Boolean.class))
			offs--;
		String Set_method_name = "set" + m.getName().substring(offs);
		if (PRINT_DEBUG != false)
			System.err.println("TemplatePropertyMergerV2::" + Get_method_name + " detected.");

		// Object value="";
		boolean equals = true; // No Equals aussi logiquement.
		Method hasSet = null; // No Set logiquement.
					
		// Chercher si y 'a une method Set.
		//@SuppressWarnings("unused")
		//Method setMeth = null;

		try
		{
			hasSet = ObjectClass.getMethod(Set_method_name, m.getReturnType());
		} 
		catch (SecurityException e)
		{
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e)
		{
			//e.printStackTrace();
		}
		
		if (d.mode == property_mode.PROPERTY_MERGEABLE)
		{
			Object value = "";
			String old_GETValue = null;
			// Parcour les valeurs et check...
			for (int j = 0; j < ListObjects.size(); j++)
			{
				Object item = ListObjects.get(j);

				if (PRINT_DEBUG)
				{
					System.err.println("TemplatePropertyMergerV2::Method = " + m);
					System.err.println("TemplatePropertyMergerV2::ItemClss = " + item.getClass());
					System.err.println("TemplatePropertyMergerV2::Item = " + item);
				}

				value = m.invoke(item);
				if (value == null)
					continue;
				String vv = value.toString();

				if ((old_GETValue != null) && (vv.equals(old_GETValue) == false))
				{
					value = null;
					equals = false;
					break;
				}
				old_GETValue = vv;
			}
			ResultatMerge rm = new ResultatMerge(ObjectClass, m, value, equals, hasSet);
			return rm;
		}
		if (d.mode == property_mode.PROPERTY_SUMMABLE)
		{
			double value = 0.0f;
			// Parcour les valeurs et check...
			for (int j = 0; j < ListObjects.size(); j++)
			{
				Object item = ListObjects.get(j);
				String valueStr = "" + m.invoke(item);
				value += Double.parseDouble("" + valueStr);
			}

			ResultatMerge rm = new ResultatMerge(ObjectClass, m, value, equals, hasSet);
			return rm;
		}
		if (d.mode == property_mode.PROPERTY_AVGABLE)
		{
			double value = 0.0f;

			ResultatMerge rm = null;
			if ((m.getReturnType() != String.class))
			{
				// Parcour les valeurs et check...
				for (int j = 0; j < ListObjects.size(); j++)
				{
					Object item = ListObjects.get(j);
					String valueStr = "" + m.invoke(item);
					value += Double.parseDouble("" + valueStr);
				}
				value /= ListObjects.size();
				rm = new ResultatMerge(ObjectClass, m, value, equals, hasSet);
			} 
			else
			{
				Object item = ListObjects.get(0);
				String valueStr = "" + m.invoke(item);
				rm = new ResultatMerge(ObjectClass, m, valueStr, false, hasSet);
			}
			return rm;
		}
		throw new Exception("No operator found for OBJ2GUI2");
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception
	{
/*		Vector<ChaussureNew> pans = new Vector<ChaussureNew>();
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 40, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 41, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 42, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 43, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 44, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "ROUGE", 45, 12.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "VERTE", 38, 11.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "VERTE", 39, 11.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 1, "VERTE", 40, 11.99f));
		pans.add(new ChaussureNew("NIKE", "AIR_MAX", 2, "VERTE", 45, 14.99f));
	*/
	}
	
}
