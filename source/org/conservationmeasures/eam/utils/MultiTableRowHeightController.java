/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.HashSet;

public class MultiTableRowHeightController implements RowHeightListener
{
	public MultiTableRowHeightController()
	{
		tables = new HashSet<TableWithRowHeightManagement>();
	}
	
	public void addTable(TableWithRowHeightManagement tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.addRowHeightListener(this);
		rowHeightChanged(tableToAdd.getRowHeight());
	}
	
	public void rowHeightChanged(int newHeight)
	{
		for(TableWithRowHeightManagement table : tables)
		{
			if(table.getRowHeight() != newHeight)
				table.setRowHeight(newHeight);
		}
	}

	HashSet<TableWithRowHeightManagement> tables;
}
