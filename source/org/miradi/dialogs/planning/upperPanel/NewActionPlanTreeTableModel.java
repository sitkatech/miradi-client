/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.NewPlanningRootNode;
import org.miradi.dialogs.planning.upperPanel.rebuilder.TreeRebuilder;
import org.miradi.project.Project;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class NewActionPlanTreeTableModel extends ExportablePlanningTreeTableModel
{
	public NewActionPlanTreeTableModel(Project projectToUse, NewPlanningRootNode rootNode) throws Exception
	{
		super(projectToUse, rootNode, RowManager.getStrategicPlanRows(), new StrategicRowColumnProvider().getColumnCodesToShow(), UNIQUE_TREE_TABLE_IDENTIFIER);
	}
	
	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	@Override
	protected void rebuildNode()
	{
		new TreeRebuilder(getProject()).rebuildTree((NewPlanningRootNode)getRootNode());
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "ActionPlanTreeTableModel";
}
