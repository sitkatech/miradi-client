/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.text.ParseException;
import java.util.HashSet;

import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class RelevancyOverrideSet extends HashSet<RelevancyOverride>
{
	public RelevancyOverrideSet()
	{
		super();
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
	
	public String toString()
	{
		if(size() == 0)
			return "";
	
		return toJson().toString();
	}
	
	private static final String TAG_RELEVANCY_OVERRIDES = "RelevancyOverrides"; 
}
