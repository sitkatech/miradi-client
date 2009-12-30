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

public class ThreatRatingQuestion extends StaticChoiceQuestion
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
				new ChoiceItem("", EAM.text("Not Specified"), Color.WHITE, ""),
				new ChoiceItem("1", EAM.text("Low"), COLOR_GREAT, getLowRatingChoiceItemDescription()),
				new ChoiceItem("2", EAM.text("Medium"), COLOR_OK, getMediumRatingChoiceItemDescription()),
				new ChoiceItem(HIGH_RATING_CODE, EAM.text("High"), COLOR_CAUTION, getHighRatingChoiceItemDescription()),
				new ChoiceItem(VERY_HIGH_RATING_CODE, EAM.text("Very High"), COLOR_ALERT, getVeryHighRatingChoiceItemDescription()),
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

	public static final String HIGH_RATING_CODE = "3";
	public static final String VERY_HIGH_RATING_CODE = "4";
}
