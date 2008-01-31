/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

public interface ExportableTableInterface
{
	public int getMaxDepthCount();
	public int getDepth(int column);
	public String getValue(int row, int column);
}
