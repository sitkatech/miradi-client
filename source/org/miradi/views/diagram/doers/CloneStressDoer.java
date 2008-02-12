/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.dialogs.stress.StressPoolTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class CloneStressDoer extends CreateStressDoer
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;

		if (validUserChoiceForObjectToClone(new StressPoolTablePanel(getProject()), EAM.text("Choose Stress to Clone")))
			super.doIt();
	}
}
