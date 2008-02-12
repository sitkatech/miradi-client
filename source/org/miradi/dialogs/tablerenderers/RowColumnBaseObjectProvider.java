/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.tablerenderers;

import org.miradi.objects.BaseObject;

public interface RowColumnBaseObjectProvider
{
	public BaseObject getBaseObjectForRowColumn(int row, int column);
}
