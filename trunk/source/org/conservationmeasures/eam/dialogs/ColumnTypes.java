/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.util.HashMap;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Strategy;

public class ColumnTypes
{
	public static int getColumnType(int objectType, String tag)
	{
		if(typeMap == null)
			typeMap = buildTypeMap();
		
		String key = buildKey(objectType, tag);
		Integer value = (Integer)typeMap.get(key);
		if(value == null)
			return COLUMN_TYPE_NORMAL;
		
		return value.intValue();
	}
	
	private static HashMap buildTypeMap()
	{
		HashMap map = new HashMap();
		
		map.put(buildKey(ObjectType.STRATEGY, Strategy.TAG_SHORT_LABEL), SHORT_LABEL);
		map.put(buildKey(ObjectType.INDICATOR, Indicator.TAG_SHORT_LABEL), SHORT_LABEL);
		map.put(buildKey(ObjectType.OBJECTIVE, Objective.TAG_SHORT_LABEL), SHORT_LABEL);
		map.put(buildKey(ObjectType.GOAL, Goal.TAG_SHORT_LABEL), SHORT_LABEL);
		map.put(buildKey(ObjectType.ACCOUNTING_CODE, AccountingCode.TAG_CODE), SHORT_LABEL);
		map.put(buildKey(ObjectType.FUNDING_SOURCE, FundingSource.TAG_CODE), SHORT_LABEL);
		map.put(buildKey(ObjectType.PROJECT_RESOURCE, ProjectResource.TAG_INITIALS), SHORT_LABEL);

		return map;
	}

	private static String buildKey(int objectType, String tag)
	{
		return Integer.toString(objectType)+"."+tag;
	}
	
	public static final int COLUMN_TYPE_NORMAL = 0;
	public static final int COLUMN_TYPE_SHORT_LABEL = 1;
	public static final int COLUMN_TYPE_ITEM_LIST = 2;
	
	// commented out to avoid warnings...uncomment if/when needed
	//private static final Integer NORMAL = new Integer(COLUMN_TYPE_NORMAL);
	
	private static final Integer SHORT_LABEL = new Integer(COLUMN_TYPE_SHORT_LABEL);
	
	// commented out to avoid warnings...uncomment if/when needed
	//private static final Integer ITEM_LIST = new Integer(COLUMN_TYPE_ITEM_LIST);

	static HashMap typeMap;
}
