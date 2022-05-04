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


public class StrategyFeasibilityQuestion extends StaticChoiceQuestionSortableByCode
{
	public StrategyFeasibilityQuestion()
	{
		super(getFeasibilityChoices(), EAM.text("Would your project team be able to implement the strategy within likely time, financial, staffing, ethical, and other constraints?"));
	}
	
	static ChoiceItem[] getFeasibilityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItemWithLongDescriptionProvider("", EAM.text("Not Specified"), Color.WHITE),
			new ChoiceItemWithDynamicColorAndLongDescriptionProvider(LOW_CODE, EAM.text("Low"), EAM.text("The strategy is not ethically, technically, OR financially feasible."), AppPreferences.TAG_COLOR_ALERT),
			new ChoiceItemWithDynamicColorAndLongDescriptionProvider(MEDIUM_CODE, EAM.text("Medium"), EAM.text("The strategy is ethically feasible, but either technically OR financially difficult without substantial additional resources."), AppPreferences.TAG_COLOR_CAUTION),
			new ChoiceItemWithDynamicColorAndLongDescriptionProvider(HIGH_CODE, EAM.text("High"), EAM.text("The strategy is ethically and technically feasible, but may require some additional financial resources."), AppPreferences.TAG_COLOR_OK),
			new ChoiceItemWithDynamicColorAndLongDescriptionProvider(VERY_HIGH_CODE, EAM.text("Very High"), EAM.text("The strategy is ethically, technically, AND financially feasible."), AppPreferences.TAG_COLOR_GREAT),
		};
	}
	
	@Override
	public boolean hasAdditionalText()
	{
		return true;
	}
	
	public static final String LOW_CODE = "1";
	public static final String MEDIUM_CODE = "2";
	public static final String HIGH_CODE = "3";
	public static final String VERY_HIGH_CODE = "4";
}
