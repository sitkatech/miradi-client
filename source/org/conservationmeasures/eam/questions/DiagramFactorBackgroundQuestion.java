/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;

public class DiagramFactorBackgroundQuestion extends StaticChoiceQuestion
{
	public DiagramFactorBackgroundQuestion(String tag)
	{
		super(tag, "Diagram Factor Background Color", getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Light Gray (Default)", DiagramConstants.TEXT_BOX_COLOR),
			new ChoiceItem("Black", "Black", new Color(0, 0, 0)),
			new ChoiceItem("DarkGray", "Dark Gray", new Color(78, 72, 72)),
			new ChoiceItem("Brown", "Brown", new Color(200, 90, 23)),
			new ChoiceItem("Tan", "Tan", new Color(237, 226, 117)),
			new ChoiceItem("White", "White", new Color(255, 255, 255)),
			new ChoiceItem("Red", "Red", new Color(255, 0, 0)),
			new ChoiceItem("Pink", "Pink", new Color(255, 0, 255)),
			new ChoiceItem("Orange", "Orange", new Color(255, 128, 64)),
			new ChoiceItem("Yellow", "Yellow", new Color(255, 255, 0)),
			new ChoiceItem("DarkGreen", "Dark Green", new Color(37, 65, 23)),
			new ChoiceItem("LightGreen", "Light Green", new Color(95, 251, 23)),
			new ChoiceItem("DarkBlue", "Dark Blue", new Color(0, 0, 204)),
			new ChoiceItem("LightBlue", "Light Blue", new Color(0, 204, 255)),
		};
	}
}
