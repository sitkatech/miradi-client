/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IndicatorId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.project.ObjectManager;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaChoice;
import org.miradi.schemas.FieldSchemaCodeToUserStringMap;
import org.miradi.schemas.FieldSchemaDate;
import org.miradi.schemas.FieldSchemaIdList;
import org.miradi.schemas.FieldSchemaMultiLineUserText;
import org.miradi.schemas.FieldSchemaReflist;
import org.miradi.schemas.FieldSchemaSingleLineUserText;

public class IndicatorPool extends EAMNormalObjectPool
{
	public IndicatorPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.INDICATOR);
	}
	
	public void put(Indicator indicator) throws Exception
	{
		put(indicator.getId(), indicator);
	}
	
	public Indicator find(BaseId id)
	{
		return (Indicator)getRawObject(id);
	}

	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId)
	{
		return new Indicator(objectManager, new IndicatorId(actualId.asInt()), createSchema());
	}
	
	public Indicator[] getAllIndicators()
	{
		BaseId[] allIds = getIds();
		Indicator[] allIndicators = new Indicator[allIds.length];
		for (int i = 0; i < allIndicators.length; i++)
			allIndicators[i] = find(allIds[i]);
			
		return allIndicators;
	}
	
	public BaseObjectSchema createSchema()
	{
		BaseObjectSchema schema = new BaseObjectSchema();
		schema.addFieldSchema(new FieldSchemaSingleLineUserText(Indicator.TAG_SHORT_LABEL));
		schema.addFieldSchema(new FieldSchemaChoice(Indicator.TAG_PRIORITY, getQuestion(PriorityRatingQuestion.class)));
		schema.addFieldSchema(new FieldSchemaIdList(Indicator.TAG_METHOD_IDS, Task.getObjectType()));
		schema.addFieldSchema(new FieldSchemaCodeToUserStringMap(Indicator.TAG_THRESHOLDS_MAP));
		schema.addFieldSchema(new FieldSchemaCodeToUserStringMap(Indicator.TAG_THRESHOLD_DETAILS_MAP));
		schema.addFieldSchema(new FieldSchemaChoice(Indicator.TAG_RATING_SOURCE, getQuestion(RatingSourceQuestion.class)));
		schema.addFieldSchema(new FieldSchemaReflist(Indicator.TAG_MEASUREMENT_REFS));
		schema.addFieldSchema(new FieldSchemaMultiLineUserText(Indicator.TAG_DETAIL));
		schema.addFieldSchema(new FieldSchemaMultiLineUserText(Indicator.TAG_COMMENTS));
		schema.addFieldSchema(new FieldSchemaMultiLineUserText(Indicator.TAG_VIABILITY_RATINGS_COMMENTS));
		schema.addFieldSchema(new FieldSchemaChoice(Indicator.TAG_FUTURE_STATUS_RATING, getQuestion(StatusQuestion.class)));
		schema.addFieldSchema(new FieldSchemaDate(Indicator.TAG_FUTURE_STATUS_DATE));
		schema.addFieldSchema(new FieldSchemaSingleLineUserText(Indicator.TAG_FUTURE_STATUS_SUMMARY));
		schema.addFieldSchema(new FieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_DETAIL));
		schema.addFieldSchema(new FieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_COMMENTS));
		
		return schema;
	}

	private ChoiceQuestion getQuestion(final Class questionClass)
	{
		return StaticQuestionManager.getQuestion(questionClass);
	}
}
