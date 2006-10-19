/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class ConceptualModelTarget extends ConceptualModelNode
{
	public ConceptualModelTarget(BaseId idToUse)
	{
		super(idToUse, DiagramNode.TYPE_TARGET);
	}
	
	public ConceptualModelTarget(ModelNodeId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, DiagramNode.TYPE_TARGET, json);
	}

	public boolean isTarget()
	{
		return true;
	}
	
	public boolean canHaveGoal()
	{
		return true;
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		return json;
	}

}
