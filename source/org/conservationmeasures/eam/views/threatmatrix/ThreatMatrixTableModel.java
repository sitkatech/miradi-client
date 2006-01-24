/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixTableModel extends AbstractTableModel
{
	public ThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public int getColumnCount()
	{
		return 2 + getTargets().length;
	}

	private ConceptualModelNode[] getDirectThreats()
	{
		return project.getNodePool().getDirectThreats();
	}

	public int getRowCount()
	{
		return reservedRows + getDirectThreats().length;
	}

	private ConceptualModelNode[] getTargets()
	{
		return project.getNodePool().getTargets();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex == 0)
			return getThreatName(rowIndex);
		
		return "a";
	}
	
	public String getThreatName(int row)
	{
		if(row < reservedRows)
			return "";
		return getDirectThreats()[row - reservedRows].getName();
	}
	
	static final int reservedRows = 2;
	
	Project project;
}