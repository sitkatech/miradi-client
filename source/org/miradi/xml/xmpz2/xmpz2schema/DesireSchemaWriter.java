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

import org.miradi.objects.Desire;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaRelevancyOverrideSetData;
import org.miradi.xml.generic.SchemaWriter;

public class DesireSchemaWriter extends BaseObjectSchemaWriter
{
	public DesireSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	public void writeFields(SchemaWriter writer) throws Exception
	{
		super.writeFields(writer);
		
		writeSingleTagAsTwoSchemaElements();
	}

	private void writeSingleTagAsTwoSchemaElements()
	{
		getCreator().writeRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_STRATEGY_IDS));
		getCreator().getSchemaWriter().println(" &");
		
		getCreator().writeRelevantSchemaElement(getBaseObjectSchema(), new FieldSchemaRelevancyOverrideSetData(RELEVANT_ACTIVITY_IDS));
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
}
