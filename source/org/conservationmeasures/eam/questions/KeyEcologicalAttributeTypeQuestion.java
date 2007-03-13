/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class KeyEcologicalAttributeTypeQuestion extends ChoiceQuestion
{
	public KeyEcologicalAttributeTypeQuestion(String tag)
	{
		super(tag, "Key Ecological Attribute Types", getKEATypeChoices());
	}
	
	static ChoiceItem[] getKEATypeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("1", "Size"),
			new ChoiceItem("2", "Condition"),
			new ChoiceItem("3", "LandScape"),
		};
	}

}
