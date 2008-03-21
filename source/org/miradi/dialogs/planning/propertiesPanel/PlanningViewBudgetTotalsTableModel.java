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
package org.miradi.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.miradi.main.EAM;
import org.miradi.objects.Assignment;
import org.miradi.project.BudgetCalculator;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;

public class PlanningViewBudgetTotalsTableModel extends PlanningViewAbstractTotalsTableModel
{
	public PlanningViewBudgetTotalsTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		totalsCalculator = new BudgetCalculator(getProject());
		dateRange = getProject().getProjectCalendar().combineStartToEndProjectRange();
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
	}
	
	public String getColumnName(int column)
	{
		return EAM.text("Total");
	}

	public Object getValueAt(int row, int column)
	{
		try
		{
			return getTotalCost(row);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}
	
	public String getTotalCost(int row) throws Exception
	{	
		Assignment assignment = getAssignment(row);	
		double totalCost = totalsCalculator.getTotalCost(assignment, dateRange);
		return currencyFormatter.format(totalCost);
	}
	
	private DecimalFormat currencyFormatter;
	private DateRange dateRange;
	private BudgetCalculator totalsCalculator;
}
