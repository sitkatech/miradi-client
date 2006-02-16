/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;


public class ThreatRatingFramework
{
	public ThreatRatingFramework()
	{
		options =  new ThreatRatingValueOption[] {
			new ThreatRatingValueOption(PRIORITY_VERY_HIGH, PRIORITY_VERY_HIGH_STRING, Color.RED),
			new ThreatRatingValueOption(PRIORITY_HIGH, PRIORITY_HIGH_STRING, Color.ORANGE),
			new ThreatRatingValueOption(PRIORITY_MEDIUM, PRIORITY_MEDIUM_STRING, Color.YELLOW),
			new ThreatRatingValueOption(PRIORITY_LOW, PRIORITY_LOW_STRING, Color.GREEN),
			new ThreatRatingValueOption(PRIORITY_NONE, PRIORITY_NONE_STRING, Color.WHITE),
		};
		
		criteria = new ThreatRatingCriterion[] {
			new ThreatRatingCriterion(0, "Scope"), 
			new ThreatRatingCriterion(1, "Severity"),
			new ThreatRatingCriterion(2, "Urgency"),
			new ThreatRatingCriterion(3, "Custom"),
		};
			
	}

	public ThreatRatingValueOption[] getRatingValueOptions()
	{
		return options;
	}
	
	public ThreatRatingCriterion[] getRatingCriteria()
	{
		return criteria;
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

	private ThreatRatingValueOption[] options;
	private ThreatRatingCriterion[] criteria;
}
