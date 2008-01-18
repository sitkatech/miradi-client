/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import org.conservationmeasures.eam.dialogs.treetables.GenericTreeTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;

public class ProjectListTreeTableModel extends GenericTreeTableModel
{
	public ProjectListTreeTableModel(FileSystemTreeNode root)
	{
		super(root);
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

	String[] COLUMN_NAMES = {EAM.text("Project"), EAM.text("Last Opened"), };
}
