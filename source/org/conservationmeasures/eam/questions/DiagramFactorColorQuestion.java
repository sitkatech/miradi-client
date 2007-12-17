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
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("1", "Black", Color.BLACK),
			new ChoiceItem("2", "Dark Gray", Color.GRAY.darker()),
			new ChoiceItem("3", "Light Gray", Color.GRAY.brighter()),
			new ChoiceItem("4", "Brown", BROWN),
			new ChoiceItem("5", "Tan", TAN),
			new ChoiceItem("6", "White", Color.WHITE),
			new ChoiceItem("7", "Red", Color.RED),
			new ChoiceItem("8", "Pink", Color.PINK),
			new ChoiceItem("9", "Orange", Color.ORANGE),
			new ChoiceItem("10", "Yellow", Color.YELLOW),
			new ChoiceItem("11", "Dark Green", Color.GREEN.darker()),
			new ChoiceItem("12", "Light Green", Color.GREEN.brighter()),
			new ChoiceItem("13", "Dark Blue", Color.BLUE.darker()),
		};
	}
	
	public static final Color BROWN = new Color(180, 90, 0);
	public static final Color TAN = new Color(230, 150, 0);
}
