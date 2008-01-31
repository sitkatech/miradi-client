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

//	FIXME implement funcionality
	public int getDepth(int column)
	{
		return 0;
	}

	public int getMaxDepthCount()
	{
		return 0;
	}

	public String getValue(int row, int column)
	{
		return null;
	}
}
