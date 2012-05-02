/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project.threatrating;

import java.util.HashMap;

public class StressBasedThreatFormula extends ThreatFormula
{
	public StressBasedThreatFormula()
	{
	}
	
	public int computeSeverityByScope(int scope, int severity)
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
				{0, 2, 2, 3, 4}, // 3 rs
				{0, 2, 3, 3, 4}, // 4 ib
			//   0  1  2  3  4        ility	
			//    Contribution 	
			
		};

		return contributionIrreversibilityTable[irreversibility][contribution];
	}
	
	public int computeThreatStressRating(int source, int stress)
	{		
		if (isInvalidValue(source))
			throw new RuntimeException("unknown source" + source);
		
		if (isInvalidValue(stress))
			throw new RuntimeException("unknown stress" + stress);

		int[][] threatStressRatingTable = 
		{
				{0, 0, 0, 0, 0}, // 0  s
				{0, 1, 1, 1, 1}, // 1  t
				{0, 1, 1, 2, 2}, // 2  r
				{0, 1, 2, 3, 3}, // 3  e
				{0, 2, 3, 4, 4}, // 4  s
			//   0  1  2  3  4         s
			//	 	source
		};
		
		return threatStressRatingTable[stress][source];
	}
	
	public boolean isInvalidValue(int value)
	{
		return value < 0 || value > 4;
	}
	
	public int getHighestRatingRule(int[] bundleValues)
	{
		HashMap<Integer, Integer> computed357Values = getBundleSummariesUsing357(bundleValues);
		int low = computed357Values.get(1);
		int medium = computed357Values.get(2);
		int high = computed357Values.get(3);
		int veryHigh = computed357Values.get(4);
		
		return getHighestWithValue(low, medium, high, veryHigh);
	}
	
}
