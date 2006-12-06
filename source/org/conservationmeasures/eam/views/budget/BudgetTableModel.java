/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.MultiCalendar;

public class BudgetTableModel extends DefaultTableModel
{
	public BudgetTableModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList  = assignmentIdListToUse;
		setProjectDateRanges();
	}
	
	private void setProjectDateRanges() throws Exception
	{
		String startDate = project.getMetadata().getStartDate();
		projectStartDate  = MultiCalendar.createFromIsoDateString(startDate);
		int year = projectStartDate.getGregorianYear();
		dateRanges = new DateRange[4];
		for (int i = 0; i < dateRanges.length; i++)
		{
			MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(year, 1, 1);
			int endMonth  =  (i + 1) * 3;
			MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(year, endMonth, 1);
			
			dateRanges[i] = new DateRange(start, end);
		}
		
	}

	public void setTask(Task task)
	{
		assignmentIdList = task.getAssignmentIdList();
	}
	
	public int getColumnCount()
	{
		return dateRanges.length + EXTRA_NUM_OF_ROWS;
	}

	public int getRowCount()
	{
		if (assignmentIdList != null)
			return assignmentIdList.size();
		
		return 0;
	}

	public String getColumnName(int col)
	{
		if (col == 0)
			return "Resources";
			
		return dateRanges[col - 1].getLabel();
	}
	
	public Object getValueAt(int row, int col)
	{
		
		if (col == 0)
			return null;
		
		return dateRanges[col - 1].getLabel();
	}
	
	Project project;
	MultiCalendar projectStartDate;
	MultiCalendar projectEndDate;
	DateRange[] dateRanges;
	IdList assignmentIdList;
	
	public static final int NUM_OF_RESOURCE_COLUMNS = 1;
	public static final int NUM_OF_TOTALS_COLUMNS = 0;
	public static final int EXTRA_NUM_OF_ROWS = NUM_OF_RESOURCE_COLUMNS + NUM_OF_TOTALS_COLUMNS; 
}
