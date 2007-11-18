/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import java.util.Vector;

public class LinkageInstance
{
	public LinkageInstance(LinkageType typeToUse, RecordInstance ownerToUse)
	{
		type = typeToUse;
		owner = ownerToUse;
		members = new Vector<RecordInstance>();
	}
	
	public String getKey()
	{
		return null;
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
			throw new MemberAlreadyExistsException(newMember.getKey());
		members.add(newMember);
	}
	
	public RecordInstance getMember(int index)
	{
		return members.get(index);
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
	private Vector<RecordInstance> members;
}
