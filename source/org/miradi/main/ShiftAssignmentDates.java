/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.main;

import java.io.File;
import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaver;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.Translation;

public class ShiftAssignmentDates
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("ShiftAssignmentDates adjusts all assignment dates within a project");
		if(args.length != 1)
		{
			System.out.println("Must specify path to project");
			System.exit(1);
		}
		File projectFile = new File(args[0]);
		if(!projectFile.getName().endsWith(".Miradi"))
		{
			System.out.println("Project not found at: " + projectFile.getAbsolutePath());
			System.exit(1);
		}
		
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
		Project project = new Project();
		project.createOrOpenWithDefaultObjects(projectFile, new NullProgressMeter());
		ProjectLoader.loadProject(projectFile, project);
		showAllAssignmentDates(project, ResourceAssignmentSchema.getObjectType());
		showAllAssignmentDates(project, ExpenseAssignmentSchema.getObjectType());
		
		shiftAssignmentDatesOneYearLater(project, ResourceAssignmentSchema.getObjectType());
		shiftAssignmentDatesOneYearLater(project, ExpenseAssignmentSchema.getObjectType());

		String newProjectName = "Shifted" + projectFile.getName();
		File newProjectFile = new File(projectFile.getParentFile(), newProjectName);
		ProjectSaver.saveProject(project, newProjectFile);
		System.out.println("Saved as " + newProjectName);
	}

	private static void showAllAssignmentDates(Project project, int objectType) throws Exception
	{
		EAMObjectPool pool = project.getPool(objectType);
		Vector<BaseObject> objects = pool.getAllObjects();
		for(BaseObject object : objects)
		{
			Assignment assignment = (Assignment) object;
			DateUnitEffortList duel = assignment.getDateUnitEffortList();
			showDates(duel);
		}
	}

	private static void shiftAssignmentDatesOneYearLater(Project project, int objectType) throws Exception
	{
		EAMObjectPool pool = project.getPool(objectType);
		Vector<BaseObject> objects = pool.getAllObjects();
		for(BaseObject object : objects)
		{
			Assignment assignment = (Assignment) object;
			DateUnitEffortList duel = assignment.getDateUnitEffortList();
			DateUnitEffortList shifted = getShiftedOneYearLater(duel);
			project.setObjectData(assignment, Assignment.TAG_DATEUNIT_DETAILS, shifted.toString());
		}
	}

	private static void showDates(DateUnitEffortList duel)
	{
		for(int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort due = duel.getDateUnitEffort(index);
			DateUnit du = due.getDateUnit();
			DateUnit shifted = getShifted(du);
			String originalDateUnitCode = du.getDateUnitCode();
			String shiftedDateUnitCode = shifted.getDateUnitCode();
			System.out.println(originalDateUnitCode + "->" + shiftedDateUnitCode);
		}
	}

	private static DateUnitEffortList getShiftedOneYearLater(DateUnitEffortList duel)
	{
		DateUnitEffortList shifted = new DateUnitEffortList();
		for(int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort due = duel.getDateUnitEffort(index);
			DateUnit du = due.getDateUnit();
			DateUnit shiftedDateUnit = getShifted(du);
			shifted.add(new DateUnitEffort(shiftedDateUnit, due.getQuantity()));
		}
		
		return shifted;
	}

	private static DateUnit getShifted(DateUnit du)
	{
		if(du.isProjectTotal())
			return du;
		if(du.isYear())
		{
			int startingYear = du.getYearYear();
			int startingMonth = du.getYearStartMonth();
			return DateUnit.createFiscalYear(startingYear+1, startingMonth);
		}
		if(du.isQuarter())
		{
			String code = du.getDateUnitCode();
			String year = code.substring(0, 4);
			String quarter = code.substring(4);
			year = Integer.toString(new Integer(year) + 1);
			return new DateUnit(year + quarter);
		}
		if(du.isMonth())
		{
			int year = du.getYear();
			int month = du.getMonth();
			++year;
			MultiCalendar cal = MultiCalendar.createFromGregorianYearMonthDay(year, month, 1);
			return DateUnit.createMonthDateUnit(cal.toIsoDateString());
		}
		if(du.isDay())
		{
			int year = du.getYear();
			int month = du.getMonth();
			int day = du.getDay();
			++year;
			MultiCalendar cal = MultiCalendar.createFromGregorianYearMonthDay(year, month, day);
			return DateUnit.createDayDateUnit(cal.toIsoDateString());
		}
		return null;
	}
}
