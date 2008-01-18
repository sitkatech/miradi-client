/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;

public class TeamPoolTable extends ObjectPoolTable
{
	public TeamPoolTable(ObjectPoolTableModel modelToUse)
	{
		super(modelToUse);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}	
	
	public static final String UNIQUE_IDENTIFIER = "TeamPoolTable";
}
