/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.MultiCalendar;

public class ProjectCalendar
{
	public ProjectCalendar(Project projectToUse) throws Exception
	{
		project = projectToUse;
		setProjectDateRanges();
	}
	
	public DateRange[] getQuarterlyDateDanges()
	{
		return dateRanges;
	}
	
	public String getDateRangeName(DateRange dateRange)
	{
		return dateRange.toString();
	}
	
	public Vector getYearlyDateRanges()
	{
		return yearlyDateRanges;
	}
	
	public DateRange combineAllDateRangesIntoOne() throws Exception
	{
		DateRange startDateRange = (DateRange)yearlyDateRanges.get(0);
		DateRange endDateRange = (DateRange)yearlyDateRanges.get(yearlyDateRanges.size() - 1);
		
		return DateRange.combine(startDateRange, endDateRange);
	}
	
	private void setProjectDateRanges() throws Exception
	{
		//TODO budget code -  move project start/end code to Project
		String startDate = project.getMetadata().getStartDate();
		MultiCalendar projectStartDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 1);
		if (startDate.length() > 0 )
			projectStartDate = MultiCalendar.createFromIsoDateString(startDate);
		
		int yearCount = getYearDifference(projectStartDate);
		
		int year = projectStartDate.getGregorianYear();
		yearlyDateRanges = new Vector();
		Vector vector = new Vector();
		for (int i = 0; i < yearCount; i++)
		{
			vector.addAll(getQuartersPlustYearlyRange(year));
			year++;
		}
		dateRanges = (DateRange[])vector.toArray(new DateRange[0]);
	}

	private int getYearDifference(MultiCalendar projectStartDate)
	{
		final int DEFAULT_YEAR_DIFF = 3;
		String endDate = project.getMetadata().getExpectedEndDate();
		
		if (endDate.length() <= 0)
			return DEFAULT_YEAR_DIFF;
		
		MultiCalendar projectEndDate = MultiCalendar.createFromIsoDateString(endDate);
		if (projectStartDate.after(projectEndDate))
			return DEFAULT_YEAR_DIFF;
		
		int yearDiff = DateRange.getYearsInBetween(projectStartDate, projectEndDate);
		if (yearDiff == 0)
			return DEFAULT_YEAR_DIFF;
		
		return yearDiff;
	}

	private Vector getQuartersPlustYearlyRange(int year) throws Exception
	{
		Vector ranges = new Vector();
		ranges.add(createQuarter(year, 1, 31));
		ranges.add(createQuarter(year, 4 , 30));
		ranges.add(createQuarter(year, 7 , 30));
		ranges.add(createQuarter(year, 10, 31));
		ranges.add(DateRange.combine((DateRange)ranges.get(0), (DateRange)ranges.get(3)));
		
		yearlyDateRanges.add(DateRange.combine((DateRange)ranges.get(0), (DateRange)ranges.get(3)));
		
		return ranges;
	}
	
	private DateRange createQuarter(int year, int startMonth, int endDay) throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(year, startMonth, 1);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(year, startMonth + 2, endDay);
		return new DateRange(start, end);
	}

	Project project;
	DateRange[] dateRanges;
	Vector yearlyDateRanges;
}
