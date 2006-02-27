/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class ThreatRatingBundle
{
	public ThreatRatingBundle(int threatIdToUse, int targetIdToUse, int defaultValueIdToUse)
	{
		this();
		
		threatId = threatIdToUse;
		targetId = targetIdToUse;
		defaultValueId = defaultValueIdToUse;		
	}
	
	public ThreatRatingBundle(JSONObject json)
	{
		this();
		
		threatId = json.getInt(TAG_THREAT_ID);
		targetId = json.getInt(TAG_TARGET_ID);
		defaultValueId = json.getInt(TAG_DEFAULT_VALUE_ID);
		
		JSONObject values = json.getJSONObject(TAG_VALUES);
		Iterator iter = values.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			int criterionId = Integer.parseInt(key);
			int valueId = values.getInt(key);
			setValueId(criterionId, valueId);
		}
	}
	
	ThreatRatingBundle()
	{
		map = new HashMap();
	}
	
	public int getThreatId()
	{
		return threatId;
	}
	
	public int getTargetId()
	{
		return targetId;
	}
	
	public int getValueId(int criterionId)
	{
		Object valueObject = map.get(new Integer(criterionId));
		if(valueObject == null)
			return defaultValueId;
		return ((Integer)valueObject).intValue();
	}
	
	public void setValueId(int criterionId, int valueId)
	{
		map.put(new Integer(criterionId), new Integer(valueId));
	}
	
	public JSONObject toJson() throws Exception
	{
		JSONObject json = new JSONObject();
		json.put(TAG_THREAT_ID, getThreatId());
		json.put(TAG_TARGET_ID, getTargetId());
		json.put(TAG_DEFAULT_VALUE_ID, defaultValueId);
		
		JSONObject values = new JSONObject();
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext())
		{
			Integer criterion = (Integer)iter.next();
			values.put(criterion.toString(), getValueId(criterion.intValue()));
		}
		json.put(TAG_VALUES, values);

		return json;
	}
	
	private static final String TAG_THREAT_ID = "ThreatId";
	private static final String TAG_TARGET_ID = "TargetId";
	private static final String TAG_DEFAULT_VALUE_ID = "DefaultValueId";
	private static final String TAG_VALUES = "Values";
	
	int threatId;
	int targetId;
	int defaultValueId;
	HashMap map;
}
