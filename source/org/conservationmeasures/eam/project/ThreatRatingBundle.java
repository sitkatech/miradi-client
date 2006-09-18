/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.json.JSONObject;

public class ThreatRatingBundle
{
	public ThreatRatingBundle(ModelNodeId threatIdToUse, ModelNodeId targetIdToUse, BaseId defaultValueIdToUse)
	{
		this();
		
		threatId = threatIdToUse;
		targetId = targetIdToUse;
		defaultValueId = defaultValueIdToUse;		
	}
	
	public ThreatRatingBundle(ThreatRatingBundle otherBundle) throws Exception
	{
		this();
		
		pullDataFrom(otherBundle);
	}
	
	public ThreatRatingBundle(JSONObject json)
	{
		this();
		
		pullDataFrom(json);
	}

	private void pullDataFrom(JSONObject json)
	{
		threatId = new ModelNodeId(json.getInt(TAG_THREAT_ID));
		targetId = new ModelNodeId(json.getInt(TAG_TARGET_ID));
		defaultValueId = new BaseId(json.getInt(TAG_DEFAULT_VALUE_ID));
		
		JSONObject values = json.getJSONObject(TAG_VALUES);
		Iterator iter = values.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			BaseId criterionId = new BaseId(Integer.parseInt(key));
			BaseId valueId = new BaseId(values.getInt(key));
			setValueId(criterionId, valueId);
		}
	}
	
	public void pullDataFrom(ThreatRatingBundle otherBundle) throws Exception
	{
		pullDataFrom(otherBundle.toJson());
	}
	
	ThreatRatingBundle()
	{
		map = new HashMap();
	}
	
	public ModelNodeId getThreatId()
	{
		return threatId;
	}
	
	public ModelNodeId getTargetId()
	{
		return targetId;
	}
	
	public BaseId getDefaultValueId()
	{
		return defaultValueId;
	}
	
	public BaseId getValueId(BaseId criterionId)
	{
		Object valueObject = map.get(criterionId);
		if(valueObject == null)
			return defaultValueId;
		return (BaseId)valueObject;
	}
	
	public void setValueId(BaseId criterionId, BaseId valueId)
	{
		map.put(criterionId, valueId);
	}
	
	public JSONObject toJson() throws Exception
	{
		JSONObject json = new JSONObject();
		json.put(TAG_THREAT_ID, getThreatId().asInt());
		json.put(TAG_TARGET_ID, getTargetId().asInt());
		json.put(TAG_DEFAULT_VALUE_ID, defaultValueId.asInt());
		
		JSONObject values = new JSONObject();
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext())
		{
			BaseId criterion = (BaseId)iter.next();
			values.put(criterion.toString(), getValueId(criterion).asInt());
		}
		json.put(TAG_VALUES, values);

		return json;
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof ThreatRatingBundle))
			return false;
		
		ThreatRatingBundle other = (ThreatRatingBundle)rawOther;
		if(!threatId.equals(other.threatId))
			return false;
		
		if(!targetId.equals(other.targetId))
			return false;
		
		return true;
	}

	public int hashCode()
	{
		return threatId.hashCode();
	}
	
	
	private static final String TAG_THREAT_ID = "ThreatId";
	private static final String TAG_TARGET_ID = "TargetId";
	private static final String TAG_DEFAULT_VALUE_ID = "DefaultValueId";
	private static final String TAG_VALUES = "Values";
	
	ModelNodeId threatId;
	ModelNodeId targetId;
	BaseId defaultValueId;
	HashMap map;
}
