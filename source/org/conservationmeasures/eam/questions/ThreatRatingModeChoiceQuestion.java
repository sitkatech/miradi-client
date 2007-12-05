/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
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

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Simple Threat Rating Mode", Color.WHITE),
				new ChoiceItem("1", "Stress Based Threat Rating Mode", Color.WHITE),
		};
	}
}