/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;

public class ConceptualModelFactor extends ConceptualModelNode
{
	public ConceptualModelFactor(NodeType nodeType)
	{
		super(nodeType);
	}

	public boolean isFactor()
	{
		return true;
	}
	
	public boolean isIndirectFactor()
	{
		return(getType().isIndirectFactor());
	}
	
	public boolean isDirectThreat()
	{
		return(getType().isDirectThreat());
	}
	
	public boolean isStress()
	{
		return(getType().isStress());
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public boolean canHavePriority()
	{
		return true;
	}

}
