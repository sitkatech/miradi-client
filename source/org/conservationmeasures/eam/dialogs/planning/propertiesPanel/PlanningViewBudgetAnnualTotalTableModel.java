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
		combinedDataRange = getProjectCalendar().combineAllDateRangesIntoOne();
		currencyFormatter = project.getCurrencyFormatter();
	}

	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return project.getProjectCalendar();
	}

	public String getColumnName(int column)
	{
		if(isGrandTotalColumn(column))
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
			
			if (isGrandTotalColumn(column))
				return getGrandTotalCost(node);
		
			return getYearlyTotalCost(node, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("[ERROR]");
		}
	}
	
	private Object getYearlyTotalCost(TreeTableNode node, int column) throws Exception
	{	
		return getBudgetCost(node, (DateRange)yearlyDateRanges.get(column));
	}

	private Object getGrandTotalCost(TreeTableNode node) throws Exception
	{
		return getBudgetCost(node, combinedDataRange);		
	}
	
	private Object getBudgetCost(TreeTableNode node, DateRange dateRange) throws Exception
	{
		double yearlyTotal = totalCalculator.calculateBudgetCost(node.getObject(), dateRange, getCostAllocationProportion(node));        
        if (yearlyTotal == 0)
        	return "";
        
		return  currencyFormatter.format(yearlyTotal);
	}

	private double getCostAllocationProportion(TreeTableNode node)
	{
		if (node.getType() != Task.getObjectType())
			return 1.0;
		
		return ((PlanningTreeTaskNode) node).getCostAllocationProportion();
	}

	private boolean isGrandTotalColumn(int column)
	{
		return column == getColumnCount() - 1;
	}
	
	private DecimalFormat currencyFormatter;
	
	private DateRange combinedDataRange;
	private Vector yearlyDateRanges;
	private BudgetCalculator totalCalculator;
	
	public static final String COST_COLUMN_NAME = EAM.text("Budget Total ($)");
}
