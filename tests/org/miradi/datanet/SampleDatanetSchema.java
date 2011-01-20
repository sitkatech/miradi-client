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

import org.miradi.datanet.DatanetSchema;
import org.miradi.datanet.LinkageType;
import org.miradi.datanet.RecordType;

public class SampleDatanetSchema extends DatanetSchema
{
	public SampleDatanetSchema() throws Exception
	{
		owner = createOwnerType();
		member = createMemberType();
		other = createOtherType();
		ownerToMember = new LinkageType(OWNER_TO_MEMBER, owner, member, LinkageType.CONTAINS);
		
		addRecordType(owner);
		addRecordType(member);
		addRecordType(other);
		addLinkageType(ownerToMember);
	}
	
	RecordType createOwnerType() throws Exception
	{
		RecordType type = new RecordType(OWNER);
		type.addField(LABEL, RecordType.STRING);
		type.addField(COMMENTS, RecordType.LONG_STRING);
		return type;
	}
	
	RecordType createMemberType() throws Exception
	{
		RecordType type = new RecordType(MEMBER);
		return type;
	}
	
	RecordType createOtherType() throws Exception
	{
		RecordType type = new RecordType(OTHER);
		return type;
	}
	
	static String OWNER = "Owner";
	static String MEMBER = "Member";
	static String OTHER = "Other";
	static String OWNER_TO_MEMBER = OWNER + "-" + MEMBER;
	
	static String LABEL = "Label";
	static String COMMENTS = "Comments";
	
	private RecordType owner;
	private RecordType member;
	private RecordType other;
	private LinkageType ownerToMember;
}
