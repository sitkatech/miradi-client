/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class DatanetSchema
{
	public DatanetSchema()
	{
		recordTypes = new HashMap<String, RecordType>();
		linkageTypes = new HashMap<String, LinkageType>();
	}
	
	public void addRecordType(RecordType typeToAdd)
	{
		recordTypes.put(typeToAdd.getName(), typeToAdd);
	}
	
	public void addLinkageType(LinkageType typeToAdd)
	{
		linkageTypes.put(typeToAdd.getName(), typeToAdd);
	}
	
	public RecordType getRecordType(String typeName)
	{
		return recordTypes.get(typeName);
	}
	
	public LinkageType getLinkageType(String typeName)
	{
		return linkageTypes.get(typeName);
	}
	
	public LinkageType[] getLinkageTypesOwnedBy(String recordTypeName)
	{
		Vector<LinkageType> ownedTypes = new Vector<LinkageType>();
		Iterator<String> linkageTypeNameIterator = linkageTypes.keySet().iterator();
		while(linkageTypeNameIterator.hasNext())
		{
			String linkageTypeName = linkageTypeNameIterator.next();
			LinkageType type = getLinkageType(linkageTypeName);
			if(type.getOwnerClassName().equals(recordTypeName))
				ownedTypes.add(type);
		}
		return ownedTypes.toArray(new LinkageType[0]);
	}
	
	private Map<String, RecordType> recordTypes;
	private Map<String, LinkageType> linkageTypes;
}
