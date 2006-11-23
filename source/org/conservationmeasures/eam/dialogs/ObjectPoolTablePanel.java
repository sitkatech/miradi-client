/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.project.Project;

abstract public class ObjectPoolTablePanel extends ObjectTablePanel
{
	public ObjectPoolTablePanel(Project projectToUse, int objectTypeToUse, ObjectPoolTableModel model)
	{
		super(projectToUse, objectTypeToUse, new ObjectPoolTable(model));
	}

}
