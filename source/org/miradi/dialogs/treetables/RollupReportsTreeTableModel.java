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

import org.miradi.dialogs.RollupReportsRowColumnProvider;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.treenodes.RollupReportsRootTreeNode;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTableModel;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class RollupReportsTreeTableModel extends ExportablePlanningTreeTableModel
{
	private RollupReportsTreeTableModel(Project project, TreeTableNode rootNode, RowColumnProvider rowColumnProvider, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, getRowColumnProvider(), rowColumnProvider.getColumnListToShow(), uniqueTreeTableModeIdentifier);
	}
	
	public static RollupReportsTreeTableModel createRollupReportsTreeTableModel(Project project, RowColumnProvider rowColumnProvider) throws Exception
	{
		TreeTableNode projectRootNode = createRootNode(project);
		return new RollupReportsTreeTableModel(project, projectRootNode, rowColumnProvider, UNIQUE_TREE_TABLE_IDENTIFIER);
	}
	
	private static TreeTableNode createRootNode(Project project) throws Exception
	{
		return new RollupReportsRootTreeNode(project, getRowColumnProvider());
	}
	
	private static CodeList getRowColumnProvider()
	{
		return new RollupReportsRowColumnProvider().getRowListToShow();
	}

	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "RollupReportsTreeTableModel";
}
