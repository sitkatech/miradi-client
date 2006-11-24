/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class SummaryColumnComparator implements  Comparator
{
	
	public SummaryColumnComparator(NonEditableThreatMatrixTableModel modelToUse )
	{
		model = modelToUse;
		framework = model.getFramework();
	}

	public int compare(Object object1, Object object2)
	{
		try
		{
			BaseId baseId1 = ((Factor) object1).getId();
			ValueOption valueOption1 = framework.getThreatThreatRatingValue(baseId1);

			BaseId baseId2 = ((Factor) object2).getId();
			ValueOption valueOption2 = framework.getThreatThreatRatingValue(baseId2);
			
			Integer value1 = new Integer(valueOption1.getNumericValue());
			Integer value2 = new Integer(valueOption2.getNumericValue());

			return value1.compareTo(value2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	NonEditableThreatMatrixTableModel model;
	ThreatRatingFramework framework;

}
