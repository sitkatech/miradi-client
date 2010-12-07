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

import org.martus.util.TestCaseEnhanced;
import org.miradi.datanet.DatanetSchema;
import org.miradi.datanet.LinkageType;
import org.miradi.datanet.RecordType;

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
