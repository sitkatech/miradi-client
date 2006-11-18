/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;

public class XLSFileFilter extends FileFilter
{

	public boolean accept(File pathname)
	{
		if (pathname.isDirectory())
			return true;
		return (pathname.getName().toLowerCase().endsWith(XLS_EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|XLS (*.xls)");
	}

	public static final String XLS_EXTENSION = ".xls";
}
