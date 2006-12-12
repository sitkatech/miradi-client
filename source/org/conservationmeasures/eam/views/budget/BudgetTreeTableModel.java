/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanRoot;

public class BudgetTreeTableModel extends TaskTreeTableModel
{
	public BudgetTreeTableModel(Project projectToUse)
	{
		super(new WorkPlanRoot(projectToUse));
		project = projectToUse;
		totalCalculator = new BudgetTotalsCalculator(project);
	}
	
	public int getColumnCount()
	{
		return COLUMN_TAGS.length;
	}

	public String getColumnName(int column)
	{
		return COLUMN_TAGS[column];
	}
	
	public Object getValueAt(Object rawNode, int column)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		double totalCost = totalCalculator.calculateTotalCost(node);
		return Double.toString(totalCost);
	}
	
	private BudgetTotalsCalculator totalCalculator;
	private Project project;
	private static final String COLUMN_TAGS[] = {"Items", "Cost", };
}
