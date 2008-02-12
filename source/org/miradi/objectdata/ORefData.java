/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.EnhancedJsonObject;

public class ORefData extends ObjectData
{
	public ORefData(String tagToUse)
	{
		super(tagToUse);
		ref = ORef.INVALID;
	}
	
	public ORef getRawRef()
	{
		return ref;
	}
	
	public ORefList getRefList()
	{
		return new ORefList(new ORef[] {ref});
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

	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		ref.toXml(out);
		endTagToXml(out);
	}

	ORef ref;
}
