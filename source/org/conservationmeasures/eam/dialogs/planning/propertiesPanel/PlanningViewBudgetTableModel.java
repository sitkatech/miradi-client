/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.Utility;

public class PlanningViewBudgetTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewBudgetTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		currencyFormatter = getProject().getCurrencyFormatter();
	}

	public Object getValueAt(int row, int column)
	{
		return getCost(row, column);
	}
	
	private Object getCost(int row, int column)
	{
		try
		{
			Assignment assignment = getAssignment(row);
			DateRangeEffortList effortList = getDateRangeEffortList(assignment);
			DateRange dateRange = dateRanges[column];
			ProjectResource currentResource = getCurrentResource(assignment);

			return getCost(currentResource, effortList, dateRange);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return "";
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
	
	public String getUnit(DateRangeEffortList effortList, DateRange dateRange) throws Exception
	{
		double units = effortList.getTotalUnitQuantity(dateRange);
		return decimalFormatter.format(units);
	}
		
	public ProjectResource getCurrentResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		ProjectResource resource = (ProjectResource) getProject().findObject(resourceRef);
		
		return resource;
	}
		
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}
	
	private DecimalFormat currencyFormatter;
}
