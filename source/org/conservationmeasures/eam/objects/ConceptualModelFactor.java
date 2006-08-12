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
		super(DiagramNode.TYPE_INDIRECT_FACTOR, json);
		setNodeType(getSubtypeFromString(json.getString(TAG_SUBTYPE)));
	}

	public boolean isFactor()
	{
		return true;
	}
	
	public boolean isIndirectFactor()
	{
		return(getNodeType().isIndirectFactor());
	}
	
	public boolean isDirectThreat()
	{
		return(getNodeType().isDirectThreat());
	}
	
	public boolean isStress()
	{
		return(getNodeType().isStress());
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
		json.put(TAG_SUBTYPE, getSubtypeString());
		return json;
	}
	
	String getSubtypeString()
	{
		if(isIndirectFactor())
			return SUBTYPE_INDIRECT_FACTOR;
		if(isDirectThreat())
			return SUBTYPE_DIRECT_THREAT;
		if(isStress())
			return SUBTYPE_STRESS;
		
		throw new RuntimeException("Factor not any known subtype");
	}
	
	public NodeType getSubtypeFromString(String subtypeString)
	{
		if(subtypeString.equals(SUBTYPE_INDIRECT_FACTOR))
			return DiagramNode.TYPE_INDIRECT_FACTOR;
		if(subtypeString.equals(SUBTYPE_DIRECT_THREAT))
			return DiagramNode.TYPE_DIRECT_THREAT;
		
		throw new RuntimeException("Unrecognized factor subtype: " + subtypeString);
	}
	
	private static final String TAG_SUBTYPE = "Subtype";
	
	private static final String SUBTYPE_INDIRECT_FACTOR = "IndirectFactor";
	private static final String SUBTYPE_DIRECT_THREAT = "DirectThreat";
	private static final String SUBTYPE_STRESS = "Stress";

}
