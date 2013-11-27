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


public class LinkageInstance
{
	public LinkageInstance(Datanet datanetToUse, LinkageType typeToUse, RecordInstance ownerToUse)
	{
		type = typeToUse;
		owner = ownerToUse;
		members = new RecordInstanceSet();
	}
	
	public int getMemberCount()
	{
		return members.size();
	}
	
	public RecordInstance getOwner()
	{
		return owner;
	}
	
	public void addMember(RecordInstance newMember) throws Exception
	{
		if(!type.getMemberClassName().equals(newMember.getType().getName()))
			throw new WrongMemberTypeException(newMember.getType().getName());
		if(members.contains(newMember))
			throw new MemberAlreadyExistsException(newMember.getKey().toString());
		members.add(newMember);
	}
	
	public void removeMember(RecordInstance record)
	{
		members.remove(record);
	}
	
	public boolean hasMember(RecordInstance possibleMember)
	{
		return getMembers().contains(possibleMember);
	}

	public RecordInstanceSet getMembers()
	{
		return members;
	}
	
	static public class WrongMemberTypeException extends Exception
	{
		public WrongMemberTypeException(String memberTypeName)
		{
			super(memberTypeName);
		}
	}
	
	static public class MemberAlreadyExistsException extends Exception
	{
		public MemberAlreadyExistsException(String key)
		{
			super(key);
		}
	}
	
	private LinkageType type;
	private RecordInstance owner;
	private RecordInstanceSet members;
}
