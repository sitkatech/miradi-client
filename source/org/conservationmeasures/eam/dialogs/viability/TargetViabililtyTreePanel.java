/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.diagram.FactorTreeTableNode;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class TargetViabililtyTreePanel extends TargetViabililtyTreeTablePanel
{
	public static TargetViabililtyTreePanel createTargetViabilityPanel(MainWindow mainWindowToUse, Project projectToUse, FactorId targetId) throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new TargetViabilityRoot(projectToUse, targetId));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}

	public static TargetViabililtyTreePanel createTargetViabilityPoolPanel(MainWindow mainWindowToUse, Project projectToUse) throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(projectToUse));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}

	public static TargetViabililtyTreePanel createTargetViabilityIndicatorPoolPanel(MainWindow mainWindowToUse, ORef factorRef, Project projectToUse) throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new FactorTreeTableNode(projectToUse, factorRef));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}
	
	private static TargetViabililtyTreePanel getTargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, GenericTreeTableModel model)
	{
		TargetViabilityTree tree = new TargetViabilityTree(projectToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, projectToUse, tree, model);
	}

	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, TargetViabilityTree treeToUse, GenericTreeTableModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
}