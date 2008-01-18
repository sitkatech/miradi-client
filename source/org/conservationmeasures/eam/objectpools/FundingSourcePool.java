/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.project.ObjectManager;

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