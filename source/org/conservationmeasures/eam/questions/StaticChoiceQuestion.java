/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

public class StaticChoiceQuestion extends ChoiceQuestion
{
	public StaticChoiceQuestion(String tagToUse, String labelToUse, ChoiceItem[] choicesToUse)
	{
		super(tagToUse, labelToUse);
		choices = choicesToUse;
	}
	
	public StaticChoiceQuestion(String tagToUse, String labelToUse, Vector<ChoiceItem> choicesToUse)
	{
		this(tagToUse, labelToUse, choicesToUse.toArray(new ChoiceItem[0]));
	}

	public ChoiceItem[] getChoices()
	{
		return choices;
	}
	
	ChoiceItem[] choices;

}
