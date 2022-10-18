/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.views.umbrella;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.ProgressInterface;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

import java.io.File;

abstract public class AbstractZippedXmlImporter extends AbstractBaseProjectImporter
{
	public AbstractZippedXmlImporter(MainWindow mainWindowToUse, boolean commandLineModeToUse)
	{
		super(mainWindowToUse, commandLineModeToUse);
	}
	
	protected ImportXmlProjectResult importProjectFromXmlEntry(MiradiZipFile zipFile, ProgressInterface progressIndicator) throws Exception
	{
		Project projectToFill = createProjectToFill();

		int documentSchemaVersion = Integer.parseInt(Xmpz2XmlConstants.NAME_SPACE_VERSION);
		ImportXmlProjectResult migrationResult = new ImportXmlProjectResult(projectToFill, false, documentSchemaVersion);

		InputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
		if (projectAsInputStream.available() == 0)
			throw new Exception(XmlExporterDoer.PROJECT_XML_FILE_NAME + EAM.text(" was empty"));

		try
		{
			migrationResult = importProjectXml(projectToFill, zipFile, projectAsInputStream, progressIndicator);
		}
		finally
		{
			projectAsInputStream.close();
		}
		
		return migrationResult;
	}

	protected Project createProjectToFill() throws Exception
	{
		Project projectToFill = new Project();
		projectToFill.finishOpeningAfterLoad("[Imported]");
		return projectToFill;
	}
	
	@Override
	public GenericMiradiFileFilter[] getFileFilters()
	{
		return new GenericMiradiFileFilter[] {createFileFilter()};
	}

	@Override
	protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception
	{
	}

	abstract protected GenericMiradiFileFilter createFileFilter();

	abstract protected ImportXmlProjectResult importProjectXml(Project projectToFill, MiradiZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception;

	protected class ImportXmlProjectResult
	{
		public ImportXmlProjectResult(Project projectToUse, boolean projectRequiresReverseMigrationToUse, int documentSchemaVersionToUse)
		{
			project = projectToUse;
			projectRequiresReverseMigration = projectRequiresReverseMigrationToUse;
			documentSchemaVersion = documentSchemaVersionToUse;
		}

		public Project getProject() { return project; }
		public boolean getProjectRequiresReverseMigration() { return projectRequiresReverseMigration; }
		public int getDocumentSchemaVersion() { return documentSchemaVersion; }

		private boolean projectRequiresReverseMigration;
		private int documentSchemaVersion;
		private Project project;
	}
}
