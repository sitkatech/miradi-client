/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestAccountingCode extends EAMTestCase
{
	public TestAccountingCode(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyTagBehavior(AccountingCode.TAG_LABEL);
		verifyTagBehavior(AccountingCode.TAG_LABEL);
		verifyTagBehavior(AccountingCode.TAG_COMMENTS);
	}

	private void verifyTagBehavior(String tag) throws Exception
	{
		String value = "ifislliefj";
		AccountingCode accountingCode = new AccountingCode(new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", accountingCode.getData(tag));
		accountingCode.setData(tag, value);
		AccountingCode got = (AccountingCode)AccountingCode.createFromJson(accountingCode.getType(), accountingCode.toJson());
		assertEquals(tag + " didn't survive json?", accountingCode.getData(tag), got.getData(tag));
	}
}
