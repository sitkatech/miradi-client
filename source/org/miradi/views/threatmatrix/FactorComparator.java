/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.threatmatrix;

import java.util.Comparator;

import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.ValueOption;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;

public class FactorComparator implements  Comparator
{
	
	public FactorComparator(int sortColumnToUse, ThreatMatrixTableModel modelToUse )
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
			FactorId nodeIdColumn = targetList[sortColumn].getFactorId();

			Factor factor1 = ((Factor) object1);
			Factor factor2 = ((Factor) object2);
			FactorId nodeIdRow1 = factor1.getFactorId();
			FactorId nodeIdRow2 = factor2.getFactorId();

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
				return factor1.getLabel().compareToIgnoreCase(factor2.getLabel());
			
			return test;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return -1;
		}
	}

	int sortColumn = 0;
	Factor[] targetList;
	ThreatMatrixTableModel model;
	SimpleThreatRatingFramework framework;

}
