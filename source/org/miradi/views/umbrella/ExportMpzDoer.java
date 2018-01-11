/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import java.io.File;

import org.miradi.main.EAM;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.VersionRange;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.MpzFileChooser;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;

public class ExportMpzDoer extends AbstractFileSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new MpzFileChooser(getMainWindow());
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		initializeSingleStepSaveProgressInterface(progressInterface);
		convertMpfToMpz(getProject(), destinationFile);
		progressInterface.incrementProgress();
		
		return true;
	}
	
	private void convertMpfToMpz(Project project, File destinationFile) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		String mpfSnapShot = ProjectSaver.createSnapShot(project);
		RawProject projectToMigrate = RawProjectLoader.loadProject(mpfSnapShot);
		MigrationResult migrationResult = migrationManager.migrate(projectToMigrate, new VersionRange(MigrationManager.OLDEST_VERSION_TO_HANDLE));
		if (migrationResult.didLoseData())
		{
			if (!userConfirmLossData(migrationResult.getUserFriendlyGroupedDataLossMessagesAsString()))
				return;
		}
		
		if (migrationResult.didFail())
		{
			EAM.errorDialog(EAM.text("Could not migrate!"));
			return;
		}
		
		final String projectFileNameToUse = project.getFilename();
		MpfToMpzConverter.convertWithoutMigrating(projectToMigrate, projectFileNameToUse, destinationFile);
	}

	public static boolean userConfirmLossData(String summaryOfMessages)
	{
		final String message = EAM.substituteSingleString(EAM.text("While migrating this project back to an older data format, some data would be lost. \n" +
							   "This would typically be because the project had data in fields that did not \n" +
							   "exist in earlier versions of Miradi. You can continue, and the original project \n" +
							   "will remain unchanged, but that data will not be included in the exported project.\n\n" +
							   "Summary of data loss:\n" + 
							   "%s \n" + 
							   "Continue with export?"), summaryOfMessages);
		
		final int userChoice = EAM.confirmDialogWithScrollPanel(EAM.text("Confirm"), message, new String[]{EAM.text("Export"), EAM.text("Cancel"), });
		if (userChoice == 0)
			return true;
		
		return false;
	}

	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export MPZ");
	}
	
	@Override
	protected boolean doesUserConfirm() throws Exception
	{
		String title = EAM.text("Title|MPZ Export Warning");
		String html = Translation.getHtmlContent("ExportMpzWarning.html");
		String export = EAM.text("Button|Export");
		String cancel = EAM.text("Button|Cancel");
		String[] buttonLabels = new String[] {export, cancel};
		boolean result = EAM.confirmDialog(title, new String[] {html}, buttonLabels);
		return result;
	}
}
