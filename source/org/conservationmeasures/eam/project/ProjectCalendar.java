/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
	
	private void setProjectDateRanges() throws Exception
	{
		String startDate = project.getMetadata().getStartDate();
		MultiCalendar projectStartDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 1);
		if (startDate.length() > 0 )
			projectStartDate = MultiCalendar.createFromIsoDateString(startDate);
		
		int yearCount = 3;
		String endDate = project.getMetadata().getExpectedEndDate();
		if (endDate.length() > 0)
			yearCount = DateRange.getYearsInBetween(projectStartDate, MultiCalendar.createFromIsoDateString(endDate));
		
		int year = projectStartDate.getGregorianYear();
		Vector vector = new Vector();
		for (int i = 0; i < yearCount; i++)
		{
			vector.addAll(getQuartersPlustYearlyRange(year));
			year++;
		}
		dateRanges = (DateRange[])vector.toArray(new DateRange[0]);
	}

	private Vector getQuartersPlustYearlyRange(int year) throws Exception
	{
		Vector ranges = new Vector();
		ranges.add(createQuarter(year, 1, 31));
		ranges.add(createQuarter(year, 4 , 30));
		ranges.add(createQuarter(year, 7 , 30));
		ranges.add(createQuarter(year, 10, 31));
		ranges.add(DateRange.combine((DateRange)ranges.get(0), (DateRange)ranges.get(3)));
		
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
}
