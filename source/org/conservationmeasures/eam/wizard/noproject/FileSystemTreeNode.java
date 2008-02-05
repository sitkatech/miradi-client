/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.reports.ReportSplitPane;

public class FileSystemTreeNode extends TreeTableNode
{
	public FileSystemTreeNode(File file) throws Exception
	{
		thisFile = file;
		children = new Vector<FileSystemTreeNode>();
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
			
			long lastModifiedMillis = thisFile.lastModified();
			return timestampToString(lastModifiedMillis);
		}
		
		throw new RuntimeException("Unknown column: " + column);
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
			if(file.isDirectory() && !isCustomReportDirectory(file))
				children.add(new FileSystemTreeNode(file));
		}
		
		Collections.sort(children);
	}

	private boolean isCustomReportDirectory(File file)
	{
		return file.getName().equals(ReportSplitPane.CUSTOM_REPORTS_DIR_NAME);
	}
	
	private boolean isProjectDirectory()
	{
		return ProjectServer.isExistingProject(thisFile);
	}

	public String toString()
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
		
		return toString().compareToIgnoreCase(other.toString());
	}
	
	private File thisFile;
	private Vector<FileSystemTreeNode> children;
}
