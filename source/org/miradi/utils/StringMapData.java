/* 
 * Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
 * (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */
package org.miradi.utils;

import org.martus.util.UnicodeWriter;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.StringMap;

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

	@Override
	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		data.toXml(out);
		endTagToXml(out);
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
