/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingTableModel extends AbstractTableModel
{
	public ThreatStressRatingTableModel(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public int getColumnCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return 0;
	}

	public Object getValueAt(int row, int tableColumn)
	{
		return null;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}
