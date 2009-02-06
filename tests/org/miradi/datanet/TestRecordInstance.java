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

import org.miradi.datanet.Datanet;
import org.miradi.datanet.RecordInstance;
import org.miradi.datanet.RecordInstanceSet;


public class TestRecordInstance extends TestCaseWithSampleDatanet
{
	public TestRecordInstance(String name)
	{
		super(name);
	}

	public void testGetKey() throws Exception
	{
		RecordInstance first = datanet.getRecord(createOwnerRecord());
		RecordInstance second = datanet.getRecord(createOwnerRecord());
		assertNotEquals("Different keys equal?", first.getKey(), second.getKey());
		
		RecordInstance dupe1 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		RecordInstance dupe2 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		assertEquals("Identical keys not equal?", dupe1, dupe2);
		
		RecordInstance sameId1 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		RecordInstance sameId2 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.MEMBER), 100);
		assertNotEquals("Different types same id equal?", sameId1.getKey(), sameId2.getKey());
	}
	
	public void testSetGet() throws Exception
	{
		try
		{
			owner.getFieldData("unknown");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		assertEquals("", owner.getFieldData(SampleDatanetSchema.LABEL));
		assertEquals("", owner.getFieldData(SampleDatanetSchema.COMMENTS));
		
		try
		{
			owner.setFieldData("unknown", "whatever");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		String SAMPLE_DATA = "Sample date";
		owner.setFieldData(SampleDatanetSchema.LABEL, SAMPLE_DATA);
		assertEquals(SAMPLE_DATA, owner.getFieldData(SampleDatanetSchema.LABEL));
	}
	
	public void testMembers() throws Exception
	{
		assertEquals(2, owner.getMemberCount(SampleDatanetSchema.OWNER_TO_MEMBER));
		RecordInstanceSet members = owner.getMembers(SampleDatanetSchema.OWNER_TO_MEMBER);
		assertContains(member, members);
		assertContains(secondMember, members);
	}
	
	public void testGetOwner() throws Exception
	{
		assertEquals(owner, member.getOwner(SampleDatanetSchema.OWNER_TO_MEMBER));
		try
		{
			RecordInstance orphan = datanet.getRecord(createMemberRecord());
			orphan.getOwner(SampleDatanetSchema.OWNER_TO_MEMBER);
			fail("Should have thrown for no owner");
		}
		catch(Datanet.NoOwnerException ignoreExpected)
		{
			
		}
		
		try
		{
			owner.getOwner(SampleDatanetSchema.OWNER_TO_MEMBER);
			fail("Should have thrown for wrong linkage type");
		}
		catch(Datanet.NotMemberOfThatLinkageException ignoreExpected)
		{
			
		}
	}
	
	public void testEquals() throws Exception
	{
		String LABEL = "Label";
		
		RecordInstance owner2 = datanet.getRecord(createOwnerRecord());
		assertNotEquals("Different record instances were equal?", owner, owner2);
		
		int ID = 100;
		RecordInstance similar1 = new RecordInstance(datanet, owner.getType(), ID);
		RecordInstance similar2 = new RecordInstance(datanet, owner.getType(), ID);
		similar1.setFieldData(LABEL, "blah");
		assertEquals("Same key not equal?", similar1, similar2);
		assertEquals("Same key different hashCode?", similar1.hashCode(), similar2.hashCode());

		RecordInstance different = new RecordInstance(datanet, member.getType(), ID);
		assertNotEquals("Same id different type were equal?", similar1, different);
	}
}
