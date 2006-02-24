package org.conservationmeasures.eam.project;

public class TNCThreatFormula
{
	
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
}
