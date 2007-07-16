/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

import org.conservationmeasures.eam.ids.FactorId;

public class CreateDiagramFactorParameter extends CreateObjectParameter
{
	public CreateDiagramFactorParameter(FactorId factorIdToUse)
	{
		factorId = factorIdToUse;
	}
	
	public FactorId getFactorId()
	{
		return factorId;
	}

	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(FactorId.class.getSimpleName(), factorId);
		
		return formatDataString(dataPairs);
	}
	
	FactorId factorId;
}
