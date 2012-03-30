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
package org.miradi.questions;

import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Strategy;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;

public class DiagramLegendQuestion extends StaticChoiceQuestion
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
				new ChoiceItem(FactorLink.OBJECT_NAME, FactorLink.OBJECT_NAME),
				new ChoiceItem(GoalSchema.OBJECT_NAME, GoalSchema.OBJECT_NAME),
				new ChoiceItem(ObjectiveSchema.OBJECT_NAME, ObjectiveSchema.OBJECT_NAME),
				new ChoiceItem(IndicatorSchema.OBJECT_NAME, IndicatorSchema.OBJECT_NAME),
				new ChoiceItem(TextBoxSchema.OBJECT_NAME, TextBoxSchema.OBJECT_NAME),
				new ChoiceItem(ScopeBoxSchema.OBJECT_NAME, ScopeBoxSchema.OBJECT_NAME),
				new ChoiceItem(STRESS_HIDDEN_TYPE_CODE, StressSchema.OBJECT_NAME),
				new ChoiceItem(TaskSchema.ACTIVITY_NAME, TaskSchema.ACTIVITY_NAME),
				new ChoiceItem(IntermediateResultSchema.OBJECT_NAME, IntermediateResultSchema.OBJECT_NAME),
				new ChoiceItem(ThreatReductionResult.OBJECT_NAME, ThreatReductionResult.OBJECT_NAME),
				new ChoiceItem(GroupBoxSchema.OBJECT_NAME, GroupBoxSchema.OBJECT_NAME),
		};
	}

	public static final String STRESS_HIDDEN_TYPE_CODE = StressSchema.OBJECT_NAME;
}
