/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.ActivitiesPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
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

		tablePanel = new ActivitiesTablePanel(mainWindowToUse.getCurrentView(), this);
		addExtraButtons(tablePanel, extraButtonActions);
		
		propertiesPanel = new ActivitiesPropertiesPanel(mainWindowToUse.getActions(), project, BaseId.INVALID, mainWindowToUse);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(tablePanel, BorderLayout.CENTER);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
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
	
	private void addExtraButtons(ActivitiesTablePanel resourcePanel, ObjectsAction[] extraButtonActions)
	{
		UiButton[] extraButtons = new ObjectsActionButton[extraButtonActions.length];
		for(int i = 0; i < extraButtons.length; ++i)
			extraButtons[i] = new ObjectsActionButton(extraButtonActions[i], resourcePanel);
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
	
	private ActivitiesPropertiesPanel propertiesPanel;
	private ActivitiesTablePanel tablePanel;
}
