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

package org.miradi.dialogs.treetables;

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.NewPlanningRootNode;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelWithRebuilder;
import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.dialogs.planning.upperPanel.rebuilder.BudgetCategoryTreeRebuilder;
import org.miradi.project.Project;

public class WorkPlanAnalysisTreeTableModel extends TreeTableModelWithRebuilder
{
	private WorkPlanAnalysisTreeTableModel(Project project, TreeTableNode rootNode, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, rowColumnProvider, uniqueTreeTableModeIdentifier);
	}
	
	public static WorkPlanAnalysisTreeTableModel createCategoryTreeTableModel(Project project, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		TreeTableNode projectRootNode = createRootNode(project, rowColumnProvider);
		return new WorkPlanAnalysisTreeTableModel(project, projectRootNode, rowColumnProvider, uniqueTreeTableModeIdentifier);
	}
	
	private static TreeTableNode createRootNode(Project project, WorkPlanCategoryTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		return new NewPlanningRootNode(project);
	}
	
	@Override
	protected AbstractTreeRebuilder createTreeRebuilder()
	{
		return new BudgetCategoryTreeRebuilder(getProject(), getRowColumnProvider());
	}
}
