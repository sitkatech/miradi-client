/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;

import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class RelevancyOverrideSet extends HashSet<RelevancyOverride>
{
	public RelevancyOverrideSet()
	{
		super();
	}
	
	public RelevancyOverrideSet(RelevancyOverrideSet overrideSet)
	{
		super(overrideSet);
	}
	
	public RelevancyOverrideSet(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}

	public RelevancyOverrideSet(EnhancedJsonObject json)
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_RELEVANCY_OVERRIDES);
		for(int i = 0; i < array.length(); ++i)
		{
			add(new RelevancyOverride(array.getJson(i)));
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonArray array = new EnhancedJsonArray();
		RelevancyOverride[] relevancyOverrides = toArray(new RelevancyOverride[0]);
		for (int i = 0; i < relevancyOverrides.length; ++i)
		{
			array.put(relevancyOverrides[i].toJson());
		}
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_RELEVANCY_OVERRIDES, array);
		
		return json;
	}
	
	public RelevancyOverride find(ORef ref)
	{
		for (Iterator<RelevancyOverride> iter = iterator(); iter.hasNext();)
		{
			RelevancyOverride override = iter.next();
			if (override.getRef().equals(ref))
				return override;
		}
		
		return null;
	}
	
	public void remove(ORef ref)
	{
		RelevancyOverride relevancyOverride = find(ref);
		remove(relevancyOverride);
	}
	
	public String toString()
	{
		if(size() == 0)
			return "";
	
		return toJson().toString();
	}
	
	private static final String TAG_RELEVANCY_OVERRIDES = "RelevancyOverrides"; 
}
