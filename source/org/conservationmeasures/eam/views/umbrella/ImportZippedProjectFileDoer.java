/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.utils.ZIPFileFilter;

public class ImportZippedProjectFileDoer  extends ImportProjectDoer
{
	public void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		ProjectUnzipper.unzipToProjectDirectory(importFile, homeDirectory, newProjectFilename);
	}

	public FileFilter getFileFilter()
	{
		return new ZIPFileFilter();
	}
	
	public String getFileExtension()
	{
		return ZIPFileFilter.EXTENSION;
	}
}
