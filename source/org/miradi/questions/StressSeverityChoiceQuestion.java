/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import java.awt.Color;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class StressSeverityChoiceQuestion extends StaticChoiceQuestionSortableByCode
{
	public StressSeverityChoiceQuestion()
	{
		super(getSeverityChoices());
	}
	
	public static ChoiceItem[] getSeverityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE),
			new ChoiceItemWithDynamicColor("1", EAM.text("Low"), AppPreferences.TAG_COLOR_GREAT),
			new ChoiceItemWithDynamicColor("2", EAM.text("Medium"), AppPreferences.TAG_COLOR_OK),
			new ChoiceItemWithDynamicColor("3", EAM.text("High"), AppPreferences.TAG_COLOR_CAUTION),
			new ChoiceItemWithDynamicColor("4", EAM.text("Very High"), AppPreferences.TAG_COLOR_ALERT),
		};
	}
}
