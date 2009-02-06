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

import org.miradi.icons.StressIcon;
import org.miradi.main.EAM;
import org.miradi.utils.MiradiResourceImageIcon;

public class ThreatRatingModeChoiceQuestion extends StaticChoiceQuestion
{
	public ThreatRatingModeChoiceQuestion()
	{
		super(getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(SIMPLE_BASED_CODE, EAM.text("Simple Threat Rating Mode"), new MiradiResourceImageIcon("icons/showRatings.png")),
				new ChoiceItem(STRESS_BASED_CODE, EAM.text("Stress Based Threat Rating Mode"), new StressIcon()),
		};
	}
	
	public static final String STRESS_BASED_CODE = "StressBased";
	public static final String SIMPLE_BASED_CODE = "";
}