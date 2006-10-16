/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.ids.BaseId;

public class RatingValue 
{
	public RatingValue(ValueOption value)
	{
		rating = value;
	}
		
	public ValueOption getRatingOption()
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
		if(!(obj instanceof RatingValue))
			return false;
		return ((RatingValue)obj).rating.equals(rating);
	}

	
	private ValueOption rating;
}
