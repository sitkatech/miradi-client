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

import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class PlanningViewSingleLevelQuestion extends DynamicChoiceQuestion
{
	public PlanningViewSingleLevelQuestion(Project projectToUse)
	{
		super();
		
		project = projectToUse;
	}

	private Vector<ChoiceItem> getSingleLevelChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();

		choices.add(new ChoiceItem(Goal.OBJECT_NAME, EAM.text("Goals Only")));
		choices.add(new ChoiceItem(Objective.OBJECT_NAME, EAM.text("Objectives Only")));
		choices.add(new ChoiceItem(Target.OBJECT_NAME, EAM.text("Targets Only")));
		
		if (getProject().getMetadata().isHumanWelfareTargetMode())
			choices.add(new ChoiceItem(HumanWelfareTarget.OBJECT_NAME, EAM.text("Human Welfare Targets Only")));
		
		choices.add(new ChoiceItem(Cause.OBJECT_NAME_THREAT, EAM.text("Direct Threats Only")));
		choices.add(new ChoiceItem(Strategy.OBJECT_NAME, EAM.text("Strategies Only")));
		choices.add(new ChoiceItem(Task.ACTIVITY_NAME, EAM.text("Activities Only")));
		choices.add(new ChoiceItem(Indicator.OBJECT_NAME, EAM.text("Indicators Only")));
		choices.add(new ChoiceItem(Task.METHOD_NAME, EAM.text("Methods Only")));
		choices.add(new ChoiceItem(ResourceAssignment.OBJECT_NAME, EAM.text("Assigned Resources Only")));
	
		return choices;
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		return getSingleLevelChoices().toArray(new ChoiceItem[0]);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
