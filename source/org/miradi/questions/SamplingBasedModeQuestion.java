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

public class SamplingBasedModeQuestion extends StaticChoiceQuestion
{
	public SamplingBasedModeQuestion()
	{
		super(getChoiceItems());
	}
	
	private static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
			new ChoiceItem(OFF_CODE, EAM.text("OFF")),
			new ChoiceItem(ON_CODE, EAM.text("ON")),
		};
	}
	
	@Override
	protected boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	protected String getReadableAlternativeDefaultCode()
	{
		return HUMAN_READABLE_OFF_CODE;
	}
	
	public static final String OFF_CODE = "";
	public static final String ON_CODE = "on";
	
	public static final String HUMAN_READABLE_OFF_CODE = "off";
}
