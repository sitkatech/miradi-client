/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.objects.Factor;

abstract public class FactorSchema extends BaseObjectSchema
{
	public FactorSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaMultiLineUserText(Factor.TAG_COMMENTS);
		addDetailsField();
		createFieldSchemaSingleLineUserText(Factor.TAG_SHORT_LABEL);
		
		createPseudoFieldSchemaString(Factor.PSEUDO_TAG_OBJECTIVES);
		createPseudoFieldSchemaString(Factor.PSEUDO_TAG_DIRECT_THREATS);
		createPseudoFieldSchemaString(Factor.PSEUDO_TAG_TARGETS);
		createPseudoFieldSchemaString(Factor.PSEUDO_TAG_INDICATORS);
		createPseudoFieldSchemaRefList(Factor.PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS);
		createPseudoFieldSchemaRefList(Factor.PSEUDO_TAG_RESULTS_CHAIN_REFS);
		createPseudoFieldSchemaRefList(Factor.PSEUDO_TAG_REFERRING_TAG_REFS);
	}

	protected void addDetailsField()
	{
		createFieldSchemaMultiLineUserText(Factor.TAG_TEXT);
	}

	protected void writeObjectiveIds()
	{
		createOwnedFieldSchemaIdList(Factor.TAG_OBJECTIVE_IDS, ObjectiveSchema.getObjectType());
	}

	protected void writeIndicatorIds()
	{
		createOwnedFieldSchemaIdList(Factor.TAG_INDICATOR_IDS, IndicatorSchema.getObjectType());
	}
}
