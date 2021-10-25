/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.questions.TaxonomyClassificationSelectionModeQuestion;

abstract public class AbstractTaxonomyAssociationSchema extends BaseObjectSchema
{
	public AbstractTaxonomyAssociationSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();	
		
		createFieldSchemaSingleLineUserText(TAG_TAXONOMY_ASSOCIATION_CODE);
		createFieldSchemaChoice(TAG_SELECTION_TYPE, TaxonomyClassificationSelectionModeQuestion.class);
		createFieldSchemaMultiLineUserText(TAG_DESCRIPTION);
		createFieldSchemaSingleLineUserText(TAG_TAXONOMY_CODE);
		createFieldSchemaInteger(TAG_BASE_OBJECT_TYPE);
	}

	public static final String TAG_TAXONOMY_ASSOCIATION_CODE = "Code";
	public static final String TAG_SELECTION_TYPE = "SelectionType";
	public static final String TAG_DESCRIPTION  = "Description";
	public static final String TAG_TAXONOMY_CODE  = "TaxonomyCode";
	public static final String TAG_BASE_OBJECT_TYPE = "BaseObjectType";
}
