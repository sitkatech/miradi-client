/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.project.threatrating;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.utils.EnhancedJsonObject;

public class ThreatRatingBundle
{
	public ThreatRatingBundle(FactorId threatIdToUse, FactorId targetIdToUse, BaseId defaultValueIdToUse)
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
		threatId = new FactorId(json.getInt(TAG_THREAT_ID));
		targetId = new FactorId(json.getInt(TAG_TARGET_ID));
		defaultValueId = new BaseId(json.getInt(TAG_DEFAULT_VALUE_ID));
		ratings = new RatingValueSet(json.getJson(TAG_VALUES));
	}
	
	public void pullDataFrom(ThreatRatingBundle otherBundle) throws Exception
	{
		pullDataFrom(otherBundle.toJson());
	}
	
	public FactorId getThreatId()
	{
		return threatId;
	}
	
	public FactorId getTargetId()
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
	
	FactorId threatId;
	FactorId targetId;
	BaseId defaultValueId;
	RatingValueSet ratings;
}
