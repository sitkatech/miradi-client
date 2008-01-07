/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		EnhancedJsonArray array = json.optJsonArray(TAG_RELEVANCY_OVERRIDE_SET_REFERENCES);
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
		json.put(TAG_RELEVANCY_OVERRIDE_SET_REFERENCES, array);
		
		return json;
	}
	
	public String toString()
	{
		if(size() == 0)
			return "";
	
		return toJson().toString();
	}
	
	private static final String TAG_RELEVANCY_OVERRIDE_SET_REFERENCES = "RelevancyOverrideReferences"; 
}
