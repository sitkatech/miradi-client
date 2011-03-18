/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.wizard.noproject.projectlist;

import java.io.File;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.ProjectMpzWriter;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.MpzFileChooser;
import org.miradi.views.umbrella.ExportMpzFileDoer;

class ProjectListExportAction extends ProjectListAction
{
	public ProjectListExportAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, getButtonLabel());
	}

	@Override
	protected void doWork() throws Exception
	{
		ProjectListExportAction.perform(EAM.getMainWindow(), getSelectedFile());
	}
	
	@Override
	protected String getErrorMessage()
	{
		return EAM.text("Error exporting project: ");
	}
	
	private static void perform(MainWindow mainWindow, File directoryToZip) throws CommandFailedException
	{
		EAMFileSaveChooser eamFileChooser = new MpzFileChooser(mainWindow);
		File chosen = eamFileChooser.displayChooser();
		if (chosen == null)
			return;
		
		if (ExportMpzFileDoer.isChosenFileInsideProjectHomeDir(chosen))
		{
			EAM.errorDialog(EAM.text("The MPZ file cannot be saved to a folder within the project being exported"));
			
			return;
		}
		try 
		{
			ProjectMpzWriter.createProjectZipFile(directoryToZip, chosen);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw ExportMpzFileDoer.createPossibleWriteProtectedException(e);
		}
	}

	private static String getButtonLabel()
	{
		return EAM.text("Export...");
	}
}