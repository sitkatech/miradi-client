/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class TargetStatusQuestion extends ChoiceQuestion
{
	public TargetStatusQuestion(String tagToUse)
	{
		super(tagToUse, "Target Status", getTargetStatuses());
	}

	static ChoiceItem[] getTargetStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem("1", "Poor", COLOR_1_OF_4),
				new ChoiceItem("2", "Fair", COLOR_2_OF_4),
				new ChoiceItem("3", "Good", COLOR_3_OF_4),
				new ChoiceItem("4", "Very Good", COLOR_4_OF_4),
		};
	}
}