/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.EAM;

public class DiagramLinkColorQuestion extends StaticChoiceQuestion
{
	public DiagramLinkColorQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Black (Default)"), Color.BLACK),
				new ChoiceItem("darkGray", EAM.text("Dark Gray"), DiagramFactorFontColorQuestion.DARK_GRAY_FROM_HEX),
				new ChoiceItem("red", EAM.text("Red"), DiagramFactorFontColorQuestion.RED_FROM_HEX),
				new ChoiceItem("DarkOrange", EAM.text("Dark Orange"), DiagramFactorFontColorQuestion.DARK_ORANGE_FROM_HEX),
				new ChoiceItem("DarkYellow", EAM.text("Dark Yellow"), DiagramFactorFontColorQuestion.DARK_YELLOW_FROM_HEX),
				new ChoiceItem("darkGreen", EAM.text("Dark Green"), DiagramFactorFontColorQuestion.DARK_GREEN_FROM_HEX),
				new ChoiceItem("darkBlue", EAM.text("Dark Blue"), DiagramFactorFontColorQuestion.DARK_BLUE_FROM_HEX),
				new ChoiceItem("DarkPurple", EAM.text("Dark Purple"), DiagramFactorFontColorQuestion.DARK_PURPLE_FROM_HEX),
				new ChoiceItem("brown", EAM.text("Brown"), DiagramFactorFontColorQuestion.BROWN_FROM_HEX),
				new ChoiceItem("lightGray", EAM.text("Light Gray"), DiagramFactorFontColorQuestion.LIGHT_GRAY_FROM_HEX),
				new ChoiceItem("White", EAM.text("White"), DiagramFactorFontColorQuestion.WHITE_FROM_HEX),
				new ChoiceItem("pink", EAM.text("Pink"), DiagramFactorFontColorQuestion.PINK_FROM_HEX),
				new ChoiceItem("orange", EAM.text("Orange"), DiagramFactorFontColorQuestion.LIGHT_ORANGE_FROM_HEX),
				new ChoiceItem("yellow", EAM.text("Yellow"), DiagramFactorFontColorQuestion.LIGHT_YELLOW_FROM_HEX),
				new ChoiceItem("lightGreen", EAM.text("Light Green"), DiagramFactorFontColorQuestion.LIGHT_GREEN_FROM_HEX),
				new ChoiceItem("lightBlue", EAM.text("Light Blue"), DiagramFactorFontColorQuestion.LIGHT_BLUE_FROM_HEX),
				new ChoiceItem("LightPurple", EAM.text("Light Purple"), DiagramFactorFontColorQuestion.LIGHT_PURPLE_FROM_HEX),
				new ChoiceItem("tan", EAM.text("Tan"), DiagramFactorFontColorQuestion.TAN_FROM_HEX),
		};
	}
}
