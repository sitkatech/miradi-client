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

import org.miradi.objects.Desire;

public class DesireSchema extends BaseObjectSchema
{
	public DesireSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(Desire.TAG_SHORT_LABEL);
		createFieldSchemaMultiLineUserText(Desire.TAG_FULL_TEXT);
		createFieldSchemaMultiLineUserText(Desire.TAG_COMMENTS);
		createFieldSchemaRelevancyOverrideSet(Desire.TAG_RELEVANT_INDICATOR_SET);
		createFieldSchemaRelevancyOverrideSet(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
		createFieldSchemaReflist(Desire.TAG_PROGRESS_PERCENT_REFS);
		
		createPseudoStringField(Desire.PSEUDO_TAG_TARGETS);
		createPseudoStringField(Desire.PSEUDO_TAG_DIRECT_THREATS);
		createPseudoStringField(Desire.PSEUDO_TAG_FACTOR);
		createPseudoRefListField(Desire.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
		createPseudoRefListField(Desire.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS);
		createPseudoRefListField(Desire.PSEUDO_TAG_RELEVANT_ACTIVITY_REFS);
		createPseudoStringField(Desire.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE);
		createPseudoStringField(Desire.PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS);
	}
}
