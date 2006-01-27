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
		if(rowIndex == 0)
			return getTargetName(columnIndex);

		if(columnIndex == 0)
			return getThreatName(rowIndex);
		
		if(rowIndex < reservedRows)
			return "";
		
		if(columnIndex < reservedColumns)
			return "";
		
		int threatIndex = rowIndex - reservedRows;
		int targetIndex = columnIndex - reservedColumns;
		int threatId = getDirectThreats()[threatIndex].getId();
		int targetId = getTargets()[targetIndex].getId();
		if(project.getLinkagePool().hasLinkage(threatId, targetId))
			return "a";
		
		return "";
	}
	
	public String getThreatName(int row)
	{
		if(row < reservedRows)
			return "";
		return getDirectThreats()[row - reservedRows].getName();
	}
	
	public String getTargetName(int column)
	{
		if(column < reservedColumns)
			return "";
		return getTargets()[column - reservedColumns].getName();
	}
	
	static final int reservedRows = 2;
	static final int reservedColumns = 2;
	
	Project project;
}