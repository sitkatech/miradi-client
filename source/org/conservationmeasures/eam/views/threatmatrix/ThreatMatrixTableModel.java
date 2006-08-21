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

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixTableModel
{
	public ThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public ThreatRatingBundle getBundle(BaseId threatId, BaseId targetId) throws Exception
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
		
		BaseId threatId = getDirectThreats()[threatIndex].getId();
		BaseId targetId = getTargets()[targetIndex].getId();
		return isActiveThreatIdTargetIdPair(threatId, targetId);
	}

	private boolean isActiveThreatIdTargetIdPair(BaseId threatId, BaseId targetId)
	{
		return project.isLinked(threatId, targetId);
	}
	
	public String getThreatName(int threatIndex)
	{
		ConceptualModelNode cmNode = getThreatNode(threatIndex);
		return cmNode.getLabel();
	}
	
	public BaseId getThreatId(int threatIndex)
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
		return cmNode.getLabel();
	}

	public BaseId getTargetId(int targetIndex)
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
	
	public BaseId findThreatByName(String threatName)
	{
		return findNodeByName(getDirectThreats(), threatName);
	}
	
	public BaseId findTargetByName(String targetName)
	{
		return findNodeByName(getTargets(), targetName);
	}
	
	private BaseId findNodeByName(ConceptualModelNode[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getLabel().equals(name))
				return nodes[i].getId();
		
		return BaseId.INVALID;
	}
	
	Project project;
}