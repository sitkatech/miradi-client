/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.project.Project;

public class BudgetTableModel extends AbstractTableModel
{
	public BudgetTableModel(Project projectToUse)
	{
		project = projectToUse;
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
	
	Project project;
}
