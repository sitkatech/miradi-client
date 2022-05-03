/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.MonthAbbreviationsQuestion;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.utils.DateRange;

public class ProjectCalendar implements CommandExecutedListener
{
	public ProjectCalendar(Project projectToUse)  throws Exception
	{
		project = projectToUse;
		clear();
	}

	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (commandInvalidatesCache(event))
			clear();
	}

	private boolean commandInvalidatesCache(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisType(ProjectMetadataSchema.getObjectType()))
			return true;

		return false;
	}

	private void clearAllCachedData()
	{
		cachedPlanningStartDate = "";
		cachedPlanningEndDate = "";
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}

	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}

	public String getLongDateUnitString(DateUnit dateUnit)
	{
		if (dateUnit.isProjectTotal())
			return EAM.text("Total");
		
		if (dateUnit.isYear())
			return getYearString(dateUnit, getFiscalYearFirstMonth());
		
		if (dateUnit.isQuarter())
			return getLongQuarterString(dateUnit, getFiscalYearFirstMonth());

		if (dateUnit.isMonth())
			return getLongMonthString(dateUnit);
		
		if (dateUnit.isDay())
			return Integer.toString(dateUnit.getDay());
		
		throw new RuntimeException("DateUnit could not be converted to long string. DateUnit = " + dateUnit + ".  Fiscal Year First Month = " + getFiscalYearFirstMonth());
	}

	public String getShortDateUnitString(DateUnit dateUnit)
	{
		return getShortDateUnit(dateUnit, getFiscalYearFirstMonth());
	}
	
	public String getDateRangeName(DateRange dateRange)
	{
		return getFullDateRangeString(dateRange, getFiscalYearFirstMonth());
	}
	
	public int getFiscalYearFirstMonth()
	{
		return getProject().getMetadata().getFiscalYearFirstMonth();
	}

	public MultiCalendar getPlanningStartMultiCalendar()
	{
		return MultiCalendar.createFromIsoDateString(getPlanningStartDate());
	}
	
	public MultiCalendar getPlanningEndMultiCalendar()
	{
		return MultiCalendar.createFromIsoDateString(getPlanningEndDate());
	}
	
	public String getPlanningStartDate()
	{
		if (cachedPlanningStartDate.isEmpty())
		{
			MultiCalendar now = new MultiCalendar();
			MultiCalendar startOfCalendarYear = MultiCalendar.createFromGregorianYearMonthDay(now.getGregorianYear(), getFiscalYearFirstMonth(), 1);

			ProjectMetadata metadata = project.getMetadata();
			String candidatesBestFirst[] = new String[] {
					metadata.getWorkPlanStartDateAsString(),
					metadata.getStartDate(),
					startOfCalendarYear.toIsoDateString(),
			};

			cachedPlanningStartDate = firstNonBlank(candidatesBestFirst);
		}

		return cachedPlanningStartDate;
	}

	public String getPlanningEndDate()
	{
		if (cachedPlanningEndDate.isEmpty())
		{
			MultiCalendar now = new MultiCalendar();
			MultiCalendar planningStartMultiCalendar = MultiCalendar.createFromGregorianYearMonthDay(now.getGregorianYear(), getFiscalYearFirstMonth(), 1);
			MultiCalendar endOfCalendarYear = getOneYearLater(planningStartMultiCalendar);
			endOfCalendarYear.addDays(-1);

			ProjectMetadata metadata = project.getMetadata();
			String candidatesBestFirst[] = new String[] {
					metadata.getWorkPlanEndDate(),
					metadata.getExpectedEndDate(),
					endOfCalendarYear.toIsoDateString(),
			};

			cachedPlanningEndDate = firstNonBlank(candidatesBestFirst);
		}

		return cachedPlanningEndDate;
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
	
	private Project getProject()
	{
		return project;
	}

	public static String getShortDateUnit(DateUnit dateUnit, int fiscalYearFirstMonth)
	{
		if (dateUnit.isProjectTotal())
			return EAM.text("Total");
		
		if (dateUnit.isYear())
			return getYearString(dateUnit, fiscalYearFirstMonth);
		
		if (dateUnit.isQuarter())
			return getQuarterString(dateUnit, fiscalYearFirstMonth);

		if (dateUnit.isMonth())
			return getMonthString(dateUnit);
		
		if (dateUnit.isDay())
			return Integer.toString(dateUnit.getDay());
		
		throw new RuntimeException("DateUnit could not be converted to string. DateUnit = " + dateUnit + ".  Fiscal Year First Month = " + fiscalYearFirstMonth);
	}

	private static String getMonthString(DateUnit dateUnit)
	{
		ChoiceQuestion question = new MonthAbbreviationsQuestion();
		String month = Integer.toString(dateUnit.getMonth());
		ChoiceItem choiceItem = question.findChoiceByCode(month);
		return choiceItem.getLabel();
	}
	
	private static String getQuarterString(DateUnit dateUnit, int fiscalYearFirstMonth)
	{
		int quarter = dateUnit.getQuarter();
		int startFiscalQuarter = (fiscalYearFirstMonth - 1 ) / 3 + 1;
		int fiscalYearQuarter = ((quarter - startFiscalQuarter) + 4) % 4 + 1;
		String quarterlyPrefixString = getQuarterlyPrefixString(); 
		if (fiscalYearFirstMonth > 1)
			quarterlyPrefixString = getFiscalQuarterlyPrefixString();
		
		return quarterlyPrefixString + fiscalYearQuarter;
	}
	
	private static String getLongQuarterString(DateUnit dateUnit, int fiscalYearFirstMonth)
	{
		DateUnit yearDateUnit = dateUnit.getSuperDateUnit(fiscalYearFirstMonth);
		String yearString = getYearString(yearDateUnit, fiscalYearFirstMonth);
		String quarterString = getQuarterString(dateUnit, fiscalYearFirstMonth);
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%year", yearString);
		tokenReplacementMap.put("%quarter", quarterString);
		return EAM.substitute("%year - %quarter", tokenReplacementMap);
	}
	
	private static String getLongMonthString(DateUnit dateUnit)
	{
		String yearString = Integer.toString(dateUnit.getYear());
		String monthString = getMonthString(dateUnit);
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%year", yearString);
		tokenReplacementMap.put("%month", monthString);
		return EAM.substitute("%year - %month", tokenReplacementMap);
	}
	
	private static String getYearString(DateUnit dateUnit, int fiscalYearFirstMonth)
	{
		String yearString = dateUnit.getYearYearString();
		int yearStartMonth = dateUnit.getYearStartMonth();
		if (yearStartMonth == 1)
			return yearString;
		
		int year = Integer.parseInt(yearString);
		int fiscalYear = year + 1;
		if (yearStartMonth == fiscalYearFirstMonth)
			return getFiscalYearString(fiscalYear);
		
		return Integer.toString(year) + "-" +Integer.toString(fiscalYear);
	}

	public static String getFullDateRangeString(DateRange dateRange, int fiscalYearFirstMonth)
	{
		String fullRange = dateRange.toString();
		
		MultiCalendar startDate = dateRange.getStartDate();
		MultiCalendar endDate = new MultiCalendar(dateRange.getEndDate());

		MultiCalendar dayAfterEndDate = new MultiCalendar(dateRange.getEndDate());
		dayAfterEndDate.addDays(1);
		
		if(startDate.getGregorianDay() != 1)
			return fullRange;
		if(dayAfterEndDate.getGregorianDay() != 1)
			return fullRange;
		
		int startFiscalMonth = startDate.getGregorianMonth();
		if((startFiscalMonth % 3) != 1)
			return fullRange;
	
		int nextFiscalMonth = dayAfterEndDate.getGregorianMonth();
		if((nextFiscalMonth % 3) != 1)
			return fullRange;

		int skew = fiscalYearFirstMonth - 1;

		int startFiscalYear = startDate.getGregorianYear();

		startFiscalMonth -= skew;

		if (startFiscalMonth < 0)
			startFiscalMonth += 12;
		else
			if (fiscalYearFirstMonth != 1)
				startFiscalYear += 1;

		int endFiscalYear = endDate.getGregorianYear();
		int endFiscalMonth = endDate.getGregorianMonth();

		endFiscalMonth -= skew;
		if (endFiscalMonth == 0)
			endFiscalMonth = 12;

		if (endFiscalMonth < 0)
			endFiscalMonth += 12;
		else
			if (fiscalYearFirstMonth != 1 && endFiscalMonth != 12)
				endFiscalYear += 1;

		String startYearString = getFiscalYearString(startFiscalYear);
		
		if(startFiscalYear == endFiscalYear && startFiscalMonth == 1 && endFiscalMonth == 12)
			return startYearString;

		String endYearString = getFiscalYearString(endFiscalYear);
		
		int startFiscalQuarter = ((startFiscalMonth - 1) / 3) + 1;
		int endFiscalQuarter = endFiscalMonth / 3;

		String firstFiscalQuarter = getQuarterlyPrefixString() + startFiscalQuarter + " " + startYearString;
		if (startFiscalQuarter == endFiscalQuarter && startFiscalYear == endFiscalYear)
			return firstFiscalQuarter;
		
		return firstFiscalQuarter + " - " + getQuarterlyPrefixString() + endFiscalQuarter + " " + endYearString;
	}

	private static String getFiscalYearString(int fiscalYear)
	{
		String yearString = Integer.toString(fiscalYear);
		return EAM.text("Fiscal Year|FY") + yearString.substring(2);
	}
	
	private static String getQuarterlyPrefixString()
	{
		return EAM.text("Quarter Prefix|Q");
	}
	
	private static String getFiscalQuarterlyPrefixString()
	{
		return EAM.text("Fiscal Quarter Prefix|FQ");
	}
	
	public DateRange convertToDateRange(DateUnit dateUnit) throws Exception
	{
		if(dateUnit.isProjectTotal())
			return getProjectPlanningDateRange();
		
		return dateUnit.asDateRange();
	}
	
	public Vector<DateUnit> getSuperDateUnitsHierarchy(DateUnit dateUnit)
	{
		Vector<DateUnit> superDateUnits = dateUnit.getSuperDateUnitHierarchy(getFiscalYearFirstMonth());
		if (shouldHideQuarterColumns())
			return removeQuarterDateUnits(superDateUnits);
		if (shouldHideDayColumns())
			return removeDayDateUnits(superDateUnits);

		return superDateUnits;
	}
	
	public Vector<DateUnit> getSubDateUnits(DateUnit dateUnit) throws Exception
	{
		Vector<DateUnit> subDateUnits = getSubDateUnitsWithinProjectPlanningDates(dateUnit);

		if (dateUnit.isYear() && shouldHideQuarterColumns())
			return getMonthAsSubDateUnitsOfYear(subDateUnits);

		if (dateUnit.isMonth() && shouldHideDayColumns())
			return new Vector<DateUnit>();

		return subDateUnits;
	}
	
	public Vector<DateUnit> getAllProjectYearDateUnits() throws Exception
	{
		return getSubDateUnits(new DateUnit());
	}
	
	public Vector<DateUnit> getAllProjectQuarterDateUnits() throws Exception
	{
		if(shouldHideQuarterColumns())
			return new Vector<DateUnit>();
		
		return getAllSubDateUnits(getAllProjectYearDateUnits());
	}
	
	public Vector<DateUnit> getAllProjectMonthDateUnits() throws Exception
	{
		if(shouldHideQuarterColumns())
			return getAllSubDateUnits(getAllProjectYearDateUnits());

		return getAllSubDateUnits(getAllProjectQuarterDateUnits());
	}

	private Vector<DateUnit> getAllSubDateUnits(Vector<DateUnit> dateUnits) throws Exception
	{
		Vector<DateUnit> subDateUnits = new Vector<DateUnit>();
		for(DateUnit dateUnit : dateUnits)
		{
			subDateUnits.addAll(getSubDateUnits(dateUnit));
		}

		return subDateUnits;
	}
	
	private Vector<DateUnit> removeQuarterDateUnits(Vector<DateUnit> superDateUnits)
	{
		Vector<DateUnit> withoutQuarters = new Vector<DateUnit>();
		for(DateUnit superDateUnit : superDateUnits)
		{
			if (!superDateUnit.isQuarter())
				withoutQuarters.add(superDateUnit);
		}
		
		return withoutQuarters;
	}

	private Vector<DateUnit> removeDayDateUnits(Vector<DateUnit> superDateUnits)
	{
		Vector<DateUnit> withoutDays = new Vector<DateUnit>();
		for(DateUnit superDateUnit : superDateUnits)
		{
			if (!superDateUnit.isDay())
				withoutDays.add(superDateUnit);
		}

		return withoutDays;
	}

	private Vector<DateUnit> getMonthAsSubDateUnitsOfYear(Vector<DateUnit> quarterDateUnits) throws Exception
	{
		LinkedHashSet<DateUnit> monthDateUnits = new LinkedHashSet<DateUnit>();
		for(DateUnit quarterDateUnit : quarterDateUnits)
		{
			Vector<DateUnit> subMonthDateUnitsOfQuarterDateUnit = getSubDateUnitsWithinProjectPlanningDates(quarterDateUnit);
			monthDateUnits.addAll(subMonthDateUnitsOfQuarterDateUnit);
		}
		
		return new Vector<DateUnit>(monthDateUnits);
	}

	private Vector<DateUnit> getSubDateUnitsWithinProjectPlanningDates(DateUnit dateUnit) throws Exception
	{
		return getSubDateUnitsWithin(dateUnit, getProjectPlanningDateRange());
	}

	public Vector<DateUnit> getSubDateUnitsWithin(DateUnit dateUnit, DateRange projectDateRange) throws Exception
	{
		if (dateUnit.isProjectTotal())
			return getProjectYearsDateUnits(projectDateRange);
		
		if (dateUnit.hasSubDateUnits())
			return dateUnit.getSubDateUnitsWithin(projectDateRange);
		
		return new Vector<DateUnit>();
	}
	
	private boolean shouldHideQuarterColumns()
	{
		return !shouldShowQuarterColumns();
	}

	public boolean shouldShowQuarterColumns()
	{
		return getProject().getMetadata().areQuarterColumnsVisible();
	}

	public boolean shouldHideDayColumns()
	{
		return !shouldShowDayColumns();
	}

	public boolean shouldShowDayColumns()
	{
		return getProject().getMetadata().areDayColumnsVisible();
	}

	public Vector<DateUnit> getProjectYearsDateUnits(DateRange dateRange)
	{
		Vector<DateUnit> dateUnits = new Vector<DateUnit>();
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
		while(isStartDateAfterEndDate(startOfFiscalYear, start))
			startOfFiscalYear = getOneYearEarlier(startOfFiscalYear);

		return startOfFiscalYear;
	}
	
	public DateRange getProjectPlanningDateRange() throws Exception
	{
		MultiCalendar thisStartDate = getPlanningStartMultiCalendar();
		MultiCalendar thisEndDate = getPlanningEndMultiCalendar();
		
		if (isStartDateAfterEndDate(thisStartDate, thisEndDate))
		{
			EAM.logWarning("Project planning DateRange end date: " + thisEndDate + " was before start date: " + thisEndDate);
			return new DateRange(thisStartDate, thisStartDate);
		}
			
		return new DateRange(thisStartDate, thisEndDate);
	}

	public boolean isStartDateAfterEndDate()
	{
		MultiCalendar thisStartDate = getPlanningStartMultiCalendar();
		MultiCalendar thisEndDate = getPlanningEndMultiCalendar();

		return thisStartDate.after(thisEndDate);
	}

	public boolean isStartDateAfterEndDate(MultiCalendar thisStartDate, MultiCalendar thisEndDate)
	{
		return thisStartDate.after(thisEndDate);
	}

	public boolean arePlanningStartAndEndDatesFlipped()
	{
		return isStartDateAfterEndDate(getPlanningStartMultiCalendar(), getPlanningEndMultiCalendar());
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

	public String convertToSafeString(DateRange combinedDateRange)
	{
		if (combinedDateRange == null)
			return "";
		
		return  getDateRangeName(combinedDateRange);
	}

	private Project project;
	private String cachedPlanningStartDate;
	private String cachedPlanningEndDate;
}
