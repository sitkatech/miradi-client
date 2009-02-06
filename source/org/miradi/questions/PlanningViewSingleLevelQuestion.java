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

import org.miradi.main.EAM;
import org.miradi.objects.Assignment;
import org.miradi.objects.Cause;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
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
				new ChoiceItem(Target.OBJECT_NAME, EAM.text("Targets Only")),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, EAM.text("Direct Threats Only")),
				new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategies Only")),
				new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Activities Only")),
				new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicators Only")),
				new ChoiceItem(Task.METHOD_NAME, EAM.text("Methods Only")),
				new ChoiceItem(Assignment.OBJECT_NAME, EAM.text("Assigned Resources Only")),
		};
	}
}
