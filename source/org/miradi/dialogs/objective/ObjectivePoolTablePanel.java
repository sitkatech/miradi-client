/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class ObjectivePoolTablePanel extends ObjectPoolTablePanel
{
	public ObjectivePoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.OBJECTIVE, 
				new ObjectivePoolTableModel(projectToUse)
		);
	}
}
