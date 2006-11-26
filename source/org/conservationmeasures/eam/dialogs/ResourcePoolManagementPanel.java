/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class ResourcePoolManagementPanel extends ModelessDialogPanel
{
	public ResourcePoolManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		this(mainWindowToUse, new ObjectsAction[0], "");
	}
	
	// TODO: The extraButtonActions parameter is deprecated, and should be removed 
	// as soon as all callers are adding the buttons themselves.
	public ResourcePoolManagementPanel(MainWindow mainWindowToUse, ObjectsAction[] extraButtonActions, String overviewText) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();

		resourceListPanel = new ResourcePoolTablePanel(project, mainWindowToUse.getActions());
		
		editResourcePanel = new ProjectResourcePropertiesPanel(project, BaseId.INVALID);
		resourceListPanel.setPropertiesPanel(editResourcePanel);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(resourceListPanel, BorderLayout.CENTER);
		add(editResourcePanel, BorderLayout.AFTER_LAST_LINE);
	}

	public void dispose()
	{
		resourceListPanel.dispose();
		resourceListPanel = null;
		
		editResourcePanel.dispose();
		editResourcePanel = null;
		super.dispose();
	}
	
	public void addTablePanelButton(ObjectsAction action)
	{
		resourceListPanel.addButton(action);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Project Resource");
	}
	
	public EAMObject getObject()
	{
		return resourceListPanel.getSelectedObject();
	}

	ResourcePoolTablePanel resourceListPanel;
	ProjectResourcePropertiesPanel editResourcePanel;
}