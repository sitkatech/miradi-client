/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.project.LinkageListener;

public class LinkagePool extends EAMObjectPool
{
	public LinkagePool(LinkageListener listenerToNotify)
	{
		super(ObjectType.MODEL_LINKAGE);
		listener = listenerToNotify;
	}
	
	public void put(ConceptualModelLinkage linkage)
	{
		put(linkage.getId(), linkage);
	}
	
	public void put(BaseId id, Object object)
	{
		super.put(id, object);
		ConceptualModelLinkage linkage = (ConceptualModelLinkage)object;
		listener.linkageWasCreated(linkage.getFromNodeId(), linkage.getToNodeId());
	}

	public void remove(BaseId id)
	{
		ConceptualModelLinkage linkage = find(id);
		super.remove(id);
		listener.linkageWasDeleted(linkage.getFromNodeId(), linkage.getToNodeId());
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
	
	LinkageListener listener;
}
