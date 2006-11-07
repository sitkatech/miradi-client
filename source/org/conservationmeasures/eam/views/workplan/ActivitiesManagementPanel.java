/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.dialogs.ProjectActivitiesPropertiesPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class ActivitiesManagementPanel extends ModelessDialogPanel
{
	public ActivitiesManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		this(mainWindowToUse, new ObjectsAction[0], "");
	}
	
	public ActivitiesManagementPanel(MainWindow mainWindowToUse, ObjectsAction[] extraButtonActions, String overviewText) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();

		activitiesPanel = new ActivitiesPanel(mainWindowToUse.getCurrentView(), this);
		addExtraButtons(activitiesPanel, extraButtonActions);
		
		editActivitiesPanel = new ProjectActivitiesPropertiesPanel(project, BaseId.INVALID);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(activitiesPanel, BorderLayout.CENTER);
		add(editActivitiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void selectObject(EAMObject objectToSelect)
	{
		activitiesPanel.selectObject(objectToSelect);
		editActivitiesPanel.setFocusOnFirstField();
	}

	public void dispose()
	{
		activitiesPanel.dispose();
		activitiesPanel = null;
		editActivitiesPanel.dispose();
		editActivitiesPanel = null;
		super.dispose();
	}
	
	private void addExtraButtons(ActivitiesPanel resourcePanel, ObjectsAction[] extraButtonActions)
	{
		UiButton[] extraButtons = new ObjectsActionButton[extraButtonActions.length];
		for(int i = 0; i < extraButtons.length; ++i)
			extraButtons[i] = new ObjectsActionButton(extraButtonActions[i], resourcePanel);
		resourcePanel.addButtons(extraButtons); 
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		editActivitiesPanel.setObjectId(selectedId);
	}
	

	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return null;
	}
	
	private ProjectActivitiesPropertiesPanel editActivitiesPanel;
	private ActivitiesPanel activitiesPanel;
}
