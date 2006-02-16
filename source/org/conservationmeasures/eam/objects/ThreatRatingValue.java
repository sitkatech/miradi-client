/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

public class ThreatRatingValue 
{
	public ThreatRatingValue(RatingValueOption value)
	{
		rating = value;
	}
		
	public RatingValueOption getRatingOption()
	{
		return rating;
	}
	
	public int getRatingOptionId()
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

	
	public boolean isNone()
	{
		return true;
	}

	public boolean isNotUsed()
	{
		return true;
	}
	

	private RatingValueOption rating;
}
