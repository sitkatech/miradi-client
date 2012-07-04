/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.RareProjectData;

public class RareProjectDataSchema extends BaseObjectSchema
{
	public RareProjectDataSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		createFieldSchemaSingleLineUserText(RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		createFieldSchemaMultiLineUserText(RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		createFieldSchemaMultiLineUserText(RareProjectData.TAG_CAMPAIGN_SLOGAN);
		createFieldSchemaMultiLineUserText(RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		createFieldSchemaMultiLineUserText(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		createFieldSchemaSingleLineUserText(RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		createFieldSchemaSingleLineUserText(RareProjectData.TAG_COHORT);
		createFieldSchemaInteger(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		createFieldSchemaMultiLineUserText(RareProjectData.LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		createFieldSchemaMultiLineUserText(RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
	}

	public static int getObjectType()
	{
		return ObjectType.RARE_PROJECT_DATA;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "RareProjectData";
}
