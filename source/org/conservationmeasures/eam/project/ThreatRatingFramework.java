/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RatingValueOption;


public class ThreatRatingFramework
{
	public ThreatRatingFramework()
	{
		options =  new RatingValueOption[] {
			new RatingValueOption(PRIORITY_VERY_HIGH, PRIORITY_VERY_HIGH_STRING, Color.RED),
			new RatingValueOption(PRIORITY_HIGH, PRIORITY_HIGH_STRING, Color.ORANGE),
			new RatingValueOption(PRIORITY_MEDIUM, PRIORITY_MEDIUM_STRING, Color.YELLOW),
			new RatingValueOption(PRIORITY_LOW, PRIORITY_LOW_STRING, Color.GREEN),
			new RatingValueOption(PRIORITY_NONE, PRIORITY_NONE_STRING, Color.WHITE),
		};
	}

	public RatingValueOption[] getRatingValueOptions()
	{
		return options;
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

	private RatingValueOption[] options;
}
