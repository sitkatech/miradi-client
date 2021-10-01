/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.MiradiShareProjectData;

public class MiradiShareProjectDataSchema extends BaseObjectSchema
{
	public MiradiShareProjectDataSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROJECT_ID);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROJECT_URL);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_ID);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_NAME);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_URL);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROJECT_TEMPLATE_ID);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROJECT_TEMPLATE_NAME);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROJECT_VERSION);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_NAME);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION_ID);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION);
		createFieldSchemaSingleLineUserText(MiradiShareProjectData.TAG_EXTRA_DATA);
		createTaxonomyClassificationSchemaField();
	}

	@Override
	protected boolean hasLabel()
	{
		return false;
	}
	
	public static int getObjectType()
	{
		return ObjectType.MIRADI_SHARE_PROJECT_DATA;
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
	
	public static final String OBJECT_NAME = "MiradiShareProjectData";
}
