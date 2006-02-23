/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;

import org.conservationmeasures.eam.project.IdAssigner;

public class ThreatRatingBundle
{
	public ThreatRatingBundle(int threatIdToUse, int targetIdToUse)
	{
		threatId = threatIdToUse;
		targetId = targetIdToUse;
		
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
			return IdAssigner.INVALID_ID;
		return ((Integer)valueObject).intValue();
	}
	
	public void setValueId(int criterionId, int valueId)
	{
		map.put(new Integer(criterionId), new Integer(valueId));
	}
	
	int threatId;
	int targetId;
	HashMap map;
}
