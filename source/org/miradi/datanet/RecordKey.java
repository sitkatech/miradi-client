/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

public class RecordKey
{
	public RecordKey(String typeNameToUse, int idToUse)
	{
		typeName = typeNameToUse;
		id = idToUse;
	}
	
	public String toString()
	{
		return typeName + ":" + id;
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof RecordKey))
			return false;
		
		RecordKey other = (RecordKey)rawOther;
		if(id != other.id)
			return false;
		return (typeName.equals(other.typeName));
	}
	
	public int hashCode()
	{
		return id;
	}
	
	private String typeName;
	private int id;
}
