/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.MultiCalendar;

public class BudgetTableModel extends AbstractTableModel
{
	public BudgetTableModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList  = assignmentIdListToUse;
		setProjectDateRanges();
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) 
	{
		return true;
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
		{
			return getSelectedResource(row);
		}
		
		return Integer.toString(0);
	}
	
	public BaseId getSelectedAssignment(int row)
	{
		return assignmentIdList.get(row);
	}
	
	public ProjectResource getSelectedResource(int row)
	{
		BaseId aId = assignmentIdList.get(row);
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, aId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		if (value == null)
		{
			EAM.logDebug("value in setValueAt is null");
			return;
		}
		if (col == 0)
		{
			try
			{
				ProjectResource projectResource = (ProjectResource)value;
				ProjectResourceId resourceId = (ProjectResourceId)(projectResource).getId();
				setResource(resourceId, row);
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
			}
		}
	}

	public void setResource(ProjectResourceId resourceId, int row) throws CommandFailedException
	{
		BaseId  assignmentId = assignmentIdList.get(row);
		Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId.toString());
		project.executeCommand(command);
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
