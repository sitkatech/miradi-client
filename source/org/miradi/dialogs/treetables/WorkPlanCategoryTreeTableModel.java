/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.treetables;

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.WorkPlanCategoryTreeRootNode;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.project.Project;

public class WorkPlanCategoryTreeTableModel extends ExportablePlanningTreeTableModel
{
	private WorkPlanCategoryTreeTableModel(Project project, TreeTableNode rootNode, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, rowColumnProvider, uniqueTreeTableModeIdentifier);
	}
	
	public static WorkPlanCategoryTreeTableModel createCategoryTreeTableModel(Project project, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		TreeTableNode projectRootNode = createRootNode(project, rowColumnProvider);
		return new WorkPlanCategoryTreeTableModel(project, projectRootNode, rowColumnProvider, uniqueTreeTableModeIdentifier);
	}
	
	private static TreeTableNode createRootNode(Project project, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		return new WorkPlanCategoryTreeRootNode(project, rowColumnProvider);
	}
}
