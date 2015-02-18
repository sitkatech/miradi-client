/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

public class ProjectSharingQuestion extends StaticChoiceQuestion
{
	public ProjectSharingQuestion()
	{
		super(getSharingCodes());
	}

	private static ChoiceItem[] getSharingCodes()
	{
		return new ChoiceItem[] {
				new ChoiceItem(SHARE_ONLY_INSIDE_ORGANIZATION, EAM.text("Within your Organization Only")),
				new ChoiceItem(SHARE_WITH_ANYONE, EAM.text("Public (anyone)")),
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
		return "Private";
	}
	
	public static final String SHARE_ONLY_INSIDE_ORGANIZATION = "";
	public static final String SHARE_WITH_ANYONE = "ShareWithAnyone";
}
