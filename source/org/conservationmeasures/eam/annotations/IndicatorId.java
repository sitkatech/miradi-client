/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.main.EAM;

public class IndicatorId 
{
	public IndicatorId()
	{
		indicator = INDICATOR_NONE;
	}
	
	public IndicatorId(int value)
	{
		indicator = value;
	}
	
	public String toString()
	{
		if(hasIndicator())
			return String.valueOf(indicator).toString();
		return INDICATOR_NONE_STRING;
	}
		
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof IndicatorId))
			return false;
		return ((IndicatorId)obj).indicator == indicator;
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
	
	public static final int INDICATOR_NONE = -1;
	public static final String INDICATOR_NONE_STRING = EAM.text("None");
	int indicator;
}
