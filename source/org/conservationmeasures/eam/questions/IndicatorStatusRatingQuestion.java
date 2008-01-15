/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

//FIXME remove after TAG_STATUS is removed
public class IndicatorStatusRatingQuestion extends StaticChoiceQuestion
{
	public IndicatorStatusRatingQuestion(String tag)
	{
		super(tag, "Status", getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Not Started", COLOR_ALERT),
			new ChoiceItem("2", "Problems Implementing", COLOR_CAUTION),
			new ChoiceItem("3", "Going Well", COLOR_OK),
			new ChoiceItem("4", "Fully on Schedule", COLOR_GREAT),
		};
	}

}
