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

import org.miradi.icons.ActivityIcon;
import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.GroupBoxIcon;
import org.miradi.icons.IconManager;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.MeasurementIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.StressIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.icons.TextBoxIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.ProjectMetadata;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;

public class RtfLegendObjectsQuestion extends StaticChoiceQuestion
{
	public RtfLegendObjectsQuestion()
	{
		super(getAllChoices());
	}
	
	private static ChoiceItem[] getAllChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(ProjectMetadata.OBJECT_NAME, EAM.text("Project"), new MiradiApplicationIcon()),
				new ChoiceItem(ConceptualModelDiagramSchema.OBJECT_NAME, EAM.text("Conceptual Model"), new ConceptualModelIcon()),
				new ChoiceItem(ResultsChainDiagramSchema.OBJECT_NAME, EAM.text("Results Chain"), new ResultsChainIcon()),
				new ChoiceItem(TargetSchema.OBJECT_NAME, EAM.text("Target"), new TargetIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, EAM.text("Direct Threat"), new DirectThreatIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, EAM.text("Contributing Factor"), new ContributingFactorIcon()),
				new ChoiceItem(IntermediateResultSchema.OBJECT_NAME, EAM.text("Intermediate Result"), new IntermediateResultIcon()),
				new ChoiceItem(ThreatReductionResultSchema.OBJECT_NAME, EAM.text("Threat Reduction Result"), new ThreatReductionResultIcon()),
				new ChoiceItem(StrategySchema.OBJECT_NAME, EAM.text("Strategy"), IconManager.getStrategyIcon()),
				new ChoiceItem(GoalSchema.OBJECT_NAME, EAM.text("Goal"), new GoalIcon()),
				new ChoiceItem(ObjectiveSchema.OBJECT_NAME, EAM.text("Objective"), new ObjectiveIcon()),
				new ChoiceItem(IndicatorSchema.OBJECT_NAME, EAM.text("Indicator"), IconManager.getIndicatorIcon()),
				new ChoiceItem(StressSchema.OBJECT_NAME, EAM.text("Stress"), new StressIcon()),
				new ChoiceItem(TextBoxSchema.OBJECT_NAME, EAM.text("Text Box"), new TextBoxIcon()),
				new ChoiceItem(GroupBoxSchema.OBJECT_NAME, EAM.text("Group Box"), new GroupBoxIcon()),
				new ChoiceItem(TaskSchema.OBJECT_NAME, EAM.text("Task"), new TaskIcon()),
				new ChoiceItem(TaskSchema.METHOD_NAME, EAM.text("Method"), new MethodIcon()),
				new ChoiceItem(TaskSchema.ACTIVITY_NAME, EAM.text("Activity"), new ActivityIcon()),
				new ChoiceItem(MeasurementSchema.OBJECT_NAME, EAM.text("Measurement"), new MeasurementIcon()),
		};
	}
}
