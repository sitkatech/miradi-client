/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.AppPreferences;


public class PlanningViewBudgetTable extends PlanningViewAbstractTableWithColoredColumns
{
	public PlanningViewBudgetTable(PlanningViewBudgetTableModel modelToUse)
	{
		super(modelToUse);
		setBackground(AppPreferences.BUDGET_TABLE_BACKGROUND);
	}

	protected int getColumnWidth(int column)
	{
		return 125;
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetTable";
}
