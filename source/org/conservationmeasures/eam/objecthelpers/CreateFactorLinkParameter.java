/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

import org.conservationmeasures.eam.ids.FactorId;

public class CreateFactorLinkParameter extends CreateObjectParameter
{
	public CreateFactorLinkParameter(ORef fromRefToUse, ORef toRefToUse)
	{
		fromRef = fromRefToUse;
		toRef = toRefToUse;
	}
	
	public FactorId getFromId()
	{
		return (FactorId) fromRef.getObjectId();
	}
	
	public FactorId getToId()
	{
		return (FactorId) toRef.getObjectId();
	}
	
	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("FactorFromRef", fromRef);
		dataPairs.put("FactorToRef", toRef);
		return formatDataString(dataPairs);
	}
	
	ORef fromRef;
	ORef toRef;
}
