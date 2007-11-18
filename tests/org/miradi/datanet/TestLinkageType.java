/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

public class TestLinkageType extends TestCaseEnhanced
{
	public TestLinkageType(String name)
	{
		super(name);
	}

	public void testCreate() throws Exception
	{
		RecordType owner = new RecordType("owner");
		RecordType member = new RecordType("member");
		String TYPE_NAME = "linkage";
		LinkageType lt = new LinkageType(TYPE_NAME, owner, member, LinkageType.CONTAINS);
		assertEquals(TYPE_NAME, lt.getName());
		assertEquals(owner.getName(), lt.getOwnerClassName());
		assertEquals(member.getName(), lt.getMemberClassName());
		assertEquals(LinkageType.CONTAINS, lt.getMembershipType());
	}
}
