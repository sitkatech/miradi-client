/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.ActivityPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class ActivityPoolManagementPanel extends ModelessDialogPanel
{
	public ActivityPoolManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		this(mainWindowToUse, new ObjectsAction[0], "");
	}
	
	public ActivityPoolManagementPanel(MainWindow mainWindowToUse, ObjectsAction[] extraButtonActions, String overviewText) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();

		tablePanel = new ActivityPoolTablePanel(mainWindowToUse.getCurrentView(), this);
		addExtraButtons(tablePanel, extraButtonActions);
		
		propertiesPanel = new ActivityPropertiesPanel(mainWindowToUse.getActions(), project, BaseId.INVALID, mainWindowToUse);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(tablePanel, BorderLayout.CENTER);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public EAMObject getSelectedObject()
	{
		if (tablePanel.getSelectedObjects().length > 0)
			return tablePanel.getSelectedObjects()[0];
		
		return null;
	}
	
	public void selectObject(EAMObject objectToSelect)
	{
		tablePanel.selectObject(objectToSelect);
		propertiesPanel.setFocusOnFirstField();
	}

	public void dispose()
	{
		tablePanel.dispose();
		tablePanel = null;
		propertiesPanel.dispose();
		propertiesPanel = null;
		super.dispose();
	}
	
	private void addExtraButtons(ActivityPoolTablePanel resourcePanel, ObjectsAction[] extraButtonActions)
	{
		UiButton[] extraButtons = new ObjectsActionButton[extraButtonActions.length];
		for(int i = 0; i < extraButtons.length; ++i)
			extraButtons[i] = createObjectsActionButton(extraButtonActions[i], resourcePanel);
		resourcePanel.addButtons(extraButtons); 
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		super.objectWasSelected(selectedId);
		propertiesPanel.setObjectId(selectedId);
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return null;
	}
	
	private ActivityPropertiesPanel propertiesPanel;
	private ActivityPoolTablePanel tablePanel;
}
