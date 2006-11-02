/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class NewResourceManagementPanel extends ModelessDialogPanel
{
	public NewResourceManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(new BorderLayout());
		project = mainWindowToUse.getProject();
		ObjectPropertiesPanel resourcesPanel = new PossibleTeamMembersPanel(mainWindowToUse, this);
		add(resourcesPanel, BorderLayout.CENTER);
		editResourcePanel = new ProjectResourcePropertiesPanel(getProject(), BaseId.INVALID);
		add(editResourcePanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Project Team");
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		editResourcePanel.setObjectId(selectedId);
	}

	Project project;
	ProjectResourcePropertiesPanel editResourcePanel;
}