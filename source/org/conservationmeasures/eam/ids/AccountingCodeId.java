/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ids;

public class AccountingCodeId extends ObjectId
{
	public AccountingCodeId(int idToUse)
	{
		super(idToUse);
	}
	
	public static final AccountingCodeId INVALID = new AccountingCodeId(IdAssigner.INVALID_ID);
}
