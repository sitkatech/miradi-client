/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class PlanningViewConfiguration extends BaseObject
{
	public PlanningViewConfiguration(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public PlanningViewConfiguration(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public PlanningViewConfiguration(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new BaseId(idAsInt), jsonObject);
	}

	public CodeList getRowConfiguration()
	{
		return rowConfigurationList.getCodeList();
	}
	
	public CodeList getColumnConfiguration()
	{
		return colConfigurationList.getCodeList();
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
	
	public String toString()
	{
		return getLabel();
	}
	
	void clear()
	{
		super.clear();
		rowConfigurationList = new CodeListData();
		colConfigurationList = new CodeListData();
		
		addField(TAG_ROW_CONFIGURATION, rowConfigurationList);
		addField(TAG_COL_CONFIGURATION, colConfigurationList);
	}

	public static final String TAG_ROW_CONFIGURATION = "TagRowConfiguration";
	public static final String TAG_COL_CONFIGURATION = "TagColConfiguration"; 
	
	public static final String OBJECT_NAME = "PlanningViewConfiguration";
	
	CodeListData rowConfigurationList;
	CodeListData colConfigurationList;
}
