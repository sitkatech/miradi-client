/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class RowHeaderTableModel extends AbstractTableModel
{
	public RowHeaderTableModel(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}

	public int getColumnCount()
	{
		return FIXED_COLUMN_COUNT;
	}

	public int getRowCount()
	{
		//FIXME must be variable depending on number of assignments
		return 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return projectResources[rowIndex];
	}
	
	private void rebuild()
	{
		projectResources = project.getAllProjectResources();
	}
	
	ProjectResource[] projectResources;
	Project project;
	
	private static final int FIXED_COLUMN_COUNT = 1;
}
