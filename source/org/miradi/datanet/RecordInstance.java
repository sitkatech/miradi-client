/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import java.util.HashMap;
import java.util.Map;

public class RecordInstance
{
	public RecordInstance(Datanet datanetToUse, RecordType typeToUse, int id)
	{
		datanet = datanetToUse;
		type = typeToUse;
		
		key = new RecordKey(typeToUse.getName(), id);
		fieldValues = new HashMap<String, String>();
	}
	
	public RecordType getType()
	{
		return type;
	}
	
	public RecordKey getKey()
	{
		return key;
	}
	
	public String getFieldData(String fieldName) throws UnknownFieldException
	{
		if(!type.fieldExists(fieldName))
			throw new UnknownFieldException(fieldName);
		String fieldValue = fieldValues.get(fieldName);
		if(fieldValue == null)
			fieldValue = "";
		return fieldValue;
	}
	
	public void setFieldData(String fieldName, String fieldValue) throws UnknownFieldException
	{
		if(!type.fieldExists(fieldName))
			throw new UnknownFieldException(fieldName);
		fieldValues.put(fieldName, fieldValue);
	}
	
	public int getMemberCount(String linkageTypeName) throws Exception
	{
		LinkageInstance linkage = datanet.getLinkage(getKey(), linkageTypeName);
		return linkage.getMemberCount();
	}
	
	public RecordInstanceSet getMembers(String linkageTypeName) throws Exception
	{
		LinkageInstance linkage = datanet.getLinkage(getKey(), linkageTypeName);
		return linkage.getMembers();
	}
	
	public RecordInstance getOwner(String linkageTypeName) throws Exception
	{
		return datanet.getRecord(datanet.getOwnerKey(getKey(), linkageTypeName));
	}

	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof RecordInstance))
			return false;
		RecordInstance other = (RecordInstance)rawOther;
		return (getKey().equals(other.getKey()));
	}
	
	public int hashCode()
	{
		return getKey().hashCode();
	}
	
	static public class UnknownFieldException extends Exception
	{
		public UnknownFieldException(String fieldName)
		{
			super(fieldName);
		}
	}
	
	private Datanet datanet;
	private RecordType type;
	private RecordKey key;
	Map<String, String> fieldValues;
}
