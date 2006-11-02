/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.workplan.ResourceManagementPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class PossibleTeamMembersPanel extends ObjectPropertiesPanel
{
	public PossibleTeamMembersPanel(MainWindow mainWindowToUse, ModelessDialogPanel owningPanel)
	{
		super(mainWindowToUse, null);
		owner = owningPanel;
		
		ResourceManagementPanel resourcePanel = new ResourceManagementPanel(getView(), this);
		
		ObjectsAction addMemberAction = getMainWindow().getActions().getObjectsAction(ActionTeamAddMember.class);
		UiButton[] extraButtons = {new ObjectsActionButton(addMemberAction, resourcePanel), };
		resourcePanel.addButtons(extraButtons);
		
		

		setLayout(new BorderLayout());
		add(new UiLabel(getOverviewText()), BorderLayout.BEFORE_FIRST_LINE);
		add(resourcePanel, BorderLayout.CENTER);
	}

	public String getPanelDescription()
	{
		return null;
	}
	
	public EAMObject getObject()
	{
		return null;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public UmbrellaView getView()
	{
		return getMainWindow().getCurrentView();
	}

	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		owner.objectWasSelected(selectedId);
	}

	String getOverviewText()
	{
		return "<html>" +
				"<p>" +
				"This table lists all the Resources that have been created within this project. " +
				"</p>" +
				"<p>" +
				"You can select existing resources and add them to the team, " +
				"or you can create new resources." +
				"</p>" +
				"</html";
	}

	MainWindow mainWindow;
	ModelessDialogPanel owner;
}
