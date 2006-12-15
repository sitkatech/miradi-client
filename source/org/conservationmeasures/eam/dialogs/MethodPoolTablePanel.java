/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class MethodPoolTablePanel extends ObjectPoolTablePanel
{
	public MethodPoolTablePanel(Project project)
	{
		super(project, ObjectType.TASK, new MethodPoolTableModel(project));
	}
}
