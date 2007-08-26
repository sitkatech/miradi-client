/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class PlanningTreeModel extends GenericTreeTableModel
{	
	public PlanningTreeModel(Object root)
	{
		super(root);
	}

	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int column)
	{
		return null;
	}
}
