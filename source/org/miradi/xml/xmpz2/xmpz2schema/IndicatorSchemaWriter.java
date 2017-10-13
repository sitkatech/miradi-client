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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.Indicator;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaRelevancyOverrideSetData;

import java.util.Vector;

public class IndicatorSchemaWriter extends BaseObjectSchemaWriterWithTaxonomyClassificationContainer
{
	public IndicatorSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		Vector<String> schemaElements = super.createCustomSchemaFields();
		schemaElements.add(getXmpz2XmlSchemaCreator().createThresholdsSchemaElement(getBaseObjectSchema()));
		schemaElements.add(getXmpz2XmlSchemaCreator().createRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_STRATEGY_IDS)));
		schemaElements.add(getXmpz2XmlSchemaCreator().createRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_ACTIVITY_IDS)));

		return schemaElements;
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Indicator.TAG_THRESHOLDS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_THRESHOLD_DETAILS_MAP))
			return true;

		if (tag.equals(Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		// TODO: fields to be deprecated in post 4.4 release...only here to support migrations
        // listed here so they can be excluded from the schema

		if (tag.equals(Indicator.TAG_ASSIGNED_LEADER_RESOURCE))
			return true;

		if (tag.equals(Indicator.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;

		if (tag.equals(Indicator.TAG_EXPENSE_ASSIGNMENT_REFS))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}	
}
