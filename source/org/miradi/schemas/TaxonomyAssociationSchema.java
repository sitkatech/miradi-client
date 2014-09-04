/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
import org.miradi.questions.TaxonomyClassificationSelectionModeQuestion;
import org.miradi.questions.TaxonomyMultiSelectModeQuestion;

public class TaxonomyAssociationSchema extends BaseObjectSchema
{
	public TaxonomyAssociationSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();	
		
		createFieldSchemaSingleLineUserText(TAG_TAXONOMY_ASSOCIATION_CODE);
		createFieldSchemaChoice(TAG_MULTI_SELECT, TaxonomyMultiSelectModeQuestion.class);
		createFieldSchemaChoice(TAG_SELECTION_TYPE, TaxonomyClassificationSelectionModeQuestion.class);
		createFieldSchemaMultiLineUserText(TAG_DESCRIPTION);
		createFieldSchemaSingleLineUserText(TAG_TAXONOMY_CODE);
		createFieldSchemaInteger(TAG_BASE_OBJECT_TYPE);
		createFieldSchemaCode(TAG_TAXONOMY_ASSOCIATION_POOL_NAME);
	}
	
	public static int getObjectType()
	{
		return ObjectType.TAXONOMY_ASSOCIATION;
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
	
	public static final String OBJECT_NAME = "TaxonomyAssociation";
	
	public static final String TAG_TAXONOMY_ASSOCIATION_CODE = "Code";
	public static final String TAG_MULTI_SELECT = "MultiSelect";
	public static final String TAG_SELECTION_TYPE = "SelectionType";
	public static final String TAG_DESCRIPTION  = "Description";
	public static final String TAG_TAXONOMY_CODE  = "TaxonomyCode";
	public static final String TAG_BASE_OBJECT_TYPE = "BaseObjectType";
	public static final String TAG_TAXONOMY_ASSOCIATION_POOL_NAME = "TaxonomyAssociationPoolName";
}
