/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.utils.MPZFileFilter;
import org.conservationmeasures.eam.utils.ZIPFileFilter;

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
