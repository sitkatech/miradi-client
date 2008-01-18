/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.project.Project;

abstract public class ObjectPoolTablePanel extends ObjectTablePanel
{
	public ObjectPoolTablePanel(Project projectToUse, int objectTypeToUse, ObjectPoolTableModel model)
	{
		super(projectToUse, new ObjectPoolTable(model, DEFAULT_SORT_COLUMN));
	}
	
	private static final int DEFAULT_SORT_COLUMN = 0;
}
