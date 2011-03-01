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


public class StrategyImpactQuestion extends StaticChoiceQuestionSortableByCode
{
	public StrategyImpactQuestion()
	{
		super(getImpactChoices(), EAM.text("If implemented, will the strategy lead to desired changes in the situation at your project site?"));
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItemWithLongDescriptionProvider("", EAM.text("Not Specified"), Color.WHITE),
			new ChoiceItemWithLongDescriptionProvider(LOW_CODE, EAM.text("Low"), EAM.text("The strategy will probably not contribute to meaningful threat mitigation or target restoration."), COLOR_ALERT),
			new ChoiceItemWithLongDescriptionProvider(MEDIUM_CODE, EAM.text("Medium"), EAM.text("The strategy could possibly help mitigate a threat or restore a target."), COLOR_CAUTION),
			new ChoiceItemWithLongDescriptionProvider(HIGH_CODE, EAM.text("High"), EAM.text("he strategy is likely to help mitigate a threat or restore a target."), COLOR_OK),
			new ChoiceItemWithLongDescriptionProvider(VERY_HIGH_CODE, EAM.text("Very High"), EAM.text("The strategy is very likely to completely mitigate a threat or restore a target."), COLOR_GREAT),
		};
	}
	
	public static final String LOW_CODE = "1";
	public static final String MEDIUM_CODE = "2";
	public static final String HIGH_CODE = "3";
	public static final String VERY_HIGH_CODE = "4";
}
