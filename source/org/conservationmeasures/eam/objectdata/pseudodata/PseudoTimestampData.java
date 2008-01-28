/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata.pseudodata;

import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objects.BaseObject;

public class PseudoTimestampData extends ObjectData
{
	public PseudoTimestampData(BaseObject owningObject, String tag)
	{
		super(tag);
		owner = owningObject;
	}

	public boolean isPseudoField()
	{
		return true;
	}
	
	public void set(String newValue) throws Exception
	{
		if (newValue.length()!=0)
			throw new RuntimeException("Set not allowed in a pseuod field");
	}

	public String get()
	{
		return owner.getPseudoData(getTag());
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringData))
			return false;
		
		StringData other = (StringData)rawOther;
		return get().equals(other.get());
	}

	public int hashCode()
	{
		return get().hashCode();
	}

	private BaseObject owner;
}
