/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		dateRange = getProject().getProjectCalendar().combineStartToEndProjectRange();
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
		double totalCost = totalsCalculator.getTotalCost(assignment, dateRange);
		return currencyFormatter.format(totalCost);
	}
	
	private DecimalFormat currencyFormatter;
	private DateRange dateRange;
	private BudgetCalculator totalsCalculator;
}
