/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class ConceptualModelTarget extends ConceptualModelNode
{
	public ConceptualModelTarget(ModelNodeId idToUse)
	{
		super(idToUse, ConceptualModelNode.TYPE_TARGET);
		clear();
	}
	
	public ConceptualModelTarget(ModelNodeId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, ConceptualModelNode.TYPE_TARGET, json);
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
