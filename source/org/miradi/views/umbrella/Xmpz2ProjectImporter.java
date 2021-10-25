/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.VersionRange;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaver;
import org.miradi.project.RawProjectSaver;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Xmpz2FileFilter;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.generic.AbstractProjectImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

import java.io.File;

public class Xmpz2ProjectImporter extends AbstractProjectImporter
{
	public Xmpz2ProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected AbstractXmlImporter createXmpzXmlImporter(Project projectToFill,	ProgressInterface progressIndicator) throws Exception
	{
		return new Xmpz2XmlImporter(projectToFill, progressIndicator);
	}

	@Override
	protected void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator) throws Exception
	{
		ImportXmlProjectResult importResult = importProject(importFile, progressIndicator);

		if (importResult.getProjectRequiresReverseMigration())
			reverseMigrateProject(importResult.getProject(), newProjectFile, importResult.getDocumentSchemaVersion());
		else
			ProjectSaver.saveProject(importResult.getProject(), newProjectFile);
	}

	private void reverseMigrateProject(Project project, File newProjectFile, int documentSchemaVersion) throws Exception
	{
		String projectMpfSnapShot = ProjectSaver.createSnapShot(project);
		RawProject rawProjectToMigrate = RawProjectLoader.loadProject(projectMpfSnapShot);
		MigrationManager migrationManager = new MigrationManager();

		// reverse back to appropriate version of Miradi for document version...subsequent open project process will then forward migrate as necessary
		int versionToReverseMigrateTo = MigrationManager.getLatestMigrationForDocumentSchemaVersion(documentSchemaVersion);
		MigrationResult migrationResult = migrationManager.migrate(rawProjectToMigrate, new VersionRange(versionToReverseMigrateTo));

		if (migrationResult.cannotMigrate())
		{
			final String message = EAM.substituteSingleString(EAM.text("Unable to complete this migration.\n\n" +
					"Issues encountered:\n" +
					"%s"), migrationResult.getUserFriendlyGroupedCannotMigrateMessagesAsString());

			EAM.errorDialog(message);
		}

		// possible edge case where the xml forward migration / import added some data that will be lost by the reverse migration
		// the premise is that the forward migration run as part of the open project process will compensate accordingly, but we log the details here just in case
		if (migrationResult.didLoseData())
		{
			final String message = EAM.substituteSingleString(EAM.text("Reverse migration completed but with data loss.\n\n" +
					"Issues encountered:\n" +
					"%s"), migrationResult.getUserFriendlyGroupedDataLossMessagesAsString());

			EAM.logWarning(message);
		}

		if (migrationResult.didFail())
		{
			EAM.errorDialog(EAM.text("Could not migrate!"));
		}

		String migratedRawProjectAsString = RawProjectSaver.saveProject(rawProjectToMigrate);
		UnicodeWriter fileWriter = new UnicodeWriter(newProjectFile);
		fileWriter.write(migratedRawProjectAsString);
		fileWriter.close();
	}

	@Override
	protected GenericMiradiFileFilter createFileFilter()
	{
		return new Xmpz2FileFilter();
	}
}
