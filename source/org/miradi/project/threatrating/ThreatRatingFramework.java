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
package org.miradi.project.threatrating;

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ThreatRatingQuestion;

abstract public class ThreatRatingFramework
{
	public ThreatRatingFramework(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public ChoiceItem convertToChoiceItem(int codeAsInt)
	{
		String code = getSafeThreatRatingCode(codeAsInt);
		return convertToChoiceItem(code);
	}
	
	private String getSafeThreatRatingCode(int codeAsInt)
	{
		switch (codeAsInt)
		{
			case 1: return "1";
			case 2: return "2";
			case 3: return "3";
			case 4: return "4";

			default: return "";
		}
	}

	public ChoiceItem convertToChoiceItem(String code)
	{
		ChoiceQuestion question = getProject().getQuestion(ThreatRatingQuestion.class);
		return question.findChoiceByCode(code);
	}

	abstract public ChoiceItem getThreatThreatRatingValue(ORef threatRef) throws Exception;
	
	protected Project project;
}
