/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import java.util.HashMap;

abstract public class ThreatFormula
{
	
	public int getMajority(int values[])
	{
		int numberOfLegalValues = 5;
		int rankTotals[] = new int[numberOfLegalValues];
		
		for(int i = 0; i < values.length; ++i)
		{
			++rankTotals[values[i]];
		}

		int total = values.length;
		int half = total/2;

		int cumulative = 0;
		for(int i = numberOfLegalValues -1; i >= 0; --i)
		{
			cumulative += rankTotals[i];
			if(cumulative > half)
				return i;
		}

		return 0;
	}
	
	public int getSummaryOfBundlesWithTwoPrimeRule(int[] bundleValues)
	{
		HashMap<Integer, Integer> computed357Values = getBundleSummariesUsing357(bundleValues);
		int low = computed357Values.get(1);
		int medium = computed357Values.get(2);
		int high = computed357Values.get(3);
		int veryHigh = computed357Values.get(4);
			
		return applyTwoPrimeRule(low, medium, high, veryHigh);
	}
	
	private int applyTwoPrimeRule(int low, int medium, int high, int veryHigh)
	{
		if(veryHigh >= 2)
			return 4;
		if(veryHigh == 1 || high >= 2)
			return 3;
		if(high == 1 || medium >= 2)
			return 2;
		if(medium >= 1 || low >= 1)
			return 1;

		return 0;
	}
			
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
