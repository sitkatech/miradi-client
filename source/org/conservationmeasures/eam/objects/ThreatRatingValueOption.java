/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

public class ThreatRatingValueOption
{
	public ThreatRatingValueOption(int idToUse, String labelToUse, Color colorToUse)
	{
		id = idToUse;
		label = labelToUse;
		color = colorToUse;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof ThreatRatingValueOption))
			return false;
		
		ThreatRatingValueOption other = (ThreatRatingValueOption)rawOther;
		return (other.getId() == getId());
	}
	
	int id;
	String label;
	Color color;
}
