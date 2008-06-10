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

public class DiagramLinkColorQuestion extends StaticChoiceQuestion
{
	public DiagramLinkColorQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Black (Default)", Color.BLACK),
				new ChoiceItem("darkGray", "Dark Gray", DiagramFactorFontColorQuestion.DARK_GRAY_FROM_HEX),
				new ChoiceItem("lightGray", "Light Gray", DiagramFactorFontColorQuestion.LIGHT_GRAY_FROM_HEX),
				new ChoiceItem("brown", "Brown", DiagramFactorFontColorQuestion.BROWN_FROM_HEX),
				new ChoiceItem("tan", "Tan", DiagramFactorFontColorQuestion.TAN_FROM_HEX),
				new ChoiceItem("red", "Red", DiagramFactorFontColorQuestion.RED_FROM_HEX),
				new ChoiceItem("pink", "Pink", DiagramFactorFontColorQuestion.PINK_FROM_HEX),
				new ChoiceItem("orange", "Orange", DiagramFactorFontColorQuestion.ORANGE_FROM_HEX),
				new ChoiceItem("yellow", "Yellow", DiagramFactorFontColorQuestion.YELLOW_FROM_HEX),
				new ChoiceItem("darkGreen", "Dark Green", DiagramFactorFontColorQuestion.DARK_GREEN_FROM_HEX),
				new ChoiceItem("lightGreen", "Light Green", DiagramFactorFontColorQuestion.LIGHT_GREEN_FROM_HEX),
				new ChoiceItem("darkBlue", "Dark Blue", DiagramFactorFontColorQuestion.DARK_BLUE_FROM_HEX),
				new ChoiceItem("lightBlue", "Light Blue", DiagramFactorFontColorQuestion.LIGHT_BLUE_FROM_HEX),
		};
	}
}
