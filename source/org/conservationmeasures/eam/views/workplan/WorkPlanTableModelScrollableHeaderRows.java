/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.views.budget.AbstractBudgetTableModel;
import org.conservationmeasures.eam.views.budget.BudgetTableModelSplittableShell;

public class WorkPlanTableModelScrollableHeaderRows extends BudgetTableModelSplittableShell
{
	public WorkPlanTableModelScrollableHeaderRows(AbstractBudgetTableModel modelToUse)
	{
		super(modelToUse);
	}

	public int getColumnCount()
	{
		return model.getColumnCount() - 2;
	}

	public int getCorrectedSplittedColumnIndex(int col)
	{
		return col + 2;
	}
}
