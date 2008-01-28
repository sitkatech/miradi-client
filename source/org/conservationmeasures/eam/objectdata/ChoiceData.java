/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ChoiceData extends StringData
{
	public ChoiceData(String tagToUse)
	{
		super(tagToUse);
	}
	
	private ChoiceQuestion question;
}
