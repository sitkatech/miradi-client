/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject;

import java.io.File;

public class FileSystemRootNode extends FileSystemTreeNode
{
	public FileSystemRootNode(File file) throws Exception
	{
		super(file);
	}
	
	public void setFile(File file)
	{
		thisFile = file;
	}
}
