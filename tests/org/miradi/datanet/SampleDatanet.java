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
		
		owner = createOwnerRecord();
		member = createMemberRecord();
		other = createOtherRecord();
//		addMember(owner, SampleDatanetSchema.OWNER_TO_MEMBER, member);
	}
	
	public SampleDatanetSchema getSampleSchema()
	{
		return (SampleDatanetSchema)getSchema();
	}
	
	public RecordInstance createOwnerRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.OWNER);
	}
	
	public RecordInstance createMemberRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.MEMBER);
	}
	
	public RecordInstance createOtherRecord() throws Exception
	{
		return createRecord(SampleDatanetSchema.OTHER);
	}
	
	RecordInstance owner;
	RecordInstance member;
	RecordInstance other;
}
