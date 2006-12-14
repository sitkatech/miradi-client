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
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.EAMObject;

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

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new AccountingCode(actualId);
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