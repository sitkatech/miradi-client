/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.util.Comparator;

import org.conservationmeasures.eam.objects.EAMObject;

public class EAMObjectComparator implements Comparator
{
		public EAMObjectComparator(LegacyObjectPoolTableModel tableModelToUse, int sortColumnToUse)
		{
			sortColumn = sortColumnToUse;
			columnTag = tableModelToUse.getColumnTag(sortColumnToUse);
			tableModel = tableModelToUse;
		}

		public int compare(Object object1, Object object2)
		{
			EAMObject eamObject1 = (EAMObject)object1;
			EAMObject eamObject2 = (EAMObject)object2;
			
			String tag = tableModel.getColumnTag(sortColumn);
			String value1 = eamObject1.getData(tag);
			String value2 = eamObject2.getData(tag);
			
			return value1.compareToIgnoreCase(value2);
		}
		int sortColumn;
		String columnTag;
		LegacyObjectPoolTableModel tableModel;
		
	}
