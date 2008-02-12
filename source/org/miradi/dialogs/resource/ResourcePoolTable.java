/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.resource;

import org.miradi.dialogs.base.ObjectPoolTable;
import org.miradi.dialogs.base.ObjectPoolTableModel;


public class ResourcePoolTable extends ObjectPoolTable
{
	public ResourcePoolTable(ObjectPoolTableModel modelToUse)
	{
		super(modelToUse);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}	
	
	public static final String UNIQUE_IDENTIFIER = "ResourcePoolTable";


}
