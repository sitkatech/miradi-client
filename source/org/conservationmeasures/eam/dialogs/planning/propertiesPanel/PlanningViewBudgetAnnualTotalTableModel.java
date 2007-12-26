/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.BudgetCalculator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewBudgetAnnualTotalTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewBudgetAnnualTotalTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);

		totalCalculator = new BudgetCalculator(project);
		yearlyDateRanges = getProjectCalendar().getYearlyDateRanges();
		currencyFormatter = project.getCurrencyFormatter();
	
		combineAllDateRangesIntoOne();
	}

	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return project.getProjectCalendar();
	}

	private void combineAllDateRangesIntoOne() throws Exception
	{
		DateRange startDateRange = (DateRange)yearlyDateRanges.get(0);
		DateRange endDateRange = (DateRange)yearlyDateRanges.get(yearlyDateRanges.size() - 1);
		combinedDataRange = DateRange.combine(startDateRange, endDateRange);
	}
	
	public String getColumnName(int column)
	{
		if(isCostColumn(column))
			return COST_COLUMN_NAME;
		
		try
		{
			return getProjectCalendar().getDateRangeName((DateRange)yearlyDateRanges.get(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}
		
	public int getColumnCount()
	{
		return yearlyDateRanges.size() + 1;
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
			TreeTableNode node = (TreeTableNode)rawNode;
			if (node.getObject() == null)
				return "";
			
			if (isCostColumn(column))
				return getCost(node);
		
			return getYearlyTotal(node, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("[ERROR]");
		}
	}
	
	private Object getYearlyTotal(TreeTableNode node, int column) throws Exception
	{	
		if (isBudgetOverrideMode(node))
			return "";
		
		DateRange dateRange = (DateRange)yearlyDateRanges.get(column);	
		double yearlyTotal = totalCalculator.calculateTotalCost(node.getObject(), dateRange, getCostAllocationProportion(node));        
        if (yearlyTotal == 0)
        	return "";
        
		return  currencyFormatter.format(yearlyTotal);
	}

	private boolean isBudgetOverrideMode(TreeTableNode node)
	{
		if (node.getType() != Task.getObjectType())
			return false;

		Task task = (Task) node.getObject();
		return task.isBudgetOverrideMode();
	}

	private double getCostAllocationProportion(TreeTableNode node)
	{
		if (node.getType() != Task.getObjectType())
			return 1.0;
		
		return ((PlanningTreeTaskNode) node).getCostAllocationProportion();
	}

	private Object getCost(TreeTableNode node) throws Exception
	{
		double totalCost = totalCalculator.calculateTotalCost(node.getObject(), combinedDataRange, getCostAllocationProportion(node));		
		if (totalCost == 0)
        	return "";
        
		return currencyFormatter.format(totalCost);
	}
	
	private boolean isCostColumn(int column)
	{
		return column == getColumnCount() - 1;
	}
	
	private DecimalFormat currencyFormatter;
	
	private DateRange combinedDataRange;
	private Vector yearlyDateRanges;
	private BudgetCalculator totalCalculator;
	
	public static final String COST_COLUMN_NAME = EAM.text("Budget Total ($)");
}
