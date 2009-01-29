/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FileSystemProjectSorter;
import org.miradi.utils.SortableTable;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class ProjectListTreeTableModel extends GenericTreeTableModel
{
	public ProjectListTreeTableModel(FileSystemTreeNode root)
	{
		super(root);
		
		currentSortDirection = SortableTable.DEFAULT_SORT_DIRECTION;
		nodeSorter = new FileSystemProjectSorter();
		root.sortBy(nodeSorter);
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
		FileSystemTreeNode fileSystemNode = (FileSystemTreeNode) getRootNode();
		String columnTag = getColumnTag(modelColumn);
		nodeSorter.setColumnSortTag(columnTag);
		nodeSorter.setSortDirection(getReverseSortDirection());
		fileSystemNode.sortBy(nodeSorter);
		
		reloadNodesWithouRebuildingNodes();
	}
	
	public void reverseSortDirection()
	{
		if (FileSystemProjectSorter.isReverseSort(currentSortDirection))
			currentSortDirection = SortableTable.DEFAULT_SORT_DIRECTION;
		else
			currentSortDirection = SortableTable.REVERSE_SORT_ORDER;
	}
	
	public int getReverseSortDirection()
	{
		reverseSortDirection();
		return currentSortDirection;
	}
		
	private String[] COLUMN_NAMES = {EAM.text("Project"), EAM.text("Last Modified"), };
	
	private int currentSortDirection;
	private FileSystemProjectSorter nodeSorter;
}
