/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.goal.GoalPoolTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class CloneGoalDoer extends CreateGoal
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		if (validUserChoiceForObjectToClone(new GoalPoolTablePanel(getProject()), EAM.text("Choose Goal to Clone")))
			super.doIt();
	}
}