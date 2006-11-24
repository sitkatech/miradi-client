/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.FactorId;

public class CreateFactorLinkParameter extends CreateObjectParameter
{
	public CreateFactorLinkParameter(FactorId from, FactorId to)
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
