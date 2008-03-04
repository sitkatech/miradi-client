/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.Utility;

public class PlanningViewBudgetTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewBudgetTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	public Object getValueAt(int row, int column)
	{
		try
		{
			return getCost(row, column);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}

		return "";
	}
	
	private Object getCost(int row, int column) throws Exception
	{
		Assignment assignment = getAssignment(row);
		DateRangeEffortList effortList = assignment.getDateRangeEffortList();
		DateRange dateRange = dateRanges[column];
		ProjectResource currentResource = getCurrentResource(assignment);

		return getCost(currentResource, effortList, dateRange);
	}
	
	public Object getCost(ProjectResource currentResource, DateRangeEffortList effortList, DateRange dateRange) throws Exception
	{
		if (currentResource == null)
			return "";

		String unit = getUnit(effortList, dateRange);
		double units = Utility.convertStringToDouble(unit);
		double costPerUnit = currentResource.getCostPerUnit();
		return currencyFormatter.format(units * costPerUnit);
	}
	
	private DecimalFormat currencyFormatter;
}
