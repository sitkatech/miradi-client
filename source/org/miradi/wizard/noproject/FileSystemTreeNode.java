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
package org.miradi.wizard.noproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.miradi.database.ProjectServer;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.LastProjectModifiedTimeHelper;
import org.miradi.utils.SortableTable;

public class FileSystemTreeNode extends TreeTableNode
{
	public FileSystemTreeNode(File file, String sortTagToUse, int sortDirectionToUse) throws Exception
	{
		thisFile = file;
		children = new Vector<FileSystemTreeNode>();
		currentSortTag = sortTagToUse;
		sortDirection = sortDirectionToUse;
		
		rebuild();
	}
	
	public TreeTableNode getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public BaseObject getObject()
	{
		throw new RuntimeException("Not an object-based tree");
	}

	public ORef getObjectReference()
	{
		throw new RuntimeException("Not an object-based tree");
	}

	public Object getValueAt(int column)
	{
		if(column == 0)
		{
			return thisFile;
		}
		if(column == 1)
		{
			if(!isProjectDirectory())
				return null;
			
			return getLastModifiedDate();
		}
		
		throw new RuntimeException("Unknown column: " + column);
	}

	private String getLastModifiedDate()
	{
		return LastProjectModifiedTimeHelper.readLastModifiedProjectTime(thisFile);
	}

	public static String timestampToString(long lastModifiedMillis)
	{
		Date date = new Date(lastModifiedMillis);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public void rebuild() throws Exception
	{
		children.clear();
		if(isProjectDirectory())
			return;
		
		File[] files = thisFile.listFiles();
		if(files == null)
			return;
		
		for(int i = 0; i < files.length; ++i)
		{
			File file = files[i];
			if(file.isDirectory() && !isCustomReportDirectory(file) && !isExternalResourceDirectory(file))
			{
				FileSystemTreeNode node = new FileSystemTreeNode(file, currentSortTag, sortDirection);				
				children.add(node);
			}
		}
		
		sortChildren(currentSortTag, sortDirection);
	}

	private void sortChildren(String sortTagToUse, int sortDirectionToUse)
	{
		Collections.sort(children);
		if (isReverseSort(sortDirectionToUse))
			Collections.reverse(children);
	}

	private boolean isCustomReportDirectory(File file)
	{
		return file.getName().equals(OLD_JASPER_EXTERNAL_REPORTS_DIR_NAME);
	}
	
	private boolean isExternalResourceDirectory(File file)
	{
		return file.getName().equals(EAM.EXTERNAL_RESOURCE_DIRECTORY_NAME);
	}
	
	public boolean isProjectDirectory()
	{
		return ProjectServer.isExistingProject(thisFile);
	}

	@Override
	public String toRawString()
	{
		return thisFile.getName();
	}

	public File getFile()
	{
		return thisFile;
	}

	@Override
	public int compareTo(Object rawOther)
	{
		if (!(rawOther instanceof FileSystemTreeNode))
			return 0;
		
		FileSystemTreeNode other = (FileSystemTreeNode) rawOther;
		if (!other.isProjectDirectory() && isProjectDirectory())
				return 1;
		
		if (other.isProjectDirectory() && !isProjectDirectory())
			return -1;
		
		return compareByTag(other);
	}

	private int compareByTag(FileSystemTreeNode other)
	{
		if (currentSortTag.equals(PROJECT_NAME_SORT_TAG))
			return toString().compareToIgnoreCase(other.toString());
		
		return getLastModifiedDate().compareTo(other.getLastModifiedDate());
	}
	
	public void reverseSortDirection()
	{
		if (isReverseSort(sortDirection))
			sortDirection = SortableTable.DEFAULT_SORT_DIRECTION;
		else
			sortDirection = SortableTable.REVERSE_SORT_ORDER;
	}
	
	private boolean isReverseSort(int sortDirectionToUse)
	{
		return sortDirectionToUse == SortableTable.REVERSE_SORT_ORDER; 
	}
	
	public void setSortTag(String sortTagToUse)
	{
		currentSortTag = sortTagToUse;
	}
	
	private static final String OLD_JASPER_EXTERNAL_REPORTS_DIR_NAME = "ExternalReports";	

	
	protected File thisFile;
	private Vector<FileSystemTreeNode> children;
	private int sortDirection;
	private String currentSortTag;
	
	public static final String PROJECT_NAME_SORT_TAG = "Project";
}
