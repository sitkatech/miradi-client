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

public class StressIrreversibilityQuestion extends StaticChoiceQuestion
{
	public StressIrreversibilityQuestion()
	{
		super(getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem(LOW_CODE, "Low", COLOR_GREAT),
				new ChoiceItem(MEDIUM_CODE, "Medium", COLOR_OK),
				new ChoiceItem(HIGH_CODE, "High", COLOR_CAUTION),
				new ChoiceItem(VERY_HIGH_CODE, "Very High", COLOR_ALERT),
		};
	}
	
	public static final String LOW_CODE = "1";
	public static final String MEDIUM_CODE = "2";
	public static final String HIGH_CODE = "3";
	public static final String VERY_HIGH_CODE = "4";
}
