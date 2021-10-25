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
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.IndicatorSchema;

public class IndicatorViabilityRatingsSubForm extends FieldPanelSpec
{
	public IndicatorViabilityRatingsSubForm()
	{
		ChoiceQuestion statusQuestion = new StatusQuestion();
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.POOR));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.FAIR));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.GOOD));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.VERY_GOOD));
				
		addLabelAndField(IndicatorSchema.getObjectType(), Indicator.TAG_VIABILITY_RATINGS_EVIDENCE_CONFIDENCE);
		addLabelAndField(IndicatorSchema.getObjectType(), Indicator.TAG_VIABILITY_RATINGS_COMMENTS);
		addLabelAndField(IndicatorSchema.getObjectType(), Indicator.TAG_VIABILITY_RATINGS_EVIDENCE_NOTES);
	}
	
	private void createThresholdField(ChoiceItem choiceItem)
	{
		addLabelAndFieldsWithLabels(choiceItem.getLabel(), IndicatorSchema.getObjectType(), new String[]{Indicator.TAG_THRESHOLDS_MAP, Indicator.TAG_THRESHOLD_DETAILS_MAP});
	}
}
