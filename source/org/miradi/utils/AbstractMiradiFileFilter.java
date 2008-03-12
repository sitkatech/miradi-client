/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

abstract public class AbstractMiradiFileFilter extends FileFilter implements MiradiFileFilter
{
	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		
		return(pathname.getName().toLowerCase().endsWith(getFileExtension()));
	}
}
