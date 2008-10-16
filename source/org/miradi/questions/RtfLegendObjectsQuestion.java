/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.MeasurementIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.StressIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.icons.TextBoxIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;

public class RtfLegendObjectsQuestion extends StaticChoiceQuestion
{
	public RtfLegendObjectsQuestion()
	{
		super(getAllChoices());
	}
	
	private static ChoiceItem[] getAllChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(ProjectMetadata.OBJECT_NAME, PROJECT_LABEL, new MiradiApplicationIcon()),
				new ChoiceItem(ConceptualModelDiagram.OBJECT_NAME, CONCEPTUAL_MODEL_LABEL, new ConceptualModelIcon()),
				new ChoiceItem(ResultsChainDiagram.OBJECT_NAME, RESULTS_CHAIN_LABEL, new ResultsChainIcon()),
				new ChoiceItem(Target.OBJECT_NAME, TARGET_LABEL, new TargetIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, DIRECT_THREAT_LABEL, new DirectThreatIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, CONTRIBUTING_FACTOR_LABEL, new ContributingFactorIcon()),
				new ChoiceItem(IntermediateResult.OBJECT_NAME, INTERMEDIATE_RESULT_LABEL, new IntermediateResultIcon()),
				new ChoiceItem(ThreatReductionResult.OBJECT_NAME, THREAT_REDUCTION_RESULT_LABEL, new ThreatReductionResultIcon()),
				new ChoiceItem(Strategy.OBJECT_NAME, STRATEGY_LABEL, new StrategyIcon()),
				new ChoiceItem(Goal.OBJECT_NAME, GOAL_LABEL, new GoalIcon()),
				new ChoiceItem(Objective.OBJECT_NAME, OBJECTIVE_LABEL, new ObjectiveIcon()),
				new ChoiceItem(Indicator.OBJECT_NAME, INDICATOR_LABEL, new IndicatorIcon()),
				new ChoiceItem(Stress.OBJECT_NAME, STRESS_LABEL, new StressIcon()),
				new ChoiceItem(TextBox.OBJECT_NAME, TEXT_BOX_LABEL, new TextBoxIcon()),
				new ChoiceItem(GroupBox.OBJECT_NAME, GROUP_BOX, new GroupBoxIcon()),
				new ChoiceItem(Task.OBJECT_NAME, TASK_LABEL, new TaskIcon()),
				new ChoiceItem(Task.METHOD_NAME, METHOD_LABEL, new MethodIcon()),
				new ChoiceItem(Task.ACTIVITY_NAME, ACTIVITY_LABEL, new ActivityIcon()),
				new ChoiceItem(Measurement.OBJECT_NAME, MEASUREMENT_ICON, new MeasurementIcon()),
		};
	}
	
	public static final String PROJECT_LABEL = EAM.text("Project");
	public static final String CONCEPTUAL_MODEL_LABEL = EAM.text("Conceptual Model");
	public static final String RESULTS_CHAIN_LABEL = EAM.text("Results Chain");
	public static final String DIRECT_THREAT_LABEL = EAM.text("Direct Threat");
	public static final String CONTRIBUTING_FACTOR_LABEL = EAM.text("Contributing Factor");
	public static final String TARGET_LABEL = EAM.text("Target");
	public static final String INTERMEDIATE_RESULT_LABEL = EAM.text("Intermediate Result");
	public static final String THREAT_REDUCTION_RESULT_LABEL = EAM.text("Threat Reduction Result");
	public static final String STRATEGY_LABEL = EAM.text("Strategy");
	public static final String GOAL_LABEL = EAM.text("Goal");
	public static final String OBJECTIVE_LABEL = EAM.text("Objective");
	public static final String INDICATOR_LABEL = EAM.text("Indicator");
	public static final String STRESS_LABEL = EAM.text("Stress");
	public static final String TEXT_BOX_LABEL = EAM.text("Text Box");
	public static final String GROUP_BOX = EAM.text("Group Box");
	public static final String TASK_LABEL = EAM.text("Task");
	public static final String METHOD_LABEL = EAM.text("Method");
	public static final String ACTIVITY_LABEL = EAM.text("Activity");
	
	public static final String MEASUREMENT_ICON = "Measurement";
}
