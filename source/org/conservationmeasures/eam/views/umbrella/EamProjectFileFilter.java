/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;

public class EamProjectFileFilter extends FileFilter 
{

	public EamProjectFileFilter() 
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		return(pathname.getName().endsWith(EAM.PROJECT_EXTENSION));
	}

	public String getDescription()
	{
		return EAM.text("FileFilter|Project Files (*.eam)");
	}

}
