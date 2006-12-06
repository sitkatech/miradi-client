/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.project.Project;
import org.martus.util.MultiCalendar;

public class BudgetTableModel extends DefaultTableModel
{
	public BudgetTableModel(Project projectToUse)
	{
		project = projectToUse;
		setProjectStartEndDates();
	}
	
	private void setProjectStartEndDates()
	{
		String startDate = project.getMetadata().getStartDate();
		projectStartDate  = MultiCalendar.createFromIsoDateString(startDate);	
	}

	public int getColumnCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return null;
	}
	
	//FIXME budget code - finish this
	//private DateRange createQuarterDateRange(MultiCalendar dateToUse) throws Exception
	//{
	//	DateRange dateRange = new DateRange(projectStartDate, projectEndDate);
	//	return dateRange;
	//}
	
	Project project;
	MultiCalendar projectStartDate;
	MultiCalendar projectEndDate;
}
