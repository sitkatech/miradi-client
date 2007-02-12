/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;

public class WorkPlanManagementPanel extends VerticalSplitPanel
{
	public WorkPlanManagementPanel(MainWindow mainWindow, WorkPlanPanel treeTablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(mainWindow);
		treeTableComponent = treeTablePanelToUse;
		propertiesPanel = propertiesPanelToUse;
    	
		treeTableComponent.setPropertiesPanel(propertiesPanel);
	
    	setComponentPreferredSize(treeTableComponent);
		setComponentPreferredSize(propertiesPanel);
		createVerticalSplitPane(treeTableComponent, propertiesPanel, getPanelDescription());
	}
	
	public void dispose()
	{
		super.dispose();
	}

	public EAMObject getObject()
	{
		return treeTableComponent.getSelectedObject().getObject();
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION + SPLITTER_TAG_NAME;
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
