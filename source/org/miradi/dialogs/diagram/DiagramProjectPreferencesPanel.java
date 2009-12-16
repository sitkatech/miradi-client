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

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
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
		super(mainWindowToUse.getProject(), ProjectMetadata.getObjectType(), projectInfo.getMetadataId());
		
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
			if(!event.isSetDataCommandWithThisType(ProjectMetadata.getObjectType()))
				return;
			
			CommandSetObjectData command = (CommandSetObjectData)event.getCommand();
			CommandSetObjectData changeToDefaultMode = new CommandSetObjectData(getProject().getCurrentViewData().getRef(), ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
			getProject().executeAsSideEffect(changeToDefaultMode);
			
			if (ourFieldChanged(command.getFieldTag()))
				mainWindow.getCurrentView().refresh();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private boolean ourFieldChanged(String tag)
	{
		Vector<ObjectDataInputField> fields = getFields();
		for(ObjectDataInputField field : fields)
		{
			if (tag.equals(field.getTag()))
				return true;
		}
		
		return false;
	}

	private MainWindow mainWindow;
}
