/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;

public class PlanningViewSingleLevelQuestion extends ChoiceQuestion
{
	public PlanningViewSingleLevelQuestion(String tagToUse)
	{
		super(tagToUse, EAM.text("Single Level Style"), getSingleLevelChoices());
	}

	static ChoiceItem[] getSingleLevelChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(Goal.OBJECT_NAME, EAM.text("Goals Only")),
				new ChoiceItem(Objective.OBJECT_NAME, EAM.text("Objectives Only")),
				new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategies Only")),
				new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Actions Only")),
				new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicators Only")),
				new ChoiceItem(Task.METHOD_NAME, EAM.text("Methods Only")),
				new ChoiceItem(Task.OBJECT_NAME, EAM.text("Tasks Only")),
		};
	}
}
