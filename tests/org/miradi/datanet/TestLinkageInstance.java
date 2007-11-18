/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

public class TestLinkageInstance extends TestCaseEnhanced
{
	public TestLinkageInstance(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		RecordType ownerType = new RecordType("owner");
		RecordType memberType = new RecordType("member");
		LinkageType linkageType = new LinkageType("linkage", ownerType, memberType, LinkageType.CONTAINS);
		RecordInstance owner = new RecordInstance(ownerType, 1);
		RecordInstance member = new RecordInstance(memberType, 2);
		LinkageInstance linkage = new LinkageInstance(linkageType, owner);
		assertEquals(0, linkage.getMemberCount());
		assertEquals(owner.getKey(), linkage.getOwner().getKey());
		try
		{
			linkage.addMember(owner);
			fail("Should have thrown for wrong member type");
		}
		catch(LinkageInstance.WrongMemberTypeException ignoreExpected)
		{
			
		}
		linkage.addMember(member);
		assertEquals(1, linkage.getMemberCount());
		try
		{
			linkage.addMember(member);
			fail("Should have thrown for member already exists");	
		}
		catch(LinkageInstance.MemberAlreadyExistsException ignoreExpected)
		{
			
		}
		assertEquals(member.getKey(), linkage.getMember(0).getKey());
	}
}
