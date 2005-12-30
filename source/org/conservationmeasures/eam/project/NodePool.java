/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class NodePool extends ObjectPool
{
	public void put(ConceptualModelNode node)
	{
		put(node.getId(), node);
	}
	
	public ConceptualModelNode find(int id)
	{
		return (ConceptualModelNode)getRawObject(id);
	}
	
}
