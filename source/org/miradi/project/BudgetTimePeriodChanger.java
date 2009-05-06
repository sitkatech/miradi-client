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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.ObjectPool;
import org.miradi.objects.ResourceAssignment;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class BudgetTimePeriodChanger
{
	public static void convertQuarterlyToYearly(Project project) throws Exception
	{
		ObjectPool assignmentPool = project.getPool(ResourceAssignment.getObjectType());
		ORefList refs = assignmentPool.getRefList();
		for(int i = 0; i < refs.size(); ++i)
			convertQuarterlyToYearly(ResourceAssignment.find(project, refs.get(i)));
	}

	private static void convertQuarterlyToYearly(ResourceAssignment assignment) throws Exception
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
		
		CommandSetObjectData cmd = new CommandSetObjectData(assignment.getRef(), ResourceAssignment.TAG_DATERANGE_EFFORTS, newEffortList.toString());
		project.executeCommand(cmd);
	}

	public static void convertYearlyToQuarterly(Project project) throws Exception
	{
		ObjectPool assignmentPool = project.getPool(ResourceAssignment.getObjectType());
		ORefList refs = assignmentPool.getRefList();
		for(int i = 0; i < refs.size(); ++i)
			convertYearlyToQuarterly(ResourceAssignment.find(project, refs.get(i)));
	
	}

	private static void convertYearlyToQuarterly(ResourceAssignment assignment) throws Exception
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
		
		CommandSetObjectData cmd = new CommandSetObjectData(assignment.getRef(), ResourceAssignment.TAG_DATERANGE_EFFORTS, newEffortList.toString());
		project.executeCommand(cmd);
	}


}
