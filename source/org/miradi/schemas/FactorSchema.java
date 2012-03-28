/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;

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
		createFieldSchemaMultiLineUserText(Factor.TAG_TEXT);
		createFieldSchemaSingleLineUserText(Factor.TAG_SHORT_LABEL);
	    createFieldSchemaIdList(Factor.TAG_INDICATOR_IDS, Indicator.getObjectType());
		createFieldSchemaIdList(Factor.TAG_OBJECTIVE_IDS, Objective.getObjectType());
		
		createPseudoString(Factor.PSEUDO_TAG_OBJECTIVES);
		createPseudoString(Factor.PSEUDO_TAG_DIRECT_THREATS);
		createPseudoString(Factor.PSEUDO_TAG_TARGETS);
		createPseudoString(Factor.PSEUDO_TAG_INDICATORS);
		createPseudoRefList(Factor.PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS);
		createPseudoRefList(Factor.PSEUDO_TAG_RESULTS_CHAIN_REFS);
		createPseudoRefList(Factor.PSEUDO_TAG_REFERRING_TAG_REFS);
	}
}
