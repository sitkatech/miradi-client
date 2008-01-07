/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

public class RelevancyOverride
{
	public RelevancyOverride(ORef refToUse, boolean overrideFlagToUse)
	{
		ref = refToUse;
		overrideFlag = overrideFlagToUse;
	}
	
	public ORef getRef()
	{
		return ref;
	}

	public boolean isOverride()
	{
		return overrideFlag;
	}
	
	private ORef ref;
	private boolean overrideFlag;
}
