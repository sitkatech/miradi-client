/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.BudgetCalculator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanningViewBudgetTotalsTableModel extends PlanningViewAbstractTotalsTableModel
{
	public PlanningViewBudgetTotalsTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		totalsCalculator = new BudgetCalculator(getProject());
		dateRanges = getProject().getProjectCalendar().getQuarterlyDateDanges();
		currencyFormatter = getProject().getCurrencyFormatter();
	}
	
	public String getColumnName(int column)
	{
		return EAM.text("Total");
	}

	public Object getValueAt(int row, int column)
	{
		try
		{
			return getTotalCost(row);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}
	
	public String getTotalCost(int row) throws Exception
	{	
		Assignment assignment = getAssignment(row);	
		DateRange combinedDateRange = getCombinedDateRange();
		double totalCost = totalsCalculator.getTotalCost(assignment, combinedDateRange);
		return currencyFormatter.format(totalCost);
	}
	
	public DateRange getCombinedDateRange() throws Exception
	{
		return DateRange.combine(dateRanges[0], dateRanges[dateRanges.length - 1]);
	}
	
	private DecimalFormat currencyFormatter;
	private DateRange[] dateRanges;
	private BudgetCalculator totalsCalculator;
}
