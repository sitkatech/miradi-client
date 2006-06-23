/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Rectangle;

import org.conservationmeasures.eam.objects.ConceptualModelFactor;

public class DiagramFactor extends DiagramNode
{
	public DiagramFactor(ConceptualModelFactor cmFactor)
	{
		super(cmFactor);
	}

	public Color getColor()
	{
		if(isIndirectFactor())
			return DiagramConstants.COLOR_INDIRECT_FACTOR;
		if(isDirectThreat())
			return DiagramConstants.COLOR_DIRECT_THREAT;
		if(isStress())
			return DiagramConstants.COLOR_STRESS;
		
		throw new RuntimeException("Unknown factor type: " + getNodeType().getClass());
	}

	public Rectangle getAnnotationsRect()
	{
		return getAnnotationsRect(getObjectives().size());
	}


}
