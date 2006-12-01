/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;


public class ChoiceQuestion
{
	public ChoiceQuestion(String tagToUse, String labelToUse, ChoiceItem[] choicesToUse)
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
	
	public ChoiceItem[] getChoices()
	{
		return choices;
	}
	
	public ChoiceItem findChoiceByCode(String code)
	{
		for(int i = 0; i < choices.length; ++i)
			if(choices[i].getCode().equals(code))
				return choices[i];
		
		return null;
	}
	
	public static final Color COLOR_1_OF_4 = Color.RED;
	public static final Color COLOR_2_OF_4 = Color.ORANGE;
	public static final Color COLOR_3_OF_4 = Color.YELLOW;
	public static final Color COLOR_4_OF_4 = Color.GREEN;
	
	String tag;
	String label;
	ChoiceItem[] choices;
}
