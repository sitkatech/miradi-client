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


public class DiagramFactorFontColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontColorQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Black (Default)"), Color.BLACK),
			new ChoiceItem(DARK_GRAY_HEX, EAM.text("Dark Gray"), DARK_GRAY_FROM_HEX),
			new ChoiceItem(RED_HEX, EAM.text("Dark Red"), RED_FROM_HEX),
			new ChoiceItem(DARK_ORANGE_HEX, EAM.text("Dark Orange"), DARK_ORANGE_FROM_HEX),
			new ChoiceItem(DARK_YELLOW_HEX, EAM.text("Dark Yellow"), DARK_YELLOW_FROM_HEX),
			new ChoiceItem(DARK_GREEN_HEX, EAM.text("Dark Green"), DARK_GREEN_FROM_HEX),
			new ChoiceItem(DARK_BLUE_HEX, EAM.text("Dark Blue"), DARK_BLUE_FROM_HEX),
			new ChoiceItem(DARK_PURPLE_HEX, EAM.text("Dark Purple"), DARK_PURPLE_FROM_HEX),
			new ChoiceItem(BROWN_HEX, EAM.text("Brown"), BROWN_FROM_HEX),
			new ChoiceItem(LIGHT_GRAY_HEX, EAM.text("Light Gray"), LIGHT_GRAY_FROM_HEX),
			new ChoiceItem(WHITE_HEX, EAM.text("White"), WHITE_FROM_HEX),
			new ChoiceItem(PINK_HEX, EAM.text("Pink"), PINK_FROM_HEX),
			new ChoiceItem(LIGHT_ORANGE_HEX, EAM.text("Light Orange"), LIGHT_ORANGE_FROM_HEX),
			new ChoiceItem(LIGHT_YELLOW_HEX, EAM.text("Light Yellow"), LIGHT_YELLOW_FROM_HEX),
			new ChoiceItem(LIGHT_GREEN_HEX, EAM.text("Light Green"), LIGHT_GREEN_FROM_HEX),
			new ChoiceItem(LIGHT_BLUE_HEX, EAM.text("Light Blue"), LIGHT_BLUE_FROM_HEX),
			new ChoiceItem(LIGHT_PURPLE_HEX, EAM.text("Light Purple"), LIGHT_PURPLE_FROM_HEX),
			new ChoiceItem(TAN_HEX, EAM.text("Tan"), TAN_FROM_HEX),
		};
	}
	
	public static final String DARK_GRAY_HEX = "#4E4848";
	public static final String LIGHT_GRAY_HEX = "#6D7B8D";
	public static final String DARK_ORANGE_HEX = "#FF6600";
	public static final String DARK_YELLOW_HEX = "#FFCC00";
	public static final String DARK_GREEN_HEX = "#007F00";
	public static final String DARK_BLUE_HEX = "#0000CC";
	public static final String DARK_PURPLE_HEX = "#9900FF";
	public static final String BROWN_HEX = "#C85A17";
	public static final String WHITE_HEX = "#FFFFFF";
	public static final String RED_HEX = "#FF0000";
	public static final String PINK_HEX = "#FF00FF";
	public static final String LIGHT_ORANGE_HEX = "#FF8040";
	public static final String LIGHT_YELLOW_HEX = "#FFFFCC";
	public static final String LIGHT_GREEN_HEX = "#5FFB17";
	public static final String LIGHT_BLUE_HEX = "#00CCFF";
	public static final String LIGHT_PURPLE_HEX = "#CC99FF";
	public static final String TAN_HEX = "#EDE275";
	
	public static final Color DARK_GRAY_FROM_HEX = Color.decode(DARK_GRAY_HEX);
	public static final Color LIGHT_GRAY_FROM_HEX = Color.decode(LIGHT_GRAY_HEX);
	public static final Color DARK_ORANGE_FROM_HEX = Color.decode(DARK_ORANGE_HEX);
	public static final Color DARK_YELLOW_FROM_HEX = Color.decode(DARK_YELLOW_HEX);
	public static final Color DARK_GREEN_FROM_HEX = Color.decode(DARK_GREEN_HEX);
	public static final Color DARK_BLUE_FROM_HEX = Color.decode(DARK_BLUE_HEX);
	public static final Color DARK_PURPLE_FROM_HEX = Color.decode(DARK_PURPLE_HEX);
	public static final Color BROWN_FROM_HEX = Color.decode(BROWN_HEX);
	public static final Color WHITE_FROM_HEX = Color.decode(WHITE_HEX);
	public static final Color RED_FROM_HEX = Color.decode(RED_HEX);
	public static final Color PINK_FROM_HEX = Color.decode(PINK_HEX);
	public static final Color LIGHT_ORANGE_FROM_HEX = Color.decode(LIGHT_ORANGE_HEX);
	public static final Color LIGHT_YELLOW_FROM_HEX = Color.decode(LIGHT_YELLOW_HEX);
	public static final Color LIGHT_GREEN_FROM_HEX = Color.decode(LIGHT_GREEN_HEX);
	public static final Color LIGHT_BLUE_FROM_HEX = Color.decode(LIGHT_BLUE_HEX);	
	public static final Color LIGHT_PURPLE_FROM_HEX = Color.decode(LIGHT_PURPLE_HEX);
	public static final Color TAN_FROM_HEX = Color.decode(TAN_HEX);
}
