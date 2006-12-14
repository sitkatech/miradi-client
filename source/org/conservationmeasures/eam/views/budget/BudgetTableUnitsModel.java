/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class BudgetTableUnitsModel extends BudgetTableModel
{
	public BudgetTableUnitsModel(Project projectToUse, IdList assignmentIdListToUse) throws Exception
	{
		super(projectToUse, assignmentIdListToUse);
		project = projectToUse;
		assignmentIdList = assignmentIdListToUse;
		budgetModel = new BudgetTableModel(project, assignmentIdList);
	}
	
	public void setTask(Task taskToUse)
	{
		budgetModel.setTask(taskToUse);
	}
	
	public boolean isCellEditable(int row, int col) 
	{
		return true;
	}
		
	public int getColumnCount()
	{
		return 10;
	}

	public int getRowCount()
	{
		return 10;
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
    
	private Project project;
	private BudgetTableModel budgetModel;
	private IdList assignmentIdList;
}
