/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class PlanningViewConfigeration extends BaseObject
{
	public PlanningViewConfigeration(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public PlanningViewConfigeration(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public PlanningViewConfigeration(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new BaseId(idAsInt), jsonObject);
	}

	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.PLANNING_VIEW_CONFIGURATION;
	}	
	
	void clear()
	{
		super.clear();
		rowConfigerationList = new CodeListData();
		colConfigerationList = new CodeListData();
		
		addField(TAG_ROW_CONFIGERATION, rowConfigerationList);
		addField(TAG_COL_CONFIGERATION, colConfigerationList);
	}

	public static final String TAG_ROW_CONFIGERATION = "TagRowConfigeration";
	public static final String TAG_COL_CONFIGERATION = "TagColConfigeration"; 
	
	public static final String OBJECT_NAME = "PlanningViewConfigeration";
	
	CodeListData rowConfigerationList;
	CodeListData colConfigerationList;
}
