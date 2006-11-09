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
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class ResourceListManagementPanel extends ModelessDialogPanel
{
	public ResourceListManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		this(mainWindowToUse, new ObjectsAction[0], "");
	}
	
	public ResourceListManagementPanel(MainWindow mainWindowToUse, ObjectsAction[] extraButtonActions, String overviewText) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();

		resourceListPanel = new ResourcePoolTablePanel(mainWindowToUse.getCurrentView(), this);
		addExtraButtons(resourceListPanel, extraButtonActions);
		
		editResourcePanel = new ProjectResourcePropertiesPanel(project, BaseId.INVALID);

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

	private void addExtraButtons(ResourcePoolTablePanel resourcePanel, ObjectsAction[] extraButtonActions)
	{
		UiButton[] extraButtons = new ObjectsActionButton[extraButtonActions.length];
		for(int i = 0; i < extraButtons.length; ++i)
			extraButtons[i] = new ObjectsActionButton(extraButtonActions[i], resourcePanel);
		resourcePanel.addButtons(extraButtons); 
	}
	
	public void selectObject(EAMObject objectToSelect)
	{
		resourceListPanel.selectObject(objectToSelect);
		editResourcePanel.setFocusOnFirstField();
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Project Resource");
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		editResourcePanel.setObjectId(selectedId);
	}

	ResourcePoolTablePanel resourceListPanel;
	ProjectResourcePropertiesPanel editResourcePanel;
}