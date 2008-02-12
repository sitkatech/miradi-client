/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Objective;
import org.miradi.project.Project;


public class ObjectivePoolTableModel extends ObjectPoolTableModel
{
	public ObjectivePoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.OBJECTIVE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Objective.TAG_SHORT_LABEL,
		Objective.TAG_LABEL,
		Objective.PSEUDO_TAG_FACTOR,
	};

}
