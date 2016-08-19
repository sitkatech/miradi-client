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

import org.miradi.objects.BaseObject;
import org.miradi.schemas.BaseObjectSchema;

import java.util.Vector;

abstract public class BaseObjectSchemaWriterWithCalculatedCostsElement extends BaseObjectSchemaWriterWithTaxonomyClassificationContainer
{
	public BaseObjectSchemaWriterWithCalculatedCostsElement(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(BaseObject.TAG_ASSIGNED_LEADER_RESOURCE))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		Vector<String> schemaElements = super.createCustomSchemaFields();
		
		schemaElements.add(getXmpz2XmlSchemaCreator().createOptionalSchemaElement(getBaseObjectSchema(), getBaseObjectSchema().getFieldSchema(BaseObject.TAG_ASSIGNED_LEADER_RESOURCE), RESOURCE_ID));

		return schemaElements;
	}

	
	@Override
	public Vector<String> createFieldSchemas() throws Exception
	{
		Vector<String> schemaElements = super.createFieldSchemas();
		
		schemaElements.add(getXmpz2XmlSchemaCreator().writeCalculatedCostSchemaElement(getBaseObjectSchema()));
		schemaElements.add(getXmpz2XmlSchemaCreator().writeCalculatedTimeframeSchemaElement(getBaseObjectSchema()));

		return schemaElements;
	}	
}
