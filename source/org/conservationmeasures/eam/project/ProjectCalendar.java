/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.MultiCalendar;

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
		DateRange startDateRange = (DateRange)getYearlyDateRanges().get(0);
		DateRange endDateRange = (DateRange)getYearlyDateRanges().get(getYearlyDateRanges().size() - 1);
		
		return DateRange.combine(startDateRange, endDateRange);
	}
	
	public void rebuildProjectDateRanges() throws Exception
	{
		//TODO budget code -  move project start/end code to Project
		String startDate = project.getMetadata().getStartDate();
		int firstCalendarYear = 2006;
		int firstCalendarMonth = project.getMetadata().getFiscalYearFirstMonth();

		if (startDate.length() > 0 )
		{
			MultiCalendar projectStartDate = MultiCalendar.createFromIsoDateString(startDate);
			int quarterStartMonth = projectStartDate.getGregorianMonth();
			quarterStartMonth -= (quarterStartMonth-1)%3;
			if(quarterStartMonth < firstCalendarMonth)
				--firstCalendarYear;	
		}
		
		MultiCalendar planningStartDate = MultiCalendar.createFromGregorianYearMonthDay(firstCalendarYear, firstCalendarMonth, 1);
		MultiCalendar planningEndDate = getPlanningEndDate(planningStartDate);
		
		yearlyDateRanges = new Vector();
		editableDateRanges = new Vector();
		Vector vector = new Vector();
		while(planningStartDate.before(planningEndDate))
		{
			vector.addAll(getQuartersPlustYearlyRange(planningStartDate));
			planningStartDate = MultiCalendar.createFromGregorianYearMonthDay(
					planningStartDate.getGregorianYear() + 1, 
					planningStartDate.getGregorianMonth(), 
					planningStartDate.getGregorianDay());
		}
		this.dateRanges = ((DateRange[])vector.toArray(new DateRange[0]));
	}

	private MultiCalendar getPlanningEndDate(MultiCalendar planningStartDate)
	{
		final int DEFAULT_YEAR_DIFF = 3;
		MultiCalendar defaultEndDate = MultiCalendar.createFromGregorianYearMonthDay(
				planningStartDate.getGregorianYear() + DEFAULT_YEAR_DIFF, 
				planningStartDate.getGregorianMonth(), 
				planningStartDate.getGregorianDay());
		defaultEndDate.addDays(-1);
		
		String endDate = project.getMetadata().getExpectedEndDate();
		
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
		
		if (planningStartDate.after(planningEndDate))
			return defaultEndDate;
		
		planningEndDate.addDays(-1);
		return planningEndDate;
	}

	private Vector getQuartersPlustYearlyRange(MultiCalendar startingDate) throws Exception
	{
		Vector ranges = new Vector();

		for(int quarter = 0; quarter < 4; ++quarter)
		{
			DateRange quarterRange = createQuarter(startingDate);
			ranges.add(quarterRange);
			editableDateRanges.add(quarterRange);
			startingDate = nextQuarter(startingDate);
		}
		
		ranges.add(DateRange.combine((DateRange)ranges.get(0), (DateRange)ranges.get(3)));
		
		getYearlyDateRanges().add(DateRange.combine((DateRange)ranges.get(0), (DateRange)ranges.get(3)));
		
		return ranges;
	}
	
	private DateRange createQuarter(MultiCalendar quarterStart) throws Exception
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
					cmd.getFieldTag().equals(ProjectMetadata.TAG_FISCAL_YEAR_START))
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
		if(dateRanges == null)
			rebuildProjectDateRanges();
		return dateRanges;
	}

	public Vector getYearlyDateRanges() throws Exception
	{
		if(yearlyDateRanges == null)
			rebuildProjectDateRanges();
		return yearlyDateRanges;
	}
	
	public boolean isDateRangeEditable(DateRange dateRange)
	{
		return editableDateRanges.contains(dateRange);
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
		
		int fiscalYear = startFiscalYear;
		String yearString = Integer.toString(fiscalYear);
		yearString = "FY" + yearString.substring(2);
		
		if(startFiscalYear+1 == endFiscalYear && startFiscalMonth == endFiscalMonth && startFiscalMonth == 1)
			return yearString;
		
		int fiscalQuarter = (startFiscalMonth-1) / 3 + 1;
		if(fiscalQuarter == 4)
		{
			if(startFiscalYear+1 != endFiscalYear)
				return fullRange;
		}
		else if(startFiscalYear != endFiscalYear)
		{
			return fullRange;
		}
		
		return "Q" + fiscalQuarter + " " + yearString;
	}

	private Project project;
	private DateRange[] dateRanges;
	private Vector yearlyDateRanges;
	private Vector<DateRange> editableDateRanges;
}
