/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;

public class TreeExportFileFilter extends FileFilter
{
	public boolean accept(File pathname)
	{
		if (pathname.isDirectory())
			return true;
		return (pathname.getName().toLowerCase().endsWith(MIRADI_TREE_EXPORT_EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|MTE (*.mte)");
	}

	public static final String MIRADI_TREE_EXPORT_EXTENSION = ".mte";
}
