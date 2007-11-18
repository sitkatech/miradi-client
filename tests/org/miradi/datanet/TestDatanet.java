/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

public class TestDatanet extends TestCaseEnhanced
{
	public TestDatanet(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		RecordType ownerType = new RecordType("owner");
		RecordType memberType = new RecordType("member");
		LinkageType linkageType = new LinkageType("linkage", ownerType, memberType, LinkageType.CONTAINS);
		DatanetSchema schema = new DatanetSchema();
		schema.addRecordType(ownerType);
		schema.addRecordType(memberType);
		schema.addLinkageType(linkageType);

		Datanet datanet = new Datanet(schema);
		RecordKey ownerKey1 = datanet.createRecord(ownerType.getName());
		RecordInstance owner1 = datanet.getRecord(ownerKey1);
		assertEquals(ownerType.getName(), owner1.getType().getName());
		RecordKey ownerKey2 = datanet.createRecord(ownerType.getName());
		RecordInstance owner2 = datanet.getRecord(ownerKey2);
		assertNotEquals("Different records are equal?", owner1, owner2);
		
		String linkageTypeName = linkageType.getName();

		RecordKey memberKey1 = datanet.createRecord(memberType.getName());
		RecordInstance member1 = datanet.getRecord(memberKey1);
		datanet.addMember(owner1, linkageTypeName, member1);
		assertEquals(1, owner1.getMemberCount(linkageTypeName));

		RecordKey memberKey2 = datanet.createRecord(memberType.getName());
		RecordInstance member2 = datanet.getRecord(memberKey2);
		datanet.addMember(owner1, linkageTypeName, member2);
		assertEquals(member1, owner1.getMember(linkageTypeName, 0));
		assertEquals(member2, owner1.getMember(linkageTypeName, 1));
	}
}
