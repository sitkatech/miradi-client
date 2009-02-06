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
package org.miradi.wizard.noproject.projectlist;

import java.io.File;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FileSystemProjectSorter;
import org.miradi.wizard.noproject.FileSystemRootNode;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class ProjectListTreeTableModel extends GenericTreeTableModel
{
	public static ProjectListTreeTableModel createProjectListTreeTableModel(File homeDirectory) throws Exception
	{
		FileSystemProjectSorter nodeSorter = new FileSystemProjectSorter();
		FileSystemRootNode rootNode = new FileSystemRootNode(homeDirectory, nodeSorter);
			
		return new ProjectListTreeTableModel(rootNode, nodeSorter);
	}
	
	private ProjectListTreeTableModel(FileSystemTreeNode root, FileSystemProjectSorter nodeSorterToUse)
	{
		super(root);
		
		nodeSorter = nodeSorterToUse;

		root.recursivelySort();
	}

	public String getColumnTag(int column)
	{
		return COLUMN_NAMES[column];
	}

	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	public String getColumnName(int column)
	{
		return COLUMN_NAMES[column];
	}
	
	public void sort(int modelColumn)
	{
		FileSystemTreeNode fileSystemNode = getFileSystemRootNode();
		String columnTag = getColumnTag(modelColumn);
		nodeSorter.resortBy(columnTag);
		fileSystemNode.recursivelySort();
		
		reloadNodesWithouRebuildingNodes();
	}

	private FileSystemRootNode getFileSystemRootNode()
	{
		return (FileSystemRootNode) getRootNode();
	}
	
	public void rebuildEntireTree(File homeDir)
	{
		getFileSystemRootNode().setFile(homeDir);
		super.rebuildEntireTree();
	}
	
	private String[] COLUMN_NAMES = {EAM.text("Project"), EAM.text("Last Modified"), };
	private FileSystemProjectSorter nodeSorter;
}
