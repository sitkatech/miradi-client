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
				new ChoiceItem(ProjectMetadata.OBJECT_NAME, EAM.text("Project"), new MiradiApplicationIcon()),
				new ChoiceItem(ConceptualModelDiagram.OBJECT_NAME, EAM.text("Conceptual Model"), new ConceptualModelIcon()),
				new ChoiceItem(ResultsChainDiagram.OBJECT_NAME, EAM.text("Results Chain"), new ResultsChainIcon()),
				new ChoiceItem(Target.OBJECT_NAME, EAM.text("Target"), new TargetIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, EAM.text("Direct Threat"), new DirectThreatIcon()),
				new ChoiceItem(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, EAM.text("Contributing Factor"), new ContributingFactorIcon()),
				new ChoiceItem(IntermediateResult.OBJECT_NAME, EAM.text("Intermediate Result"), new IntermediateResultIcon()),
				new ChoiceItem(ThreatReductionResult.OBJECT_NAME, EAM.text("Threat Reduction Result"), new ThreatReductionResultIcon()),
				new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategy"), new StrategyIcon()),
				new ChoiceItem(Goal.OBJECT_NAME, EAM.text("Goal"), new GoalIcon()),
				new ChoiceItem(Objective.OBJECT_NAME, EAM.text("Objective"), new ObjectiveIcon()),
				new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicator"), new IndicatorIcon()),
				new ChoiceItem(Stress.OBJECT_NAME, EAM.text("Stress"), new StressIcon()),
				new ChoiceItem(TextBox.OBJECT_NAME, EAM.text("Text Box"), new TextBoxIcon()),
				new ChoiceItem(GroupBox.OBJECT_NAME, EAM.text("Group Box"), new GroupBoxIcon()),
				new ChoiceItem(Task.OBJECT_NAME, EAM.text("Task"), new TaskIcon()),
				new ChoiceItem(Task.METHOD_NAME, EAM.text("Method"), new MethodIcon()),
				new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Activity"), new ActivityIcon()),
				new ChoiceItem(Measurement.OBJECT_NAME, EAM.text("Measurement"), new MeasurementIcon()),
		};
	}
}
