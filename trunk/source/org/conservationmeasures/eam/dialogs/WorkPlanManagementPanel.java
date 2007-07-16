/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;

public class WorkPlanManagementPanel extends VerticalSplitPanel
{
	public WorkPlanManagementPanel(MainWindow mainWindow, WorkPlanPanel treeTablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(mainWindow);
		treeTableComponent = treeTablePanelToUse;
		propertiesPanel = propertiesPanelToUse;
    	
		treeTableComponent.setPropertiesPanel(propertiesPanel);
	
		//TODO do we need this setters
    	setComponentPreferredSize(treeTableComponent);
		setComponentPreferredSize(propertiesPanel);
		createVerticalSplitPane(treeTableComponent, propertiesPanel, getSplitterDescription());
	}
	
	public void dispose()
	{
		super.dispose();
	}

	public BaseObject getObject()
	{
		return treeTableComponent.getSelectedTreeNode().getObject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public String getSplitterDescription()
	{
		return getPanelDescription() + SPLITTER_TAG_NAME;
	}
	
	public WorkPlanPanel getWorkPlanPanel()
	{
		return treeTableComponent;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Work Plan");
	private static final String SPLITTER_TAG_NAME = " Splitter";
	WorkPlanPanel treeTableComponent;
	ObjectDataInputPanel propertiesPanel;
}
