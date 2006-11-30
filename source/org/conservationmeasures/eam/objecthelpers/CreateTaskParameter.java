/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

public class CreateTaskParameter extends CreateObjectParameter
{
	public CreateTaskParameter(ORef parentRefToUse)
	{
		parentRef = parentRefToUse;
	}

	public ORef getParentRef()
	{
		return parentRef;
	}
	
	ORef parentRef;
}
