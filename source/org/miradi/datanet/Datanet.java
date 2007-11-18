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
		linkages = new HashMap<String, LinkageInstance>();
	}
	
	public void close()
	{
	}

	public RecordInstance createRecord(String typeName) throws UnknownRecordTypeException
	{
		RecordType type = getRecordType(typeName);
		if(type == null)
			throw new UnknownRecordTypeException(typeName);
		RecordInstance created = new RecordInstance(this, type, nextId++);
		createLinkages(created);
		return created;
	}
	
	public void addMember(RecordInstance owner, String linkageTypeName, RecordInstance member) throws Exception
	{
		LinkageInstance linkage = getLinkage(owner, linkageTypeName);
		linkage.addMember(member);
	}

	public int getMemberCount(RecordInstance owner, String linkageTypeName)
	{
		LinkageInstance linkage = getLinkage(owner, linkageTypeName);
		return linkage.getMemberCount();
	}

	public Object getMember(RecordInstance owner, String linkageTypeName, int index)
	{
		LinkageInstance linkage = getLinkage(owner, linkageTypeName);
		return linkage.getMember(index);
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
	private Map<String,LinkageInstance> linkages;
}
