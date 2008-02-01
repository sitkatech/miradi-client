/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class ExportableTreeTable extends JTreeTable implements ExportableTableInterface
{
	public ExportableTreeTable(TreeTableModel treeTableModel)
	{
		super(treeTableModel);
	}

	public int getDepth(int row)
	{
		return getTree().getPathForRow(row).getPath().length - ROOT_PLUS_TOPLEVEL_ADJUSTMENT;
	}

	public int getMaxDepthCount()
	{
		int maxRowDepth = 0;
		int rowCount = getTree().getRowCount();
		for (int row = 0; row < rowCount; ++row)
		{
			int rowDepth = getTree().getPathForRow(row).getPath().length - ROOT_PLUS_TOPLEVEL_ADJUSTMENT;
			maxRowDepth = Math.max(maxRowDepth, rowDepth);
		}
		
		return maxRowDepth;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		if (column == 0)
			return getTree().getPathForRow(row).getLastPathComponent().toString();
		
		return super.getValueAt(row, column);
	}
	
	public String getValueFor(int row, int column)
	{
		Object value = getValueAt(row, column);
		if (value == null)
			return "";
		
		return value.toString();
	}
	
	public String getHeaderFor(int column)
	{
		return getColumnName(column);
	}
	
	private static final int ROOT_PLUS_TOPLEVEL_ADJUSTMENT = 2;
}
