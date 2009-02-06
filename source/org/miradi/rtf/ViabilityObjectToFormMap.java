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
package org.miradi.rtf;

import org.miradi.forms.PanelHolderSpec;
import org.miradi.forms.PropertiesPanelSpec;
import org.miradi.forms.objects.AccountingCodePropertiesForm;
import org.miradi.forms.objects.ActivityPropertiesForm;
import org.miradi.forms.objects.FundingSourcePropertiesForm;
import org.miradi.forms.objects.GoalPropertiesForm;
import org.miradi.forms.objects.IndicatorPropertiesForm;
import org.miradi.forms.objects.IntermediateResultsPropertiesForm;
import org.miradi.forms.objects.KeyEcologicalAttributePropertiesForm;
import org.miradi.forms.objects.MeasurementPropertiesForm;
import org.miradi.forms.objects.MethodPropetiesForm;
import org.miradi.forms.objects.ObjectivePropertiesForm;
import org.miradi.forms.objects.OrganizationPropertiesForm;
import org.miradi.forms.objects.ResourcePropertiesForm;
import org.miradi.forms.objects.ResultsChainPropertiesForm;
import org.miradi.forms.objects.StrategyPropertiesForm;
import org.miradi.forms.objects.TargetPropertiesForm;
import org.miradi.forms.objects.TaskPropertiesForm;
import org.miradi.forms.objects.ThreatPropertiesForm;
import org.miradi.forms.objects.ThreatReductionResultPropertiesForm;
import org.miradi.forms.objects.ViabilityIndicatorPropertiesForm;
import org.miradi.forms.objects.ViabilityProjectForm;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Organization;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;

public class ViabilityObjectToFormMap
{
	public static PropertiesPanelSpec getForm(BaseObject baseObject)
	{
		int objectType = baseObject.getType();
		if (Target.is(objectType))
			return new TargetPropertiesForm();

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
		
		if (Indicator.is(objectType))
			return getIndicatorForm((Indicator) baseObject);
		
		if (Measurement.is(objectType))
			return new MeasurementPropertiesForm();
		
		if (KeyEcologicalAttribute.is(objectType))
			return new KeyEcologicalAttributePropertiesForm();
		
		if (ProjectResource.is(objectType))
			return new ResourcePropertiesForm();
		
		if (FundingSource.is(objectType))
			return new FundingSourcePropertiesForm();
		
		if (AccountingCode.is(objectType))
			return new AccountingCodePropertiesForm();
		
		if (Organization.is(objectType))
			return new OrganizationPropertiesForm();
		
		if (ResultsChainDiagram.is(objectType))
			return new ResultsChainPropertiesForm();
		
		if (ProjectMetadata.is(objectType))
			return new ViabilityProjectForm();

		throw new RuntimeException("Form not found for type:" + objectType);
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
		
		if (task.isMethod())
			return new MethodPropetiesForm();
		
		return new TaskPropertiesForm();
	}
}
