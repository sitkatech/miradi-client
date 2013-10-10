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
