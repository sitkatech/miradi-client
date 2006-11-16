/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ProjectUnzipper;

public class ImportZipFileDoer  extends ImportDoer
{
	public boolean createProject(File finalProjectDirectory, File importFile)
	{
		try
		{
			ProjectUnzipper.unzipToProjectDirectory(importFile, finalProjectDirectory);
			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public boolean isCorrectFileType(File importFile)
	{
		try
		{
			return (ProjectUnzipper.isZipFileImportable(importFile));
		}
		catch (Exception e) 
		{
			EAM.logException(e);
			return false;
		}
	}
}
