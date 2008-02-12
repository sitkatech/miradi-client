/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FundingSource;
import org.miradi.project.ObjectManager;

public class FundingSourcePool extends EAMNormalObjectPool
{
	public FundingSourcePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.FUNDING_SOURCE);
	}
	
	public FundingSource find(BaseId id)
	{
		return (FundingSource)findObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new FundingSource(objectManager, actualId);
	}

	public FundingSource[] getAllFundingSources()
	{
		BaseId[] allIds = getIds();
		FundingSource[] allFundingSources = new FundingSource[allIds.length];
		for (int i = 0; i < allFundingSources.length; i++)
			allFundingSources[i] = find(allIds[i]);
			
		return allFundingSources;
	}
}