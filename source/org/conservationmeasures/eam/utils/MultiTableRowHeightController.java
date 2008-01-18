/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.HashSet;

import org.conservationmeasures.eam.main.EAM;

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
		EAM.logVerbose("rowHeightChanged to " + newHeight);
		for(TableWithRowHeightManagement table : tables)
		{
			if(table.getRowHeight() != newHeight)
				table.setRowHeight(newHeight);
		}
		EAM.logVerbose("rowHeightChanged done");
	}

	HashSet<TableWithRowHeightManagement> tables;
}
