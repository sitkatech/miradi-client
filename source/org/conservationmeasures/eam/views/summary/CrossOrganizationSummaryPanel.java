/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextArea;

public class CrossOrganizationSummaryPanel extends ObjectDataInputPanel
{
	public CrossOrganizationSummaryPanel(MainWindow mainWindowToUse, ProjectMetadata metadata)
	{
		super(mainWindowToUse.getProject(), metadata.getType(), metadata.getId());

		add(new UiLabel(EAM.text("Label|Filename")));
		UiTextArea filename = new UiTextArea(1, 50);
		filename.setText(getProject().getFilename());
		filename.setEditable(false);
		filename.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		filename.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		add(filename);
		
		addField(createStringField(metadata.TAG_PROJECT_NAME));
		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_VISION));
		addField(createDateField(metadata.TAG_START_DATE));
		addField(createDateField(metadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createNumericField(metadata.TAG_SIZE_IN_HECTARES));
		
		add(new UiLabel(EAM.text("Label|Team Members:")));
		teamEditorComponent = new TeamEditorComponent(getProject(), mainWindowToUse.getActions());
		add(teamEditorComponent);
		
		updateFieldsFromProject();
	}

	public void rebuild()
	{
		teamEditorComponent.rebuild();
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
		if(cmd.getObjectType() != ObjectType.PROJECT_METADATA)
			return;
		
		rebuild();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("General");
	}

	TeamEditorComponent teamEditorComponent;
}
