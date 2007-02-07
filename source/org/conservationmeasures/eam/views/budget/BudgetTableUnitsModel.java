/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;

public class BudgetTableUnitsModel extends AbstractBudgetTableModel
{
	public BudgetTableUnitsModel(Project projectToUse) throws Exception
	{
		super(projectToUse, new IdList());
	}
		
	public void setTask(Task taskToUse)
	{
		if (isAlreadyCurrentTask(taskToUse))
			return;
		
		task = taskToUse;
		dataWasChanged();
	}
	
	public void dataWasChanged()
	{
		if(task == null)
			assignmentIdList = new IdList();
		else
			assignmentIdList = task.getAssignmentIdList();

		fireTableDataChanged();
	}
	
	public int getColumnCount()
	{
		return (dateRanges.length + UNIT_ROW_HEADER_COLUMN_COUNT + TOTALS_COLUMN_COUNT - 1);
	}
	
	public int getRowCount()
	{
		if (assignmentIdList != null)
			return assignmentIdList.size();
		
		return 0;
	}
	
	public boolean isUnitsTotalColumn(int col)
	{
		return col == getColumnCount() - 1 ;
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		if (isUnitsTotalColumn(col))
			return false;
		
		if (isLabelColumn(col))
			return false;
		
		if (isYearlyTotalColumn(col))
			return false;
		
		return true;
	}
	
	public String getColumnName(int col)
	{
		if (isResourceColumn(col))
			return "Resources";
		
		if (isUnitsLabelColumn(col))
			return "Unit";
		
		if (isLabelColumn(col))
			return "";
		
		if (isUnitsTotalColumn(col))
			return "Unit Totals";
	
		if (isUnitsColumn(col))
			return dateRanges[convertColumnToMatchDataRangeArray(col)].toString();
		
		return "";
	}

	public Object getValueAt(int row, int col)
	{
		BaseId assignmentForRow = getAssignmentForRow(row);
		
		if (isResourceColumn(col))
			return getCurrentResource(assignmentForRow);
		
		if (isLabelColumn(col))
			return getResourceCellLabel(row, col);
		
		if (isUnitsColumn(col))
		{
			DateRangeEffortList effortList;
			try
			{
				effortList = getDateRangeEffortList(getAssignment(row));
				DateRange dateRange = dateRanges[convertColumnToMatchDataRangeArray(col)];
				return getUnit(effortList, dateRange);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		if (isUnitsTotalColumn(col))
		{
			return getTotalUnits(getAssignment(row));
		}
		
		return new String("");
	}
		
	public Assignment getAssignment(int row)
	{
		return (Assignment)project.findObject(ObjectType.ASSIGNMENT, getAssignmentForRow(row));
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		if (value == null)
		{
			EAM.logDebug("value in setValueAt is null");
			return;
		}
		BaseId assignmentId = getAssignmentForRow(row);
		if (isResourceColumn(col))
		{
			ProjectResource projectResource = (ProjectResource)value;
			setResource(projectResource, assignmentId);
			return;
		}
		
		if (isUnitsColumn(col))
		{
			try
			{
				Assignment assignment = getAssignment(row);
				DateRangeEffortList effortList = getDateRangeEffortList(assignment);
				DateRangeEffort effort = getDateRangeEffort(assignment, dateRanges[convertColumnToMatchDataRangeArray(col)]);

				double units = 0;
				String valueAsString = value.toString().trim();
				if (! valueAsString.equals(""))
					units = Double.parseDouble(valueAsString);

				//FIXME budget code - take out daterange
				if (effort == null)
					effort = new DateRangeEffort("", units, dateRanges[convertColumnToMatchDataRangeArray(col)]);

				setUnits(assignment, effortList, effort, units);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}

	private int convertColumnToMatchDataRangeArray(int col)
	{
		return col - UNIT_ROW_HEADER_COLUMN_COUNT;
	}
	
	private String getResourceCellLabel(int row, int col)
	{
		ProjectResource resource = getCurrentResource(getAssignmentForRow(row));
		if (resource  == null)
			return "";
		
		if (col == getUnitsLabelColumnIndex())
			return resource.getData(ProjectResource.TAG_COST_UNIT);
		
		return "";
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return assignmentIdList.get(row);
	}
	
	public boolean isYearlyTotalColumn(int col)
	{
		if (col < UNIT_ROW_HEADER_COLUMN_COUNT)
			return false;

		//TODO only dealing with QUARTERS,  this method has to be revised
		//if dealing with anything other than QUARTERS
		final int QUARTERLY_TOTAL_COUNT = 5;
		int convertedCol = col - (UNIT_ROW_HEADER_COLUMN_COUNT - 1);
		
		if (convertedCol % QUARTERLY_TOTAL_COUNT == 0)
			return true;
		
		return false;
	}
	
	public boolean isTotalsRow(int row)
	{
		if (row < (getRowCount() - 1))
			return false;
			
		return true;
	}
	
	protected boolean isUnitsColumn(int col)
	{
		if (col < (UNIT_ROW_HEADER_COLUMN_COUNT))
			return false;
		
		if (col  < (getColumnCount() - UNIT_TOTAL_COLUMN_COUNT ))
			return true;
		
		return false;
	}
	
	protected boolean isLabelColumn(int col)
	{
		return col == 1;
	}
	
	public boolean doubleRowed()
	{
		return false;
	}
	
	public int getUnitsLabelColumnIndex()
	{
		return UNITS_LABEL_COLUMN_INDEX;
	}
	
	public int getAccountingCodeColumnIndex()
	{
		return -1;
	}

	public int getFundingSourceColumnIndex()
	{
		return -1;
	}
	
	private static final int UNITS_LABEL_COLUMN_INDEX = 1;
	static final int UNIT_ROW_HEADER_COLUMN_COUNT = 2;
	static final int UNIT_TOTAL_COLUMN_COUNT = 1;
}
