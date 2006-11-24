/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.LinkageListener;

public class LinkagePool extends EAMObjectPool
{
	public LinkagePool(LinkageListener listenerToNotify)
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
		FactorLink linkage = find((ModelLinkageId)id);
		super.remove(id);
		listener.linkageWasDeleted(linkage.getFromNodeId(), linkage.getToNodeId());
	}

	public FactorLink find(ModelLinkageId id)
	{
		return (FactorLink)getRawObject(id);
	}
	
	public boolean hasLinkage(ModelNodeId nodeId1, ModelNodeId nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			FactorLink thisLinkage = getLinkage(i);
			ModelNodeId fromId = thisLinkage.getFromNodeId();
			ModelNodeId toId = thisLinkage.getToNodeId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return true;
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return true;
		}
		return false;
	}
	
	public ModelLinkageId[] getModelLinkageIds()
	{
		BaseId[] rawIds = getIds();
		ModelLinkageId[] linkageIds = new ModelLinkageId[rawIds.length];
		System.arraycopy(rawIds, 0, linkageIds, 0, rawIds.length);
		return linkageIds;
	}
	
	private FactorLink getLinkage(int index)
	{
		return find((ModelLinkageId)getIds()[index]);
	}
	
	LinkageListener listener;
}
