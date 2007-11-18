/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

abstract public class TestCaseWithSampleDatanet extends TestCaseEnhanced
{
	public TestCaseWithSampleDatanet(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		datanet = new SampleDatanet();
		owner = datanet.owner;
		member = datanet.member;
		secondMember = datanet.secondMember;
		other = datanet.other;
	}
	
	protected void tearDown() throws Exception
	{
		datanet.close();
		super.tearDown();
	}
	
	RecordKey createOwnerRecord() throws Exception
	{
		return datanet.createOwnerRecord();
	}
	
	RecordKey createMemberRecord() throws Exception
	{
		return datanet.createRecord(SampleDatanetSchema.MEMBER);
	}
	
	RecordKey createOtherRecord() throws Exception
	{
		return datanet.createRecord(SampleDatanetSchema.OTHER);
	}
	
	SampleDatanet datanet;
	RecordInstance owner;
	RecordInstance member;
	RecordInstance secondMember;
	RecordInstance other;
}
