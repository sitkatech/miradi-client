/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class KeyEcologicalAttributeTypeQuestion extends StaticChoiceQuestion
{
	public KeyEcologicalAttributeTypeQuestion(String tag)
	{
		super(tag, "Key Ecological Attribute Types", getKEATypeChoices());
	}
	
	static ChoiceItem[] getKEATypeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem(SIZE, "Size"),
			new ChoiceItem(CONDITION, "Condition"),
			new ChoiceItem(LANDSCAPE, "LandScape"),
		};
	}

	public static final String SIZE = "10";
	public static final String CONDITION = "20";
	public static final String LANDSCAPE = "30";

}
