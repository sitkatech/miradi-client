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

package org.miradi.xml.generic;

import org.miradi.objects.RareProjectData;
import org.miradi.xml.wcs.WcsXmlConstants;

public class RareProjectDataSchemaElement extends BaseObjectSchemaElement
{
	public RareProjectDataSchemaElement()
	{
		super(WcsXmlConstants.RARE_PROJECT_DATA);
		
		createTextField(RareProjectData.TAG_COHORT);
		
		createTextField(RareProjectData.TAG_THREATS_ADDRESSED_NOTES);
		createTextField(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		createTextField(RareProjectData.TAG_AUDIENCE);
		createTextField(RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		createTextField(RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		createTextField(RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		createTextField(RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		
		createTextField(RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		createTextField(RareProjectData.TAG_CAMPAIGN_SLOGAN);
		createTextField(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		createTextField(RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
		createTextField(RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES);
		createTextField(RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES);
		
		createTextField(RareProjectData.TAG_COURSE_MANAGER_NOTES);
		createTextField(RareProjectData.TAG_REGIONAL_DIRECTOR_NOTES);
		createTextField(RareProjectData.TAG_CAMPAIGN_MANAGER_NOTES);
		createTextField(RareProjectData.TAG_LOCAL_PARTNER_CONTACT_NOTES);
		createTextField(RareProjectData.TAG_BINGO_PARTNER_CONTACT_NOTES);
		createTextField(RareProjectData.TAG_THREAT_REDUCTION_PARTNER_CONTACT_NOTES);
		createTextField(RareProjectData.TAG_MONITORING_PARTNER_CONTACT_NOTES);
	}
}
