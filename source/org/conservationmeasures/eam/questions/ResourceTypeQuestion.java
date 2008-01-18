/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class ResourceTypeQuestion extends StaticChoiceQuestion
{
	public ResourceTypeQuestion(String tagToUse, String labelToUse)
	{
		super(tagToUse, labelToUse, getStaticChoices());
	}

	private static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Person"),
			new ChoiceItem("Group", "Group"),
			new ChoiceItem("Material", "Material"),
		};
	}

}
