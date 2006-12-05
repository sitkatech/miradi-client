/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.martus.swing.UiTable;

public class BudgetTable extends UiTable
{
	public BudgetTable(BudgetTableModel budgetTableModelToUse)
	{
		super(budgetTableModelToUse);
		budgetTableModel = budgetTableModelToUse;
	}
	
	public BudgetTableModel getBudgetModel()
	{
		return budgetTableModel;
	}

	BudgetTableModel budgetTableModel;
}
