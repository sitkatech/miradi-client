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
	
	@Override
	public BaseObjectSchema createSchema()
	{
		BaseObjectSchema schema = new BaseObjectSchema();
		schema.createFieldSchemaSingleLineUserText(Indicator.TAG_SHORT_LABEL);
		schema.createFieldSchemaChoice(Indicator.TAG_PRIORITY, getQuestion(PriorityRatingQuestion.class));
		schema.createFieldSchemaIdList(Indicator.TAG_METHOD_IDS, Task.getObjectType());
		schema.createFieldSchemaCodeToUserStringMap(Indicator.TAG_THRESHOLDS_MAP);
		schema.createFieldSchemaCodeToUserStringMap(Indicator.TAG_THRESHOLD_DETAILS_MAP);
		schema.createFieldSchemaChoice(Indicator.TAG_RATING_SOURCE, getQuestion(RatingSourceQuestion.class));
		schema.createFieldSchemaReflist(Indicator.TAG_MEASUREMENT_REFS);
		schema.createFieldSchemaMultiLineUserText(Indicator.TAG_DETAIL);
		schema.createFieldSchemaMultiLineUserText(Indicator.TAG_COMMENTS);
		schema.createFieldSchemaMultiLineUserText(Indicator.TAG_VIABILITY_RATINGS_COMMENTS);
		schema.createFieldSchemaChoice(Indicator.TAG_FUTURE_STATUS_RATING, getQuestion(StatusQuestion.class));
		schema.createFieldSchemaDate(Indicator.TAG_FUTURE_STATUS_DATE);
		schema.createFieldSchemaSingleLineUserText(Indicator.TAG_FUTURE_STATUS_SUMMARY);
		schema.createFieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_DETAIL);
		schema.createFieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_COMMENTS);
		
		return schema;
	}

	private ChoiceQuestion getQuestion(final Class questionClass)
	{
		return StaticQuestionManager.getQuestion(questionClass);
	}
}
