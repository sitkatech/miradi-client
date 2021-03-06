/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.EAM;

public class FontSizeQuestion extends StaticChoiceQuestion
{
	public FontSizeQuestion()
	{
		super(getSizeChoices());
	}
	
	static ChoiceItem[] getSizeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(DEFAULT_FONT_SIZE_CODE, EAM.text("Default System Size")),
			new ChoiceItem("6", "6"),
			new ChoiceItem("8", "8"),
			new ChoiceItem("10", "10"),
			new ChoiceItem("11", "11"),
			new ChoiceItem("12", "12"),
			new ChoiceItem("14", "14"),
			new ChoiceItem("18", "18"),
			new ChoiceItem("24", "24"),
		};
	}
	
	public static String getDefaultSizeCode()
	{
		return "12";
	}
	
	public static final String DEFAULT_FONT_SIZE_CODE = "";
}
