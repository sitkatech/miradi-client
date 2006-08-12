/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.ids.BaseId;

public class ThreatRatingValue 
{
	public ThreatRatingValue(ThreatRatingValueOption value)
	{
		rating = value;
	}
		
	public ThreatRatingValueOption getRatingOption()
	{
		return rating;
	}
	
	public BaseId getRatingOptionId()
	{
		return rating.getId();
	}
	
	public String getStringValue()
	{
		return rating.getLabel();
	}
	
	public Color getColor()
	{
		return rating.getColor();
	}
	
	public String toString()
	{
		return getStringValue();
	}
	
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof ThreatRatingValue))
			return false;
		return ((ThreatRatingValue)obj).rating.equals(rating);
	}

	
	private ThreatRatingValueOption rating;
}
