/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;

public class JPEGFileFilter extends FileFilter 
{
	public JPEGFileFilter() 
	{
		super();
	}

	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		return(pathname.getName().toLowerCase().endsWith(JPG_EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|JPEG (*.jpg)");
	}

	static final String JPG_EXTENSION = ".jpg";
}
