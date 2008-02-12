/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.datanet;

import org.conservationmeasures.eam.datanet.LinkageInstance;
import org.conservationmeasures.eam.datanet.LinkageType;
import org.conservationmeasures.eam.datanet.RecordInstance;


public class TestLinkageInstance extends TestCaseWithSampleDatanet
{
	public TestLinkageInstance(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		RecordInstance anotherOwner = datanet.getRecord(createOwnerRecord());
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
		
		RecordInstance anotherMember = datanet.getRecord(createMemberRecord());
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
		assertContains(anotherMember, localLinkage.getMembers());
	}
}
