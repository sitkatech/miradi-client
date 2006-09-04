/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.ids.BaseId;
import org.json.JSONObject;

public class ConceptualModelFactor extends ConceptualModelNode
{
	public ConceptualModelFactor(BaseId idToUse, NodeType nodeType)
	{
		super(idToUse, nodeType);
	}
	
	public ConceptualModelFactor(JSONObject json)
	{
		super(DiagramNode.TYPE_FACTOR, json);
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
	
	public boolean canHavePriority()
	{
		return true;
	}

	public JSONObject toJson()
	{
		JSONObject json = createBaseJsonObject(FACTOR_TYPE);
		return json;
	}
	
	private int targetCount;

}
