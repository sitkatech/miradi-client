/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodes.types.NodeType;

public class ConceptualModelFactor extends ConceptualModelObject
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
