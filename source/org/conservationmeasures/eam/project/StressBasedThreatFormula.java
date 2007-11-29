/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

public class StressBasedThreatFormula
{
	public StressBasedThreatFormula()
	{
	}
	
	public int computeSevertyByScope(int scope, int severity)
	{
		if (isInvalidValue(scope))
			throw new RuntimeException("unknown scope" + scope);
		
		if (isInvalidValue(severity))
			throw new RuntimeException("unknown severity" + severity);
		
		return Math.min(scope, severity);
	}

	private boolean isInvalidValue(int value)
	{
		return value < 0 || value > 4;
	}
}
