/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.CodeListData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

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
		rowConfigurationList = new CodeListData(TAG_ROW_CONFIGURATION, getQuestion(InternalQuestionWithoutValues.class));
		colConfigurationList = new CodeListData(TAG_COL_CONFIGURATION, getQuestion(InternalQuestionWithoutValues.class));
		
		addField(TAG_ROW_CONFIGURATION, rowConfigurationList);
		addField(TAG_COL_CONFIGURATION, colConfigurationList);
	}

	public static final String TAG_ROW_CONFIGURATION = "TagRowConfiguration";
	public static final String TAG_COL_CONFIGURATION = "TagColConfiguration"; 
	
	public static final String OBJECT_NAME = "PlanningViewConfiguration";
	
	CodeListData rowConfigurationList;
	CodeListData colConfigurationList;
}
