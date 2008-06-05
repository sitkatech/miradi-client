/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.planning.upperPanel;

import java.text.DecimalFormat;
import java.util.Vector;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.DateRange;

public class PlanningViewBudgetAnnualTotalTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewBudgetAnnualTotalTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);

		yearlyDateRanges = getProjectCalendar().getYearlyDateRanges();
		combinedDataRange = getProjectCalendar().combineStartToEndProjectRange();
		currencyFormatter = project.getCurrencyFormatterWithCommas();
	}

	private ProjectCalendar getProjectCalendar() throws Exception
	{
		return project.getProjectCalendar();
	}

	public String getColumnName(int column)
	{
		if(isGrandTotalColumn(column))
			return GRAND_TOTAL_COLUMN_NAME;
		
		try
		{
			return getProjectCalendar().getDateRangeName((DateRange)yearlyDateRanges.get(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}
		
	public int getColumnCount()
	{
		return yearlyDateRanges.size() + 1;
	}
	
	public Object getValueAt(int row, int column)
	{
		BaseObject object = getBaseObjectForRowColumn(row, column);
		return getValueAt(object, column);
	}

	public Object getValueAt(Object rawObject, int column)
	{
		if (rawObject == null)
			return "";
		
		try
		{
			BaseObject object = (BaseObject) rawObject;
			if (isGrandTotalColumn(column))
				return getGrandTotalCost(object);
		
			return getYearlyTotalCost(object, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("[ERROR]");
		}
	}
	
	private Object getYearlyTotalCost(BaseObject objectForRow, int column) throws Exception
	{	
		return getBudgetCost(objectForRow, (DateRange)yearlyDateRanges.get(column));
	}

	private Object getGrandTotalCost(BaseObject objectForRow) throws Exception
	{
		return getBudgetCost(objectForRow, combinedDataRange);		
	}
	
	private Object getBudgetCost(BaseObject object, DateRange dateRange) throws Exception
	{
		double totalCost = object.getProportionalBudgetCost(dateRange);      
        if (totalCost == 0)
        	return "";
        
		return  currencyFormatter.format(totalCost);
	}

	private boolean isGrandTotalColumn(int column)
	{
		return column == getColumnCount() - 1;
	}
	
	private DecimalFormat currencyFormatter;
	
	private DateRange combinedDataRange;
	private Vector yearlyDateRanges;
	
	public static final String GRAND_TOTAL_COLUMN_NAME = EAM.text("Budget Total");
}
