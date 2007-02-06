/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

abstract public class BudgetTableModelSplittableShell extends  AssignmentTableModelSplittableShell
{
	public BudgetTableModelSplittableShell(AbstractBudgetTableModel modelToUse)
	{
		super(modelToUse);
	}

	public boolean doubleRowed()
	{
		return true;
	}
	
	protected final static int LOCKED_COLUMN_COUNT = 5;
}