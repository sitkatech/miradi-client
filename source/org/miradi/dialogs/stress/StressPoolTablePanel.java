/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.stress;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.objects.Stress;
import org.miradi.project.Project;

public class StressPoolTablePanel extends ObjectPoolTablePanel
{
	public StressPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, Stress.getObjectType(), 
				new StressPoolTableModel(projectToUse)
		);
	}
}
