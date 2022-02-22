/**
 * 
 */
package io.github.warnotte.waxlib3.core.XPathUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Warnotte Renaud
 *
 */
public class FileNameFilterByExtension implements FilenameFilter
{
	String extension = "*";
	
	
	public FileNameFilterByExtension(String ext)
	{
		this.extension=ext;
	}
	
	@Override
	public boolean accept(File dir, String name)
	{
		if (name.endsWith(extension)) 
			return true;
		return false;
	}
	
}

