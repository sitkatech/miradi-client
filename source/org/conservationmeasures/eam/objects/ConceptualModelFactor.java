/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ConceptualModelFactor extends ConceptualModelNode
{
	public ConceptualModelFactor(BaseId idToUse)
	{
		super(idToUse, new NodeTypeFactor());
	}
	
	public ConceptualModelFactor(ModelNodeId idToUse, EnhancedJsonObject json)
	{
		super(idToUse, DiagramNode.TYPE_FACTOR, json);
	}

	public boolean isFactor()
	{
		return true;
	}
	
	public boolean isIndirectFactor()
	{
		return !isDirectThreat();
	}
	
	public boolean isDirectThreat()
	{
		return (targetCount > 0);
	}
	
	public void increaseTargetCount()
	{
		++targetCount;
	}
	
	public void decreaseTargetCount()
	{
		--targetCount;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = createBaseJsonObject(NodeTypeFactor.FACTOR_TYPE);
		return json;
	}
	
	private int targetCount;

}
