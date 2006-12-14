/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.FundingSource;

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

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new FundingSource(actualId);
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