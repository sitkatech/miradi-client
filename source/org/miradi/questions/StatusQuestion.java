/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

public class StatusQuestion extends StaticChoiceQuestionSortableByCode
{
	public StatusQuestion()
	{
		super(getStatuses());
	}

	static ChoiceItem[] getStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE),
				new ChoiceItemWithDynamicColor("1", EAM.text("Poor"), AppPreferences.TAG_COLOR_ALERT),
				new ChoiceItemWithDynamicColor("2", EAM.text("Fair"), AppPreferences.TAG_COLOR_CAUTION),
				new ChoiceItemWithDynamicColor("3", EAM.text("Good"), AppPreferences.TAG_COLOR_OK),
				new ChoiceItemWithDynamicColor("4", EAM.text("Very Good"), AppPreferences.TAG_COLOR_GREAT),
		};
	}
	
	public static final String UNSPECIFIED = "";
	public static final String POOR = "1";
	public static final String FAIR = "2";
	public static final String GOOD = "3";
	public static final String VERY_GOOD = "4";
}
