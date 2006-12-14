/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class BudgetTableUnitsModel extends AbstractBudgetTableModel
{
	
	public BudgetTableUnitsModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		budgetModel = new BudgetTableModel(projectToUse, assignmentIdListToUse);
	}
	
	public void dataWasChanged()
	{
		budgetModel.dataWasChanged();
		fireTableDataChanged();
	}
	
	public void setTask(Task taskToUse)
	{
		budgetModel.setTask(taskToUse);
		fireTableDataChanged();
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		return budgetModel.isCellEditable(row * 2, col);
	}
		
	public int getColumnCount()
	{
		return budgetModel.getColumnCount();
	}
	
	public int getRowCount()
	{
		int rowCount = budgetModel.getRowCount();
		return rowCount / 2;
	}
		
	public String getColumnName(int col)
	{
		return budgetModel.getColumnName(col);
	}

	public Object getValueAt(int row, int col)
	{
		return budgetModel.getValueAt(row * 2, col);
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		budgetModel.setValueAt(value, row * 2, col);
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return budgetModel.getAssignmentForRow(row * 2);
	}
    
	BudgetTableModel budgetModel;
}
