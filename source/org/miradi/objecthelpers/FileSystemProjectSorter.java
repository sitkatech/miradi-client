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
package org.miradi.objecthelpers;

import java.util.Comparator;

import org.miradi.utils.SortableTable;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class FileSystemProjectSorter implements Comparator<FileSystemTreeNode>
{
	public FileSystemProjectSorter(String sortTagToUse, int sortDiretionToUse)
	{
		currentSortTag = sortTagToUse;
		currentSortDirection = sortDiretionToUse;
	}

	public int compare(FileSystemTreeNode node1, FileSystemTreeNode node2)
	{
		if (!node1.isProjectDirectory() && node2.isProjectDirectory())
				return 1;
		
		if (node1.isProjectDirectory() && !node2.isProjectDirectory())
			return -1;
		
		return compareByTag(node1, node2);
	}

	private int compareByTag(FileSystemTreeNode node1, FileSystemTreeNode node2)
	{
		if (currentSortTag.equals(PROJECT_NAME_SORT_TAG))
			return node1.toString().compareToIgnoreCase(node2.toString());
		
		return node1.getLastModifiedDate().compareTo(node2.getLastModifiedDate());
	}

	public boolean isReverseSort()
	{
		return isReverseSort(currentSortDirection);
	}
	
	public static boolean isReverseSort(int sortDirectionToUse)
	{
		return sortDirectionToUse == SortableTable.REVERSE_SORT_ORDER;
	}
		
	public static final String PROJECT_NAME_SORT_TAG = "Project";
	
	private String currentSortTag;
	private int currentSortDirection;
}
