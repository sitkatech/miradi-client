/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

public class LinkageKey
{
	public LinkageKey(RecordKey ownerKeyToUse, String linkageTypeNameToUse)
	{
		ownerKey = ownerKeyToUse;
		linkageTypeName = linkageTypeNameToUse;
	}
	
	public String getTypeName()
	{
		return linkageTypeName;
	}

	public String toString()
	{
		return ownerKey.toString() + ":" + linkageTypeName;
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof LinkageKey))
			return false;
		
		LinkageKey other = (LinkageKey)rawOther;
		if(!ownerKey.equals(other.ownerKey))
			return false;
		return (linkageTypeName.equals(other.linkageTypeName));
	}
	
	public int hashCode()
	{
		return linkageTypeName.hashCode();
	}
	


	RecordKey ownerKey;
	String linkageTypeName;
}
