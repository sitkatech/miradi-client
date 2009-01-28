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

import java.awt.Color;
import java.util.Vector;

import javax.swing.Icon;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.icons.IconManager;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.DateRange;

public class PlanningViewBudgetAnnualTotalTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewBudgetAnnualTotalTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);

		yearlyDateRanges = getProjectCalendar().getYearlyDateRanges();
		combinedDataRange = getProjectCalendar().combineStartToEndProjectRange();
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

	@Override
	public boolean isCurrencyColumn(int column)
	{
		return true;
	}
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject object = getBaseObjectForRowColumn(row, column);
		if (object == null)
			return new EmptyChoiceItem();
		
		try
		{
			int shares = getProportionShares(row);
			
			String value = "";
			if (isGrandTotalColumn(column))
				value = getGrandTotalCost(object, shares);
			else
				value = getYearlyTotalCost(object, column, shares);

			Icon icon = null;
			if(value.length() > 0 && areBudgetValuesAllocated(row))
				icon = IconManager.getAllocatedCostIcon();

			return new TaglessChoiceItem(value, icon);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new TaglessChoiceItem(EAM.text("[Error]"));
		}
	}

	private String getYearlyTotalCost(BaseObject objectForRow, int column, int shares) throws Exception
	{	
		return getBudgetCost(objectForRow, (DateRange)yearlyDateRanges.get(column), shares);
	}

	private String getGrandTotalCost(BaseObject objectForRow, int shares) throws Exception
	{
		return getBudgetCost(objectForRow, combinedDataRange, shares);
	}
	
	private String getBudgetCost(BaseObject object, DateRange dateRange, int shares) throws Exception
	{
		double totalCost = object.getBudgetCost(dateRange);
        if (totalCost == 0)
        	return "";
        
        int totalShareCount = object.getTotalShareCount();
        double thisCost = totalCost * shares / totalShareCount;
        
		return  Double.toString(thisCost);
	}

	private boolean isGrandTotalColumn(int column)
	{
		return column == getColumnCount() - 1;
	}
	
	public Color getCellBackgroundColor(int column)
	{
		int columnCount = getColumnCount();
		final int TOTALS_COLUMN = columnCount - 1;
		if (column == TOTALS_COLUMN)
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
		
		return AppPreferences.BUDGET_TABLE_BACKGROUND;
	}

	private DateRange combinedDataRange;
	private Vector yearlyDateRanges;
	
	public static final String GRAND_TOTAL_COLUMN_NAME = EAM.text("Budget Total");
}
