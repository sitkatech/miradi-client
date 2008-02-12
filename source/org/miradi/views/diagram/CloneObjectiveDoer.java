/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.objective.ObjectivePoolTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class CloneObjectiveDoer extends CreateObjective
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		if (validUserChoiceForObjectToClone(new ObjectivePoolTablePanel(getProject()), EAM.text("Choose Objective to Clone")))
			super.doIt();
	}
}