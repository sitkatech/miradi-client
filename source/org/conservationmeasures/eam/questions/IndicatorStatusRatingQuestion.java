/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public class IndicatorStatusRatingQuestion extends ChoiceQuestion
{
	public IndicatorStatusRatingQuestion(String tag)
	{
		super(tag, "Status", getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Not Started", COLOR_1_OF_4),
			new ChoiceItem("2", "Problems Implementing", COLOR_2_OF_4),
			new ChoiceItem("3", "Going Well", COLOR_3_OF_4),
			new ChoiceItem("4", "Fully on Schedule", COLOR_4_OF_4),
		};
	}

}
