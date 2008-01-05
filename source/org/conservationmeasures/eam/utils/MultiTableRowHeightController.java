/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.HashSet;

import javax.swing.JTable;

public class MultiTableRowHeightController implements RowHeightListener
{
	public MultiTableRowHeightController()
	{
		tables = new HashSet<JTable>();
	}
	
	public void addTable(TableWithColumnWidthSaver tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.addRowHeightListener(this);
		rowHeightChanged(tableToAdd.getRowHeight());
	}
	
	public void rowHeightChanged(int newHeight)
	{
		for(JTable table : tables)
		{
			if(table.getRowHeight() != newHeight)
				table.setRowHeight(newHeight);
		}
	}

	HashSet<JTable> tables;
}
