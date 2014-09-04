/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import java.util.Iterator;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DynamicChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

abstract public class BaseObjectSchema implements Iterable<AbstractFieldSchema>, Xmpz2XmlConstants
{
	public BaseObjectSchema()
	{
		fieldSchemas = new Vector<AbstractFieldSchema>();
		fillFieldSchemas();
	}
	
	public AbstractFieldSchema getFieldSchema(final String tag)
	{
		for(AbstractFieldSchema fieldSchema : this)
		{
			if (fieldSchema.getTag().equals(tag))
				return fieldSchema;
		}
		
		throw new RuntimeException("Tag is not contained in this schema. Tag = " + tag);
	}
	
	protected AbstractFieldSchema addPseudoFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchema.setIsPseudoField();
		return addFieldSchema(fieldSchema);
	}
	
	public AbstractFieldSchema addOwnedFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchema.setIsOwned();
		return addFieldSchema(fieldSchema);
	}
	
	protected AbstractFieldSchema addFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchemas.add(fieldSchema);
		return fieldSchema;
	}
	
	private Vector<AbstractFieldSchema> getFieldSchemas()
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
	
	//FIXME urgent : instead of using createFieldSchemaRequiredChoice() for fields that use a question
	//that have a readable default choice,  use this method,  but place a condition if none of the choices
	// are equal to "", then call the createFieldSchemaRequiredChoice;.  Then make createFieldSchemaRequiredChoice private. 
	public AbstractFieldSchema createFieldSchemaChoice(final String fieldTag, Class questionClass)
	{
		return addFieldSchema(new FieldSchemaChoice(fieldTag, getQuestion(questionClass)));
	}
	
	public AbstractFieldSchema createFieldSchemaRequiredChoice(final String fieldTag, Class questionClass)
	{
		return addFieldSchema(new FieldSchemaRequiredChoice(fieldTag, getQuestion(questionClass)));
	}
	
	public AbstractFieldSchema createFieldSchemaRequiredChoice(final String fieldTag, DynamicChoiceQuestion questionToUse)
	{
		return addFieldSchema(new FieldSchemaRequiredChoice(fieldTag, questionToUse));
	}
	
	public AbstractFieldSchema createFieldSchemaIdList(final String fieldTag, final int objectType)
	{
		return addFieldSchema(new FieldSchemaIdList(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createOwnedFieldSchemaIdList(final String fieldTag, final int objectType)
	{
		return addOwnedFieldSchema(new FieldSchemaIdList(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createFieldSchemaCodeToUserStringMap(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaCodeToUserStringMap(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaRequiredBaseId(final String fieldTag, final int objectType)
	{
		return addFieldSchema(new FieldSchemaRequiredBaseId(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createFieldSchemaOptionalBaseId(final String fieldTag, final int objectType)
	{
		return addFieldSchema(new FieldSchemaOptionalBaseId(fieldTag, objectType));
	}
	
	public AbstractFieldSchema createFieldSchemaRequiredRef(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaRequiredRef(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaOptionalRef(final String fieldTag)
	{
		return addFieldSchema(new FieldSchemaOptionalRef(fieldTag));
	}
	
	public AbstractFieldSchema createOwnedFieldSchemaRef(final String fieldTag)
	{
		return addOwnedFieldSchema(new FieldSchemaRequiredRef(fieldTag));
	}
	
	public AbstractFieldSchema createFieldSchemaReflist(final String fieldTag, final String typeName)
	{
		return addFieldSchema(new FieldSchemaReflist(fieldTag, typeName));
	}
	
	public AbstractFieldSchema createOwnedFieldSchemaReflist(final String fieldTag, final String typeName)
	{
		return addOwnedFieldSchema(new FieldSchemaReflist(fieldTag, typeName));
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
	
	public AbstractFieldSchema createFieldSchemaExpandingUserText(String tag)
	{
		return addFieldSchema(new FieldSchemaExpandingUserText(tag));
	}
	
	public AbstractFieldSchema createTaxonomyClassifications(String tag)
	{
		return addFieldSchema(new FieldSchemaTaxonomyClassifications(tag));
	}
	
	public AbstractFieldSchema createTaxonomyElementList(String tag)
	{
		return addFieldSchema(new FieldSchemaTaxonomyElementList(tag));
	}
	
	public void createPseudoFieldSchemaString(final String fieldTag)
	{
		addPseudoFieldSchema(new FieldSchemaPseudoStringField(fieldTag));
	}
	
	public void createPseudoFieldSchemaQuestion(final String fieldTag)
	{
		addPseudoFieldSchema(new FieldSchemaPseudoQuestionField(fieldTag));
	}
	
	public void createPseudoFieldSchemaRefList(String tag)
	{
		addPseudoFieldSchema(new FieldSchemaPseudoReflistField(tag));
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
		if (hasLabel())
			createFieldSchemaExpandingUserText(BaseObject.TAG_LABEL);
	}

	protected boolean hasLabel()
	{
		return true;
	}

	protected void createBudgetSchemas()
	{
		createOwnedFieldSchemaIdList(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, ResourceAssignmentSchema.getObjectType());
		createOwnedFieldSchemaReflist(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS, EXPENSE_ASSIGNMENT);
		
		createPseudoFieldSchemaString(BaseObject.PSEUDO_TAG_WHEN_TOTAL);
	}

	protected void createProgressReportSchema()
	{
		createOwnedFieldSchemaReflist(BaseObject.TAG_PROGRESS_REPORT_REFS, PROGRESS_REPORT);
		
		createPseudoFieldSchemaQuestion(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE);
		createPseudoFieldSchemaString(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS);
	}
	
	protected void createTaxonomyClassificationSchemaField()
	{
		createTaxonomyClassifications(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
	}
	
	public boolean isPseudoField(final String tag)
	{
		return getFieldSchema(tag).isPseudoField();
	}
	
	public String getXmpz2ElementName()
	{
		return getObjectName();
	}
	
	public boolean containsField(String tag)
	{
		for(AbstractFieldSchema fieldSchema : this)
		{
			if (fieldSchema.getTag().equals(tag))
				return true;
		}
		return false;
	}
	
	public Iterator<AbstractFieldSchema> iterator()
	{
		return fieldSchemas.iterator();
	}
	
	abstract public int getType();
	
	abstract public String getObjectName();
	
	private Vector<AbstractFieldSchema> fieldSchemas;
}
