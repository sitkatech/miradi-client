/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.main.AppPreferences;


public class PlanningViewBudgetTable extends PlanningViewTableWithSizedColumns
{
	public PlanningViewBudgetTable(PlanningViewBudgetTableModel modelToUse)
	{
		super(modelToUse);
		setBackground(AppPreferences.BUDGET_TABLE_BACKGROUND);
		getTableHeader().setBackground(getBackground());
	}
}
