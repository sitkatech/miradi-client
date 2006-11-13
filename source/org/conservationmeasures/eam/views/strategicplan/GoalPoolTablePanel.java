package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class GoalPoolTablePanel extends ObjectPoolTablePanel
{
	public GoalPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.GOAL, 
				new GoalPoolTableModel(projectToUse)
		);
	}
}
