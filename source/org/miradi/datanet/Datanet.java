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
		linkages = new HashMap<String, LinkageInstance>();
	}
	
	public void close()
	{
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
	
	public RecordInstance getRecord(RecordKey key)
	{
		return records.get(key);
	}
	
	public void addMemberKey(RecordKey ownerKey, String linkageTypeName, RecordKey memberKey) throws Exception
	{
		LinkageInstance linkage = getLinkage(getRecord(ownerKey), linkageTypeName);
		linkage.addMember(getRecord(memberKey));
	}

	public int getMemberCount(RecordKey ownerKey, String linkageTypeName)
	{
		LinkageInstance linkage = getLinkage(ownerKey, linkageTypeName);
		return linkage.getMemberCount();
	}

	public RecordKey[] getMemberKeys(RecordKey ownerKey, String linkageTypeName)
	{
		LinkageInstance linkage = getLinkage(ownerKey, linkageTypeName);
		RecordKey[] memberKeys = new RecordKey[linkage.getMemberCount()];
		for(int i = 0; i < memberKeys.length; ++i)
		{
			memberKeys[i] = linkage.getMember(i).getKey();
		}
		return memberKeys;
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
			String linkageKey = getLinkageKey(newRecord, linkageTypes[type].getName());
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
		String linkageKey = getLinkageKey(owner, linkageTypeName);
		LinkageInstance linkage = linkages.get(linkageKey);
		return linkage;
	}

	private String getLinkageKey(RecordInstance owner, String linkageTypeName)
	{
		String linkageKey = owner.getKey() + "/" + linkageTypeName;
		return linkageKey;
	}
	
	static public class UnknownRecordTypeException extends Exception
	{
		public UnknownRecordTypeException(String typeName)
		{
			super(typeName);
		}
	}
	
	private DatanetSchema schema;
	private int nextId;
	private Map<RecordKey,RecordInstance> records;
	private Map<String,LinkageInstance> linkages;
}
