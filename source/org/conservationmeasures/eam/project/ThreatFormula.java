/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.HashMap;

abstract public class ThreatFormula
{

	public HashMap<Integer, Integer> getBundleSummariesUsing357(int[] bundleValues)
	{
		int low = count(bundleValues, 1);
		int medium = count(bundleValues, 2);
		int high = count(bundleValues, 3);
		int veryHigh = count(bundleValues, 4);
		
		// 3-5-7 rule
		int newLow = low % 7;
		medium += ((low - newLow) / 7);
		low = newLow;
		
		int newMedium = medium % 5;
		high += ((medium - newMedium) / 5);
		medium = newMedium;
		
		int newHigh = high % 3;
		veryHigh += ((high - newHigh) / 3);
		high = newHigh;
	
		HashMap<Integer, Integer> computedValues = new HashMap();
		computedValues.put(1, low);
		computedValues.put(2, medium);
		computedValues.put(3, high);
		computedValues.put(4, veryHigh);
		
		return computedValues;
	}
	
	private int count(int[] values, int lookFor)
	{
		int result = 0;
		for(int i = 0; i < values.length; ++i)
			if(values[i] == lookFor)
				++result;
		
		return result;
	}
}
