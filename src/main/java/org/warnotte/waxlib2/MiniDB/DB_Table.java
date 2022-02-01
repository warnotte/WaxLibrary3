package org.warnotte.waxlib2.MiniDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.warnotte.waxlib2.Identifiable.Identifiable;
import org.warnotte.waxlib2.TemplateCollectionFilter.BaseComparator;
import org.warnotte.waxlib2.TemplateCollectionFilter.WCollectionFilter;

import com.thoughtworks.xstream.XStream;

/**
 * Cette petite classe permet de stocker des object selon un identifiant unique dans une DB "virtuelle" en m�moire.
 * Il y'a des fonctions de recherche assez souples pour permettre tout sorte de recherches.
 * @author Warnotte Renaud
 * @param <T> L'objet template (le type en qlq sorte) � utiliser pour cette BD.
 */
public class DB_Table<T extends Identifiable> extends WCollectionFilter<T>
{
	HashMap<Integer, T> elements = new HashMap<Integer, T> ();
		
	String fileName = "new.xml";
	
	public Class<?> getTypeInside() throws ClassNotFoundException
	{
		ParameterizedType generic = (ParameterizedType)this.getClass().getGenericSuperclass();
		Class<?> cls = (Class<?>) generic.getActualTypeArguments()[0];
		return cls;//Class.forName(obj.toString());
	}
	
	public void put(int i, T element)
	{
		elements.put(i, element);
	}
	
	public void add(ArrayList<T> materials_list)
	{
		for (int i = 0; i < materials_list.size(); i++)
		{
			T iL = materials_list.get(i);
			put((int) iL.getId(), iL);
		}
		
	}
	
	/*********************************************************************
	 * 
	 * FONCTION DE RECHERCHE
	 * 
	 *********************************************************************/
	
	/**
	 * Recupere l'element a l'id donn�.
	 * @param idMaterial l'identifiant du truc.
	 * @return
	 */
	public T getByID(int idMaterial)
	{
		return elements.get(idMaterial);

	}

	/**
	 * Recuperer le X element de la DB.
	 * @param index
	 * @return
	 */
	public T getByListIndex(int index)
	{
		if (index<=-1) return null; 
		return toList().get(index);
	}
	/**
	 * Recupere le dernier ID utilis� (le plus grand afin de faire +1 pour avoir un new ID);
	 * @return
	 */
	public int getLastId()
	{
		if (elements.size()==0) return 0;
		
		Set<Integer> set = elements.keySet();
		ArrayList<Integer> list = new ArrayList<Integer>(set);
		Collections.sort(list);
		
		for (int i = 0; i < list.size()-1; i++)
		{
			int v0 = list.get(i+0);
			int v1 = list.get(i+1);
			// Si y'a pas de "trou" entre les 2 index, alors on continue;
			if ((v1-1) == v0)
				continue;
			else
				return v0+1;
		}
		return list.size();
	}
	
	public ArrayList<T> getByComparator(BaseComparator<T> comparator)
	{
		return (ArrayList<T>) getByComparator(elements.values(), comparator);
	}
	
	public ArrayList<T> getByFieldValue(String Field_name, Object valueToMatch, boolean EqualsOrDifferent) throws Exception
	{
		return getByFieldValue(elements.values(), Field_name, valueToMatch, EqualsOrDifferent);
	}
	
	public ArrayList<T> getByFieldValue(String Field_name, Object valueToMatch) throws Exception
	{
		return getByFieldValue(elements.values(), Field_name, valueToMatch);
	}

	public List<T> toList()
	{
		return new ArrayList<T>(elements.values());
	}
	
	/*********************************************************************
	 * 
	 * FONCTION DE SAVE/LOAD dans XML
	 * 
	 *********************************************************************/
		
	/**
	 * Charge un fichier XML avec une table complete.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void loadXML(XStream xstream, String XMLfilename) throws IOException
	{
		this.elements.clear();
		File f = new File(XMLfilename);
		FileInputStream fos = new FileInputStream(f);
		elements = (HashMap<Integer, T>) xstream.fromXML(fos);
		fos.close();
		this.fileName=XMLfilename;
	}
	
	
	public void loadXML(XStream xstream) throws Exception
	{
		if (fileName!=null)
			this.loadXML(xstream, fileName);
		else
			throw new Exception("Filename not set for save DB");
	}
	
	@SuppressWarnings("unchecked")
	public void loadXML(XStream xstream, InputStream out) throws IOException, ClassCastException, ClassNotFoundException
	{
		Object o = ((ObjectInputStream)out).readObject();
		elements = (HashMap<Integer, T>)o;
		//elements = (HashMap<Integer, T>)xstream.fromXML(out);//out.readObject();
	}

	
	public void saveXML(XStream xstream, OutputStream oos) throws IOException
	{
		xstream.toXML(elements, oos);
		
	}
	
	/**
	 * Sauve un fichier XML avec une table complete.
	 * @throws IOException 
	 */
	public void saveXML(XStream xstream, String XMLfilename) throws Exception
	{
		System.err.println("Save file "+XMLfilename);
		// save an xml
	    File f = new File(XMLfilename);
		// TODO : pq ???
		FileOutputStream fos = new FileOutputStream(f);
		xstream.toXML(elements, fos);
		fos.flush();
		fos.close();
		this.fileName=XMLfilename;
	}
	
	public void saveXML(XStream xstream) throws Exception
	{
		if (fileName!=null)
			this.saveXML(xstream, fileName);
		else
			throw new Exception("Filename not set for save DB");
	}

	
	@SuppressWarnings("unchecked")
	public void addNew() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Class<?> classDefinition = getTypeInside();
		int lastId = getLastId() ;
		T object = (T) classDefinition.getConstructor().newInstance();
		Identifiable obj = object;
		obj.setId(lastId);
		put(lastId, object);
		//elements.put(new Integer(lastId), object);
	}
	
	//@SuppressWarnings("unchecked")
	public void addNew(T o) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
	//	Class<?> classDefinition = getTypeInside();
		int lastId = getLastId() ;
		Identifiable obj = o;
		obj.setId(lastId);
		put(lastId, o);
	}
	
	public T removeByID(long key)
	{
		return elements.remove((int)key);
	}

	public void clear()
	{
		elements.clear();
	}
	
	public int size()
	{
		return elements.size();
	}

	public boolean contains(T prof)
	{
		return elements.containsValue(prof);
	}
	
	public T getEquals(T prof)
	{
		for (int i = 0; i < elements.size(); i++)
		{
			@SuppressWarnings("unchecked")
			T ref = (T) ((Entry<?, ?>) elements.entrySet().toArray()[i]).getValue();
			if (ref.equals(prof))
				return ref;
		}
		return null; 
	}
}
