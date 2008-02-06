/* 
 * Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
 * (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objecthelpers.StringMap;

public class StringMapData extends ObjectData
{
	public StringMapData(String tagToUse)
	{
		super(tagToUse);
		data = new StringMap();
	}

	public String get()
	{
		return getStringMap().toString();
	}
	
	public StringMap getStringMap()
	{
		return data;
	}

	public void set(String newValue) throws Exception
	{
		data = new StringMap(newValue);
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringMapData))
			return false;

		StringMapData other = (StringMapData) rawOther;
		return other.data.equals(data);
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	private StringMap data;
}
