/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import java.util.HashMap;
import java.util.Map;


public class Datanet
{
	public Datanet(DatanetSchema schemaToUse)
	{
		schema = schemaToUse;
		records = new HashMap<RecordKey, RecordInstance>();
		linkages = new HashMap<LinkageKey, LinkageInstance>();
	}
	
	public void close()
	{
	}

	public RecordInstance getRecord(RecordKey key)
	{
		return records.get(key);
	}
	
	public RecordKey createRecord(String typeName) throws UnknownRecordTypeException
	{
		RecordType type = getRecordType(typeName);
		if(type == null)
			throw new UnknownRecordTypeException(typeName);
		RecordInstance created = new RecordInstance(this, type, nextId++);
		records.put(created.getKey(), created);
		createLinkages(created);
		return created.getKey();
	}
	
	public void deleteRecord(RecordKey recordKey)
	{

		// remove as member from all linkages
		RecordInstance record = getRecord(recordKey);
		for(LinkageKey linkageKey : linkages.keySet())
		{
			LinkageType linkageType = getSchema().getLinkageType(linkageKey.getTypeName());
			String memberClassName = linkageType.getMemberClassName();
			String recordTypeName = record.getType().getName();
			if(!recordTypeName.equals(memberClassName))
				continue;

			LinkageInstance linkage = linkages.get(linkageKey);
			if(linkage.hasMember(record))
			{
				linkage.removeMember(record);
			}
		}

		// remove all members
		
		records.remove(recordKey);
	}
	
	public void addMemberKey(RecordKey ownerKey, String linkageTypeName, RecordKey memberKey) throws Exception
	{
		LinkageInstance linkage = getLinkage(getRecord(ownerKey), linkageTypeName);
		linkage.addMember(getRecord(memberKey));
	}
	
	public void removeMemberKey(RecordKey ownerKey, String linkageTypeName, RecordKey memberKey)
	{
		LinkageInstance linkage = getLinkage(getRecord(ownerKey), linkageTypeName);
		linkage.removeMember(getRecord(memberKey));
	}

	public int getMemberCount(RecordKey ownerKey, String linkageTypeName)
	{
		LinkageInstance linkage = getLinkage(ownerKey, linkageTypeName);
		return linkage.getMemberCount();
	}

	public RecordKeySet getMemberKeys(RecordKey ownerKey, String linkageTypeName)
	{
		LinkageInstance linkage = getLinkage(ownerKey, linkageTypeName);
		RecordKeySet memberKeys = new RecordKeySet();
		for(RecordInstance record : linkage.getMembers())
			memberKeys.add(record.getKey());

		return memberKeys;
	}
	
	public RecordKey getOwnerKey(RecordKey memberKey, String linkageTypeName) throws Exception
	{
		LinkageType linkageType = getSchema().getLinkageType(linkageTypeName);
		if(!linkageType.getMemberClassName().equals(memberKey.getRecordTypeName()))
			throw new NotMemberOfThatLinkageException(memberKey.toString() + " " + linkageTypeName);
		
		RecordInstance member = getRecord(memberKey);
		for(LinkageKey linkageKey : linkages.keySet())
		{
			if(!linkageTypeName.equals(linkageKey.getTypeName()))
				continue;
			LinkageInstance linkage = linkages.get(linkageKey);
			if(linkage.getMembers().contains(member))
				return linkage.getOwner().getKey();
		}
		
		throw new NoOwnerException(memberKey.toString() + " " + linkageTypeName);
	}

	RecordType getRecordType(String typeName)
	{
		return schema.getRecordType(typeName);
	}
	
	DatanetSchema getSchema()
	{
		return schema;
	}
	
	private void createLinkages(RecordInstance newRecord)
	{
		LinkageType[] linkageTypes = getSchema().getLinkageTypesOwnedBy(newRecord.getType().getName());
		for(int type = 0; type < linkageTypes.length; ++type)
		{
			LinkageKey linkageKey = new LinkageKey(newRecord.getKey(), linkageTypes[type].getName());
			LinkageInstance linkage = new LinkageInstance(this, linkageTypes[type], newRecord);
			linkages.put(linkageKey, linkage);
		}
	}
	
	LinkageInstance getLinkage(RecordKey ownerKey, String linkageTypeName)
	{
		RecordInstance owner = getRecord(ownerKey);
		LinkageInstance linkage = getLinkage(owner, linkageTypeName);
		return linkage;
	}
	
	private LinkageInstance getLinkage(RecordInstance owner, String linkageTypeName)
	{
		LinkageKey linkageKey = new LinkageKey(owner.getKey(), linkageTypeName);
		LinkageInstance linkage = linkages.get(linkageKey);
		return linkage;
	}

	static public class UnknownRecordTypeException extends Exception
	{
		public UnknownRecordTypeException(String typeName)
		{
			super(typeName);
		}
	}
	
	static public class NoOwnerException extends Exception
	{
		public NoOwnerException(String details)
		{
			super(details);
		}
	}
	
	static public class NotMemberOfThatLinkageException extends Exception
	{
		public NotMemberOfThatLinkageException(String details)
		{
			super(details);
		}
	}
	
	
	private DatanetSchema schema;
	private int nextId;
	private Map<RecordKey,RecordInstance> records;
	private Map<LinkageKey,LinkageInstance> linkages;
}
