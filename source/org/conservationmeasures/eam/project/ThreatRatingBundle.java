/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
		this(otherBundle.toJson());
	}
	
	public ThreatRatingBundle(EnhancedJsonObject json)
	{
		this();
		
		pullDataFrom(json);
	}

	protected ThreatRatingBundle()
	{
		ratings = new RatingValueSet();
	}
	
	private void pullDataFrom(EnhancedJsonObject json)
	{
		threatId = new ModelNodeId(json.getInt(TAG_THREAT_ID));
		targetId = new ModelNodeId(json.getInt(TAG_TARGET_ID));
		defaultValueId = new BaseId(json.getInt(TAG_DEFAULT_VALUE_ID));
		ratings = new RatingValueSet(json.getJson(TAG_VALUES));
	}
	
	public void pullDataFrom(ThreatRatingBundle otherBundle) throws Exception
	{
		pullDataFrom(otherBundle.toJson());
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
		return ratings.getValueId(criterionId, defaultValueId);
	}
	
	public void setValueId(BaseId criterionId, BaseId valueId)
	{
		ratings.setValueId(criterionId, valueId);
	}
	
	public EnhancedJsonObject toJson() throws Exception
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_THREAT_ID, getThreatId().asInt());
		json.put(TAG_TARGET_ID, getTargetId().asInt());
		json.put(TAG_DEFAULT_VALUE_ID, defaultValueId.asInt());
		json.put(TAG_VALUES, ratings.toJson());

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
	RatingValueSet ratings;
}
