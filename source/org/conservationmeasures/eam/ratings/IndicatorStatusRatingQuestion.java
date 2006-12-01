/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

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
			new ChoiceItem("1", "Not Started", Color.RED),
			new ChoiceItem("2", "Problems Implementing", Color.ORANGE),
			new ChoiceItem("3", "Going Well", Color.YELLOW),
			new ChoiceItem("4", "Fully on Schedule", Color.GREEN),
		};
	}

}
