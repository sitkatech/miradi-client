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
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;

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