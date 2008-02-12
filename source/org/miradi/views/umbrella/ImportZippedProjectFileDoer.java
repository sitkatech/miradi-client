/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.MPZFileFilter;
import org.miradi.utils.ZIPFileFilter;

public class ImportZippedProjectFileDoer  extends ImportProjectDoer
{
	public void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		ProjectUnzipper.unzipToProjectDirectory(importFile, homeDirectory, newProjectFilename);
	}

	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new ZIPFileFilter(), new MPZFileFilter()};
	}
}
