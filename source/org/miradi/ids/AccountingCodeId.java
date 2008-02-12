/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;

public class AccountingCodeId extends ObjectId
{
	public AccountingCodeId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final AccountingCodeId INVALID = new AccountingCodeId(IdAssigner.INVALID_ID);
}
