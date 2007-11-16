/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTablePanel;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.project.Project;

public class StressPoolTablePanel extends ObjectPoolTablePanel
{
	public StressPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, Stress.getObjectType(), 
				new StressPoolTableModel(projectToUse)
		);
	}
}
