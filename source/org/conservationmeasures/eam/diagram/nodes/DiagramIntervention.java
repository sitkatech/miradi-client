/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;

public class DiagramIntervention extends DiagramNode
{
	public DiagramIntervention(ConceptualModelIntervention cmIntervention)
	{
		super(cmIntervention);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_INTERVENTION;
	}

}
