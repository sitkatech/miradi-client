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

	private int getCriterionValue(ThreatRatingBundle bundle, String label)
	{
		int criterionId = framework.findCriterionByLabel(label).getId();
		return framework.getValueOption(bundle.getValueId(criterionId)).getNumericValue();
	}
	
	ThreatRatingFramework framework;
}
