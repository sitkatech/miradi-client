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

import org.miradi.main.EAM;

public class PrecisionTypeQuestion extends StaticChoiceQuestion
{
	public PrecisionTypeQuestion()
	{
		super(getChoiceItems());
	}
	
	private static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
			new ChoiceItem(UNSPECIFIED_CODE, EAM.text("Unspecified")),
			new ChoiceItem(SD_CODE, EAM.text("SD (standard deviation)")),
			new ChoiceItem(SE_CODE, EAM.text("SE (standard error)")),
			new ChoiceItem(CONFIDENCE_INTERVAL_95_PERCENT_CODE, EAM.text("95% C.I. (confidence interval)")),
			new ChoiceItem(OTHER_CONFIDENCE_INTERVAL_CODE, EAM.text("Other confidence interval")),
		};
	}
	
	public static final String UNSPECIFIED_CODE = "";
	public static final String SD_CODE = "StandardDeviation";
	public static final String SE_CODE = "StandardError";
	public static final String CONFIDENCE_INTERVAL_95_PERCENT_CODE = "ConfidenceInterval95Percent";
	public static final String OTHER_CONFIDENCE_INTERVAL_CODE = "ConfidenceIntervalOther";
}
