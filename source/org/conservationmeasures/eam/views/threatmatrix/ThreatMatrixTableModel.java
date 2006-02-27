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

import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixTableModel
{
	public ThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public int getColumnCount()
	{
		return reservedColumns + getTargets().length;
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

	public boolean isActiveCell(int rowIndex, int columnIndex)
	{
		int threatIndex = rowIndex - reservedRows;
		int targetIndex = columnIndex - reservedColumns;
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		int threatId = getDirectThreats()[threatIndex].getId();
		int targetId = getTargets()[targetIndex].getId();
		if(project.getLinkagePool().hasLinkage(threatId, targetId))
			return true;
		
		return false;
	}
	
	public String getThreatName(int row)
	{
		if(row < reservedRows)
			return "";
		ConceptualModelNode cmNode = getThreatNode(row);
		return cmNode.getName();
	}
	
	public int getThreatId(int row)
	{
		if(row < reservedRows)
			return IdAssigner.INVALID_ID;
		ConceptualModelNode cmNode = getThreatNode(row);
		return cmNode.getId();
	}

	private ConceptualModelNode getThreatNode(int row)
	{
		ConceptualModelNode cmNode = getDirectThreats()[row - reservedRows];
		return cmNode;
	}
	
	public String getTargetName(int column)
	{
		if(column < reservedColumns)
			return "";
		ConceptualModelNode cmNode = getTargetNode(column);
		return cmNode.getName();
	}

	public int getTargetId(int column)
	{
		if(column < reservedColumns)
			return IdAssigner.INVALID_ID;
		ConceptualModelNode cmNode = getTargetNode(column);
		return cmNode.getId();
	}

	private ConceptualModelNode getTargetNode(int column)
	{
		ConceptualModelNode cmNode = getTargets()[column - reservedColumns];
		return cmNode;
	}
	
	static final int reservedRows = 2;
	static final int reservedColumns = 2;
	
	Project project;
}