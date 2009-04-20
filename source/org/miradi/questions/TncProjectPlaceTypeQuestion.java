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

public class TncProjectPlaceTypeQuestion extends StaticChoiceQuestion
{
	public TncProjectPlaceTypeQuestion()
	{
		super(getProjectTypeChoices());
	}

	private static ChoiceItem[] getProjectTypeChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem(SINGLE_PLACE_BASED_PROJECT_CODE, "Single-Place-Based Project"),
				new ChoiceItem(MULTI_PLACE_BASED_PROJECT_CODE, "Multi-Place-Based Project"),
				new ChoiceItem(NON_PLACE_BASED_PROJECT_CODE, "Non-Place-Based Project"),
		};
	}
	
	public static final String SINGLE_PLACE_BASED_PROJECT_CODE = "Single-Place-Based Project";
	public static final String MULTI_PLACE_BASED_PROJECT_CODE = "Multi-Place-Based Project";
	public static final String NON_PLACE_BASED_PROJECT_CODE = "Non-Place-Based Project";
}
