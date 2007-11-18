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
		assertNotEquals("Different records are equal?", ownerKey1, ownerKey2);
		
		String linkageTypeName = linkageType.getName();

		RecordKey memberKey1 = datanet.createRecord(memberType.getName());
		datanet.addMember(ownerKey1, linkageTypeName, memberKey1);
		assertEquals(1, datanet.getMemberCount(ownerKey1, linkageTypeName));

		RecordKey memberKey2 = datanet.createRecord(memberType.getName());
		datanet.addMember(ownerKey1, linkageTypeName, memberKey2);
		assertEquals(memberKey1, datanet.getMember(ownerKey1, linkageTypeName, 0));
		assertEquals(memberKey2, datanet.getMember(ownerKey1, linkageTypeName, 1));
	}
}
