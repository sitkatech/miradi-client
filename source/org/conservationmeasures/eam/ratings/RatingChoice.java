/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class RatingChoice
{
	public RatingChoice(int numericValueToUse, String labelToUse, Color colorToUse)
	{
		numericValue = numericValueToUse;
		label = labelToUse;
		color = colorToUse;
	}
	
	public int getNumericValue()
	{
		return numericValue;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	int numericValue;
	String label;
	Color color;
}
