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
package org.miradi.rtf;

import org.miradi.forms.HumanWelfareTargetPropertiesForm;
import org.miradi.forms.PanelHolderSpec;
import org.miradi.forms.PropertiesPanelSpec;
import org.miradi.forms.objects.*;
import org.miradi.objects.*;

public class ViabilityObjectToFormMap
{
	public static PropertiesPanelSpec getForm(BaseObject baseObject)
	{
		int objectType = baseObject.getType();
		if (Target.is(objectType))
			return new TargetPropertiesForm();

		if (HumanWelfareTarget.is(objectType))
			return new HumanWelfareTargetPropertiesForm();
		
		if (Goal.is(objectType))
			return new GoalPropertiesForm();
		
		if (Cause.is(objectType))
			return getCauseForm((Cause) baseObject);
		
		if (ThreatReductionResult.is(objectType))
			return new ThreatReductionResultPropertiesForm();
		
		if (IntermediateResult.is(objectType))
			return new IntermediateResultsPropertiesForm();
		
		if (Objective.is(objectType))
			return new ObjectivePropertiesForm();
		
		if (Strategy.is(objectType))
			return new StrategyPropertiesForm();
		
		if (Task.is(objectType))
			return getTaskForm((Task) baseObject);
		
		if (Method.is(objectType))
			return getMethodForm((Method) baseObject);

		if (Indicator.is(objectType))
			return getIndicatorForm((Indicator) baseObject);
		
		if (Measurement.is(objectType))
			return new MeasurementPropertiesForm();
		
		if (KeyEcologicalAttribute.is(objectType))
			return new KeyEcologicalAttributePropertiesForm();
		
		if (ProjectResource.is(objectType))
			return new ResourcePropertiesForm();
		
		if (FundingSource.is(objectType))
			return new BudgetCategoryPropertiesForm(objectType);
		
		if (AccountingCode.is(objectType))
			return new BudgetCategoryPropertiesForm(objectType);
		
		if (Organization.is(objectType))
			return new OrganizationPropertiesForm();
		
		if (ResultsChainDiagram.is(objectType))
			return new ResultsChainPropertiesForm();
		
		if (ProjectMetadata.is(objectType))
			return new ViabilityProjectForm();

		if (FutureStatus.is(objectType))
			return new FutureStatusPropertiesForm();

		if (AnalyticalQuestion.is(objectType))
			return new AnalyticalQuestionPropertiesForm();

		if (Assumption.is(objectType))
			return new AssumptionPropertiesForm();

		throw new RuntimeException("Form not found for type:" + objectType);
	}

	private static MethodPropertiesForm getMethodForm(Method method)
	{
		return new MethodPropertiesForm();
	}

	private static PanelHolderSpec getIndicatorForm(Indicator indicator)
	{
		if (indicator.isViabilityIndicator())
			return new ViabilityIndicatorPropertiesForm();

		return new IndicatorPropertiesForm();
	}

	private static PropertiesPanelSpec getCauseForm(Cause baseObject)
	{
		if (baseObject.isDirectThreat())
			return new ThreatPropertiesForm();
		
		throw new RuntimeException("Form not found for cause");
	}

	private static PropertiesPanelSpec getTaskForm(Task task)
	{
		if (task.isActivity())
			return new ActivityPropertiesForm();

		return new TaskPropertiesForm();
	}
}
