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

public class OrganizationalPrioritiesQuestion extends StaticChoiceQuestion
{
	public OrganizationalPrioritiesQuestion()
	{
		super(getOrganizationalPrioritiesChoices());
	}

	private static ChoiceItem[] getOrganizationalPrioritiesChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem(CAPITAL_CAMPAIGN_CODE, "Capital Campaign"),
				new ChoiceItem(REGIONAL_PRIORITY_CODE, "Regional Priority"),
				new ChoiceItem(CSD_CLIMATE_CODE, "CSD - Climate"),
				new ChoiceItem(CSD_MARINE_CODE, "CSD - Marine"),
				new ChoiceItem(CSD_FRESHWATER_CODE, "CSD - Freshwater"),
				new ChoiceItem(CSD_PROTECTED_AREAS_CODE, "CSD - Protected Areas"),
		};
	}
	
	public static final String CAPITAL_CAMPAIGN_CODE = "Capital Campaign";
	public static final String REGIONAL_PRIORITY_CODE = "Regional Priority";
	public static final String CSD_CLIMATE_CODE = "CSD - Climate";
	public static final String CSD_MARINE_CODE = "CSD - Marine";
	public static final String CSD_FRESHWATER_CODE = "CSD - Freshwater";
	public static final String CSD_PROTECTED_AREAS_CODE = "CSD - Protected Areas";
}
