/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.AbstractTableModel;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewBudgetAnnualTotalTableModel extends AbstractTableModel
{
	public PlanningViewBudgetAnnualTotalTableModel(TreeTableModelAdapter adapterToUse)
	{
		adapter = adapterToUse;
	}
	
	public int getColumnCount()
	{
		return 1;
	}
	
	public int getRowCount()
	{
		return adapter.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		//FIXME planning - add real data
		return "herE";
	}
	
	private TreeTableModelAdapter adapter;
}
