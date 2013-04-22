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


public class StatusConfidenceQuestion extends StaticChoiceQuestion
{
	public StatusConfidenceQuestion()
	{
		super(getStatusConfidences());
	}
	
	static ChoiceItem[] getStatusConfidences()
	{
		return new ChoiceItem[] {
			new ChoiceItem(NOT_SPECIFIED, EAM.text("Not Specified")),
			new ChoiceItem(ROUGH_GUESS_CODE, EAM.text("Rough Guess")),
			new ChoiceItem(EXPERT_KNOWLEDGE_CODE, EAM.text("Expert Knowledge")),
			new ChoiceItem(RAPID_ASSESSMENT_CODE, EAM.text("Rapid Assessment")),
			new ChoiceItem(INTENSIVE_ASSESSMENT_CODE, EAM.text("Intensive Assessment"))
		};
	}
	
	public static final String NOT_SPECIFIED = "";
	public static final String ROUGH_GUESS_CODE = "RoughGuess";
	public static final String EXPERT_KNOWLEDGE_CODE = "ExpertKnowledge";
	public static final String RAPID_ASSESSMENT_CODE = "RapidAssessment";
	public static final String INTENSIVE_ASSESSMENT_CODE = "IntensiveAssessment";
}
