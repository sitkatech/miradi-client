/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.views.budget.BudgetTotalsCalculator;

public class PlanningViewBudgetTotalsTableModel extends PlanningViewAbstractTotalsTableModel
{
	public PlanningViewBudgetTotalsTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		totalsCalculator = new BudgetTotalsCalculator(getProject());
		dateRanges = new ProjectCalendar(getProject()).getQuarterlyDateDanges();
		currencyFormatter = getProject().getCurrencyFormatter();
	}
	
	public String getColumnName(int column)
	{
		return "Total";
	}

	public Object getValueAt(int row, int column)
	{
		return getTotalCost(row);
	}
	
	private Object getTotalCost(int row)
	{
		Assignment assignment = getAssignment(row);
		return getTotalCost(assignment);
	}
	
	public String getTotalCost(Assignment assignment)
	{		
		try
		{
			DateRange combinedDateRange = getCombinedDateRange();
			double totalCost = totalsCalculator.getTotalCost(assignment, combinedDateRange);
			return currencyFormatter.format(totalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}
	
	public DateRange getCombinedDateRange() throws Exception
	{
		return DateRange.combine(dateRanges[0], dateRanges[dateRanges.length - 1]);
	}
	
	private DecimalFormat currencyFormatter;
	private DateRange[] dateRanges;
	private BudgetTotalsCalculator totalsCalculator;
}
