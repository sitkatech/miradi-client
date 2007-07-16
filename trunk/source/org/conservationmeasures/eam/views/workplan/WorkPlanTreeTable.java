/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.BoldedStrategyIndicatorTableTree;

public class WorkPlanTreeTable extends BoldedStrategyIndicatorTableTree 
{
	public WorkPlanTreeTable(Project projectToUse, WorkPlanTreeTableModel monitoringModelToUse)
	{
		super(projectToUse, monitoringModelToUse);
		setModel(treeTableModelAdapter);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
	}
}
