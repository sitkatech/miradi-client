/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;

public class ThreatRatingValue 
{
	private ThreatRatingValue(int ratingOptionId)
	{
		rating = getRatingValueOption(ratingOptionId);
	}
	
	private RatingValueOption getRatingValueOption(int ratingOptionId)
	{
		switch(ratingOptionId)
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
				throw new RuntimeException("Unknown threat rating id: " + ratingOptionId);
		}
	}

	public static ThreatRatingValue createFromInt(int ratingOptionId)
	{
		return new ThreatRatingValue(ratingOptionId);
	}

	public static ThreatRatingValue createNotUsed()
	{
		return new ThreatRatingValue(PRIORITY_NOT_USED);
	}

	public static ThreatRatingValue createNone()
	{
		return new ThreatRatingValue(PRIORITY_NONE);
	}

	public static ThreatRatingValue createLow()
	{
		return new ThreatRatingValue(PRIORITY_LOW);
	}

	public static ThreatRatingValue createMedium()
	{
		return new ThreatRatingValue(PRIORITY_MEDIUM);
	}

	public static ThreatRatingValue createHigh()
	{
		return new ThreatRatingValue(PRIORITY_HIGH);
	}

	public static ThreatRatingValue createVeryHigh()
	{
		return new ThreatRatingValue(PRIORITY_VERY_HIGH);
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
		return rating.equals(none);
	}

	public boolean isNotUsed()
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

	private static final RatingValueOption notUsed = new RatingValueOption(PRIORITY_NOT_USED, "", Color.BLACK);
	private static final RatingValueOption none = new RatingValueOption(PRIORITY_NONE, PRIORITY_NONE_STRING, Color.WHITE);
	private static final RatingValueOption low = new RatingValueOption(PRIORITY_LOW, PRIORITY_LOW_STRING, Color.GREEN);
	private static final RatingValueOption medium = new RatingValueOption(PRIORITY_MEDIUM, PRIORITY_MEDIUM_STRING, Color.YELLOW);
	private static final RatingValueOption high = new RatingValueOption(PRIORITY_HIGH, PRIORITY_HIGH_STRING, Color.ORANGE);
	private static final RatingValueOption veryHigh = new RatingValueOption(PRIORITY_VERY_HIGH, PRIORITY_VERY_HIGH_STRING, Color.RED);
	
	private RatingValueOption rating;

}
