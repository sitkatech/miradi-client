/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.HashMap;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class ObjectPool
{
	public ObjectPool()
	{
		map = new HashMap();
	}
	
	public int size()
	{
		return map.size();
	}

	public void put(ConceptualModelNode node)
	{
		map.put(createKey(node.getId()), node);
	}
	
	public ConceptualModelNode find(int id)
	{
		return (ConceptualModelNode)map.get(createKey(id));
	}
	
	public void remove(int id)
	{
		map.remove(createKey(id));
	}

	private Integer createKey(int id)
	{
		return new Integer(id);
	}
	
	
	HashMap map;
}
