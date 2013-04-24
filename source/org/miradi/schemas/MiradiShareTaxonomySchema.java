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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.questions.MiradiShareTaxonomyQuestion;

public class MiradiShareTaxonomySchema extends BaseObjectSchema
{
	public MiradiShareTaxonomySchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaCode(TAG_TAXONOMY_CODE);
		createFieldSchemaSingleLineUserText(TAG_TAXONOMY_VERSION);
		createFieldSchemaCodeList(TAG_TAXONOMY_ELEMENTS, new MiradiShareTaxonomyQuestion());
		createFieldSchemaCodeList(TAG_TAXONOMY_TOP_LEVEL_ELEMENT_CODES, new MiradiShareTaxonomyQuestion());
	}
	
	@Override
	protected boolean hasLabel()
	{
		return false;
	}

	public static int getObjectType()
	{
		return ObjectType.MIRADI_SHARE_TAXONOMY;
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
	
	public static final String OBJECT_NAME = "Taxonomy";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_TAXONOMY_VERSION = "TaxonomyVersion";
	public static final String TAG_TAXONOMY_ELEMENTS = "TaxonomyElements";
	public static final String TAG_TAXONOMY_TOP_LEVEL_ELEMENT_CODES = "TaxonomyTopLevelElementCodes";
}
