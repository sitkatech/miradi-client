/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.main.EAM;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class PlanningViewSingleLevelQuestion extends StaticChoiceQuestion
{
	public PlanningViewSingleLevelQuestion()
	{
		super(getSingleLevelChoices());
	}

	private static ChoiceItem[] getSingleLevelChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(Goal.OBJECT_NAME, EAM.text("Goals Only")),
				new ChoiceItem(Objective.OBJECT_NAME, EAM.text("Objectives Only")),
				new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategies Only")),
				new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Activities Only")),
				new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicators Only")),
				new ChoiceItem(Task.METHOD_NAME, EAM.text("Methods Only")),
				new ChoiceItem(Measurement.OBJECT_NAME, EAM.text("Measurements Only")),
		};
	}
}
