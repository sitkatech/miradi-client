/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanRoot;

public class BudgetTreeTableModel extends TaskTreeTableModel
{
	public BudgetTreeTableModel(Project projectToUse) throws Exception
	{
		super(new WorkPlanRoot(projectToUse));
		project = projectToUse;
		totalCalculator = new BudgetTotalsCalculator(project);
		yearlyDateRanges = new ProjectCalendar(project).getYearlyDateRanges();
		combineColumnNames();
	}
	
	private void combineColumnNames()
	{
		columnNames = new Vector();
		columnNames.addAll(Arrays.asList(COLUMN_TAGS));
		columnNames.addAll(yearlyDateRanges);
	}

	
	public int getColumnCount()
	{
		return columnNames.size();
	}

	public String getColumnName(int column)
	{
		return columnNames.get(column).toString();
	}
	
	public Object getValueAt(Object rawNode, int col)
	{
		if (isCostColumn(col))
			return getCost(rawNode);
		if (isItemsColumn(col))
			return "";
		
		return getYearlyTotal(rawNode, col);
	}

	private Object getYearlyTotal(Object rawNode, int col)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		DateRange dateRange = (DateRange)columnNames.get(col);
        double total = totalCalculator.calculateTotalCost(node, dateRange); 
		return new Double(total);
	}

	private Object getCost(Object rawNode)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		double totalCost = totalCalculator.calculateTotalCost(node);
		return Double.toString(totalCost);
	}

	
	private boolean isItemsColumn(int col)
	{
		return col == ITEMS_COLUMN;
	}
	
	private boolean isCostColumn(int col)
	{
		return col == COST_COLUMN;
	}
	
	private Vector columnNames;
	private Vector yearlyDateRanges;
	private BudgetTotalsCalculator totalCalculator;
	private Project project;
	private static final String COLUMN_TAGS[] = {"Items", "Cost", };
	
	private static final int ITEMS_COLUMN = 0;
	private static final int COST_COLUMN = 1;
	
}
