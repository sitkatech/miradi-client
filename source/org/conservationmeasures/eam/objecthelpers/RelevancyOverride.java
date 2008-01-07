/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class RelevancyOverride
{
	public RelevancyOverride(ORef refToUse, boolean overrideFlagToUse)
	{
		ref = refToUse;
		overrideFlag = overrideFlagToUse;
	}
	
	public RelevancyOverride(EnhancedJsonObject json)
	{
		this(json.getRef(TAG_OVERRIDE_REF), json.getBoolean(TAG_OVERRIDE_FLAG));
	}
	
	public RelevancyOverride(String jsonAsString) throws Exception
	{
		this(new EnhancedJsonObject(jsonAsString));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.putRef(TAG_OVERRIDE_REF, getRef());
		json.put(TAG_OVERRIDE_FLAG, isOverride());
		
		return json;
	}
		
	public ORef getRef()
	{
		return ref;
	}

	public boolean isOverride()
	{
		return overrideFlag;
	}
	
	public boolean equals(Object rawOther)
	{
		if (!(rawOther instanceof RelevancyOverride))
			return false;

		RelevancyOverride other = (RelevancyOverride) rawOther;
		if (!getRef().equals(other.getRef()))
			return false;
		
		if (isOverride() != other.isOverride())
			return false;
		
		return true;
	}
	
	public int hashCode()
	{
		return getRef().hashCode();
	}
	
	public String toString()
	{
		return toJson().toString();
	}
	
	private ORef ref;
	private boolean overrideFlag;
	
	private static final String TAG_OVERRIDE_REF = "OverrideRef";
	private static final String TAG_OVERRIDE_FLAG = "OverrideFlag";
}
