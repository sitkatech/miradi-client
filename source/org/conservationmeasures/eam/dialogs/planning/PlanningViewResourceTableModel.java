/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;

public class PlanningViewResourceTableModel extends AbstractTableModel
{
	public PlanningViewResourceTableModel(Project projectToUse)
	{
		project = projectToUse;
		assignmentRefs = new ORefList();
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public int getRowCount()
	{
		return assignmentRefs.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return null;
	}
	
	Project project;
	private ORefList assignmentRefs;
	
	private static final int COLUMN_COUNT = 4;
}
