/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;


public class Datanet
{
	static public class UnknownRecordTypeException extends Exception
	{
		public UnknownRecordTypeException(String typeName)
		{
			super(typeName);
		}
	}
	
	public Datanet(DatanetSchema schemaToUse)
	{
		schema = schemaToUse;
//		linkages = new HashMap<RecordInstance, LinkageInstance>();
	}
	
	public RecordInstance createRecord(String typeName) throws UnknownRecordTypeException
	{
		RecordType type = getRecordType(typeName);
		if(type == null)
			throw new UnknownRecordTypeException(typeName);
		return type.create(nextId++);
	}
	
	RecordType getRecordType(String typeName)
	{
		return schema.getRecordType(typeName);
	}
	
	private DatanetSchema schema;
	private int nextId;
//	private Map<RecordInstance,LinkageInstance> linkages;
}
