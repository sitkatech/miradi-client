/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.io.IOException;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.UnicodeWriter;

public class ORefData extends ObjectData
{
	public ORefData()
	{
		ref = ORef.INVALID;
	}
	
	public ORefData(String data) throws Exception
	{
		set(data);
	}
	
	public ORef getRawRef()
	{
		return ref;
	}
	
	public boolean isValid()
	{
		return !ref.equals(ORef.INVALID);
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
   
	public void set(ORef refToUse)
	{
		ref = refToUse;
	}
	 
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
			ref = ORef.INVALID;
		else
			ref = new ORef(new EnhancedJsonObject(newValue));
	}

	public void toXml(UnicodeWriter out) throws IOException
	{
		ref.toXml(out);
	}

	ORef ref;
}
