/**
 * 
 */
package org.warnotte.waxlib3.waxlib2.XPathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Document; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2
import org.jdom2.JDOMException; // The package org.jdom2 is accessible from more than one module: jdom2, jdom2
import org.jdom2.input.SAXBuilder;

/**
 * @author Warnotte Renaud
 *
 */
public class DBXML
{
	Map<File, Document>	map_dbxml_file_to_doc	= new HashMap<>();
	Map<Integer, File>	map_dbxml_doc_to_file	= new HashMap<>();

	// Synchronized lists to get easy elements.
	private final List<Document> list_documents = new ArrayList<>();
	private final List<File> list_file = new ArrayList<>();
	
	/**
	 * @return
	 */
	public List<Document> getDocuments()
	{
		return list_documents;
	}
	
	public int getSize()
	{
		return map_dbxml_file_to_doc.size();
	}
	
	public Document getDocument(int index)
	{
		return list_documents.get(index);
	}
	
	public File getFile(int index)
	{
		return list_file.get(index);
	}
	
	public File getFile(Document doc)
	{
		return list_file.get(list_documents.indexOf(doc));
	}
	
	/**
	 * Read A folder of XML File recursively.
	 * @param directory
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void loadXMLS(String directory) throws JDOMException, IOException
	{
		long	start	= System.currentTimeMillis();
		loadXMLSRecurse(directory);
		long	stop	= System.currentTimeMillis();
		long	elapsed	= stop - start;
		System.out.println("Total Loading Time Elapsed : " + elapsed);
		System.out.println("Total XML Loaded : " + map_dbxml_file_to_doc.size());
		
	}
	
	/**
	 * Read A folder of XML File recursively.
	 * @param directory
	 * @throws JDOMException
	 * @throws IOException
	 */
	private void loadXMLSRecurse(String directory) throws JDOMException, IOException
	{
		File	dir		= new File(directory);
		File[]	xmls	= dir.listFiles(new FileNameFilterByExtension(".xml"));
		
		System.out.printf("Loading %d XML file in directoy %s\r\n", xmls.length, directory);
		
		for (int i = 0; i < xmls.length; i++)
		{
			System.out.printf("Loading XML file %s\r\n", xmls[i]);
			File		file		= xmls[i];
			InputStream	in			= new FileInputStream(file);
			SAXBuilder	builder		= new SAXBuilder();
			Document	document	= builder.build(in);
			in.close();
			map_dbxml_file_to_doc.put(file, document);
            // TODO : Peut être mettre le nom du fichier ou autre chose que int ... mais j'ai mis int parce document passait pas bien je crois ? plutot que le hashcode
			map_dbxml_doc_to_file.put(document.hashCode(), file);
			list_documents.add(document);
			list_file.add(file);
		}
		
		File[] directories = dir.listFiles(File::isDirectory);
		for (int i = 0; i < directories.length; i++)
			loadXMLSRecurse(directories[i].getAbsolutePath());
		
	}


	public List<Entry<File, Document>> findXMLThatMathPredicates(Predicate<Document> pred_group_final)
	{
		return findXMLThatMathPredicates(pred_group_final, null);
	}
	
	/**
	 * Attention, que le tri se fait en parallel, donc il faudra demander au stream un ForEarchOrdered par exemple
	 * @param pred_group_final
	 * @param compareByAll
	 * @return
	 */
	public List<Entry<File, Document>> findXMLThatMathPredicates(Predicate<Document> pred_group_final, Comparator<Document> compareByAll)
	{
		Stream<Document> outstream = map_dbxml_file_to_doc.values().parallelStream().filter(pred_group_final);
		
		// Ralentit un peu le bazard
		if (compareByAll!=null)
			outstream = outstream.parallel().sorted(compareByAll);
		
		List<Entry<File, Document>> returned_files = new ArrayList<>();
		List<Document> docs = outstream.collect(Collectors.toList());
		for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();)
		{
			Document document = iterator.next();
	         // TODO : Peut être mettre le nom du fichier ou autre chose que int ... mais j'ai mis int parce document passait pas bien je crois ? plutot que le hashcode
			returned_files.add(new AbstractMap.SimpleEntry<>(map_dbxml_doc_to_file.get(document.hashCode()), document));
		}
		return returned_files;
	}

	


}
