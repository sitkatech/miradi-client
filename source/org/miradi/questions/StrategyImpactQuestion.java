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

import java.awt.Color;

import org.miradi.main.EAM;


public class StrategyImpactQuestion extends StaticChoiceQuestionSortableByCode
{
	public StrategyImpactQuestion()
	{
		super(getImpactChoices(), EAM.text("To track the confidence of your evidence that the strategy will achieve its desired goals and/or objectives, use the following scale:"));
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItemWithLongDescriptionProvider("", EAM.text("Not Specified"), Color.WHITE),
			new ChoiceItemWithLongDescriptionProvider(LOW_CODE, EAM.text("Low"), EAM.text("The strategy is unlikely to meaningfully contribute to project goals and/or objectives."), COLOR_ALERT),
			new ChoiceItemWithLongDescriptionProvider(MEDIUM_CODE, EAM.text("Medium"), EAM.text("The strategy could meaningfully contribute to project goals and/or objectives, but would need pilot-testing to ensure it is effective under this project’s conditions."), COLOR_CAUTION),
			new ChoiceItemWithLongDescriptionProvider(HIGH_CODE, EAM.text("High"), EAM.text("The strategy is likely to meaningfully contribute to project goals and/or objectives, but would need effectiveness monitoring to ensure it is effective under this project’s conditions."), COLOR_OK),
			new ChoiceItemWithLongDescriptionProvider(VERY_HIGH_CODE, EAM.text("Very High"), EAM.text("The strategy is very likely to meaningfully contribute to one or more project goals and/or objectives and can be implemented at scale with only implementation monitoring."), COLOR_GREAT),
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
