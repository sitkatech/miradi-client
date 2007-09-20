/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.project.Project;

public class PlanningViewWorkPlanTable extends PlanningViewAbstractRightAlignedTable
{
	public PlanningViewWorkPlanTable(Project projectToUse, PlanningViewAbstractBudgetTableModel modelToUse) throws Exception
	{
		super(modelToUse);
	}
	
	protected int getColumnWidth(int column)
	{
		return getColumnHeaderWidth(column);
	}

	public Color getColumnBackGroundColor(int columnCount, int column)
	{
		return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
	}	
}
