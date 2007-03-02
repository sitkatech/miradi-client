/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorLinkListener;

public class FactorLinkPool extends PoolWithIdAssigner
{
	public FactorLinkPool(IdAssigner idAssignerToUse, FactorLinkListener listenerToNotify)
	{
		super(ObjectType.FACTOR_LINK, idAssignerToUse);
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
		listener.factorLinkWasCreated(linkage.getFromFactorId(), linkage.getToFactorId());
	}

	public void remove(BaseId id)
	{
		FactorLink linkage = find((FactorLinkId)id);
		super.remove(id);
		listener.factorLinkWasDeleted(linkage.getFromFactorId(), linkage.getToFactorId());
	}

	public FactorLink find(FactorLinkId id)
	{
		return (FactorLink)getRawObject(id);
	}
	
	public boolean isLinked(FactorId nodeId1, FactorId nodeId2)
	{
		return (getLinkedId(nodeId1, nodeId2)!=null);
	}
	
	
	public FactorLinkId getLinkedId(FactorId nodeId1, FactorId nodeId2)
	{
		for(int i = 0; i < getIds().length; ++i)
		{
			FactorLink thisLinkage = getLinkage(i);
			FactorId fromId = thisLinkage.getFromFactorId();
			FactorId toId = thisLinkage.getToFactorId();
			if(fromId.equals(nodeId1) && toId.equals(nodeId2))
				return (FactorLinkId) thisLinkage.getId();
			if(fromId.equals(nodeId2) && toId.equals(nodeId1))
				return (FactorLinkId) thisLinkage.getId();
		}
		return null;
	}
	
	
	public FactorLinkId[] getFactorLinkIds()
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
