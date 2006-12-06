/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.TncCapWorkbookImporter;
import org.conservationmeasures.eam.utils.XLSFileFilter;

public class ImportTncCapWorkbookDoer extends ImportDoer
{
	public boolean createProject(File finalProjectDirectory, File importFile)
	{
		try
		{
			TncCapWorkbookImporter importer =new TncCapWorkbookImporter(importFile.getAbsolutePath());
			Project project = new Project();
			project.createOrOpen(finalProjectDirectory);
			
			try
			{
				project.setMetadata(ProjectMetadata.TAG_PROJECT_NAME, finalProjectDirectory.getName());
				project.setMetadata(ProjectMetadata.TAG_PROJECT_VISION, importer.getProjectVision());
				project.setMetadata(ProjectMetadata.TAG_PROJECT_SCOPE, importer.getProjectScopeFull());
				project.setMetadata(ProjectMetadata.TAG_START_DATE, importer.getProjectStartDate());
				project.setMetadata(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, importer.getProjectDataEffectiveDate());
				project.setMetadata(ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES, importer.getProjectSize());
				project.setMetadata(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, importer.getProjectDownloadDate());
				project.setMetadata(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE, importer.getProjectVersionDate());
				project.setMetadata(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER, importer.getProjectVersion());
				project.setMetadata(ProjectMetadata.TAG_TNC_LESSONS_LEARNED, importer.getProjectLessonsLearned());
				project.setMetadata(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, importer.getPlanningTeamComment());
			} 
			finally
			{
				project.close();
			}
			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	
	public FileFilter getFileFilter()
	{
		return new XLSFileFilter();
	}
	
	public String getFileExtension()
	{
		return XLSFileFilter.EXTENSION;
	}
	
}
