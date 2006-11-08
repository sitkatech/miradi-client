/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.Comparator;

import org.conservationmeasures.eam.objects.EAMObject;

public class EAMObjectComparator implements Comparator
{
		public EAMObjectComparator(AnnotationTableModel tableModelToUse, int sortColumnToUse)
		{
			sortColumn = sortColumnToUse;
			columnTag = tableModelToUse.getColumnTag(sortColumnToUse);
			tableModel = tableModelToUse;
		}

		public int compare(Object object1, Object object2)
		{
			int row1 = ((ObjectManagerTableModel)tableModel).getRowIndex((EAMObject)object1);
			int row2 = ((ObjectManagerTableModel)tableModel).getRowIndex((EAMObject)object2);
			
			String value1 = tableModel.getTableCellDisplayString(row1, sortColumn, ((EAMObject)object1).getId(), columnTag);
			String value2 = tableModel.getTableCellDisplayString(row2, sortColumn, ((EAMObject)object2).getId(), columnTag);
			
			return value1.compareToIgnoreCase(value2);
		}
		int sortColumn;
		String columnTag;
		AnnotationTableModel tableModel;
		
	}
