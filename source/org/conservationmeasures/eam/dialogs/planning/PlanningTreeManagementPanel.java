/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogs.ObjectListManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class PlanningTreeManagementPanel extends ObjectListManagementPanel
{
	public PlanningTreeManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, PlanningTreeTablePanel.createPlanningTreeTablePenel(mainWindowToUse), getPropertiesPanel(mainWindowToUse));
	}

	public static PlanningTreePropertiesPanel getPropertiesPanel(MainWindow mainWindowToUse)
	{
		return new PlanningTreePropertiesPanel(mainWindowToUse.getProject(), ORef.INVALID);
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	private static String PANEL_DESCRIPTION = EAM.text("Tab|Planning");
}
