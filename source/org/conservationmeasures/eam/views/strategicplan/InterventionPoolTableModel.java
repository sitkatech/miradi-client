/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class InterventionPoolTableModel extends ObjectPoolTableModel
{
	public InterventionPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.MODEL_NODE,projectToUse.getFactorPool().getInterventionIds(), COLUMN_TAGS);	
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Strategy.TAG_LABEL,
	};

}
