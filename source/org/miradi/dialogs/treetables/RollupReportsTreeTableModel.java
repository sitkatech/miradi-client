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

import org.miradi.dialogs.planning.AbstractUnspecifiedRowCategoryProvider;
import org.miradi.dialogs.planning.treenodes.RollupReportsRootTreeNode;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.project.Project;

public class RollupReportsTreeTableModel extends ExportablePlanningTreeTableModel
{
	private RollupReportsTreeTableModel(Project project, TreeTableNode rootNode, AbstractUnspecifiedRowCategoryProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, rowColumnProvider.getRowListToShow(), rowColumnProvider.getColumnListToShow(), uniqueTreeTableModeIdentifier);
	}
	
	public static RollupReportsTreeTableModel createRollupReportsTreeTableModel(Project project, AbstractUnspecifiedRowCategoryProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		TreeTableNode projectRootNode = createRootNode(project, rowColumnProvider);
		return new RollupReportsTreeTableModel(project, projectRootNode, rowColumnProvider, uniqueTreeTableModeIdentifier);
	}
	
	private static TreeTableNode createRootNode(Project project, AbstractUnspecifiedRowCategoryProvider rowColumnProvider) throws Exception
	{
		return new RollupReportsRootTreeNode(project, rowColumnProvider);
	}
}
