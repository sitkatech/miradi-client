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
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;

public class BaseObjectSchema
{
	public BaseObjectSchema()
	{
		fieldSchemas = new Vector<AbstractFieldSchema>();
		fillFieldSchemas();
	}
	
	private AbstractFieldSchema addFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchemas.add(fieldSchema);
		return fieldSchema;
	}
	
	public Vector<AbstractFieldSchema> getFieldSchemas()
	{
		return fieldSchemas;
	}
	
	public AbstractFieldSchema createFieldSchemaSingleLineUserText(String fieldTag)
	{
		return addFieldSchema(new FieldSchemaSingleLineUserText(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaChoice(final String fieldTag, final ChoiceQuestion question)
	{
		return addFieldSchema(new FieldSchemaChoice(fieldTag, question));
	}
	
	public AbstractFieldSchema createFieldSchemaIdList(final String fieldTag, final int objectType)
	{
		return addFieldSchema(new FieldSchemaIdList(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeToUserStringMapField(String tag)
	{
		return addFieldSchema(new FieldSchemaCodeToUserStringMapData(tag));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeToUserStringMap(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCodeToUserStringMap(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaBaseId(final String fieldTag, final int objectType)
	{
		return addFieldSchema(new FieldSchemaBaseId(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createFieldSchemaRef(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaRef(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaReflist(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaReflist(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaMultiLineUserText(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaMultiLineUserText(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaDate(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaDate(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeField(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCode(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaIntegerField(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaInteger(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaDateUnitListField(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaDateUnitList(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaCodeToCodeListMapField(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCodeToCodeListMap(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeToChoiceMapField(String tag)
	{
		return addFieldSchema(new FieldSchemaCodeToChoiceMapData(tag));
	}

	public AbstractFieldSchema createFieldSchemaRefListList(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaRefListList(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaTagList(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaTagList(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaCodeToCodeMapField(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCodeToCodeMap(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaDateUnitEffortList(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaDateUnitEffortList(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaRelevancyOverrideSet(String tag)
	{
		return addFieldSchema(new FieldSchemaRelevancyOverrideSetData(tag));
	}
	
	public AbstractFieldSchema createFieldSchemaPoint(String tag)
	{
		return addFieldSchema(new FieldSchemaPoint(tag));
	}

	public AbstractFieldSchema createFieldSchemaDimension(String tag)
	{
		return addFieldSchema(new FieldSchemaDimension(tag));
	}
	
	public AbstractFieldSchema createFieldSchemaBoolean(String tag)
	{
		return addFieldSchema(new FieldSchemaBoolean(tag));
	}

	public AbstractFieldSchema createFieldSchemaPointList(String tag)
	{
		return addFieldSchema(new FieldSchemaPointList(tag));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeList(final String tag, final ChoiceQuestion questionToUse)
	{
		return addFieldSchema(new FieldSchemaCodeList(tag, questionToUse));	
	}

	public AbstractFieldSchema createFieldSchemaNumber(final String tag)
	{
		return addFieldSchema(new FieldSchemaNumber(tag));
	}
	
	public AbstractFieldSchema createFieldSchemaPercentage(final String tag)
	{
		return addFieldSchema(new FieldSchemaPercentage(tag));
	}
	
	public void createPseudoStringField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoStringField(fieldTag));
	}
	
	public void createPseudoQuestionField(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoQuestionField(fieldTag));
	}
	
	public void createPseudoRefListField(String tag)
	{
		addFieldSchema(new FieldSchemaPseudoRefListData(tag));
	}

	
	public Vector<ObjectData> createFields(BaseObject baseObjectToUse)
	{
		Vector<ObjectData> fields = new Vector<ObjectData>();
		for(AbstractFieldSchema fieldSchema : getFieldSchemas())
		{
			ObjectData field = fieldSchema.createField(baseObjectToUse);
			field.setNavigationField(fieldSchema.isNavigationField());
			fields.add(field);
		}
		
		return fields;
	}
	
	protected static ChoiceQuestion getQuestion(Class questionClass)
	{
		return StaticQuestionManager.getQuestion(questionClass);
	}
	
	protected void fillFieldSchemas()
	{
	}
	
	private Vector<AbstractFieldSchema> fieldSchemas;
}
