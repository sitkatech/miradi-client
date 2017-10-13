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

import java.io.File;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.ProjectSaver;
import org.miradi.views.MainWindowDoer;
import org.miradi.views.umbrella.CreateProjectDialog;
import org.miradi.wizard.noproject.projectlist.ProjectListTreeTable;

public class SaveProjectAsDoer extends MainWindowDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}

	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		if (!isUndoStackEmpty())
		{
			String[] buttons = new String[2];
			final int CANCEL_CHOICE = 0;
			buttons[CANCEL_CHOICE] = EAM.text("Button|Cancel");
			buttons[1] = EAM.text("Button|Continue");
			
			String body = EAM.text("WARNING: The changes caused by the most recent Undo commands have already been saved. \n" +
									"If you go ahead with Save As right now, both the existing project and the new project \n" +
									"will contain identical data, and those recent Undo commands will not be able to be reverted \n" +
									"using Redo. This is different behavior from most applications.\n\n" +

									"Continue with Save As?");
			
			if (EAM.confirmDialog(EAM.text("Confirmation"), body, buttons) == CANCEL_CHOICE)
				return;
		}
		
		final File projectFile = getProject().getProjectFile();
		CreateProjectDialog saveDialog = new CreateProjectDialog(getMainWindow(), EAM.text("Save As..."), projectFile.getParentFile(), projectFile.getName());
		if(!saveDialog.showSaveAsDialog())
			return;

		File chosenFile = saveDialog.getSelectedFile();
		try
		{
			saveAs(chosenFile);
			getMainWindow().closeProject();
			ProjectListTreeTable.doProjectOpen(getMainWindow(), chosenFile);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unexpected error during Save As: " + e);
		}
	}

	private boolean isUndoStackEmpty()
	{
		return getProject().getLastExecutedCommand() == null;
	}

	private void saveAs(File chosenFile) throws Exception
	{
		ProjectSaver.saveProject(getProject(), chosenFile);
	}
}
