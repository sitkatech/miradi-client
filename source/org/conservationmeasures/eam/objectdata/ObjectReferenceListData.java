/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectReferenceList;

public class ObjectReferenceListData extends ObjectData
{
	public ObjectReferenceListData()
	{
		objectReferenceList = new ObjectReferenceList();
	}
	
	public ObjectReferenceListData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch(Exception e)
		{
			EAM.logDebug("ObjectReferenceListData ignoring invalid: " + valueToUse);
		}
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof ObjectReferenceListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	public String get()
	{
		return objectReferenceList.toString();
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	public void set(String newValue) throws Exception
	{
		set(new ObjectReferenceList(newValue));	
	}
	
	private void set(ObjectReferenceList objectReferenceToUse)
	{
		objectReferenceList = objectReferenceToUse;
	}

	private ObjectReferenceList objectReferenceList;
}
