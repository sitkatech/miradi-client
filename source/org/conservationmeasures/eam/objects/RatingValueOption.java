/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

public class RatingValueOption
{
	public RatingValueOption(int idToUse, String labelToUse, Color colorToUse)
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
	
	int id;
	String label;
	Color color;
}
