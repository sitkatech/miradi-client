/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;

public class IndicatorPool extends EAMObjectPool
{
	public IndicatorPool()
	{
		super(ObjectType.INDICATOR);
	}
	
	public void put(Indicator indicator)
	{
		put(indicator.getId(), indicator);
	}
	
	public Indicator find(BaseId id)
	{
		return (Indicator)getRawObject(id);
	}



}
