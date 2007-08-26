/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class PlanningTreeTable extends TreeTableWithStateSaving
{
	public PlanningTreeTable(Project projectToUse, PlanningTreeModel planningTreeModelToUse)
	{
		super(projectToUse, planningTreeModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
