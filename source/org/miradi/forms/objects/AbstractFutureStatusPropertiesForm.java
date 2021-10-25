/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.objects.Indicator;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;

abstract public class AbstractFutureStatusPropertiesForm extends FieldPanelSpec
{
	public AbstractFutureStatusPropertiesForm()
	{
		final int type = FutureStatusSchema.getObjectType();
		addLabelAndField(type, FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY, TYPE_SINGLE_LINE_STRING);
		if (isViabilityFutureStatus())
			addChoiceField(type, FutureStatusSchema.TAG_FUTURE_STATUS_RATING, StatusQuestion.class);
		
		addLabelAndReadOnlySingleLineField(IndicatorSchema.getObjectType(), Indicator.TAG_UNIT);
		addDateField(type, FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
		addLabelAndField(type, FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL, TYPE_MULTILINE_STRING);
		addLabelAndField(type, FutureStatusSchema.TAG_FUTURE_STATUS_COMMENTS, TYPE_MULTILINE_STRING);
	}

	abstract protected boolean isViabilityFutureStatus();
}
