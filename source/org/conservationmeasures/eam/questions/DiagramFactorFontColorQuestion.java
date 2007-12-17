/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class DiagramFactorFontColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontColorQuestion(String tag)
	{
		super(tag, "Diagram Factor Color", getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Black (Default)", Color.BLACK),
			new ChoiceItem("#4E4848", "Dark Gray", Color.GRAY.darker()),
			new ChoiceItem("#6D7B8D", "Light Gray", Color.GRAY.brighter()),
			new ChoiceItem("#C85A17", "Brown", BROWN),
			new ChoiceItem("#EDE275", "Tan", TAN),
			new ChoiceItem("#FFFFFF", "White", Color.WHITE),
			new ChoiceItem("#FF0000", "Red", Color.RED),
			new ChoiceItem("#FF00FF", "Pink", Color.PINK),
			new ChoiceItem("#FF8040", "Orange", Color.ORANGE),
			new ChoiceItem("#FFFF00", "Yellow", Color.YELLOW),
			new ChoiceItem("#254117", "Dark Green", Color.GREEN.darker()),
			new ChoiceItem("#5FFB17", "Light Green", Color.GREEN.brighter()),
			new ChoiceItem("#736AFF", "Dark Blue", Color.BLUE.darker()),
		};
	}
	
	public static final Color BROWN = new Color(180, 90, 0);
	public static final Color TAN = new Color(230, 150, 0);
}
