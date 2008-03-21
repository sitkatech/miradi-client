/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;

public class SVGFileFilter extends FileFilter implements MiradiFileFilter
{
	public SVGFileFilter() 
	{
		super();
	}

	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		return(pathname.getName().toLowerCase().endsWith(EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|SVG (*.svg)");
	}
	
	public String getFileExtension()
	{
		return EXTENSION;
	}

	public static final String EXTENSION = ".svg";

}
