/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;

public class ThreatRatingBundle
{
	public ThreatRatingBundle(int threatIdToUse, int targetIdToUse, int defaultValueIdToUse)
	{
		threatId = threatIdToUse;
		targetId = targetIdToUse;
		defaultValueId = defaultValueIdToUse;
		
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
	
	int threatId;
	int targetId;
	int defaultValueId;
	HashMap map;
}
