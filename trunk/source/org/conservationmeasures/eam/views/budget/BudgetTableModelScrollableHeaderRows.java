/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;


public class BudgetTableModelScrollableHeaderRows extends BudgetTableModelSplittableShell
{
	public BudgetTableModelScrollableHeaderRows(AbstractBudgetTableModel modelToUse)
	{
		super(modelToUse);
	}
	
	public int getColumnCount()
	{
		return model.getColumnCount() - LOCKED_COLUMN_COUNT;
	}
	
	public int getCorrectedSplittedColumnIndex(int col)
	{
		return col + LOCKED_COLUMN_COUNT;
	}
}
