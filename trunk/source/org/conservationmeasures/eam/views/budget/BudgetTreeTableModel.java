/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.text.DecimalFormat;
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
		currencyFormatter = project.getCurrencyFormatter();
		
		combineAllDateRangesIntoOne();
		combineColumnNames();
	}

	private void combineAllDateRangesIntoOne() throws Exception
	{
		DateRange startDateRange = (DateRange)yearlyDateRanges.get(0);
		DateRange endDateRange = (DateRange)yearlyDateRanges.get(yearlyDateRanges.size() -1);
		combinedDataRange = DateRange.combine(startDateRange, endDateRange);
	}
	
	private void combineColumnNames()
	{
		columnNames = new Vector();
		columnNames.add(ITEMS_COLUMN_NAME);
		columnNames.addAll(yearlyDateRanges);
		columnNames.add(COST_COLUMN_NAME);
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
        
        if (total == 0)
        	return "";
        
		return  currencyFormatter.format(total);
	}

	private Object getCost(Object rawNode)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		double totalCost = totalCalculator.calculateTotalCost(node, combinedDataRange);
		
		if (totalCost == 0)
        	return "";
        
		return currencyFormatter.format(totalCost);
	}

	
	private boolean isItemsColumn(int col)
	{
		return col == ITEMS_COLUMN;
	}
	
	private boolean isCostColumn(int col)
	{
		return col == getColumnCount() - 1;
	}
	
	DecimalFormat currencyFormatter;
	
	private DateRange combinedDataRange;
	private Vector columnNames;
	private Vector yearlyDateRanges;
	private BudgetTotalsCalculator totalCalculator;
	private Project project;
	
	private static final String ITEMS_COLUMN_NAME = "Items";
	private static final String COST_COLUMN_NAME = "Total";
	
	private static final int ITEMS_COLUMN = 0;
}
