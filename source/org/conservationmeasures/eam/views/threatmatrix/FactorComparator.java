/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class FactorComparator implements  Comparator
{
	
	public FactorComparator(int sortColumnToUse, NonEditableThreatMatrixTableModel modelToUse )
	{
		sortColumn = sortColumnToUse;
		model = modelToUse;
		targetList = model.getTargets();
		framework = model.getFramework();
	}

	public int compare(Object object1, Object object2)
	{
		try
		{
			FactorId nodeIdColumn = targetList[sortColumn].getModelNodeId();

			Factor factor1 = ((Factor) object1);
			Factor factor2 = ((Factor) object2);
			FactorId nodeIdRow1 = factor1.getModelNodeId();
			FactorId nodeIdRow2 = factor2.getModelNodeId();

			ThreatRatingBundle bundle1 = model.getBundle(nodeIdRow1,nodeIdColumn);
			ThreatRatingBundle bundle2 = model.getBundle(nodeIdRow2,nodeIdColumn);

			if (bundle1==null && bundle2==null) return 0;
			if (bundle1==null) return -1;
			if (bundle2==null) return 1;
			
			ValueOption valueOption1 = framework.getBundleValue(bundle1);
			ValueOption valueOption2 = framework.getBundleValue(bundle2);

			Integer value1 = new Integer(valueOption1.getNumericValue());
			Integer value2 = new Integer(valueOption2.getNumericValue());
			
			int test = value1.compareTo(value2);
			
			if (test == 0)
			{
				String label1 = factor1.getData(Factor.TAG_LABEL);
				String label2 = factor2.getData(Factor.TAG_LABEL);
				return label1.compareToIgnoreCase(label2);
			}
			return test;
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	int sortColumn = 0;
	Factor[] targetList;
	NonEditableThreatMatrixTableModel model;
	ThreatRatingFramework framework;

}
