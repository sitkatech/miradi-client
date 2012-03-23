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

import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.questions.ChoiceQuestion;

public class BaseObjectSchema
{
	public BaseObjectSchema()
	{
		fieldSchemas = new Vector<AbstractFieldSchema>();
	}
	
	private void addFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchemas.add(fieldSchema);
	}
	
	public Vector<AbstractFieldSchema> getFieldSchemas()
	{
		return fieldSchemas;
	}
	
	public void createFieldSchemaSingleLineUserText(String fieldTag)
	{
		addFieldSchema(new FieldSchemaSingleLineUserText(fieldTag));
	}
	
	public void createFieldSchemaChoice(final String fieldTag, final ChoiceQuestion question)
	{
		addFieldSchema(new FieldSchemaChoice(fieldTag, question));
	}
	
	public void createFieldSchemaIdList(final String fieldTag, final int objectType)
	{
		addFieldSchema(new FieldSchemaIdList(fieldTag, objectType));
	}
	
	public void createFieldSchemaCodeToUserStringMap(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaCodeToUserStringMap(fieldTag));
	}
	
	public void createFieldSchemaReflist(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaReflist(fieldTag));
	}
	
	public void createFieldSchemaMultiLineUserText(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaMultiLineUserText(fieldTag));
	}
	
	public void createFieldSchemaDate(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaDate(fieldTag));
	}
	
	public Vector<ObjectData> createTagFields()
	{
		Vector<ObjectData> fields = new Vector<ObjectData>();
		for(AbstractFieldSchema fieldSchema : getFieldSchemas())
		{
			ObjectData field = fieldSchema.createField();
			fields.add(field);
		}
		
		return fields;
	}
	
	private Vector<AbstractFieldSchema> fieldSchemas;
}
