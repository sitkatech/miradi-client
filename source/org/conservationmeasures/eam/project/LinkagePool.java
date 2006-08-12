/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;

public class LinkagePool extends EAMObjectPool
{
	public void put(ConceptualModelLinkage linkage)
	{
		put(linkage.getId(), linkage);
	}
	
	public ConceptualModelLinkage find(BaseId id)
	{
		return (ConceptualModelLinkage)getRawObject(id);
	}
	
	public boolean hasLinkage(BaseId nodeId1, BaseId nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			ConceptualModelLinkage thisLinkage = getLinkage(i);
			BaseId fromId = thisLinkage.getFromNodeId();
			BaseId toId = thisLinkage.getToNodeId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return true;
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return true;
		}
		return false;
	}
	
	private ConceptualModelLinkage getLinkage(int index)
	{
		return find(getIds()[index]);
	}
}
