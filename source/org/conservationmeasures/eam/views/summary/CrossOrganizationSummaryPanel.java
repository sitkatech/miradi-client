/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class CrossOrganizationSummaryPanel extends MetadataEditingPanel implements CommandExecutedListener
{
	public CrossOrganizationSummaryPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		add(new UiLabel(EAM.text("Label|Filename:")));
		UiTextField filename = new UiTextField(getProject().getFilename());
		filename.setEditable(false);
		add(filename);
		
		add(new UiLabel(EAM.text("Label|Project Name:")));
		projectName = createFieldComponent(ProjectMetadata.TAG_PROJECT_NAME, 50);
		addFieldComponent(projectName);
		
		add(new UiLabel(EAM.text("Label|Project Scope:")));
		projectScope = createFieldComponent(ProjectMetadata.TAG_PROJECT_SCOPE, 50);
		addFieldComponent(projectScope);
		
		add(new UiLabel(EAM.text("Label|Short Project Scope:")));
		shortProjectScope = createFieldComponent(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, 50);
		addFieldComponent(shortProjectScope);
		
		add(new UiLabel(EAM.text("Label|Project Vision:")));
		projectVision = createFieldComponent(ProjectMetadata.TAG_PROJECT_VISION, 50);
		addFieldComponent(projectVision);
		
		add(new UiLabel(EAM.text("Label|Short Project Vision:")));
		shortProjectVision = createFieldComponent(ProjectMetadata.TAG_SHORT_PROJECT_VISION, 50);
		addFieldComponent(shortProjectVision);
		
		add(new UiLabel(EAM.text("Label|Start Date:")));
		startDate = createFieldComponent(ProjectMetadata.TAG_START_DATE, 10);
		addFieldComponent(startDate);

		add(new UiLabel(EAM.text("Label|Data Effective Date:")));
		effectiveDate = createFieldComponent(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, 10);
		addFieldComponent(effectiveDate);

		add(new UiLabel(EAM.text("Label|Size in Hectares:")));
		sizeInHectares = createFieldComponent(ProjectMetadata.TAG_SIZE_IN_HECTARES, 10);
		addFieldComponent(sizeInHectares);
		
		add(new UiLabel(EAM.text("Label|Team Members:")));
		teamEditorComponent = new TeamEditorComponent(getProject(), mainWindow.getActions());
		add(teamEditorComponent);
		
		mainWindow.getProject().addCommandExecutedListener(this);
	}

	public void rebuild()
	{
		teamEditorComponent.rebuild();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		updateTeamList(event);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
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
	
	public String getPanelDescriptionText()
	{
		return EAM.text("General");
	}

	UiTextField projectName;
	UiTextField projectScope;
	UiTextField shortProjectScope;
	UiTextField projectVision;
	UiTextField shortProjectVision;
	UiTextField startDate;
	UiTextField effectiveDate;
	UiTextField sizeInHectares;
	TeamEditorComponent teamEditorComponent;
}
