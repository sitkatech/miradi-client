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

import org.miradi.ids.BaseId;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ValueOption;

public class SimpleThreatFormula extends ThreatFormula
{
	public SimpleThreatFormula()
	{
	}
	
	public SimpleThreatFormula(SimpleThreatRatingFramework frameworkToUse)
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
	
	public int computeSeriousness(int magnitude, int irreversibility)
	{
		if(magnitude == 0 || irreversibility == 0)
			return 0;
		if(irreversibility == 2 || irreversibility == 3)
			return magnitude;
		if(magnitude == 4 && irreversibility == 4)
			return 4;
		if(magnitude == 1 && irreversibility == 1)
			return 1;
		if(irreversibility == 4)
			return (magnitude + 1);
		if(irreversibility == 1)
			return (magnitude - 1);
		
		throw new RuntimeException("unknown magnitude or urgency" + magnitude + " " + irreversibility);
	}
	
	public int computeBundleValue(int scope, int severity, int irreversibility)
	{
		return computeSeriousness(computeMagnitude(scope, severity), irreversibility);
	}
	
	public int computeBundleValue(ThreatRatingBundle bundle)
	{
		
		int scope = getCriterionValue(bundle, "Scope");
		int severity = getCriterionValue(bundle, "Severity");
		int urgency = getCriterionValue(bundle, "Irreversibility");
		
		return computeBundleValue(scope, severity, urgency);
	}
	
	public int getHighestValue(int[] values)
	{
		int highestValue = 0;
		
		for(int i = 0; i < values.length; ++i)
		{
			if(values[i] > highestValue)
				highestValue = values[i];
		}
		
		return highestValue;
	}

	private int getCriterionValue(ThreatRatingBundle bundle, String label)
	{
		RatingCriterion criterion = framework.findCriterionByLabel(label);
		if(criterion == null)
			return DEFAULT_VALUE;
		
		BaseId criterionId = criterion.getId();
		BaseId valueId = bundle.getValueId(criterionId);
		ValueOption valueOption = framework.getValueOption(valueId);
		return valueOption.getNumericValue();
	}
	
	static final int DEFAULT_VALUE = 0;
	
	SimpleThreatRatingFramework framework;
}
