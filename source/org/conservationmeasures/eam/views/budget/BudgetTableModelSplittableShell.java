/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

public class BudgetTableModelSplittableShell extends AbstractTableModel
{
	public BudgetTableModelSplittableShell(BudgetTableUnitsModel modelToUse)
	{
		model = modelToUse;
	}
	
	public int getColumnCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return model.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return model.getValueAt(rowIndex, columnIndex);
	}
	
	public Class getColumnClass(int columnIndex)
	{
		return model.getColumnClass(columnIndex);
	}

	public String getColumnName(int column)
	{
		return model.getColumnName(column);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return model.isCellEditable(rowIndex, columnIndex);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		model.setValueAt(aValue, rowIndex, columnIndex);
	}

	protected BudgetTableUnitsModel model;
}
