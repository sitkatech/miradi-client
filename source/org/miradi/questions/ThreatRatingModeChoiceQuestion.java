/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.utils.MiradiResourceImageIcon;

public class ThreatRatingModeChoiceQuestion extends StaticChoiceQuestion
{
	public ThreatRatingModeChoiceQuestion()
	{
		super(getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Simple Threat Rating Mode", new MiradiResourceImageIcon("icons/showRatings.png")),
				new ChoiceItem(STRESS_BASED_CODE, "Stress Based Threat Rating Mode", new KeyEcologicalAttributeIcon()),
		};
	}
	
	public static final String STRESS_BASED_CODE = "StressBased";
}