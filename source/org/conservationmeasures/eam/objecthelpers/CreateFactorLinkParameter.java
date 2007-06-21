/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

public class CreateFactorLinkParameter extends CreateObjectParameter
{
	public CreateFactorLinkParameter(ORef fromRefToUse, ORef toRefToUse)
	{
		fromRef = fromRefToUse;
		toRef = toRefToUse;
	}
	
	public ORef getFromRef()
	{
		return fromRef;
	}
	
	public ORef getToRef()
	{
		return toRef;
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
