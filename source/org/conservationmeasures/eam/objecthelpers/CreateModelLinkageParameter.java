/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.FactorId;

public class CreateModelLinkageParameter extends CreateObjectParameter
{
	public CreateModelLinkageParameter(FactorId from, FactorId to)
	{
		fromId = from;
		toId = to;
	}

	public FactorId getFromId()
	{
		return fromId;
	}
	
	public FactorId getToId()
	{
		return toId;
	}
	
	FactorId fromId;
	FactorId toId;
}
