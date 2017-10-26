/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.umbrella.doers;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.VersionRange;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.migrations.forward.MigrationTo39;
import org.miradi.project.ProjectSaver;
import org.miradi.project.RawProjectSaver;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MpfFileChooser;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.umbrella.AbstractFileSaverDoer;
import org.miradi.views.umbrella.ExportMpzDoer;

import java.io.File;

public class ExportMpf44VersionDoer extends AbstractFileSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new MpfFileChooser(getMainWindow());
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		initializeSingleStepSaveProgressInterface(progressInterface);
		String mpfSnapShot = ProjectSaver.createSnapShot(getProject());
		MigrationManager migrationManager = new MigrationManager();
		RawProject rawProjectToMigrate = RawProjectLoader.loadProject(mpfSnapShot);
		MigrationResult migrationResult = migrationManager.migrate(rawProjectToMigrate, new VersionRange(MigrationTo39.VERSION_TO));
		if (migrationResult.cannotMigrate())
		{
			final String message = EAM.substituteSingleString(EAM.text("Unable to complete this migration.\n\n" +
					"Issues encountered:\n" +
					"%s"), migrationResult.getUserFriendlyGroupedCannotMigrateMessagesAsString());

			EAM.errorDialog(message);
			return false;
		}
		if (migrationResult.didLoseData())
		{
			if (!ExportMpzDoer.userConfirmLossData(migrationResult.getUserFriendlyGroupedDataLossMessagesAsString()))
				return false;
		}
		
		if (migrationResult.didFail())
		{
			EAM.errorDialog(EAM.text("Could not migrate!"));
			return false;
		}
		
		String migratedRawProjectAsString = RawProjectSaver.saveProject(rawProjectToMigrate);
		UnicodeWriter fileWriter = new UnicodeWriter(destinationFile);
		fileWriter.write(migratedRawProjectAsString);
		fileWriter.close();
		progressInterface.incrementProgress();
		
		return true;
	}

	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export 4.4 Miradi");
	}
}
