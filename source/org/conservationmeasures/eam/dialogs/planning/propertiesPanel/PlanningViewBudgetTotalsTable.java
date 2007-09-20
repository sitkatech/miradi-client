/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;


import javax.swing.table.TableModel;

import org.conservationmeasures.eam.main.AppPreferences;

public class PlanningViewBudgetTotalsTable extends PlanningViewAbstractTableWithSizedColumns
{
	public PlanningViewBudgetTotalsTable(TableModel model)
	{
		super(model);
		setBackground(AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND);
		getTableHeader().setBackground(getBackground());
	}

	int getPreferredScrollableViewportWidth()
	{
		return 125;
	}
	
	protected int getColumnWidth(int column)
	{
		return 125;
	}	
}