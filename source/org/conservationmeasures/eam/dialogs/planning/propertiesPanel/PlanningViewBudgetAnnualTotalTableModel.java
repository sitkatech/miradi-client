/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.dialogs.planning.PlanniningViewBudgetTotalsCalculator;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.ColumnTagProvider;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.views.TreeTableNode;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewBudgetAnnualTotalTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public PlanningViewBudgetAnnualTotalTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		project = projectToUse;
		adapter = adapterToUse;

		totalCalculator = new PlanniningViewBudgetTotalsCalculator(project);
		yearlyDateRanges = new ProjectCalendar(project).getYearlyDateRanges();
		currencyFormatter = project.getCurrencyFormatter();
	
		combineAllDateRangesIntoOne();
		combineColumnNames();
	}

	private void combineAllDateRangesIntoOne() throws Exception
	{
		DateRange startDateRange = (DateRange)yearlyDateRanges.get(0);
		DateRange endDateRange = (DateRange)yearlyDateRanges.get(yearlyDateRanges.size() - 1);
		combinedDataRange = DateRange.combine(startDateRange, endDateRange);
	}
	
	private void combineColumnNames()
	{
		columnNames = new Vector();
		columnNames.addAll(yearlyDateRanges);
		columnNames.add(COST_COLUMN_NAME);
	}

	public String getColumnName(int column)
	{
		return columnNames.get(column).toString();
	}
		
	public int getColumnCount()
	{
		return columnNames.size();
	}
	
	public int getRowCount()
	{
		return adapter.getRowCount();
	}

	public Object getValueAt(int row, int column)
	{
		Object rawTreeNode = getNodeForRow(row);
		return getValueAt(rawTreeNode, column);
	}

	public Object getValueAt(Object rawNode, int column)
	{
		try
		{
			if (isCostColumn(column))
				return getCost(rawNode);
		
			return getYearlyTotal(rawNode, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("[ERROR]");
		}
	}
	
	public Object getNodeForRow(int row)
	{
		return adapter.nodeForRow(row);
	}
		
	private Object getYearlyTotal(Object rawNode, int column) throws Exception
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		DateRange dateRange = (DateRange)columnNames.get(column);
        double yearlyTotal = totalCalculator.calculateTotalCost(node, dateRange);
        
        if (yearlyTotal == 0)
        	return "";
        
		return  currencyFormatter.format(yearlyTotal);
	}

	private Object getCost(Object rawNode) throws Exception
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		double totalCost = totalCalculator.calculateTotalCost(node, combinedDataRange);
		
		if (totalCost == 0)
        	return "";
        
		return currencyFormatter.format(totalCost);
	}
	
	private boolean isCostColumn(int column)
	{
		return column == getColumnCount() - 1;
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	private TreeTableModelAdapter adapter;
	DecimalFormat currencyFormatter;
	
	private DateRange combinedDataRange;
	private Vector columnNames;
	private Vector yearlyDateRanges;
	private PlanniningViewBudgetTotalsCalculator totalCalculator;
	private Project project;
	
	public static final String COST_COLUMN_NAME = EAM.text("Budget Total ($)");
}
