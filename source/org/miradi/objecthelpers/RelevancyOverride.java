/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import org.miradi.utils.EnhancedJsonObject;

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

	public void setOverride(boolean isOverride)
	{
		overrideFlag = isOverride;
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
	
	private static final String TAG_OVERRIDE_REF = "Ref";
	private static final String TAG_OVERRIDE_FLAG = "Flag";
}
