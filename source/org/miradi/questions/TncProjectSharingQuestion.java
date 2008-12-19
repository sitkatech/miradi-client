/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

public class TncProjectSharingQuestion extends StaticChoiceQuestion
{
	public TncProjectSharingQuestion()
	{
		super(getSharingCodes());
	}

	private static ChoiceItem[] getSharingCodes()
	{
		return new ChoiceItem[] {
				new ChoiceItem(SHARE_TNC_ONLY, EAM.text("No")),
				new ChoiceItem(SHARE_WITH_ANYONE, EAM.text("Yes")),
		};
	}
	
	public static final String SHARE_TNC_ONLY = "";
	public static final String SHARE_WITH_ANYONE = "ShareWithAnyone";
}
