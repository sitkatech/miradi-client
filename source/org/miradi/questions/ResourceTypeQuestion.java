/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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


public class ResourceTypeQuestion extends StaticChoiceQuestion
{
	public ResourceTypeQuestion()
	{
		super(getStaticChoices());
	}

	private static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(PERSON_CODE, EAM.text("Person")),
			new ChoiceItem(GROUP_CODE, EAM.text("Group")),
		};
	}
	
	@Override
	public boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	public String getReadableAlternativeDefaultCode()
	{
		return "Person";
	}
	
	public static final String PERSON_CODE = "";
	public static final String GROUP_CODE = "Group";
}
