/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class TargetViabilityTree extends TreeTableWithStateSaving 
{
	public TargetViabilityTree(Project projectToUse, GenericTreeTableModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
	}
}
