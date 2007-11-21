/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

public class ThreatTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public ThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
	}
	public int getColumnCount()
	{
		return 1;
	}

	public int getRowCount()
	{
		return 0;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		return null;
	}
	
	public Project getProject()
	{
		return project;
	}

	public String getColumnTag(int column)
	{
		return "";
	}

	private Project project;

}
