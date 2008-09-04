/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.questions;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;

public class DiagramFactorBackgroundQuestion extends StaticChoiceQuestion
{
	public DiagramFactorBackgroundQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Light Gray (Default)", DiagramConstants.TEXT_BOX_COLOR),
			new ChoiceItem("White", "White", new Color(255, 255, 255)),
			new ChoiceItem("Pink", "Pink", new Color(255, 0, 255)),
			new ChoiceItem("Orange", "Light Orange", new Color(255, 128, 64)),
			new ChoiceItem("LightYellow", "Light Yellow", new Color(255, 255, 205)),
			new ChoiceItem("LightGreen", "Light Green", new Color(95, 251, 23)),
			new ChoiceItem("LightBlue", "Light Blue", new Color(0, 204, 255)),
			new ChoiceItem("LightPurple", "Light Purple", new Color(204, 153, 255)),
			new ChoiceItem("Tan", "Tan", new Color(237, 226, 117)),
			new ChoiceItem("Black", "Black", new Color(0, 0, 0)),
			new ChoiceItem("DarkGray", "Dark Gray", new Color(78, 72, 72)),

			new ChoiceItem("Red", "Dark Red", new Color(255, 0, 0)),
			new ChoiceItem("DarkOrange", "Dark Orange", new Color(255, 102, 0)),
			new ChoiceItem("DarkYellow", "Dark Yellow", new Color(255, 204, 0)),
			new ChoiceItem("DarkGreen", "Dark Green", new Color(0, 128, 0)),
			new ChoiceItem("DarkBlue", "Dark Blue", new Color(0, 0, 204)),
			new ChoiceItem("DarkPurple", "Dark Purple", new Color(153, 0, 255)),
			new ChoiceItem("Brown", "Brown", new Color(200, 90, 23)),
		};
	}
}
