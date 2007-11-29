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

	public int computeContributionByIrreversibility(int contribution, int irreversibility)
	{
		if (isInvalidValue(contribution))
			throw new RuntimeException("unknown contribution" + contribution);
		
		if (isInvalidValue(irreversibility))
			throw new RuntimeException("unknown irreversibility" + irreversibility);

		int[][] contributionIrreversibilityTable = 
		{
				{0, 0, 0, 0, 0}, // 0 Ir	
				{0, 1, 1, 2, 3}, // 1 re		
				{0, 1, 2, 2, 3}, // 2 ve
				{0, 1, 2, 3, 4}, // 3 rs
				{0, 1, 3, 3, 4}, // 4 ib
			//   0  1  2  3  4        ility	
			//    Contribution 	
			
		};

		return contributionIrreversibilityTable[contribution][irreversibility];
	}
	
	private boolean isInvalidValue(int value)
	{
		return value < 0 || value > 4;
	}
}
