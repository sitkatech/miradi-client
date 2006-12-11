/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.utils.DateRange;

public class BudgetTotalColumnCalculator
{
	public BudgetTotalColumnCalculator()
	{
	}
	
	public String getTotalUnitsAsString(Assignment assignment, DateRange dateRange) throws Exception
	{
		double totalUnits = 0.0;
		DateRangeEffortList effortList = getDateRangeEffortList(assignment);
		totalUnits = effortList.getTotalUnitQuantity(dateRange);
		return Double.toString(totalUnits);
	}
	
	private DateRangeEffortList getDateRangeEffortList(Assignment assignment) throws Exception
	{
		String effortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
		return new DateRangeEffortList(effortListAsString);
	}
}
