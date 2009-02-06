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

//FIXME remove after TAG_STATUS is removed
public class IndicatorStatusRatingQuestion extends StaticChoiceQuestion
{
	public IndicatorStatusRatingQuestion()
	{
		super(getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE),
			new ChoiceItem(NOT_STARTED_CODE, EAM.text("Not Started"), COLOR_ALERT),
			new ChoiceItem(PROBLEMS_IMPLEMENTING_CODE, EAM.text("Problems Implementing"), COLOR_CAUTION),
			new ChoiceItem(GOING_WELL_CODE, EAM.text("Going Well"), COLOR_OK),
			new ChoiceItem(FULLY_ON_SCHEDULE_CODE, EAM.text("Fully on Schedule"), COLOR_GREAT),
		};
	}
	
	public static final String NOT_STARTED_CODE = "1";
	public static final String PROBLEMS_IMPLEMENTING_CODE = "2";
	public static final String GOING_WELL_CODE = "3";
	public static final String FULLY_ON_SCHEDULE_CODE = "4";
}
