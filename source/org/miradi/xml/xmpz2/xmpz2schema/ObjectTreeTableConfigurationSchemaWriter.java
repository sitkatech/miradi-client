/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import java.util.Vector;

import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.questions.CustomPlanningAllRowsQuestion;
import org.miradi.schemas.BaseObjectSchema;

public class ObjectTreeTableConfigurationSchemaWriter extends BaseObjectSchemaWriter
{
	public ObjectTreeTableConfigurationSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		Vector<String> schemaElements = super.createCustomSchemaFields();
		
		String customRowsSchemaElement = getXmpz2XmlSchemaCreator().createCodelistSchemaElement(getBaseObjectSchema(), getBaseObjectSchema().getFieldSchema(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION), new CustomPlanningAllRowsQuestion());
		schemaElements.add(customRowsSchemaElement);
		
		return schemaElements;
	}
}
