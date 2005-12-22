/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;


public class ConceptualModelIntervention extends ConceptualModelObject
{
	public ConceptualModelIntervention()
	{
		super(DiagramNode.TYPE_INTERVENTION);
	}

	public boolean isIntervention()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}

}
