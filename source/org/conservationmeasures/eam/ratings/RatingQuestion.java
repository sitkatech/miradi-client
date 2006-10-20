/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

public class RatingQuestion
{
	public RatingQuestion(String tagToUse, String labelToUse, RatingChoice[] choicesToUse)
	{
		tag = tagToUse;
		label = labelToUse;
		choices = choicesToUse;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public RatingChoice[] getChoices()
	{
		return choices;
	}
	
	String tag;
	String label;
	RatingChoice[] choices;
}
