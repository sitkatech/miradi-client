/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

public class Indicator 
{
	public Indicator()
	{
		indicator = INDICATOR_NONE;
	}
	
	public String toString()
	{
		if(hasIndicator())
			return String.valueOf(indicator).toString();
		return "";
	}
		
	
	public boolean hasIndicator()
	{
		return indicator != INDICATOR_NONE;
	}

	public int getValue()
	{
		return indicator;
	}
	
	public void setValue(int value)
	{
		indicator = value;
	}
	
	public Color getColor()
	{
		return LIGHT_PURPLE;
	}
	
	public static final int INDICATOR_NONE = -1;
	private static final Color LIGHT_PURPLE = Color.getHSBColor((float)0.70,(float)0.40,(float)0.95);
	int indicator;
}
