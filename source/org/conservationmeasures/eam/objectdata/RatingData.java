/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.RatingValueSet;

public class RatingData extends ObjectData
{
	public RatingData()
	{
		ratings = new RatingValueSet();
	}
	
	public RatingData(String newValue) throws Exception
	{
		this();
		set(newValue);
	}
	
	public String get()
	{
		return ratings.toString();
	}

	public void set(String newValue) throws Exception
	{
		ratings.fillFrom(newValue);
	}

	public void setValueId(BaseId criterionId, BaseId valueId)
	{
		ratings.setValueId(criterionId, valueId);
	}
	
	public BaseId getValueId(BaseId criterionId, BaseId defaultValueId)
	{
		return ratings.getValueId(criterionId, defaultValueId);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof RatingData))
			return false;
		
		RatingData other = (RatingData)rawOther;
		return ratings.equals(other.ratings);
	}

	public int hashCode()
	{
		return ratings.hashCode();
	}


	RatingValueSet ratings;
}
