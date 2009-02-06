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
package org.miradi.dialogs.diagram;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ProjectInfo;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.FontSizeQuestion;

public class DiagramProjectPreferencesPanel extends ObjectDataInputPanel
{
	public DiagramProjectPreferencesPanel(MainWindow mainWindowToUse, Project projectToUse, ProjectInfo projectInfo)
	{
		super(projectToUse, ProjectMetadata.getObjectType(), projectInfo.getMetadataId());
		mainWindow = mainWindowToUse;
		addField(createChoiceField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_DIAGRAM_FONT_SIZE, new FontSizeQuestion()));
		addField(createChoiceField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY, new FontFamiliyQuestion()));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return null;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		try
		{
			// NOTE: This is a hack. We only want to refresh the view IF
			// one of our fields was changed. For now we will roughly 
			// approximate that by saying if any project metadata field 
			// was changed, we will refresh the view
			if(!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
				return;
			CommandSetObjectData command = (CommandSetObjectData)event.getCommand();
			if(!command.getObjectORef().equals(getProject().getMetadata().getRef()))
				return;
			
			CommandSetObjectData changeToDefaultMode = new CommandSetObjectData(getProject().getCurrentViewData().getRef(), ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
			getProject().executeAsSideEffect(changeToDefaultMode);

			mainWindow.getCurrentView().refresh();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	MainWindow mainWindow;
}
