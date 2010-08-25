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
import org.miradi.datanet.RecordInstance;
import org.miradi.datanet.RecordKey;

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
