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
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixTableModel
{
	public ThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public ThreatRatingBundle getBundle(int threatId, int targetId) throws Exception
	{
		if(!isActiveThreatIdTargetIdPair(threatId, targetId))
			return null;
		
		return project.getThreatRatingFramework().getBundle(threatId, targetId);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public int getTargetCount()
	{
		return getTargets().length;
	}

	ConceptualModelNode[] getDirectThreats()
	{
		return project.getNodePool().getDirectThreats();
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}

	ConceptualModelNode[] getTargets()
	{
		return project.getNodePool().getTargets();
	}

	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		int threatId = getDirectThreats()[threatIndex].getId();
		int targetId = getTargets()[targetIndex].getId();
		return isActiveThreatIdTargetIdPair(threatId, targetId);
	}

	private boolean isActiveThreatIdTargetIdPair(int threatId, int targetId)
	{
		return project.isLinked(threatId, targetId);
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

	public String[] getThreatNames()
	{
		String[] names = new String[getThreatCount()];
		for(int i = 0; i < names.length; ++i)
			names[i] = getThreatName(i);
		return names;
	}
	
	public String[] getTargetNames()
	{
		String[] names = new String[getTargetCount()];
		for(int i = 0; i < names.length; ++i)
			names[i] = getTargetName(i);
		return names;
	}

	private ConceptualModelNode getTargetNode(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargets()[targetIndex];
		return cmNode;
	}
	
	public int findThreatByName(String threatName)
	{
		return findNodeByName(getDirectThreats(), threatName);
	}
	
	public int findTargetByName(String targetName)
	{
		return findNodeByName(getTargets(), targetName);
	}
	
	private int findNodeByName(ConceptualModelNode[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getName().equals(name))
				return nodes[i].getId();
		
		return IdAssigner.INVALID_ID;
	}
	
	Project project;
}