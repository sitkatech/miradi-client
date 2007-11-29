/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import org.conservationmeasures.eam.objects.BaseObject;

public interface RowColumnBaseObjectProvider
{
	public BaseObject getBaseObjectForRow(int row);
}
