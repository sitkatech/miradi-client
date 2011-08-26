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

public class TncOrganizationalPrioritiesQuestion extends StaticChoiceQuestion
{
	public TncOrganizationalPrioritiesQuestion()
	{
		super(getOrganizationalPrioritiesChoices());
	}

	private static ChoiceItem[] getOrganizationalPrioritiesChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem(CAPITAL_CAMPAIGN_CODE, "Capital Campaign"),
				new ChoiceItem(REGIONAL_PRIORITY_CODE, "Regional Priority"),
				new ChoiceItem(FOCAL_AREA_CLIMATE_CODE, "Focal Area - Climate"),
				new ChoiceItem(FOCAL_AREA_MARINE_CODE, "Focal Area - Marine"),
				new ChoiceItem(FOCAL_AREA_FRESHWATER_CODE, "Focal Area - Freshwater"),
				new ChoiceItem(FOCAL_AREA_PROTECTED_AREAS_CODE, "Focal Area - Protected Areas"),
		};
	}
	
	public static final String CAPITAL_CAMPAIGN_CODE = "CapitalCampaign";
	public static final String REGIONAL_PRIORITY_CODE = "RegionalPriority";
	public static final String FOCAL_AREA_CLIMATE_CODE = "FocalAreaClimate";
	public static final String FOCAL_AREA_MARINE_CODE = "FocalAreaMarine";
	public static final String FOCAL_AREA_FRESHWATER_CODE = "FocalAreaFreshwater";
	public static final String FOCAL_AREA_PROTECTED_AREAS_CODE = "FocalAreaProtected Areas";
}
