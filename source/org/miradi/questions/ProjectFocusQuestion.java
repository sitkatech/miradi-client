/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

public class ProjectFocusQuestion extends StaticChoiceQuestion
{
	public ProjectFocusQuestion()
	{
		super(getStaticChoices());
	}

	private static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Not Specified")),
			new ChoiceItem(PLACE_CODE, EAM.text("Place")),	
			new ChoiceItem(POLICY_AND_PRACTICE_CODE, EAM.text("Policy and Practice")),
			new ChoiceItem(PERFORMANCE_CODE, EAM.text("Performance")),
		};
	}
	
	private static final String PLACE_CODE = "Place";
	public static final String POLICY_AND_PRACTICE_CODE = "PolicyAndPractice";
	private static final String PERFORMANCE_CODE = "Performance";
}
