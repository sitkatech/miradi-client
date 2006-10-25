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
		mainWindow = mainWindowToUse;

		add(new UiLabel(EAM.text("Label|Filename:")));
		add(new UiLabel(getProject().getFilename()));
		
		add(new UiLabel(EAM.text("Label|Project Name:")));
		projectName = new UiTextField(50);
		projectName.setText(getProject().getMetadata().getProjectName());
		projectName.addFocusListener(new FocusHandler(ProjectMetadata.TAG_PROJECT_NAME, projectName));
		add(projectName);
		
		add(new UiLabel(EAM.text("Label|Project Scope:")));
		projectScope = new UiTextField(50);
		projectScope.setText(getProject().getMetadata().getProjectScope());
		projectScope.addFocusListener(new FocusHandler(ProjectMetadata.TAG_PROJECT_SCOPE, projectScope));
		add(projectScope);
		
		add(new UiLabel(EAM.text("Label|Project Vision:")));
		projectVision = new UiTextField(50);
		projectVision.setText(getProject().getMetadata().getProjectVision());
		projectVision.addFocusListener(new FocusHandler(ProjectMetadata.TAG_PROJECT_VISION, projectVision));
		add(projectVision);
		
		add(new UiLabel(EAM.text("Label|Start Date:")));
		startDate = new UiTextField(10);
		startDate.setText(getProject().getMetadata().getStartDate());
		startDate.addFocusListener(new FocusHandler(ProjectMetadata.TAG_START_DATE, startDate));
		add(startDate);

		add(new UiLabel(EAM.text("Label|Data Effective Date:")));
		effectiveDate = new UiTextField(10);
		effectiveDate.setText(getProject().getMetadata().getEffectiveDate());
		effectiveDate.addFocusListener(new FocusHandler(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, effectiveDate));;
		add(effectiveDate);

		add(new UiLabel(EAM.text("Label|Size in Hectares:")));
		sizeInHectares = new UiTextField(10);
		sizeInHectares.setText(getProject().getMetadata().getSizeInHectares());
		sizeInHectares.addFocusListener(new FocusHandler(ProjectMetadata.TAG_SIZE_IN_HECTARES, sizeInHectares));
		add(sizeInHectares);
		
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


	UiTextField projectName;
	UiTextField projectScope;
	UiTextField projectVision;
	UiTextField startDate;
	UiTextField effectiveDate;
	UiTextField sizeInHectares;
	TeamEditorComponent teamEditorComponent;
}
