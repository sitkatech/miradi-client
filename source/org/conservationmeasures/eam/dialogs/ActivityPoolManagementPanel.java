/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
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

		tablePanel = new ActivityPoolTablePanel(project);
		
		propertiesPanel = new ActivityPropertiesPanel(mainWindowToUse.getActions(), project, BaseId.INVALID, mainWindowToUse);
		tablePanel.setPropertiesPanel(propertiesPanel);

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
		add(tablePanel, BorderLayout.CENTER);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		tablePanel.dispose();
		tablePanel = null;
		propertiesPanel.dispose();
		propertiesPanel = null;
		super.dispose();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Activities");
	}
	
	public Icon getIcon()
	{
		return new ActivityIcon();
	}
	
	public EAMObject getObject()
	{
		return tablePanel.getSelectedObject();
	}

	private ActivityPropertiesPanel propertiesPanel;
	private ActivityPoolTablePanel tablePanel;
}
