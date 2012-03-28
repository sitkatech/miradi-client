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

package org.miradi.schemas;

import java.util.HashSet;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
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
	
	public AbstractFieldSchema createFieldSchemaCode(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCode(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaInteger(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaInteger(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaDateUnitList(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaDateUnitList(fieldTag));
	}

	public AbstractFieldSchema createFieldSchemaCodeToCodeListMap(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCodeToCodeListMap(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeToChoiceMap(String tag)
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

	public AbstractFieldSchema createFieldSchemaCodeToCodeMap(final String fieldTag)
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
	
	public AbstractFieldSchema createFieldSchemaStringRefMap(String tag)
	{
		return addFieldSchema(new FieldSchemaStringRefMap(tag));
	}

	public AbstractFieldSchema createFieldSchemaFloat(String tag)
	{
		return addFieldSchema(new FieldSchemaFloat(tag));
	}
	
	private AbstractFieldSchema createFieldSchemaExpandingUserText(String tag)
	{
		return addFieldSchema(new FieldSchemaExpandingUserText(tag));
	}
	
	public void createPseudoFieldSchemaString(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoStringField(fieldTag));
	}
	
	public void createPseudoFieldSchemaQuestion(final String fieldTag)
	{
		addFieldSchema(new FieldSchemaPseudoQuestionField(fieldTag));
	}
	
	public void createPseudoFieldSchemaQuestion(String tag, HashSet<String> set)
	{
		addFieldSchema(new FieldSchemaPseudoQuestionField(tag, set));
	}
	
	public void createPseudoFieldSchemaRefList(String tag)
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
		createFieldSchemaExpandingUserText(BaseObject.TAG_LABEL);
		createFieldSchemaIdList(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, ResourceAssignment.getObjectType());
		createFieldSchemaReflist(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS);
		createFieldSchemaReflist(BaseObject.TAG_PROGRESS_REPORT_REFS);
		
		createPseudoFieldSchemaString(BaseObject.PSEUDO_TAG_WHEN_TOTAL);
		createPseudoFieldSchemaQuestion(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, BaseObject.createSet(BaseObject.TAG_PROGRESS_REPORT_REFS));
		createPseudoFieldSchemaString(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS);
	}
	
	private Vector<AbstractFieldSchema> fieldSchemas;
}
