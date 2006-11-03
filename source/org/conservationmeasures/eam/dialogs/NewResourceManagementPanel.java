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
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.workplan.ResourceListPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class NewResourceManagementPanel extends ModelessDialogPanel
{
	public NewResourceManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		this(mainWindowToUse, new ObjectsAction[0], "");
	}
	
	public NewResourceManagementPanel(MainWindow mainWindowToUse, ObjectsAction[] extraButtonActions, String overviewText) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();

		ResourceListPanel resourcePanel = new ResourceListPanel(mainWindowToUse.getCurrentView(), this);
		addExtraButtons(resourcePanel, extraButtonActions);
		
		editResourcePanel = new ProjectResourcePropertiesPanel(project, BaseId.INVALID);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(resourcePanel, BorderLayout.CENTER);
		add(editResourcePanel, BorderLayout.AFTER_LAST_LINE);
	}

	private void addExtraButtons(ResourceListPanel resourcePanel, ObjectsAction[] extraButtonActions)
	{
		UiButton[] extraButtons = new ObjectsActionButton[extraButtonActions.length];
		for(int i = 0; i < extraButtons.length; ++i)
			extraButtons[i] = new ObjectsActionButton(extraButtonActions[i], resourcePanel);
		resourcePanel.addButtons(extraButtons); 
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Project Team");
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		editResourcePanel.setObjectId(selectedId);
	}

	ProjectResourcePropertiesPanel editResourcePanel;
}