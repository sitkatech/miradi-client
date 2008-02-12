/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.HashMap;

public class CreateThreatStressRatingParameter extends CreateObjectParameter
{
	public CreateThreatStressRatingParameter(ORef stressRefToUse)
	{
		stressRef = stressRefToUse;
	}
	
	public ORef getStressRef()
	{
		return stressRef;
	}
	
	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("StressRef", stressRef);
		
		return formatDataString(dataPairs);
	}

	private ORef stressRef;
}
