/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectInfo;
import org.conservationmeasures.eam.questions.FontFamiliyQuestion;
import org.conservationmeasures.eam.questions.FontSizeQuestion;

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
			getProject().executeInsideListener(changeToDefaultMode);

			mainWindow.getCurrentView().refresh();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	MainWindow mainWindow;
}
