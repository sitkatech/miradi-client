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

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Measurement;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;

public class MeasurementSchema extends BaseObjectSchema
{
	public MeasurementSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaChoice(Measurement.TAG_TREND, getQuestion(TrendQuestion.class));
		createFieldSchemaChoice(Measurement.TAG_STATUS, getQuestion(StatusQuestion.class));
		createFieldSchemaDate(Measurement.TAG_DATE);
		createFieldSchemaSingleLineUserText(Measurement.TAG_SUMMARY);
		createFieldSchemaMultiLineUserText(Measurement.TAG_DETAIL);
		createFieldSchemaChoice(Measurement.TAG_STATUS_CONFIDENCE, getQuestion(StatusConfidenceQuestion.class));
		createFieldSchemaMultiLineUserText(Measurement.TAG_COMMENTS);
	}

	public static int getObjectType()
	{
		return ObjectType.MEASUREMENT;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "Measurement";
}
