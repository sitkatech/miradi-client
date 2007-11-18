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
		RecordInstance owner1 = datanet.createRecord(ownerType.getName());
		assertEquals(ownerType.getName(), owner1.getType().getName());
		RecordInstance owner2 = datanet.createRecord(ownerType.getName());
		assertNotEquals("Different records are equal?", owner1, owner2);
		
		String linkageTypeName = linkageType.getName();

		RecordInstance member1 = datanet.createRecord(memberType.getName());
		datanet.addMember(owner1, linkageTypeName, member1);
		assertEquals(1, owner1.getMemberCount(linkageTypeName));

		RecordInstance member2 = datanet.createRecord(memberType.getName());
		datanet.addMember(owner1, linkageTypeName, member2);
		assertEquals(member1, owner1.getMember(linkageTypeName, 0));
		assertEquals(member2, owner1.getMember(linkageTypeName, 1));
	}
}
