/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.miradi.main.EAM;
import org.miradi.objects.Assignment;
import org.miradi.project.BudgetCalculator;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;

public class PlanningViewBudgetTotalsTableModel extends PlanningViewAbstractTotalsTableModel
{
	public PlanningViewBudgetTotalsTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		totalsCalculator = new BudgetCalculator(getProject());
		dateRange = getProject().getProjectCalendar().combineStartToEndProjectRange();
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
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
		double totalCost = totalsCalculator.getTotalCost(assignment, dateRange);
		return currencyFormatter.format(totalCost);
	}
	
	private DecimalFormat currencyFormatter;
	private DateRange dateRange;
	private BudgetCalculator totalsCalculator;
}
