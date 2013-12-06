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
import org.miradi.datanet.LinkageType;
import org.miradi.datanet.RecordType;

public class TestLinkageType extends TestCaseEnhanced
{
	public TestLinkageType(String name)
	{
		super(name);
	}

	public void testCreate() throws Exception
	{
		RecordType owner = new RecordType("owner");
		RecordType member = new RecordType("member");
		String TYPE_NAME = "linkage";
		LinkageType lt = new LinkageType(TYPE_NAME, owner, member, LinkageType.CONTAINS);
		assertEquals(TYPE_NAME, lt.getName());
		assertEquals(owner.getName(), lt.getOwnerClassName());
		assertEquals(member.getName(), lt.getMemberClassName());
		assertEquals(LinkageType.CONTAINS, lt.getMembershipType());
	}
}
