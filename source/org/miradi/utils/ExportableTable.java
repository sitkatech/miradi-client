/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.table.TableModel;

import org.miradi.objects.BaseObject;

public class ExportableTable extends TableWithHelperMethods implements ExportableTableInterface
{
	public ExportableTable()
	{
		super();
	}

	public ExportableTable(TableModel model)
	{
		super(model);
	}

	public int getDepth(int row)
	{
		return 0;
	}

	public int getMaxDepthCount()
	{
		return 0;
	}

	public String getHeaderFor(int column)
	{
		return getColumnName(column);
	}

	public BaseObject getBaseObjectForRow(int row)
	{
		return null;
	}	
}
