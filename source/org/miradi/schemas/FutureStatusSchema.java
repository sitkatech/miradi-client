/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objects.Indicator;
import org.miradi.questions.StatusQuestion;

public class FutureStatusSchema extends BaseObjectSchema
{
	public FutureStatusSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();	
		
		createFieldSchemaChoice(Indicator.TAG_FUTURE_STATUS_RATING, StatusQuestion.class);
		createFieldSchemaDate(Indicator.TAG_FUTURE_STATUS_DATE);
		createFieldSchemaSingleLineUserText(Indicator.TAG_FUTURE_STATUS_SUMMARY);
		createFieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_DETAIL);
		createFieldSchemaMultiLineUserText(Indicator.TAG_FUTURE_STATUS_COMMENTS);
	}
	
	public static int getObjectType()
	{
		return ObjectType.FUTURE_STATUS;
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
	
	public static final String OBJECT_NAME = "FutureStatus";
	
	public static final String TAG_FUTURE_STATUS_RATING  = "FutureStatusRating";
	public static final String TAG_FUTURE_STATUS_DATE = "FutureStatusDate";
	public static final String TAG_FUTURE_STATUS_SUMMARY = "FutureStatusSummary";
	public static final String TAG_FUTURE_STATUS_DETAIL = "FutureStatusDetail";
	public static final String TAG_FUTURE_STATUS_COMMENTS = "FutureStatusComment";
}	
