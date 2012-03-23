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

import java.util.HashSet;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;
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
	
	public void createFieldSchemaCodeField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaCode(fieldTag));
	}
	
	public void createFieldSchemaIntegerField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaInteger(fieldTag));
	}

	public void createFieldSchemaDateUnitListField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaDateUnitList(fieldTag));
	}

	public void createFieldSchemaCodeToCodeListMapField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaCodeToCodeListMap(fieldTag));
	}

	public void createFieldSchemaRefListList(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaRefListList(fieldTag));
	}

	public void createFieldSchemaTagList(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaTagList(fieldTag));
	}

	public void createFieldSchemaCodeToCodeMapField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaCodeToCodeMap(fieldTag));
	}
	
	public void createPseudoStringField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoStringField(fieldTag));
	}
	
	public void createPseudoQuestionField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoQuestionField(fieldTag));
	}
	
	public void setNonUserField(String fieldTag)
	{
		nonUserFields.add(fieldTag);
	}
	
	public HashSet<String> getNonUserFields()
	{
		return nonUserFields;
	}
	
	public Vector<ObjectData> createFields(BaseObject baseObjectToUse)
	{
		Vector<ObjectData> fields = new Vector<ObjectData>();
		for(AbstractFieldSchema fieldSchema : getFieldSchemas())
		{
			ObjectData field = fieldSchema.createField(baseObjectToUse);
			fields.add(field);
		}
		
		return fields;
	}
	
	private Vector<AbstractFieldSchema> fieldSchemas;
	private HashSet<String> nonUserFields;
}
