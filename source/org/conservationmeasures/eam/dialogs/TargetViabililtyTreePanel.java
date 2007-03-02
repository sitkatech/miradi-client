/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;

public class TargetViabililtyTreePanel extends TaskTreeTablePanel
{
	public static TargetViabililtyTreePanel createWorkPlanPanel(MainWindow mainWindowToUse, Project projectToUse)
	{
		TargetViabilityTreeTableModel model = new TargetViabilityTreeTableModel(projectToUse);
		TargetViabilityTreeTable tree = new TargetViabilityTreeTable(projectToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, projectToUse, tree, model);
	}
	
	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, TargetViabilityTreeTable treeToUse, TargetViabilityTreeTableModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
	
}
