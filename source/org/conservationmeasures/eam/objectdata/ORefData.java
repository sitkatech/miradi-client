/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ORefData extends ObjectData
{
	public ORefData()
	{
		ref = new ORef(ObjectType.FAKE, BaseId.INVALID);
	}
	
	public ORefData(String data) throws Exception
	{
		set(data);
	}
	
	public ORef getRawRef()
	{
		return ref;
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof ORefData))
			return false;
		
		ORefData other = (ORefData)rawOther;
		return ref.equals(other.ref);
	}

	public EnhancedJsonObject toJson()
	{
		return ref.toJson();
	}
	
	public String get()
	{
		return ref.toString();
	}

	public int hashCode()
	{
		return ref.hashCode();
	}

	public void set(String newValue) throws Exception
	{
		ref = new ORef(new EnhancedJsonObject(newValue));
	}

	ORef ref;
}
