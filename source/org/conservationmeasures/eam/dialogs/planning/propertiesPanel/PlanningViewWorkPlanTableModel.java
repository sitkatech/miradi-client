/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;


import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;

public class PlanningViewWorkPlanTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewWorkPlanTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
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
	
	private Object getUnits(int row, int column) throws Exception
	{
		DateRangeEffortList effortList = getDateRangeEffortList(row);
		DateRange dateRange = dateRanges[column];
		
		return getUnit(effortList, dateRange);
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
		DateRangeEffortList effortList = getDateRangeEffortList(row);
		DateRangeEffort effort = getDateRangeEffort(row, column);
		double units = 0;
		if (effort == null)
			effort = new DateRangeEffort("", units, dateRanges[column]);

		String valueAsString = value.toString().trim();
		if (! valueAsString.equals(""))
			units = Double.parseDouble(valueAsString);

		Assignment assignment = getAssignment(row);
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
	
	public DateRangeEffort getDateRangeEffort(Assignment assignment, DateRange dateRange) throws Exception
	{
		DateRangeEffort dateRangeEffort = null;
		DateRangeEffortList effortList = assignment.getDateRangeEffortList();
		dateRangeEffort = effortList.getEffortForDateRange(dateRange);
		return dateRangeEffort;
	}
	
	private DateRangeEffortList getDateRangeEffortList(int row) throws Exception
	{
		return getAssignment(row).getDateRangeEffortList();
	}
	
	private DateRangeEffort getDateRangeEffort(int row, int column) throws Exception
	{
		return getDateRangeEffort(getAssignment(row), dateRanges[column]);
	}
}
