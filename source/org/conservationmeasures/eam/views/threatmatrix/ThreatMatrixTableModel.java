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
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixTableModel
{
	public ThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public int getTargetCount()
	{
		return getTargets().length;
	}

	private ConceptualModelNode[] getDirectThreats()
	{
		return project.getNodePool().getDirectThreats();
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}

	private ConceptualModelNode[] getTargets()
	{
		return project.getNodePool().getTargets();
	}

	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		int threatId = getDirectThreats()[threatIndex].getId();
		int targetId = getTargets()[targetIndex].getId();
		if(project.getLinkagePool().hasLinkage(threatId, targetId))
			return true;
		
		return false;
	}
	
	public String getThreatName(int threatIndex)
	{
		ConceptualModelNode cmNode = getThreatNode(threatIndex);
		return cmNode.getName();
	}
	
	public int getThreatId(int threatIndex)
	{
		ConceptualModelNode cmNode = getThreatNode(threatIndex);
		return cmNode.getId();
	}

	private ConceptualModelNode getThreatNode(int threatIndex)
	{
		ConceptualModelNode cmNode = getDirectThreats()[threatIndex];
		return cmNode;
	}
	
	public String getTargetName(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargetNode(targetIndex);
		return cmNode.getName();
	}

	public int getTargetId(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargetNode(targetIndex);
		return cmNode.getId();
	}

	private ConceptualModelNode getTargetNode(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargets()[targetIndex];
		return cmNode;
	}
	
	Project project;
}