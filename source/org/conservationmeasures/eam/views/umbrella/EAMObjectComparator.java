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
		public EAMObjectComparator(String columnTagToUse)
		{
			columnTag = columnTagToUse;
		}

		public int compare(Object object1, Object object2)
		{
			String value1 = ((EAMObject) object1).getData(columnTag);
			String value2 = ((EAMObject) object2).getData(columnTag);
			return value1.compareToIgnoreCase(value2);

		}

		String columnTag;
	}
