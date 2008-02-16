/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.Objective;
import org.miradi.project.Project;

public class ObjectiveListTableModel extends ObjectListTableModel
{
	public ObjectiveListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Factor.TAG_OBJECTIVE_IDS, ObjectType.OBJECTIVE, getColumnTags());
	}

	private static String[] getColumnTags()
	{
		return new String[] {
			Objective.TAG_ID,
			Objective.TAG_LABEL,
			Objective.TAG_FULL_TEXT,
		};
	}
}
