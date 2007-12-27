/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.BudgetCalculator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanningViewBudgetCalculator
{
	public PlanningViewBudgetCalculator(Project projectToUse) throws Exception
	{
		totalCalculator = new BudgetCalculator(projectToUse);
	
		currencyFormatter = projectToUse.getCurrencyFormatter();
		combinedDataRange = projectToUse.getProjectCalendar().combineStartToEndProjectRange();
	}
	
	public String getBudgetTotalsAsFormatedString(ORef ref) throws Exception
	{
		double total = totalCalculator.calculateBudgetCost(ref, combinedDataRange);
		return format(total);
	}

	public double getBudgetTotals(ORef ref) throws Exception
	{
		return totalCalculator.calculateBudgetCost(ref, combinedDataRange);
	}
	
	private String format(double total)
	{
		if (total == 0)
			return "";
			
		return currencyFormatter.format(total);
	}
		
	private DecimalFormat currencyFormatter;
	private DateRange combinedDataRange;
	private BudgetCalculator totalCalculator;
}
