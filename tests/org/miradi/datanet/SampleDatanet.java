/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.miradi.datanet.Datanet;
import org.miradi.datanet.RecordInstance;
import org.miradi.datanet.RecordKey;

public class SampleDatanet extends Datanet
{
	public SampleDatanet() throws Exception
	{
		super(new SampleDatanetSchema());
		
		owner = getRecord(createOwnerRecord());
		member = getRecord(createMemberRecord());
		secondMember = getRecord(createMemberRecord());
		other = getRecord(createOtherRecord());
		addMemberKey(owner.getKey(), SampleDatanetSchema.OWNER_TO_MEMBER, member.getKey());
		addMemberKey(owner.getKey(), SampleDatanetSchema.OWNER_TO_MEMBER, secondMember.getKey());
	}
	
	public SampleDatanetSchema getSampleSchema()
	{
		return (SampleDatanetSchema)getSchema();
	}
	
	public RecordKey createOwnerRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.OWNER);
	}
	
	public RecordKey createMemberRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.MEMBER);
	}
	
	public RecordKey createOtherRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.OTHER);
	}
	
	RecordInstance owner;
	RecordInstance member;
	RecordInstance secondMember;
	RecordInstance other;
}
