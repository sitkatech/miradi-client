/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Rectangle;

import org.conservationmeasures.eam.objects.ConceptualModelCluster;

public class DiagramCluster extends DiagramNode
{
	public DiagramCluster(ConceptualModelCluster cmGroup)
	{
		super(cmGroup);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_CLUSTER;
	}

	public Rectangle getAnnotationsRect()
	{
		return getAnnotationsRect(0);
	}

}
