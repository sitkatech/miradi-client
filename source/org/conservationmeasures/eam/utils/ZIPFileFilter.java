/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;

public class ZIPFileFilter extends FileFilter 
{
	public ZIPFileFilter() 
	{
		super();
	}

	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		return(pathname.getName().toLowerCase().endsWith(ZIP_EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|ZIP (*.zip)");
	}

	public static final String ZIP_EXTENSION = ".zip";
}
