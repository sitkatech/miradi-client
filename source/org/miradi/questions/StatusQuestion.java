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

public class StatusQuestion extends StaticChoiceQuestion
{
	public StatusQuestion()
	{
		super(getStatuses());
	}

	static ChoiceItem[] getStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE),
				new ChoiceItem("1", EAM.text("Poor"), COLOR_ALERT),
				new ChoiceItem("2", EAM.text("Fair"), COLOR_CAUTION),
				new ChoiceItem("3", EAM.text("Good"), COLOR_OK),
				new ChoiceItem("4", EAM.text("Very Good"), COLOR_GREAT),
		};
	}
	
	public static final String UNSPECIFIED = "";
	public static final String POOR = "1";
	public static final String FAIR = "2";
	public static final String GOOD = "3";
	public static final String VERY_GOOD = "4";
}
