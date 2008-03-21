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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.ValueOption;
import org.miradi.project.SimpleThreatRatingFramework;

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
