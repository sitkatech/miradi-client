/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.goal.GoalPoolTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;

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