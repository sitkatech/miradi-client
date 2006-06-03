/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.ConceptualModelLinkage;

public class LinkagePool extends EAMObjectPool
{
	public void put(ConceptualModelLinkage linkage)
	{
		put(linkage.getId(), linkage);
	}
	
	public ConceptualModelLinkage find(int id)
	{
		return (ConceptualModelLinkage)getRawObject(id);
	}
	
	public boolean hasLinkage(int nodeId1, int nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			ConceptualModelLinkage thisLinkage = getLinkage(i);
			int fromId = thisLinkage.getFromNodeId();
			int toId = thisLinkage.getToNodeId();
			if(fromId == nodeId1 && toId == nodeId2)
				return true;
			if(fromId == nodeId2 && toId == nodeId1)
				return true;
		}
		return false;
	}
	
	private ConceptualModelLinkage getLinkage(int index)
	{
		return find(getIds()[index]);
	}
}
