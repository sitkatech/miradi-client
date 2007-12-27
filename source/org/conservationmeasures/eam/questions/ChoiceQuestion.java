/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public abstract class ChoiceQuestion
{
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
	
	public int size()
	{
		return getChoices().length;
	}
	
	public String getCode(int index)
	{
		return getChoices()[index].getCode();
	}
	
	abstract public ChoiceItem[] getChoices();
	
	public ChoiceItem findChoiceByCode(String code)
	{
		int index = findIndexByCode(code);
		if (index<0)
			return null;
		return getChoices()[index];
	}
	
	public ChoiceItem findChoiceByLabel(String labelToFind)
	{
		int index = findIndexByLabel(labelToFind);
		if (index < 0)
			return null;
		
		return getChoices()[index];
	}
	
	public int findIndexByCode(String code)
	{
		ChoiceItem[] choices = getChoices();
		for(int i = 0; i < choices.length; ++i)
		{
			if(choices[i].getCode().equals(code))
				return i;
		}
		
		return -1;
	}
	
	public int findIndexByLabel(String labelToFind)
	{
		ChoiceItem[] choices = getChoices();
		for(int i = 0; i < choices.length; ++i)
		{
			if(choices[i].getLabel().equals(labelToFind))
				return i;
		}
		
		return -1;
	}
	
	public static final Color DARK_YELLOW = new Color(255, 230, 0);
	public static final Color LIGHT_GREEN = new Color(128, 255, 0); 
	public static final Color DARK_GREEN = new Color(0, 160, 0);
		
	public static final Color COLOR_ALERT = Color.RED;
	public static final Color COLOR_CAUTION = DARK_YELLOW;
	public static final Color COLOR_OK = LIGHT_GREEN;
	public static final Color COLOR_GREAT = DARK_GREEN;
	
	private String tag;
	private String label;
}
