/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;


public class TestLinkageInstance extends TestCaseWithSampleDatanet
{
	public TestLinkageInstance(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		RecordInstance anotherOwner = createOwnerRecord();
		LinkageType linkageType = datanet.getSampleSchema().getLinkageType(SampleDatanetSchema.OWNER_TO_MEMBER);
		LinkageInstance localLinkage = new LinkageInstance(datanet, linkageType, anotherOwner);
		assertEquals(anotherOwner, localLinkage.getOwner());
		try
		{
			localLinkage.addMember(anotherOwner);
			fail("Should have thrown for wrong member type");
		}
		catch(LinkageInstance.WrongMemberTypeException ignoreExpected)
		{
			
		}
		
		RecordInstance anotherMember = createMemberRecord();
		localLinkage.addMember(anotherMember);
		assertEquals(1, localLinkage.getMemberCount());
		try
		{
			localLinkage.addMember(anotherMember);
			fail("Should have thrown for member already exists");	
		}
		catch(LinkageInstance.MemberAlreadyExistsException ignoreExpected)
		{
			
		}
		assertEquals(anotherMember, localLinkage.getMember(0));
	}
}
