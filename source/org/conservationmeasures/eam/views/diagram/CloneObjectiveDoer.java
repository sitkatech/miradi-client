/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.objective.ObjectivePoolTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;

public class CloneObjectiveDoer extends CreateObjective
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		askUserForObjectToClone(new ObjectivePoolTablePanel(getProject()), EAM.text("Choose Objective to Clone"));
		super.doIt();
	}
}