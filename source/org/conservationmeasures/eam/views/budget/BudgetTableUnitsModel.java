/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class BudgetTableUnitsModel extends AbstractBudgetTableModel
{
	public BudgetTableUnitsModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		project = projectToUse;
		assignmentIdList = assignmentIdListToUse;
		budgetModel = new BudgetTableModel(project, assignmentIdList);
	}
	
	public void setTask(Task taskToUse)
	{
		
	}
	
	public boolean isCellEditable(int row, int col) 
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
	
	public String getColumnName(int col)
	{
		return "test";
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return "";
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		
	}
    
	BudgetTableModel budgetModel;
	private Project project;
	private IdList assignmentIdList;
}
