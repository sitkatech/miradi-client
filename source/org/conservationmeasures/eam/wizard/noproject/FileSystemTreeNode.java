/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;

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
			Date date = new Date(lastModifiedMillis);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return format.format(date);
		}
		
		throw new RuntimeException("Unknown column: " + column);
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
			if(file.isDirectory())
				children.add(new FileSystemTreeNode(file));
		}
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

	private File thisFile;
	private Vector<FileSystemTreeNode> children;
}
