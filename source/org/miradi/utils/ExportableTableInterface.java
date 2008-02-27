/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import org.miradi.objects.BaseObject;

public interface ExportableTableInterface
{
	public int getMaxDepthCount();
	public int getDepth(int row);
	public int getColumnCount();
	public int getRowCount();
	public Object getValueAt(int row, int column);
	public BaseObject getObjectForRow(int row);
	public String getHeaderFor(int column);
}
