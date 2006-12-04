/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.martus.swing.UiScrollPane;

public class WorkPlanManagementPanel extends ModelessDialogPanel
{
	public WorkPlanManagementPanel(WorkPlanPanel treeTablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		treeTableComponent = treeTablePanelToUse;
		add(treeTableComponent, BorderLayout.CENTER);
		
		propertiesPanel = propertiesPanelToUse;
        treeTableComponent.setPropertiesPanel(propertiesPanel);
		add(new UiScrollPane(propertiesPanel), BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		treeTableComponent.dispose();
		treeTableComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}

	public EAMObject getObject()
	{
		return treeTableComponent.getSelectedObject().getObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Work Panel Tab");
	}
	
	WorkPlanPanel treeTableComponent;
	ObjectDataInputPanel propertiesPanel;
}
