/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.views.budget.AbstractBudgetTableModel;
import org.conservationmeasures.eam.views.budget.AssignmentTableModelSplittableShell;

abstract public class WorkPlanTableModelSplittableShell extends AssignmentTableModelSplittableShell
{
	public WorkPlanTableModelSplittableShell(AbstractBudgetTableModel modelToUse)
	{
		super(modelToUse);
	}

	public boolean doubleRowed()
	{
		return false;
	}
	
	public int getCorrectedRow(int row)
	{
		return row;
	}
	
	protected final static int LOCKED_COLUMN_COUNT = 2;
}
