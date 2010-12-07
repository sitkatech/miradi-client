/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.planning.treenodes.WorkPlanHiddenTreeRootNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class WorkPlanTreeTableModel extends ExportablePlanningTreeTableModel
{
	public WorkPlanTreeTableModel(Project project) throws Exception
	{
		super(project, createWorkPlanRootNode(project, RowManager.getWorkPlanRows(project)), RowManager.getWorkPlanRows(project), ColumnManager.getWorkPlanColumns(project), UNIQUE_TREE_TABLE_IDENTIFIER);
	}

	private static TreeTableNode createWorkPlanRootNode(Project project, CodeList visibleRowCodes) throws Exception
	{
		return new WorkPlanHiddenTreeRootNode(project, visibleRowCodes);
	}
	
	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	public static final String UNIQUE_TREE_TABLE_IDENTIFIER = "WorkPlanTreeTableModel";
}
