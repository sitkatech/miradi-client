/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Task;

abstract public class AssignmentTableModelSplittableShell extends AbstractTableModel
{
	public AssignmentTableModelSplittableShell(AbstractBudgetTableModel modelToUse)
	{
		model = modelToUse;
	}
	
	public void addTableModelListener(TableModelListener l)
	{
		model.addTableModelListener(l);
	}

	public void removeTableModelListener(TableModelListener l)
	{
		model.removeTableModelListener(l);
	}

	public void setTask(Task taskToUse)
	{
		model.setTask(taskToUse);
	}
	
	public void dataWasChanged()
	{
		model.dataWasChanged();
	}
	
	public boolean isCostPerUnitLabelColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isCostPerUnitLabelColumn(correctedColumnIndex);
	}
	
	public boolean isCostColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isCostColumn(correctedColumnIndex);
	}
	
	public boolean isUnitsLabelColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isUnitsLabelColumn(correctedColumnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(columnIndex);
		return model.getValueAt(rowIndex, correctedColumnIndex);
	}
	
	public Class getColumnClass(int columnIndex)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(columnIndex);
		return model.getColumnClass(correctedColumnIndex);
	}

	public String getColumnName(int column)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(column);
		return model.getColumnName(correctedColumnIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(columnIndex);
		return model.isCellEditable(rowIndex, correctedColumnIndex);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(columnIndex);
		model.setValueAt(aValue, rowIndex, correctedColumnIndex);
	}
	
	public BaseId getAssignmentForRow(int row)
	{
		return model.getAssignmentForRow(row);
	}
	
	public boolean isCostTotalsColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isCostTotalsColumn(correctedColumnIndex);
	}

	public boolean isUnitsTotalColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isUnitsTotalColumn(correctedColumnIndex);
	}

	public boolean isYearlyTotalColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isYearlyTotalColumn(correctedColumnIndex);
	}

	public boolean isUnitsColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isUnitsColumn(correctedColumnIndex);
	}

	public boolean isAccountingCodeColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isAccountingCodeColumn(correctedColumnIndex);
	}

	public boolean isFundingSourceColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isFundingSourceColumn(correctedColumnIndex);
	}

	public boolean isResourceColumn(int col)
	{
		int correctedColumnIndex = getCorrectedSplittedColumnIndex(col);
		return model.isResourceColumn(correctedColumnIndex);
	}

	public int getCorrectedRow(int row)
	{
		return row /= 2;
	}
	
	public int getRowCount()
	{
		return model.getRowCount();
	}
	
	abstract public boolean doubleRowed();
	abstract public int getColumnCount();
	abstract public int getCorrectedSplittedColumnIndex(int col);

	protected AbstractBudgetTableModel model;
}
