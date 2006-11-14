/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.tree.DefaultTreeCellRenderer;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class StrategicPlanTreeTable extends TreeTableWithStateSaving
{
	public StrategicPlanTreeTable(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		setModel(treeTableModelAdapter);
		DefaultTreeCellRenderer renderer = new Renderer();
		tree.setCellRenderer(renderer);
	}
}
