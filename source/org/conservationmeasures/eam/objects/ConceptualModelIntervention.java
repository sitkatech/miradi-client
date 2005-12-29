/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.json.JSONObject;


public class ConceptualModelIntervention extends ConceptualModelNode
{
	public ConceptualModelIntervention()
	{
		super(DiagramNode.TYPE_INTERVENTION);
	}
	
	public ConceptualModelIntervention(JSONObject json)
	{
		super(DiagramNode.TYPE_INTERVENTION, json);
	}

	public boolean isIntervention()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}

	public JSONObject toJson()
	{
		JSONObject json = createBaseJsonObject(INTERVENTION_TYPE);
		return json;
	}

}
