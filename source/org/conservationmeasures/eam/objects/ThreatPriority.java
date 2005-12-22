/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.main.EAM;

public class ThreatPriority 
{
	private ThreatPriority(int priorityToUse)
	{
		priority = priorityToUse;
	}

	public static ThreatPriority createFromInt(int value)
	{
		return new ThreatPriority(value);
	}

	public static ThreatPriority createPriorityNotUsed()
	{
		return new ThreatPriority(PRIORITY_NOT_USED);
	}

	public static ThreatPriority createPriorityNone()
	{
		return new ThreatPriority(PRIORITY_NONE);
	}

	public static ThreatPriority createPriorityLow()
	{
		return new ThreatPriority(PRIORITY_LOW);
	}

	public static ThreatPriority createPriorityMedium()
	{
		return new ThreatPriority(PRIORITY_MEDIUM);
	}

	public static ThreatPriority createPriorityHigh()
	{
		return new ThreatPriority(PRIORITY_HIGH);
	}

	public static ThreatPriority createPriorityVeryHigh()
	{
		return new ThreatPriority(PRIORITY_VERY_HIGH);
	}
	
	public int getValue()
	{
		return priority;
	}
	
	public String getStringValue()
	{
		return toString();
	}
	
	public String toString()
	{
		switch(priority)
		{
			case PRIORITY_VERY_HIGH:
				return PRIORITY_VERY_HIGH_STRING;
			case PRIORITY_HIGH:
				return PRIORITY_HIGH_STRING;
			case PRIORITY_MEDIUM:
				return PRIORITY_MEDIUM_STRING;
			case PRIORITY_LOW:
				return PRIORITY_LOW_STRING;
			case PRIORITY_NONE:
				return PRIORITY_NONE_STRING;
			default:
				return "";
		}
	}
	
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof ThreatPriority))
			return false;
		return ((ThreatPriority)obj).priority == priority;
	}

	
	public boolean isPriorityNone()
	{
		return priority == PRIORITY_NONE;
	}

	public boolean isPriorityNotUsed()
	{
		return priority == PRIORITY_NOT_USED;
	}

	public static final int PRIORITY_NOT_USED =-1;
	public static final int PRIORITY_VERY_HIGH =0;
	public static final int PRIORITY_HIGH =1;
	public static final int PRIORITY_MEDIUM =2;
	public static final int PRIORITY_LOW =3;
	public static final int PRIORITY_NONE =4;
	
	private static final String PRIORITY_VERY_HIGH_STRING = EAM.text("Label|Very High");
	private static final String PRIORITY_HIGH_STRING = EAM.text("Label|High");
	private static final String PRIORITY_MEDIUM_STRING = EAM.text("Label|Medium");
	private static final String PRIORITY_LOW_STRING = EAM.text("Label|Low");
	private static final String PRIORITY_NONE_STRING = EAM.text("Label|None");

	private int priority;

}
