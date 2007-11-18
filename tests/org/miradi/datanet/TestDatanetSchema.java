/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

public class TestDatanetSchema extends TestCaseEnhanced
{
	public TestDatanetSchema(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		String OWNER = "owner";
		String MEMBER = "member";
		String LINKAGE = "linkage";

		DatanetSchema schema = new DatanetSchema();

		RecordType owner = new RecordType(OWNER);
		RecordType member = new RecordType(MEMBER);
		LinkageType lt = new LinkageType(LINKAGE, owner, member, LinkageType.CONTAINS);

		schema.addRecordType(owner);
		schema.addRecordType(member);
		schema.addLinkageType(lt);
		
		assertEquals(owner.getName(), schema.getRecordType(OWNER).getName());
		assertEquals(lt.getMemberClassName(), schema.getLinkageType(LINKAGE).getMemberClassName());
	}
}
