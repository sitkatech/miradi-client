/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;

public class SummaryColumnComparator implements  Comparator
{
	
	public SummaryColumnComparator(ThreatMatrixTableModel modelToUse )
	{
		model = modelToUse;
		framework = model.getFramework();
	}

	public int compare(Object object1, Object object2)
	{
		try
		{
			Factor factor1 = (Factor) object1;
			BaseId baseId1 = factor1.getId();
			ValueOption valueOption1 = framework.getThreatThreatRatingValue(baseId1);

			Factor factor2 = (Factor) object2;
			BaseId baseId2 = factor2.getId();
			ValueOption valueOption2 = framework.getThreatThreatRatingValue(baseId2);
			
			Integer value1 = new Integer(valueOption1.getNumericValue());
			Integer value2 = new Integer(valueOption2.getNumericValue());
			
			int test = value1.compareTo(value2);
			if (test == 0)
				return factor1.getLabel().compareToIgnoreCase(factor2.getLabel());

			return test;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return -1;
		}
	}

	ThreatMatrixTableModel model;
	SimpleThreatRatingFramework framework;

}
