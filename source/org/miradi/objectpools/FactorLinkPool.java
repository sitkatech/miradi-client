/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;

public class FactorLinkPool extends PoolWithIdAssigner
{
	public FactorLinkPool(IdAssigner idAssignerToUse)
	{
		super(ObjectType.FACTOR_LINK, idAssignerToUse);
	}
	
	public void put(FactorLink linkage) throws Exception
	{
		put(linkage.getId(), linkage);
	}
	
	public FactorLink find(FactorLinkId id)
	{
		return (FactorLink)getRawObject(id);
	}
	
	public boolean areLinked(Factor factor1, Factor factor2)
	{
		return !getLinkedRef(factor1, factor2).isInvalid();
	}
	
	public ORef getLinkedRef(Factor factor1, Factor factor2)
	{
		ORefList links1 = factor1.findObjectsThatReferToUs(FactorLink.getObjectType());
		ORefList links2 = factor2.findObjectsThatReferToUs(FactorLink.getObjectType());
		
		ORefList overlap = links1.getOverlappingRefs(links2);
		if(overlap.size() > 1)
			EAM.logWarning("Duplicate links from " + factor1.getRef() + " to " + factor2.getRef());
		if(overlap.size() == 1)
			return overlap.get(0);
		
		return ORef.INVALID;
	}

	
	public FactorLinkId[] getFactorLinkIds()
	{
		BaseId[] rawIds = getIds();
		FactorLinkId[] linkageIds = new FactorLinkId[rawIds.length];
		System.arraycopy(rawIds, 0, linkageIds, 0, rawIds.length);
		return linkageIds;
	}
	
	public ORefList getFactorLinkRefs()
	{
		IdList factorLinkIds = new IdList(FactorLink.getObjectType(), getFactorLinkIds());
		return new ORefList(FactorLink.getObjectType(), factorLinkIds);
	}
	
}
