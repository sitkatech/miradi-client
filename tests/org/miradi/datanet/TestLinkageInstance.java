/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.datanet;

import org.miradi.datanet.LinkageInstance;
import org.miradi.datanet.LinkageType;
import org.miradi.datanet.RecordInstance;


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
