/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import java.util.HashMap;
import java.util.Map;

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
	
	private Map<String, RecordType> recordTypes;
	private Map<String, LinkageType> linkageTypes;
}
