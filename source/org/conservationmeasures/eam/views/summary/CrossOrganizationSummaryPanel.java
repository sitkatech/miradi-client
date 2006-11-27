/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class CrossOrganizationSummaryPanel extends ObjectDataInputPanel
{
	public CrossOrganizationSummaryPanel(MainWindow mainWindowToUse, ProjectMetadata metadata)
	{
		super(mainWindowToUse.getProject(), metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.PSEUDO_TAG_PROJECT_FILENAME));
		addField(createStringField(metadata.TAG_PROJECT_NAME));
		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_VISION));
		addField(createDateField(metadata.TAG_START_DATE));
		addField(createDateField(metadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createNumericField(metadata.TAG_SIZE_IN_HECTARES));
		
		addLabel(EAM.text("Label|Team Members:"));
		teamEditorComponent = new TeamEditorComponent(getProject(), mainWindowToUse.getActions());
		add(teamEditorComponent);
		
		updateFieldsFromProject();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		updateTeamList(event);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		updateTeamList(event);
	}
	
	public void commandFailed(Command command, CommandFailedException exception)
	{
	}
	
	private void updateTeamList(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		
		teamEditorComponent.updateTableAfterCommand(cmd);
	}

	public String getPanelDescription()
	{
		return EAM.text("General");
	}

	TeamEditorComponent teamEditorComponent;
}
