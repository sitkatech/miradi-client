/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
