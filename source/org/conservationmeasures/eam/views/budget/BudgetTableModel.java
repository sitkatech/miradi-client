/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.MultiCalendar;

public class BudgetTableModel extends DefaultTableModel
{
	public BudgetTableModel(Project projectToUse, DateRange[] dateRangesToUse, Assignment[] assignmentsToUse)
	{
		project = projectToUse;
		dateRanges = dateRangesToUse;
		assignments  = assignmentsToUse;
		setProjectStartEndDates();
	}
	
	private void setProjectStartEndDates()
	{
		String startDate = project.getMetadata().getStartDate();
		projectStartDate  = MultiCalendar.createFromIsoDateString(startDate);	
	}

	public int getColumnCount()
	{
		return dateRanges.length;
	}

	public int getRowCount()
	{
		return assignments.length;
	}

	public String getColumnName(int column)
	{
		return dateRanges[column].getLabel();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return dateRanges[columnIndex];
	}
	
	Project project;
	MultiCalendar projectStartDate;
	MultiCalendar projectEndDate;
	DateRange[] dateRanges;
	Assignment[] assignments;
}
