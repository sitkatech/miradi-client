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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.CategoryTwoCoreRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.BaseObjectTreeRootNode;
import org.miradi.dialogs.planning.treenodes.HiddenRootNodeWithBaseObjectChild;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class CategoryTwoTreeTableModel extends ExportablePlanningTreeTableModel
{
	private CategoryTwoTreeTableModel(Project project, TreeTableNode rootNode, CodeList visibleColumnCodes) throws Exception
	{
		this(project, rootNode, visibleColumnCodes, UNIQUE_TREE_TABLE_IDENTIFIER);
	}
	
	private CategoryTwoTreeTableModel(Project project, TreeTableNode rootNode, CodeList visibleColumns, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, getRowColumnProvider(), visibleColumns, uniqueTreeTableModeIdentifier);
	}
	
	public static CategoryTwoTreeTableModel createOperationalPlanCategoryTwoTreeTableModel(Project project, CodeList visibleColumns) throws Exception
	{
		TreeTableNode projectRootNode = createHiddenRootNode(project);
		return new CategoryTwoTreeTableModel(project, projectRootNode, visibleColumns);
	}
	
	public static CategoryTwoTreeTableModel createCategoryTwoTreeTableModel(Project project, CodeList visibleColumns, String uniqueTreeTableModeIdentifier) throws Exception
	{
		TreeTableNode projectRootNode = createRootNode(project);
		
		return new CategoryTwoTreeTableModel(project, projectRootNode, visibleColumns, uniqueTreeTableModeIdentifier);
	}
	
	private static TreeTableNode createRootNode(Project project) throws Exception
	{
		return new BaseObjectTreeRootNode(project, BudgetCategoryTwo.getObjectType(), BudgetCategoryTwo.OBJECT_NAME, getRowColumnProvider());
	}
	
	private static TreeTableNode createHiddenRootNode(Project project) throws Exception
	{
		return new HiddenRootNodeWithBaseObjectChild(project, BudgetCategoryTwo.getObjectType(), BudgetCategoryTwo.OBJECT_NAME, getRowColumnProvider());
	}
	
	private static CodeList getRowColumnProvider()
	{
		return new CategoryTwoCoreRowColumnProvider().getRowListToShow();
	}

	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "CategoryTwoTreeTableModel";
}
