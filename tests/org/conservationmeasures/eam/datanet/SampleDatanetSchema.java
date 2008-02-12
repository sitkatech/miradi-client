/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.datanet;

import org.conservationmeasures.eam.datanet.DatanetSchema;
import org.conservationmeasures.eam.datanet.LinkageType;
import org.conservationmeasures.eam.datanet.RecordType;

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
