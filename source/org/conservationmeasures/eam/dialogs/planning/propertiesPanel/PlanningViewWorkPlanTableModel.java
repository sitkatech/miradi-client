/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;


import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;

public class PlanningViewWorkPlanTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewWorkPlanTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}	
	
	public boolean isCellEditable(int row, int column)
	{
		DateRange dateRange = getDateRangeForColumn(column);
		ProjectCalendar projectCalendar = getProject().getProjectCalendar();
		return projectCalendar.isDateRangeEditable(dateRange);
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			return getUnits(row, column);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}

		return "";
	}
	
	public String getUnit(DateRangeEffortList effortList, DateRange dateRange)
	{
		double units = 0.0;
		try
		{
			units = effortList.getTotalUnitQuantity(dateRange);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return decimalFormatter.format(units);
	}
	
	public void setValueAt(Object value, int row, int column)
	{
		try
		{
			setUnitsForColumn(value, row, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setUnitsForColumn(Object value, int row, int column) throws Exception
	{
		DateRangeEffort effort = getDateRangeEffort(row, column);
		double units = 0;
		if (effort == null)
			effort = new DateRangeEffort("", units, dateRanges[column]);

		String valueAsString = value.toString().trim();
		if (! valueAsString.equals(""))
			units = Double.parseDouble(valueAsString);

		Assignment assignment = getAssignment(row);
		DateRangeEffortList effortList = getDateRangeEffortList(row);
		setUnits(assignment, effortList, effort, units);
	}
	
	public void setUnits(Assignment assignment, DateRangeEffortList effortList, DateRangeEffort effort, double units) throws Exception
	{
		effort.setUnitQuantity(units);
		effortList.setDateRangeEffort(effort);
		String newEffortListString = effortList.toString();
		if(newEffortListString.equals(assignment.getData(assignment.TAG_DATERANGE_EFFORTS)))
			return;
		
		Command command = new CommandSetObjectData(assignment.getType(), assignment.getId(), assignment.TAG_DATERANGE_EFFORTS, newEffortListString);
		getProject().executeCommand(command);
	}
	
	private DateRangeEffort getDateRangeEffort(int row, int column) throws Exception
	{
		return getDateRangeEffort(getAssignment(row), dateRanges[column]);
	}
}
