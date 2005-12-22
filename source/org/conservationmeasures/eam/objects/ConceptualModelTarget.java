/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;


public class ConceptualModelTarget extends ConceptualModelObject
{
	public ConceptualModelTarget()
	{
		super(DiagramNode.TYPE_TARGET);
	}

	public boolean isTarget()
	{
		return true;
	}
	
	public boolean canHaveGoal()
	{
		return true;
	}

}
