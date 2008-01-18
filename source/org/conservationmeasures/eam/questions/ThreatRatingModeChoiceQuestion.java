/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class ThreatRatingModeChoiceQuestion extends StaticChoiceQuestion
{
	public ThreatRatingModeChoiceQuestion(String tagToUse)
	{
		super(tagToUse, "Threat Rating Mode", getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Simple Threat Rating Mode", Color.WHITE),
				new ChoiceItem(STRESS_BASED_CODE, "Stress Based Threat Rating Mode", Color.WHITE),
		};
	}
	
	public static final String STRESS_BASED_CODE = "StressBased";
}