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

import org.miradi.dialogs.planning.ProjectResourceRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.BaseObjectTreeRootNode;
import org.miradi.dialogs.planning.treenodes.HiddenRootNodeWithBaseObjectChild;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class ProjectResourceTreeTableModel extends ExportablePlanningTreeTableModel
{
	private ProjectResourceTreeTableModel(Project project, TreeTableNode rootNode, CodeList visibleColumnCodes) throws Exception
	{
		this(project, rootNode, visibleColumnCodes, UNIQUE_TREE_TABLE_IDENTIFIER);
	}
	
	private ProjectResourceTreeTableModel(Project project, TreeTableNode rootNode, CodeList visibleColumns, String uniqueTreeTableModeIdentifier) throws Exception
	{
		super(project, rootNode, getProjectResourceRows(), visibleColumns, uniqueTreeTableModeIdentifier);
	}
	
	public static ProjectResourceTreeTableModel createProjectResourceTreeTableModel(Project project, CodeList visibleColumns) throws Exception
	{
		TreeTableNode projectRootNode = createProjectResourceRootNode(project);
		return new ProjectResourceTreeTableModel(project, projectRootNode, visibleColumns);
	}
	
	public static ProjectResourceTreeTableModel createOperationalPlanResourceTreeTableModel(Project project, CodeList visibleColumns) throws Exception
	{
		TreeTableNode projectRootNode = createHiddenResourceRootNode(project);
		return new ProjectResourceTreeTableModel(project, projectRootNode, visibleColumns);
	}
	
	public static ProjectResourceTreeTableModel createProjectResourceTreeTableModel(Project project, CodeList visibleColumns, String uniqueTreeTableModeIdentifier) throws Exception
	{
		TreeTableNode projectRootNode = createProjectResourceRootNode(project);
		
		return new ProjectResourceTreeTableModel(project, projectRootNode, visibleColumns, uniqueTreeTableModeIdentifier);
	}
	
	private static TreeTableNode createProjectResourceRootNode(Project project) throws Exception
	{
		return new BaseObjectTreeRootNode(project, ProjectResource.getObjectType(), ProjectResource.OBJECT_NAME, getProjectResourceRows());
	}
	
	private static TreeTableNode createHiddenResourceRootNode(Project project) throws Exception
	{
		return new HiddenRootNodeWithBaseObjectChild(project, ProjectResource.getObjectType(), ProjectResource.OBJECT_NAME, getProjectResourceRows());
	}
	
	private static CodeList getProjectResourceRows()
	{
		return new ProjectResourceRowColumnProvider().getRowListToShow();
	}

	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "ProjectResourceTreeTableModel";
}
