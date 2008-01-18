/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.ObjectPool;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;

public class BudgetTimePeriodChanger
{
	public static void convertQuarterlyToYearly(Project project) throws Exception
	{
		ObjectPool assignmentPool = project.getPool(Assignment.getObjectType());
		ORefList refs = assignmentPool.getRefList();
		for(int i = 0; i < refs.size(); ++i)
			convertQuarterlyToYearly(Assignment.find(project, refs.get(i)));
	}

	private static void convertQuarterlyToYearly(Assignment assignment) throws Exception
	{
		Project project = assignment.getProject();
		ProjectCalendar projectCalendar = project.getProjectCalendar();
		
		DateRangeEffortList oldEffortList = assignment.getDateRangeEffortList();
		if(oldEffortList.size() == 0)
			return;
		
		DateRangeEffortList newEffortList = new DateRangeEffortList();
		
		Vector<DateRange> yearlyRanges = projectCalendar.getYearlyDateRanges();
		for(DateRange range : yearlyRanges)
		{
			double units = oldEffortList.getTotalUnitQuantity(range);
			if(units > 0)
			{
				DateRangeEffort dre = new DateRangeEffort("", units, range);
				newEffortList.add(dre);
			}
		}
		
		CommandSetObjectData cmd = new CommandSetObjectData(assignment.getRef(), Assignment.TAG_DATERANGE_EFFORTS, newEffortList.toString());
		project.executeCommand(cmd);
	}

	public static void convertYearlyToQuarterly(Project project) throws Exception
	{
		ObjectPool assignmentPool = project.getPool(Assignment.getObjectType());
		ORefList refs = assignmentPool.getRefList();
		for(int i = 0; i < refs.size(); ++i)
			convertYearlyToQuarterly(Assignment.find(project, refs.get(i)));
	
	}

	private static void convertYearlyToQuarterly(Assignment assignment) throws Exception
	{
		Project project = assignment.getProject();
		ProjectCalendar projectCalendar = project.getProjectCalendar();
		
		DateRangeEffortList oldEffortList = assignment.getDateRangeEffortList();
		if(oldEffortList.size() == 0)
			return;
		
		DateRangeEffortList newEffortList = new DateRangeEffortList();

		for(int i = 0; i < oldEffortList.size(); ++i)
		{
			DateRangeEffort oldDre = oldEffortList.get(i);
			DateRange firstQuarter = projectCalendar.createQuarter(oldDre.getDateRange().getStartDate());
			DateRangeEffort newDre = new DateRangeEffort("", oldDre.getUnitQuantity(), firstQuarter);
			newEffortList.add(newDre);
		}
		
		CommandSetObjectData cmd = new CommandSetObjectData(assignment.getRef(), Assignment.TAG_DATERANGE_EFFORTS, newEffortList.toString());
		project.executeCommand(cmd);
	}


}
