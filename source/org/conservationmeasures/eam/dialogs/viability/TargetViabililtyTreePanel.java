/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;

public class TargetViabililtyTreePanel extends TargetViabililtyTreeTablePanel
{
	public static TargetViabililtyTreePanel createTargetViabilityPanel(MainWindow mainWindowToUse, Project projectToUse, FactorId targetId)
	{
		TargetViabilityTreeModel model = new TargetViabilityTreeModel(projectToUse, targetId);
		TargetViabilityTree tree = new TargetViabilityTree(projectToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, projectToUse, tree, model);
	}
	
	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, TargetViabilityTree treeToUse, TargetViabilityTreeModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
	
}