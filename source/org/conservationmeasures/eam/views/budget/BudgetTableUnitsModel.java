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
	
	public int getColumnCount()
	{
		int colCount  = budgetModel.getColumnCount();
		colCount -= TOTAL_ROW_HEADER_COLUMN_COUNT;
		colCount /= 2;
		colCount += UNIT_ROW_HEADER_COLUMN_COUNT;
		
		return colCount;
	}
	
	public int getRowCount()
	{
		int rowCount = budgetModel.getRowCount();
		return rowCount / 2;
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		return budgetModel.isCellEditable(row * 2, translateToBudgetModelCol(col));
	}
	
	public String getColumnName(int col)
	{
		return budgetModel.getColumnName(translateToBudgetModelCol(col));
	}

	public Object getValueAt(int row, int col)
	{
		return budgetModel.getValueAt(row * 2, translateToBudgetModelCol(col));
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		budgetModel.setValueAt(value, row * 2, translateToBudgetModelCol(col));
	}
	
	public int translateToBudgetModelCol(int col)
	{
		if (col < UNIT_ROW_HEADER_COLUMN_COUNT)
			return col;
		col -= UNIT_ROW_HEADER_COLUMN_COUNT;
		col *= 2;
		col += TOTAL_ROW_HEADER_COLUMN_COUNT;
		
		return col;
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return budgetModel.getAssignmentForRow(row * 2);
	}
    
	static final int UNIT_ROW_HEADER_COLUMN_COUNT = 3;
	
	BudgetTableModel budgetModel;
}
