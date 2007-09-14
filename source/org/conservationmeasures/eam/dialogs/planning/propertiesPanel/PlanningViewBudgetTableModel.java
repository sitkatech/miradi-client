/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.project.Project;

public class PlanningViewBudgetTableModel extends PlanningViewAbstractBudgetTableModel
{
	public PlanningViewBudgetTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return null;
	}
}
