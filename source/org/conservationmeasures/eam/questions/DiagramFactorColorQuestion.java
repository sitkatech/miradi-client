/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class DiagramFactorColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorColorQuestion(String tag)
	{
		super(tag, "Diagram Factor Color", getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Black (Default)", Color.BLACK),
			new ChoiceItem("DarkGray", "Dark Gray", Color.GRAY.darker()),
			new ChoiceItem("LightGray", "Light Gray", Color.GRAY.brighter()),
			new ChoiceItem("Brown", "Brown", BROWN),
			new ChoiceItem("Tan", "Tan", TAN),
			new ChoiceItem("White", "White", Color.WHITE),
			new ChoiceItem("Red", "Red", Color.RED),
			new ChoiceItem("Pink", "Pink", Color.PINK),
			new ChoiceItem("Orange", "Orange", Color.ORANGE),
			new ChoiceItem("Yellow", "Yellow", Color.YELLOW),
			new ChoiceItem("DarkGreen", "Dark Green", Color.GREEN.darker()),
			new ChoiceItem("LightGreen", "Light Green", Color.GREEN.brighter()),
			new ChoiceItem("DarkBlue", "Dark Blue", Color.BLUE.darker()),
		};
	}
	
	public static final Color BROWN = new Color(180, 90, 0);
	public static final Color TAN = new Color(230, 150, 0);
}
