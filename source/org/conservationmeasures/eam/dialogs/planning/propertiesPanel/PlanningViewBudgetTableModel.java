/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.project.Project;

public class PlanningViewBudgetTableModel extends PlanningViewAbstractAssignmentTabelModel
{
	public PlanningViewBudgetTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	
	public int getColumnCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return null;
	}
}
