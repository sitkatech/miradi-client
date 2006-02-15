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
		rating = getRatingValueOption(priorityToUse);
	}
	
	private RatingValueOption getRatingValueOption(int ratingValueId)
	{
		switch(ratingValueId)
		{
			case PRIORITY_VERY_HIGH:
				return veryHigh;
			case PRIORITY_HIGH:
				return high;
			case PRIORITY_MEDIUM:
				return medium;
			case PRIORITY_LOW:
				return low;
			case PRIORITY_NONE:
				return none;
			case PRIORITY_NOT_USED:
				return notUsed;
			default:
				throw new RuntimeException("Unknown threat rating id: " + ratingValueId);
		}
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
		return rating.getId();
	}
	
	public String getStringValue()
	{
		return toString();
	}
	
	public String toString()
	{
		return rating.getLabel();
	}
	
	
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof ThreatPriority))
			return false;
		return ((ThreatPriority)obj).rating.equals(rating);
	}

	
	public boolean isPriorityNone()
	{
		return rating.equals(none);
	}

	public boolean isPriorityNotUsed()
	{
		return rating.equals(notUsed);
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

	private static final RatingValueOption notUsed = new RatingValueOption(PRIORITY_NOT_USED, "");
	private static final RatingValueOption none = new RatingValueOption(PRIORITY_NONE, PRIORITY_NONE_STRING);
	private static final RatingValueOption low = new RatingValueOption(PRIORITY_LOW, PRIORITY_LOW_STRING);
	private static final RatingValueOption medium = new RatingValueOption(PRIORITY_MEDIUM, PRIORITY_MEDIUM_STRING);
	private static final RatingValueOption high = new RatingValueOption(PRIORITY_HIGH, PRIORITY_HIGH_STRING);
	private static final RatingValueOption veryHigh = new RatingValueOption(PRIORITY_VERY_HIGH, PRIORITY_VERY_HIGH_STRING);
	
	private RatingValueOption rating;

}
