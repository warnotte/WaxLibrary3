/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.warnotte.waxlib2.Archives;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Cleaner
{

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		List<?> files = getFileListing(new File("c:\\flm"));
		for (int i = 0; i < files.size(); i++)
		{
			File f = (File) files.get(i);
			System.err.println("Delete " + f);
			f.delete();
		}
	}

	/**
	 * Recursively walk a directory tree and return a List of all Files found;
	 * the List is sorted using File.compareTo.
	 *
	 * @param aStartingDir
	 *            is a valid directory, which can be read.
	 */
	@SuppressWarnings("unchecked")
	static public List<?> getFileListing(File aStartingDir) throws FileNotFoundException
	{
		validateDirectory(aStartingDir);
		List<File> result = new ArrayList<File>();

		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		Iterator<File> filesIter = filesDirs.iterator();
		File file = null;
		while (filesIter.hasNext())
		{
			file = filesIter.next();
			if ((file.getAbsolutePath().endsWith(".sfv")) || (file.getAbsolutePath().endsWith(".log")) || (file.getAbsolutePath().contains("COMPLETE")))
				result.add(file); //always add, even if directory
			if (!file.isFile())
			{
				//must be a directory
				//recursive call!
				List<?> deeperList = getFileListing(file);
				result.addAll((Collection<? extends File>) deeperList);
			}

		}

		Collections.sort(result);
		return result;
	}

	/**
	 * Directory is valid if it exists, does not represent a file, and can be
	 * read.
	 */
	static private void validateDirectory(File aDirectory) throws FileNotFoundException
	{
		if (aDirectory == null)
		{
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!aDirectory.exists())
		{
			throw new FileNotFoundException("Directory does not exist: " + aDirectory);
		}
		if (!aDirectory.isDirectory())
		{
			throw new IllegalArgumentException("Is not a directory: " + aDirectory);
		}
		if (!aDirectory.canRead())
		{
			throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
		}
	}

	public static void clean(String string) throws FileNotFoundException
	{
		System.err.println("Will clean directory " + string);
		List<?> files = Cleaner.getFileListing(new File(string));
		for (int i = 0; i < files.size(); i++)
		{
			File f = (File) files.get(i);
			System.err.println("Delete " + f);
			f.delete();
		}
	}

}
