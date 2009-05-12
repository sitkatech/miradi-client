/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.ProjectMetadata;
import org.miradi.utils.DateRange;

public class ProjectCalendar implements CommandExecutedListener
{
	public ProjectCalendar(Project projectToUse) throws Exception
	{
		project = projectToUse;
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	public void clearDateRanges()
	{
		dateRanges = null;
		yearlyDateRanges = null;
		editableDateRanges = null;
	}

	public DateRange[] getQuarterlyDateRanges() throws Exception
	{
		return getDateRanges();
	}
	
	public String getDateRangeName(DateRange dateRange)
	{
		return getFiscalYearQuarterName(dateRange, getProject().getMetadata().getFiscalYearFirstMonth());
	}
	
	public DateRange combineStartToEndProjectRange() throws Exception
	{
		DateRange startDateRange = dateRanges.get(0);
		DateRange endDateRange = dateRanges.get(dateRanges.size() - 1);
		
		return DateRange.combine(startDateRange, endDateRange);
	}
	
	public void rebuildProjectDateRanges() throws Exception
	{
		//TODO budget code -  move project start/end code to Project
		startDate = getPlanningStartDate();
		int firstCalendarYear = new MultiCalendar().getGregorianYear();
		firstCalendarMonth = getFiscalYearFirstMonth();

		if (startDate.length() > 0 )
		{
			MultiCalendar projectStartDate = MultiCalendar.createFromIsoDateString(startDate);
			firstCalendarYear = projectStartDate.getGregorianYear();
			int quarterStartMonth = projectStartDate.getGregorianMonth();
			quarterStartMonth -= (quarterStartMonth-1)%3;
			if(quarterStartMonth < firstCalendarMonth)
				--firstCalendarYear;	
		}
		
		MultiCalendar planningStartDate = MultiCalendar.createFromGregorianYearMonthDay(firstCalendarYear, firstCalendarMonth, 1);
		MultiCalendar planningEndDate = calculatePlanningEndDate(planningStartDate);
		
		yearlyDateRanges = new Vector();
		editableDateRanges = new Vector();
		dateRanges = new Vector();
		while(planningStartDate.before(planningEndDate))
		{
			createQuartersPlustYearlyRange(planningStartDate);
			planningStartDate = getOneYearLater(planningStartDate);
		}
	}

	private int getFiscalYearFirstMonth()
	{
		return project.getMetadata().getFiscalYearFirstMonth();
	}

	public String getPlanningStartDate()
	{
		MultiCalendar now = new MultiCalendar();
		MultiCalendar startOfCalendarYear = MultiCalendar.createFromGregorianYearMonthDay(now.getGregorianYear(), 1, 1);

		ProjectMetadata metadata = project.getMetadata();
		String candidatesBestFirst[] = new String[] {
			metadata.getWorkPlanStartDateAsString(),
			metadata.getStartDate(),
			startOfCalendarYear.toIsoDateString(),
		};
		
		return firstNonBlank(candidatesBestFirst);
	}

	public String getPlanningEndDate()
	{
		MultiCalendar now = new MultiCalendar();
		MultiCalendar endOfCalendarYear = MultiCalendar.createFromGregorianYearMonthDay(now.getGregorianYear(), 12, 31);

		ProjectMetadata metadata = project.getMetadata();
		String candidatesBestFirst[] = new String[] {
			metadata.getWorkPlanEndDate(),
			metadata.getExpectedEndDate(),
			endOfCalendarYear.toIsoDateString(),
		};
		
		return firstNonBlank(candidatesBestFirst);
	}

	private String firstNonBlank(String[] candidatesBestFirst)
	{
		for(String candidate : candidatesBestFirst)
		{
			if(candidate.length() != 0)
				return candidate;
		}
		
		throw new RuntimeException("All candidate strings were blank");
	}
	
	private MultiCalendar calculatePlanningEndDate(MultiCalendar planningStartDate)
	{
		final int DEFAULT_YEAR_DIFF = 3;
		MultiCalendar defaultEndDate = MultiCalendar.createFromGregorianYearMonthDay(
				planningStartDate.getGregorianYear() + DEFAULT_YEAR_DIFF, 
				planningStartDate.getGregorianMonth(), 
				planningStartDate.getGregorianDay());
		defaultEndDate.addDays(-1);
		
		endDate = getPlanningEndDate();
		if (endDate.length() <= 0)
			return defaultEndDate;
		
		MultiCalendar projectEndDate = MultiCalendar.createFromIsoDateString(endDate);
		projectEndDate.addDays(1);
		int endYear = projectEndDate.getGregorianYear();
		int endMonth = planningStartDate.getGregorianMonth();
		
		int userSpecifiedLastMonth = projectEndDate.getGregorianMonth();
		boolean extendsIntoFollowingYear = userSpecifiedLastMonth > endMonth;
		if(userSpecifiedLastMonth == endMonth && projectEndDate.getGregorianDay() > 1)
			extendsIntoFollowingYear = true;
		if(extendsIntoFollowingYear)
			++endYear;
		
		MultiCalendar planningEndDate = MultiCalendar.createFromGregorianYearMonthDay(endYear, endMonth, 1);
		
		if (!planningStartDate.before(planningEndDate))
			return defaultEndDate;
		
		planningEndDate.addDays(-1);
		return planningEndDate;
	}

	private void createQuartersPlustYearlyRange(MultiCalendar startingDate) throws Exception
	{
		Vector<DateRange> quarterlyRanges = new Vector();
		for(int quarter = 0; quarter < 4; ++quarter)
		{
			DateRange quarterRange = createQuarter(startingDate);
			quarterlyRanges.add(quarterRange);
			startingDate = nextQuarter(startingDate);
		}
		
		isBudgetTimePeriodQuarterly = isProjectBudgetTimePeriodQuarterly();
		if(isBudgetTimePeriodQuarterly)
		{
			dateRanges.addAll(quarterlyRanges);
			editableDateRanges.addAll(quarterlyRanges);
		}
		
		DateRange firstQuarter = quarterlyRanges.get(0);
		DateRange lastQuarter = quarterlyRanges.get(3);
		DateRange yearRange = DateRange.combine(firstQuarter, lastQuarter);	

		dateRanges.add(yearRange);
		yearlyDateRanges.add(yearRange);
		
		if(getProject().getMetadata().isBudgetTimePeriodYearly())
		{
			editableDateRanges.add(yearRange);
		}
	}

	private boolean isProjectBudgetTimePeriodQuarterly()
	{
		return getProject().getMetadata().isBudgetTimePeriodQuarterly();
	}

	public DateRange createYear(String isoYearStart) throws Exception
	{
		return createYear(MultiCalendar.createFromIsoDateString(isoYearStart));
	}

	public DateRange createYear(MultiCalendar yearStart) throws Exception
	{
		int endYear = yearStart.getGregorianYear() + 1;
		int endMonth = yearStart.getGregorianMonth();
		int endDay = yearStart.getGregorianDay();
		MultiCalendar yearEnd = MultiCalendar.createFromGregorianYearMonthDay(endYear, endMonth, endDay);
		yearEnd.addDays(-1);
		return new DateRange(yearStart, yearEnd);
	}

	public DateRange createQuarter(String isoQuarterStart) throws Exception
	{
		return createQuarter(MultiCalendar.createFromIsoDateString(isoQuarterStart));
	}
	
	public DateRange createQuarter(MultiCalendar quarterStart) throws Exception
	{
		MultiCalendar end = nextQuarter(quarterStart);
		end.addDays(-1);
		return new DateRange(quarterStart, end);
	}
	
	private MultiCalendar nextQuarter(MultiCalendar quarterStart)
	{
		int year = quarterStart.getGregorianYear();
		int month = quarterStart.getGregorianMonth();
		month += 3;
		if(month > 12)
		{
			month -= 12;
			++year;
		}
		return MultiCalendar.createFromGregorianYearMonthDay(year, month, 1);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(!event.isSetDataCommand())
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData) event.getCommand();
		if(cmd.getObjectType() != ProjectMetadata.getObjectType())
			return;
		
		try
		{
			if(cmd.getFieldTag().equals(ProjectMetadata.TAG_START_DATE) ||
					cmd.getFieldTag().equals(ProjectMetadata.TAG_EXPECTED_END_DATE) ||
					cmd.getFieldTag().equals(ProjectMetadata.TAG_FISCAL_YEAR_START) ||
					cmd.getFieldTag().equals(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT))
			{
				clearDateRanges();
			}
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private Project getProject()
	{
		return project;
	}

	private DateRange[] getDateRanges() throws Exception
	{
		if(isInvalidProjectCalendar())
			rebuildProjectDateRanges();
		return dateRanges.toArray(new DateRange[0]);
	}

	public Vector<DateRange> getYearlyDateRanges() throws Exception
	{
		if(isInvalidProjectCalendar())
			rebuildProjectDateRanges();
		return yearlyDateRanges;
	}
	
	private boolean isInvalidProjectCalendar()
	{
		if (dateRanges == null)
			return true;
		
		if (yearlyDateRanges == null)
			return true;
			
		if (!getPlanningStartDate().equals(startDate))
			return true;
		
		if (!getPlanningEndDate().equals(endDate))
			return true;
		
		if (firstCalendarMonth != getFiscalYearFirstMonth())
			return true;
		
		if (isBudgetTimePeriodQuarterly != isProjectBudgetTimePeriodQuarterly())
			return true;
		
		return false;
	}

	private static int getFiscalYearMonthSkew(int fiscalYearFirstMonth)
	{
		switch(fiscalYearFirstMonth)
		{
			case 1: return 0;
			case 4: return 3;
			case 7: return -6;
			case 10: return -3;
		}
		
		throw new RuntimeException("Unknown fiscal year month start: " + fiscalYearFirstMonth);
	}

	public static String getFiscalYearQuarterName(DateRange dateRange, int fiscalYearFirstMonth)
	{
		String fullRange = dateRange.toString();
		
		MultiCalendar startDate = dateRange.getStartDate();
		MultiCalendar afterEndDate = new MultiCalendar(dateRange.getEndDate());
		afterEndDate.addDays(1);
		
		if(startDate.getGregorianDay() != 1)
			return fullRange;
		if(afterEndDate.getGregorianDay() != 1)
			return fullRange;
		
		int skew = ProjectCalendar.getFiscalYearMonthSkew(fiscalYearFirstMonth);
		
		int startFiscalMonth = startDate.getGregorianMonth();
		if((startFiscalMonth % 3) != 1)
			return fullRange;
	
		int endFiscalMonth = afterEndDate.getGregorianMonth();
		if((endFiscalMonth % 3) != 1)
			return fullRange;
	
		int startFiscalYear = startDate.getGregorianYear();
		startFiscalMonth -= skew;
		while(startFiscalMonth < 1)
		{
			startFiscalMonth += 12;
			--startFiscalYear;
		}
		while(startFiscalMonth > 12)
		{
			startFiscalMonth -= 12;
			++startFiscalYear;
		}
		
		int endFiscalYear = afterEndDate.getGregorianYear();
		endFiscalMonth -= skew;
		while(endFiscalMonth < 1)
		{
			endFiscalMonth += 12;
			--endFiscalYear;
		}
		while(endFiscalMonth > 12)
		{
			endFiscalMonth -= 12;
			++endFiscalYear;
		}
		
		String startYearString = getFiscalYearString(startFiscalYear);
		
		if(startFiscalYear+1 == endFiscalYear && startFiscalMonth == endFiscalMonth && startFiscalMonth == 1)
			return startYearString;
		
		String endYearString = getFiscalYearString(endFiscalYear);
		
		int startFiscalQuarter = (startFiscalMonth-1) / 3 + 1;
		int endFiscalQuarter = (endFiscalMonth - 1) / 3;
		if (endFiscalQuarter == 0)
		{
			endFiscalQuarter = 4;
		}
		
		if(startFiscalQuarter == 4)
		{
			if(startFiscalYear+1 != endFiscalYear)
				return fullRange;
		}
		else if(startFiscalYear != endFiscalYear)
		{
			return fullRange;
		}
		
		String firstFiscalQuarter = "Q" + startFiscalQuarter + " " + startYearString;
		if (startFiscalQuarter == endFiscalQuarter)
			return firstFiscalQuarter;
		
		return firstFiscalQuarter + " - Q" + endFiscalQuarter + " " + endYearString;
	}

	private static String getFiscalYearString(int fiscalYear)
	{
		String yearString = Integer.toString(fiscalYear);
		return "FY" + yearString.substring(2);
	}
	
	public DateRange convertToDateRange(DateUnit dateUnit) throws Exception
	{
		if(dateUnit.isBlank())
			return getProjectStartEndDateRange();
		
		return dateUnit.asDateRange();
	}

	private DateRange getProjectStartEndDateRange() throws Exception
	{
		int thisStartYear = MultiCalendar.createFromIsoDateString(getPlanningStartDate()).getGregorianYear();
		int thisEndYear = MultiCalendar.createFromIsoDateString(getPlanningEndDate()).getGregorianYear();

		MultiCalendar thisStartDate = MultiCalendar.createFromGregorianYearMonthDay(thisStartYear, 1, 1);
		MultiCalendar thisEndDate = MultiCalendar.createFromGregorianYearMonthDay(thisEndYear, 12, 31);
		
		return new DateRange(thisStartDate, thisEndDate);
	}
	
	public boolean hasSubDateUnits(DateUnit dateUnit) throws Exception
	{
		return getSubDateUnits(dateUnit).size() > 0;
	}
	
	public Vector<DateUnit> getSubDateUnits(DateUnit dateUnit) throws Exception
	{
		DateRange dateRange = convertToDateRange(dateUnit);
		if (dateUnit.isBlank())
			return getProjectYearsDateUnits(dateRange);
		
		if (dateUnit.hasSubDateUnits())
			return dateUnit.getSubDateUnits();
		
		return new Vector<DateUnit>();
	}

	public Vector<DateUnit> getProjectYearsDateUnits(DateRange dateRange)
	{
		Vector<DateUnit> dateUnits = new Vector();
		MultiCalendar start = dateRange.getStartDate();
		MultiCalendar end = dateRange.getEndDate();

		MultiCalendar startOfFiscalYear = getStartOfFiscalYearContaining(start);
		while(startOfFiscalYear.before(end))
		{
			DateUnit year = DateUnit.createFiscalYear(startOfFiscalYear.getGregorianYear(), startOfFiscalYear.getGregorianMonth());
			dateUnits.add(year);
			startOfFiscalYear = getOneYearLater(startOfFiscalYear);
		}
		
		return dateUnits;
	}

	public MultiCalendar getStartOfFiscalYearContaining(MultiCalendar start)
	{
		int startYear = start.getGregorianYear();
		int startMonth = getFiscalYearFirstMonth();
		MultiCalendar startOfFiscalYear = MultiCalendar.createFromGregorianYearMonthDay(startYear, startMonth, 1);
		while(startOfFiscalYear.after(start))
			startOfFiscalYear = getOneYearEarlier(startOfFiscalYear);

		return startOfFiscalYear;
	}
	
	public DateRange getProjectPlanningDateRange() throws Exception
	{
		DateRange dateRange = null;
		Vector<DateUnit> dateUnits = getProjectYearsDateUnits(getProjectStartEndDateRange());
		for(DateUnit dateUnit : dateUnits)
		{
			DateRange thisDateRange = dateUnit.asDateRange();
			if(dateRange == null)
				dateRange = new DateRange(thisDateRange);
			else
				dateRange = DateRange.combine(dateRange, thisDateRange);
		}
		
		return dateRange;
	}
	
	public DateUnit getProjectPlanningDateUnit() throws Exception
	{		
		return DateUnit.createFromDateRange(getProjectPlanningDateRange());
	}

	private MultiCalendar getOneYearLater(MultiCalendar startOfFiscalYear)
	{
		return getShiftedByYears(startOfFiscalYear, 1);
	}

	private MultiCalendar getOneYearEarlier(MultiCalendar startOfFiscalYear)
	{
		return getShiftedByYears(startOfFiscalYear, -1);
	}

	private MultiCalendar getShiftedByYears(MultiCalendar startOfFiscalYear,
			int delta)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(
				startOfFiscalYear.getGregorianYear() + delta, 
				startOfFiscalYear.getGregorianMonth(), 
				startOfFiscalYear.getGregorianDay());
	}

	private Project project;
	private Vector<DateRange> dateRanges;
	private Vector<DateRange> yearlyDateRanges;
	private Vector<DateRange> editableDateRanges;
	
	private String startDate;
	private String endDate;
	private int firstCalendarMonth;
	private boolean isBudgetTimePeriodQuarterly;
}
