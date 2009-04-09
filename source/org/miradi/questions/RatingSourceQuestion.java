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

import org.miradi.main.EAM;


public class RatingSourceQuestion extends StaticChoiceQuestion
{
	public RatingSourceQuestion()
	{
		super(getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Not Specified")),
			new ChoiceItem(ROUGH_GUES_CODE, EAM.text("Rough Guess")),
			new ChoiceItem(EXPERT_KNOWLEGE_CODE, EAM.text("Expert Knowledge")),
			new ChoiceItem(EXTERNAL_RESEARCH_CODE, EAM.text("External Research")),
			new ChoiceItem(ONSITE_RESEARCH_CODE, EAM.text("Onsite Research")),
		};
	}
	
	public static final String ROUGH_GUES_CODE = "RoughGuess";
	public static final String EXPERT_KNOWLEGE_CODE = "ExpertKnowlege";
	public static final String EXTERNAL_RESEARCH_CODE = "ExternalResearch";
	public static final String ONSITE_RESEARCH_CODE = "OnsiteResearch";
}