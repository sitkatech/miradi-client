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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.Vector;

import org.miradi.objects.Desire;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaRelevancyOverrideSetData;

public class DesireSchemaWriter extends BaseObjectSchemaWriter
{
	public DesireSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		return writeSingleTagAsTwoSchemaElements();
	}

	private Vector<String> writeSingleTagAsTwoSchemaElements()
	{
		Vector<String> schemaElements = new Vector<String>();
		schemaElements.add(getXmpz2XmlSchemaCreator().createRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_STRATEGY_IDS)));
		schemaElements.add(getXmpz2XmlSchemaCreator().createRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_ACTIVITY_IDS)));
		
		return schemaElements;
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
}
