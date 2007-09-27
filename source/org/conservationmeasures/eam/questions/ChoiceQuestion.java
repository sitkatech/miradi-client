/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public class ChoiceQuestion
{
	public ChoiceQuestion(String tagToUse, String labelToUse, ChoiceItem[] choicesToUse)
	{
		this(tagToUse, labelToUse);
		choices = choicesToUse;
	}
	
	public ChoiceQuestion(String tagToUse, String labelToUse)
	{
		tag = tagToUse;
		label = labelToUse;
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
		int index = findIndexByCode(code);
		if (index<0)
			return null;
		return choices[index];
	}
	
	public int findIndexByCode(String code)
	{
		for(int i = 0; i < choices.length; ++i)
			if(choices[i].getCode().equals(code))
				return i;
		return -1;
	}
	
	public static final Color DARK_YELLOW = new Color(255, 230, 0);
	public static final Color LIGHT_GREEN = new Color(128, 255, 0); 
	public static final Color DARK_GREEN = new Color(0, 160, 0);
		
	public static final Color COLOR_1_OF_4 = Color.RED;
	public static final Color COLOR_2_OF_4 = DARK_YELLOW;
	public static final Color COLOR_3_OF_4 = LIGHT_GREEN;
	public static final Color COLOR_4_OF_4 = DARK_GREEN;
	
	String tag;
	String label;
	ChoiceItem[] choices;
}
