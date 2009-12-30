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

public class ScopeThreatRatingQuestion extends ThreatRatingQuestion
{
	public ScopeThreatRatingQuestion()
	{
		super(getChoiceItems(), getDescription());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE, ""),
				new ChoiceItem("1", EAM.text("Low"), COLOR_GREAT, EAM.text("Low: The threat is likely to be very narrow in its scope, affecting the target across a small proportion (1-10%) of its occurrence/population.")),
				new ChoiceItem("2", EAM.text("Medium"), COLOR_OK, EAM.text("Medium: The threat is likely to be restricted in its scope, affecting the target across some (11-30%) of its occurrence/population.")),
				new ChoiceItem(HIGH_RATING_CODE, EAM.text("High"), COLOR_CAUTION, EAM.text("High: The threat is likely to be widespread in its scope, affecting the target across much (31-70%) of its occurrence/population.")),
				new ChoiceItem(VERY_HIGH_RATING_CODE, EAM.text("Very High"), COLOR_ALERT, EAM.text("Very High: The threat is likely to be pervasive in its scope, affecting the target across all or most (71-100%) of its occurrence/population.")),
		};
	}

	private static String getDescription()
	{
		return EAM.text("Scope - Most commonly defined spatially as the proportion of the target " +
				"that can reasonably be expected to be affected by the threat within ten years given " +
				"the continuation of current circumstances and trends. For ecosystems and ecological communities, " +
				"measured as the proportion of the target's occurrence. For species, measured as the proportion " +
				"of the target's population.");
	}
}
