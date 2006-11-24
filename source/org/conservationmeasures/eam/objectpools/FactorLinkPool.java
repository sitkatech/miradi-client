/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorLinkListener;

public class FactorLinkPool extends EAMObjectPool
{
	public FactorLinkPool(FactorLinkListener listenerToNotify)
	{
		super(ObjectType.MODEL_LINKAGE);
		listener = listenerToNotify;
	}
	
	public void put(FactorLink linkage)
	{
		put(linkage.getId(), linkage);
	}
	
	public void put(BaseId id, Object object)
	{
		super.put(id, object);
		FactorLink linkage = (FactorLink)object;
		listener.linkageWasCreated(linkage.getFromNodeId(), linkage.getToNodeId());
	}

	public void remove(BaseId id)
	{
		FactorLink linkage = find((FactorLinkId)id);
		super.remove(id);
		listener.linkageWasDeleted(linkage.getFromNodeId(), linkage.getToNodeId());
	}

	public FactorLink find(FactorLinkId id)
	{
		return (FactorLink)getRawObject(id);
	}
	
	public boolean hasLinkage(FactorId nodeId1, FactorId nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			FactorLink thisLinkage = getLinkage(i);
			FactorId fromId = thisLinkage.getFromNodeId();
			FactorId toId = thisLinkage.getToNodeId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return true;
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return true;
		}
		return false;
	}
	
	public FactorLinkId[] getModelLinkageIds()
	{
		BaseId[] rawIds = getIds();
		FactorLinkId[] linkageIds = new FactorLinkId[rawIds.length];
		System.arraycopy(rawIds, 0, linkageIds, 0, rawIds.length);
		return linkageIds;
	}
	
	private FactorLink getLinkage(int index)
	{
		return find((FactorLinkId)getIds()[index]);
	}
	
	FactorLinkListener listener;
}
