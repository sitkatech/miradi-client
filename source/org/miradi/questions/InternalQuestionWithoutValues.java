/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

public class InternalQuestionWithoutValues extends DynamicChoiceQuestion
{
	public ChoiceItem[] getChoices()
	{
		return new ChoiceItem[0];
	}
	
	public String getValue(String code)
	{
		return code;
	}
}
