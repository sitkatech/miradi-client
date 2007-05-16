/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.TncCapWorkbookImporter;
import org.conservationmeasures.eam.utils.XLSFileFilter;

public class ImportTncCapWorkbookDoer extends ImportProjectDoer
{
	public void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		TncCapWorkbookImporter importer =new TncCapWorkbookImporter(importFile.getAbsolutePath());
		Project project = new Project();
		project.createOrOpen(new File(homeDirectory, newProjectFilename));
		
		try
		{
			project.setMetadata(ProjectMetadata.TAG_PROJECT_NAME, newProjectFilename);
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
	}

	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new XLSFileFilter()};
	}
}
