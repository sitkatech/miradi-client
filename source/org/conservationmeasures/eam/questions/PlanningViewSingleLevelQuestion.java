/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ThreatReductionResult;

public class PlanningViewSingleLevelQuestion extends StaticChoiceQuestion
{
	public PlanningViewSingleLevelQuestion()
	{
		super(getEmptyTag(), getSingleLevelChoices());
	}

	private static String getEmptyTag()
	{
		return "";
	}

	private static ChoiceItem[] getSingleLevelChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(Target.OBJECT_NAME, EAM.text("Targets Only")),
				new ChoiceItem(Goal.OBJECT_NAME, EAM.text("Goals Only")),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, EAM.text("Direct Threats Only")),
				new ChoiceItem(ThreatReductionResult.OBJECT_NAME, EAM.text("Threat Reduction Results Only")),
				new ChoiceItem(Objective.OBJECT_NAME, EAM.text("Objectives Only")),
				new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategies Only")),
				new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Activities Only")),
				new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicators Only")),
				new ChoiceItem(Task.METHOD_NAME, EAM.text("Methods Only")),
				new ChoiceItem(Task.OBJECT_NAME, EAM.text("Tasks Only")),
				new ChoiceItem(Measurement.OBJECT_NAME, EAM.text("Measurements Only")),
		};
	}
}
