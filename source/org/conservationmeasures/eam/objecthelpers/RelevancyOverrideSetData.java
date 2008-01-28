/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.objectdata.ObjectData;

public class RelevancyOverrideSetData extends ObjectData
{
	public RelevancyOverrideSetData(String tagToUse)
	{
		super(tagToUse);
		relevancyOverrideSet = new RelevancyOverrideSet();
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof RelevancyOverrideSetData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	public RelevancyOverrideSet getRawRelevancyOverrideSet()
	{
		return relevancyOverrideSet;
	}
	
	public String get()
	{
		return relevancyOverrideSet.toString();
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	public void set(String newValue) throws Exception
	{
		set(new RelevancyOverrideSet(newValue));	
	}
	
	private void set(RelevancyOverrideSet relevancyOverrideSetToUse)
	{
		relevancyOverrideSet = relevancyOverrideSetToUse;
	}
	
	private RelevancyOverrideSet relevancyOverrideSet;
}
