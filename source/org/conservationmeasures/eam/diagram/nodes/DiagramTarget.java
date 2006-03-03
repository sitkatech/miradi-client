/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.objects.ConceptualModelTarget;

public class DiagramTarget extends DiagramNode
{
	public DiagramTarget(ConceptualModelTarget cmTarget)
	{
		super(cmTarget);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_TARGET;
	}
	
}
