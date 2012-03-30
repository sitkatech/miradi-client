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

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.icons.ActivityIcon;
import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.HumanWelfareTargetIcon;
import org.miradi.icons.IconManager;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.MeasurementIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.SubTargetIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.Measurement;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TargetSchema;

public class CustomPlanningRowsQuestion extends DynamicChoiceQuestion
{
	public CustomPlanningRowsQuestion(Project projectToUse)
	{
		super();

		project = projectToUse;
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		return getRowChoices().toArray(new ChoiceItem[0]);
	}

	private Vector<ChoiceItem> getRowChoices()
	{	
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();

		choiceItems.add(createChoiceItem(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagramSchema.OBJECT_NAME, new ConceptualModelIcon()));
		choiceItems.add(createChoiceItem(ResultsChainDiagramSchema.getObjectType(), ResultsChainDiagramSchema.OBJECT_NAME, new ResultsChainIcon()));
		choiceItems.add(createChoiceItem(TargetSchema.getObjectType(), TargetSchema.OBJECT_NAME, new TargetIcon()));
		
		if (getProject().getMetadata().isHumanWelfareTargetMode())
			choiceItems.add(createChoiceItem(HumanWelfareTargetSchema.getObjectType(), HumanWelfareTargetSchema.OBJECT_NAME, new HumanWelfareTargetIcon()));
		
		choiceItems.add(createChoiceItem(SubTarget.getObjectType(), SubTarget.OBJECT_NAME, new SubTargetIcon()));
		choiceItems.add(createChoiceItem(GoalSchema.getObjectType(), GoalSchema.OBJECT_NAME, new GoalIcon()));
		choiceItems.add(createChoiceItem(ObjectiveSchema.getObjectType(), ObjectiveSchema.OBJECT_NAME, new ObjectiveIcon()));
		choiceItems.add(createChoiceItem(CauseSchema.getObjectType(), Cause.OBJECT_NAME_THREAT, new DirectThreatIcon()));
		choiceItems.add(createChoiceItem(CauseSchema.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon())); 
		choiceItems.add(createChoiceItem(ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME, new ThreatReductionResultIcon()));
		choiceItems.add(createChoiceItem(IntermediateResultSchema.getObjectType(), IntermediateResultSchema.OBJECT_NAME, new IntermediateResultIcon()));
		choiceItems.add(createChoiceItem(StrategySchema.getObjectType(), StrategySchema.OBJECT_NAME, IconManager.getStrategyIcon()));
		choiceItems.add(createChoiceItem(Task.getObjectType(), Task.ACTIVITY_NAME, new ActivityIcon()));
		choiceItems.add(createChoiceItem(IndicatorSchema.getObjectType(), IndicatorSchema.OBJECT_NAME, IconManager.getIndicatorIcon()));
		choiceItems.add(createChoiceItem(Task.getObjectType(), Task.METHOD_NAME, new MethodIcon()));
		choiceItems.add(createChoiceItem(Task.getObjectType(), Task.OBJECT_NAME, new TaskIcon()));
		choiceItems.add(createChoiceItem(Measurement.getObjectType(), Measurement.OBJECT_NAME, new MeasurementIcon()));
		
		return choiceItems;
	}

	private static ChoiceItem createChoiceItem(int objectType, String objectName, Icon iconToUse)
	{
		return new ChoiceItem(objectName, EAM.fieldLabel(objectType, objectName), iconToUse);
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
}
