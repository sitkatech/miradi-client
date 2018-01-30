/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import java.util.HashSet;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class BaseObjectSchemaWriter implements Xmpz2XmlConstants
{
	public BaseObjectSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse)
	{
		this(creatorToUse, null);
	}
	
	public BaseObjectSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		creator = creatorToUse;
		baseObjectSchema = baseObjectSchemaToUse;
	}
	
	public Vector<String> createFieldSchemas() throws Exception
	{
		HashSet<String> fieldSchemasAsString = new HashSet<String>();
		if (hasIdAttributeElement())
			fieldSchemasAsString.add("attribute " + ID + " "+ "{xsd:integer}");
		
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			if (fieldSchema.isPseudoField())
				continue;
			
			if (shouldOmitField(fieldSchema.getTag()))
				continue;

			if (doesFieldRequireSpecialHandling(fieldSchema.getTag()))
				continue;

			ObjectData objectData = fieldSchema.createField(null);
			fieldSchemasAsString.add(objectData.createXmpz2SchemaElementString(getXmpz2XmlSchemaCreator(), getBaseObjectSchema(), fieldSchema));
		}
		
		fieldSchemasAsString.addAll(createCustomSchemaFields());
		final Vector<String> sortedFieldSchemas = new Vector<String>(fieldSchemasAsString);
		
		return sortedFieldSchemas;
	}
	
	protected boolean shouldOmitField(String tag)
	{
		if (tag.equalsIgnoreCase(BaseObject.TAG_UUID) && !ObjectType.requiresUUID(baseObjectSchema.getType()))
			return true;

		return false;
	}
	
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		return false;
	}

	protected Vector<String> createCustomSchemaFields()
	{
		return new Vector<String>();
	}

	protected boolean hasIdAttributeElement()
	{
		return true;
	}

	protected Xmpz2XmlSchemaCreator getXmpz2XmlSchemaCreator()
	{
		return creator;
	}

	public String getPoolName()
	{
		return Xmpz2XmlWriter.createPoolElementName(getXmpz2ElementName());
	}
	
	public String getXmpz2ElementName()
	{
		return getBaseObjectSchema().getXmpz2ElementName();
	}
	
	public BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	private Xmpz2XmlSchemaCreator creator;
	private BaseObjectSchema baseObjectSchema;
}
