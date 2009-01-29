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
	public FileSystemProjectSorter()
	{
		currentSortTag = PROJECT_NAME_SORT_TAG;
		currentSortDirection = SortableTable.DEFAULT_SORT_DIRECTION;
	}
	
	public int compare(FileSystemTreeNode node1, FileSystemTreeNode node2)
	{
		int rawComparisonResult = compareWithoutDirection(node1, node2);
		if (isReverseSort())
			return getNegatedValue(rawComparisonResult);
		
		return rawComparisonResult;
	}

	private int compareWithoutDirection(FileSystemTreeNode node1, FileSystemTreeNode node2)
	{
		if (!node1.isProjectDirectory() && node2.isProjectDirectory())
			return 1;
		
		if (node1.isProjectDirectory() && !node2.isProjectDirectory())
			return -1;
		
		return compareByTag(node1, node2);
	}

	private int getNegatedValue(int compareByTag)
	{
		final int NEGATIVE_ONE = -1;
		return compareByTag * NEGATIVE_ONE;
	}

	private int compareByTag(FileSystemTreeNode node1, FileSystemTreeNode node2)
	{
		if (currentSortTag.equals(PROJECT_NAME_SORT_TAG))
			return node1.toString().compareToIgnoreCase(node2.toString());
		
		return node1.getLastModifiedDate().compareTo(node2.getLastModifiedDate());
	}

	private boolean isReverseSort()
	{
		return isReverseSort(currentSortDirection);
	}
	
	public void setColumnSortTag(String columnSortTagToUse)
	{
		currentSortTag = columnSortTagToUse;
	}

	public void reverseSortDirection()
	{
		if (isReverseSort(currentSortDirection))
			currentSortDirection = SortableTable.DEFAULT_SORT_DIRECTION;
		else
			currentSortDirection = SortableTable.REVERSE_SORT_ORDER;
	}
	
	private boolean isReverseSort(int sortDirectionToUse)
	{
		return sortDirectionToUse == SortableTable.REVERSE_SORT_ORDER;
	}
				
	public static final String PROJECT_NAME_SORT_TAG = "Project";
	
	private String currentSortTag;
	private int currentSortDirection;
}
