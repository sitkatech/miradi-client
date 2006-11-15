/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;

public class ORefListData extends ObjectData
{
	public ORefListData()
	{
		objectReferenceList = new ORefList();
	}
	
	public ORefListData(String valueToUse)
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
		if (! (rawOther instanceof ORefListData))
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
		set(new ORefList(newValue));	
	}
	
	private void set(ORefList objectReferenceToUse)
	{
		objectReferenceList = objectReferenceToUse;
	}

	private ORefList objectReferenceList;
}
