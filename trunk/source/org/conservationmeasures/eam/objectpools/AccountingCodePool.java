/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.ObjectManager;

public class AccountingCodePool extends EAMNormalObjectPool
{
	public AccountingCodePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.ACCOUNTING_CODE);
	}
	
	public AccountingCode find(BaseId id)
	{
		return (AccountingCode)findObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new AccountingCode(objectManager, actualId);
	}
	
	public AccountingCode[] getAllAccountingCodes()
	{
		BaseId[] allIds = getIds();
		AccountingCode[] allAccountingCodes = new AccountingCode[allIds.length];
		for (int i = 0; i < allAccountingCodes.length; i++)
			allAccountingCodes[i] = find(allIds[i]);
			
		return allAccountingCodes;
	}

}