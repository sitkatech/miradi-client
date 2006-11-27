/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;


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
