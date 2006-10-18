/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.project.RatingValueSet;

public class RatingData extends ObjectData
{
	public RatingData()
	{
		ratings = new RatingValueSet();
	}
	
	public String get()
	{
		return ratings.toString();
	}

	public void set(String newValue) throws Exception
	{
		ratings.fillFrom(newValue);
	}

	RatingValueSet ratings;
}
