/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.types.NodeType;

public class DiagramFactor extends DiagramNode
{
	public DiagramFactor(NodeType nodeType)
	{
		super(new ConceptualModelObject(nodeType));
	}

	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public boolean canHavePriority()
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
	
	public boolean isFactor()
	{
		return true;
	}

	public Color getColor()
	{
		if(isIndirectFactor())
			return COLOR_INDIRECT_FACTOR;
		if(isDirectThreat())
			return COLOR_DIRECT_THREAT;
		if(isStress())
			return COLOR_STRESS;
		
		throw new RuntimeException("Unknown factor type: " + getType().getClass());
	}
	
	public static final Color COLOR_STRESS = Color.MAGENTA;
	public static final Color COLOR_DIRECT_THREAT = Color.PINK;
	public static final Color COLOR_INDIRECT_FACTOR = Color.ORANGE;

}
