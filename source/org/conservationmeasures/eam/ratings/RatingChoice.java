/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class RatingChoice
{
	public RatingChoice(String codeToUse, String labelToUse, Color colorToUse)
	{
		code = codeToUse;
		label = labelToUse;
		color = colorToUse;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	
	String code;
	String label;
	Color color;
}
