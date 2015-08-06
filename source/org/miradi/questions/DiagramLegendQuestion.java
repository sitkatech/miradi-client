/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.questions;

import org.miradi.objects.Cause;
import org.miradi.objects.Strategy;
import org.miradi.schemas.*;

public class DiagramLegendQuestion extends MultipleSelectStaticChoiceQuestion
{
	public DiagramLegendQuestion()
	{
		super(getLegendChoices());
	}

	static ChoiceItem[] getLegendChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(StrategySchema.OBJECT_NAME, StrategySchema.OBJECT_NAME),
				new ChoiceItem(Strategy.OBJECT_NAME_DRAFT, Strategy.OBJECT_NAME_DRAFT),
				new ChoiceItem(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, Cause.OBJECT_NAME_CONTRIBUTING_FACTOR),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, Cause.OBJECT_NAME_THREAT),
				new ChoiceItem(TargetSchema.OBJECT_NAME, TargetSchema.OBJECT_NAME),
				new ChoiceItem(HumanWelfareTargetSchema.OBJECT_NAME, HumanWelfareTargetSchema.OBJECT_NAME),
				new ChoiceItem(BiophysicalFactorSchema.OBJECT_NAME, BiophysicalFactorSchema.OBJECT_NAME),
				new ChoiceItem(BiophysicalResultSchema.OBJECT_NAME, BiophysicalResultSchema.OBJECT_NAME),
				new ChoiceItem(FactorLinkSchema.OBJECT_NAME, FactorLinkSchema.OBJECT_NAME),
				new ChoiceItem(GoalSchema.OBJECT_NAME, GoalSchema.OBJECT_NAME),
				new ChoiceItem(ObjectiveSchema.OBJECT_NAME, ObjectiveSchema.OBJECT_NAME),
				new ChoiceItem(IndicatorSchema.OBJECT_NAME, IndicatorSchema.OBJECT_NAME),
				new ChoiceItem(TextBoxSchema.OBJECT_NAME, TextBoxSchema.OBJECT_NAME),
				new ChoiceItem(ScopeBoxSchema.OBJECT_NAME, ScopeBoxSchema.OBJECT_NAME),
				new ChoiceItem(STRESS_HIDDEN_TYPE_CODE, StressSchema.OBJECT_NAME),
				new ChoiceItem(TaskSchema.ACTIVITY_NAME, TaskSchema.ACTIVITY_NAME),
				new ChoiceItem(IntermediateResultSchema.OBJECT_NAME, IntermediateResultSchema.OBJECT_NAME),
				new ChoiceItem(ThreatReductionResultSchema.OBJECT_NAME, ThreatReductionResultSchema.OBJECT_NAME),
				new ChoiceItem(GroupBoxSchema.OBJECT_NAME, GroupBoxSchema.OBJECT_NAME),
		};
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(HumanWelfareTargetSchema.OBJECT_NAME))
			return HumanWelfareTargetSchema.HUMAN_WELLBEING_TARGET;
		
		return super.convertToReadableCode(code);
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(HumanWelfareTargetSchema.HUMAN_WELLBEING_TARGET))
			return HumanWelfareTargetSchema.OBJECT_NAME;
		
		return super.convertToInternalCode(code);
	}
	
	public static final String STRESS_HIDDEN_TYPE_CODE = StressSchema.OBJECT_NAME;
}
