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

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class ThreatRatingQuestion extends StaticChoiceQuestionSortableByCode
{
	public ThreatRatingQuestion(String questionDescriptionToUse)
	{
		super(questionDescriptionToUse);
	}
	
	public ThreatRatingQuestion()
	{
		this("");
	}

	@Override
	protected ChoiceItem[] createChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(UNSPECIFIED_CODE, EAM.text("Not Specified"), Color.WHITE, UNSPECIFIED_CODE),
				new ChoiceItemWithDynamicColor("1", EAM.text("Low"), AppPreferences.TAG_COLOR_GREAT, getLowRatingChoiceItemDescription()),
				new ChoiceItemWithDynamicColor(MEDIUM_RATING_CODE, EAM.text("Medium"), AppPreferences.TAG_COLOR_OK, getMediumRatingChoiceItemDescription()),
				new ChoiceItemWithDynamicColor(HIGH_RATING_CODE, EAM.text("High"), AppPreferences.TAG_COLOR_CAUTION, getHighRatingChoiceItemDescription()),
				new ChoiceItemWithDynamicColor(VERY_HIGH_RATING_CODE, EAM.text("Very High"), AppPreferences.TAG_COLOR_ALERT, getVeryHighRatingChoiceItemDescription()),
		};
	}
	
	protected String getLowRatingChoiceItemDescription()
	{
		return "";
	}
	
	protected String getMediumRatingChoiceItemDescription()
	{
		return "";
	}

	protected String getHighRatingChoiceItemDescription()
	{
		return "";
	}

	protected String getVeryHighRatingChoiceItemDescription()
	{
		return "";
	}

	public static final String UNSPECIFIED_CODE = "";
	public static final String MEDIUM_RATING_CODE = "2";
	public static final String HIGH_RATING_CODE = "3";
	public static final String VERY_HIGH_RATING_CODE = "4";

	public static final String NOT_APPLICABLE = EAM.text("N/A");
	public static final Color NOT_APPLICABLE_COLOR = Color.WHITE;

}
