/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;

public class TaskSchema extends FactorSchema
{
	public TaskSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaIdList(Task.TAG_SUBTASK_IDS, TaskSchema.getObjectType());
		createFieldSchemaMultiLineUserText(Task.TAG_DETAILS);
		
		createPseudoFieldSchemaString(Task.PSEUDO_TAG_STRATEGY_LABEL);
		createPseudoFieldSchemaString(Task.PSEUDO_TAG_INDICATOR_LABEL);
		createPseudoFieldSchemaRefList(Task.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS);
		createPseudoFieldSchemaRefList(Task.PSEUDO_TAG_RELEVANT_GOAL_REFS);
	}

	public static int getObjectType()
	{
		return ObjectType.TASK;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "Task";
	public static final String METHOD_NAME = "Method";
	public static final String ACTIVITY_NAME = "Activity";
}
