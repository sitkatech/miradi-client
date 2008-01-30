/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class ProtectedAreaCategoryQuestion extends StaticChoiceQuestion
{
	public ProtectedAreaCategoryQuestion()
	{
		super(getStatuses());
	}

	static ChoiceItem[] getStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("Ia", "Category Ia: Strict nature reserve/wilderness protection area"),
				new ChoiceItem("Ib", "Category Ib: Wilderness area"),
				new ChoiceItem("II", "Category II: National park"),
				new ChoiceItem("III", "Category III: Natural monument"),
				new ChoiceItem("IV", "Category IV: Habitat/Species Management Area"),
				new ChoiceItem("V", "Category V: Protected Landscape/Seascape"),
				new ChoiceItem("VI", "Category VI: Managed Resource Protected Area"),
		};
	}
}
