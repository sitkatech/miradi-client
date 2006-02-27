package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;

public class TNCThreatFormula
{
	TNCThreatFormula(ThreatRatingFramework frameworkToUse)
	{
		framework = frameworkToUse;
	}
	
	public int computeMagnitude(int scope, int severity)
	{		
		if(scope == 0 || severity == 0)
			return 0;
		if(scope == 1 || severity == 1)
			return 1;
		if(scope == 2 || severity == 2)
			return 2;
		if(scope == 3 || severity == 3)
			return 3;
		if(scope == 4 || severity == 4)
			return 4;
		
		throw new RuntimeException("unknown scope or severity" + scope + " " + severity);
	}
	
	public int computeSeriousness(int magnitude, int urgency)
	{
		if(magnitude == 0 || urgency == 0)
			return 0;
		if(urgency == 3 || urgency == 4)
			return magnitude;
		if(magnitude == 1 || magnitude == 2 )
			return 1;
		if(magnitude == 3 && urgency == 2)
			return 2;
		if(magnitude == 4 && urgency == 2)
			return 3;
		if(magnitude == 3 && urgency == 1)
			return 1;
		if(magnitude == 4 && urgency == 1)
			return 2;
		
		throw new RuntimeException("unknown magnitude or urgency" + magnitude + " " + urgency);
	}
	
	public int computeBundleValue(int scope, int severity, int urgency)
	{
		return computeSeriousness(computeMagnitude(scope, severity), urgency);
	}
	
	public int computeBundleValue(ThreatRatingBundle bundle)
	{
		
		int scope = getCriterionValue(bundle, "Scope");
		int severity = getCriterionValue(bundle, "Severity");
		int urgency = getCriterionValue(bundle, "Custom1");
		
		return computeBundleValue(scope, severity, urgency);
	}
	
	public int getSummaryOfBundles(int[] bundleValues)
	{
		int veryHigh =0;
		int high = 0;
		int medium = 0;
		int low = 0;
		
		for(int i =0; i < bundleValues.length; ++i)
		{
			if(bundleValues[i] == 4)
				++veryHigh;
			if(bundleValues[i] == 3)
				++high;
			if(bundleValues[i] == 2)
				++medium;
			if(bundleValues[i] == 1)
				++low;
		}
		
		int newLow = low % 7;
		medium += ((low - newLow) / 7);
		low = newLow;
		
		int newMedium = medium % 5;
		high += ((medium - newMedium) / 5);
		medium = newMedium;
		
		int newHigh = high % 3;
		veryHigh += ((high - newHigh) / 3);
		high = newHigh;
		
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

	private int getCriterionValue(ThreatRatingBundle bundle, String label)
	{
		int criterionId = framework.findCriterionByLabel(label).getId();
		return framework.getValueOption(bundle.getValueId(criterionId)).getNumericValue();
	}
	
	ThreatRatingFramework framework;
}
