/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

public class SampleDatanet extends Datanet
{
	public SampleDatanet() throws Exception
	{
		super(new SampleDatanetSchema());
		
		owner = getRecord(createOwnerRecord());
		member = getRecord(createMemberRecord());
		other = getRecord(createOtherRecord());
//		addMember(owner, SampleDatanetSchema.OWNER_TO_MEMBER, member);
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
	RecordInstance other;
}
