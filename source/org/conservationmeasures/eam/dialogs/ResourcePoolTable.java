/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;


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
