/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.datanet;

import org.conservationmeasures.eam.datanet.Datanet;
import org.conservationmeasures.eam.datanet.DatanetSchema;
import org.conservationmeasures.eam.datanet.LinkageType;
import org.conservationmeasures.eam.datanet.RecordInstance;
import org.conservationmeasures.eam.datanet.RecordKey;
import org.conservationmeasures.eam.datanet.RecordKeySet;
import org.conservationmeasures.eam.datanet.RecordType;
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
		datanet.addMemberKey(ownerKey1, linkageTypeName, memberKey1);
		assertEquals(1, datanet.getMemberCount(ownerKey1, linkageTypeName));

		RecordKey memberKey2 = datanet.createRecord(memberType.getName());
		datanet.addMemberKey(ownerKey1, linkageTypeName, memberKey2);
		RecordKeySet memberKeys = datanet.getMemberKeys(ownerKey1, linkageTypeName);
		assertContains(memberKey1, memberKeys);
		assertContains(memberKey2, memberKeys);
		
		datanet.removeMemberKey(ownerKey1, linkageTypeName, memberKey1);
		datanet.deleteRecord(memberKey2);
		assertEquals(0, datanet.getMemberCount(ownerKey1, linkageTypeName));
		
		RecordKey thirdMemberKey = datanet.createRecord(memberType.getName());
		datanet.addMemberKey(ownerKey1, linkageTypeName, thirdMemberKey);
		datanet.deleteRecord(ownerKey1);
		try
		{
			datanet.getRecord(thirdMemberKey);
			fail("Should have thrown for member auto-deleted");
		}
		catch(Datanet.RecordNotFoundException ignoreExpected)
		{
			
		}
	}
}
