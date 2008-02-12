/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.HashMap;

import org.miradi.ids.FactorId;

public class CreateDiagramFactorParameter extends CreateObjectParameter
{
	public CreateDiagramFactorParameter(ORef factorRefToUse)
	{
		factorRef = factorRefToUse;
	}
	
	public FactorId getFactorId()
	{
		return new FactorId(factorRef.getObjectId().asInt());
	}

	public ORef getFactorRef()
	{
		return factorRef;
	}
	
	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(ORef.class.getSimpleName(), factorRef);
		
		return formatDataString(dataPairs);
	}
	
	private ORef factorRef;
}
