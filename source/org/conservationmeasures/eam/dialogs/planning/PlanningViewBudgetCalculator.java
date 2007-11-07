/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.text.DecimalFormat;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanningViewBudgetCalculator
{
	public PlanningViewBudgetCalculator(Project projectToUse) throws Exception
	{
		totalCalculator = new PlanniningViewBudgetTotalsCalculator(projectToUse);
		yearlyDateRanges = new ProjectCalendar(projectToUse).getYearlyDateRanges();
		currencyFormatter = projectToUse.getCurrencyFormatter();
		combineAllDateRangesIntoOne();
	}
	
	private void combineAllDateRangesIntoOne() throws Exception
	{
		DateRange startDateRange = (DateRange)yearlyDateRanges.get(0);
		DateRange endDateRange = (DateRange)yearlyDateRanges.get(yearlyDateRanges.size() - 1);
		combinedDataRange = DateRange.combine(startDateRange, endDateRange);
	}
	
	public String getBudgetTotals(ORef ref) throws Exception
	{
		double total = totalCalculator.calculateTotalCost(ref, combinedDataRange);
		return format(total);
	}

	public String getBudgetTotals(TreeTableNode node) throws Exception
	{
		double total = totalCalculator.calculateTotalCost(node, combinedDataRange);
		return format(total);
	}
	
	private String format(double total)
	{
		if (total == 0)
			return "";
			
		return currencyFormatter.format(total);
	}
		
	private DecimalFormat currencyFormatter;
	private DateRange combinedDataRange;
	private Vector yearlyDateRanges;
	private PlanniningViewBudgetTotalsCalculator totalCalculator;
}
