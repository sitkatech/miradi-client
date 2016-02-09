/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;
import org.miradi.schemas.ObjectTreeTableConfigurationSchema;
import org.miradi.utils.CodeList;

public class ObjectTreeTableConfiguration extends BaseObject implements PlanningTreeRowColumnProvider
{
	public ObjectTreeTableConfiguration(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static ObjectTreeTableConfigurationSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ObjectTreeTableConfigurationSchema createSchema(ObjectManager objectManager)
	{
		return (ObjectTreeTableConfigurationSchema) objectManager.getSchemas().get(ObjectType.OBJECT_TREE_TABLE_CONFIGURATION);
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return getCodeListData(TAG_ROW_CONFIGURATION);
	}
	
	public CodeList getColumnCodesToShow() throws Exception
	{
		return getCodeListData(TAG_COL_CONFIGURATION);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public boolean shouldPutTargetsAtTopLevelOfTree() throws Exception
	{
		String code = getData(TAG_TARGET_NODE_POSITION);
		return PlanningTreeTargetPositionQuestion.shouldPutTargetsAtTopLevelOfTree(code);
	}
	
	public boolean shouldIncludeResultsChain()
	{
		return DiagramObjectDataInclusionQuestion.shouldIncludeResultsChain(getDiagramInclusionCode());
	}

	public boolean shouldIncludeConceptualModelPage()
	{
		return DiagramObjectDataInclusionQuestion.shouldIncludeConceptualModelPage(getDiagramInclusionCode());
	}
	
	private String getDiagramInclusionCode()
	{
		return getData(TAG_DIAGRAM_DATA_INCLUSION);
	}
	
	public boolean doObjectivesContainStrategies() throws Exception
	{
		String order = getData(TAG_STRATEGY_OBJECTIVE_ORDER);
		return order.equals(StrategyObjectiveTreeOrderQuestion.OBJECTIVE_CONTAINS_STRATEGY_CODE);
	}
	
	public String getDiagramFilter() throws Exception
	{
		return getData(TAG_DIAGRAM_FILTER);
	}

	@Override
	public String toString()
	{
		return getLabel();
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ObjectTreeTableConfigurationSchema.getObjectType();
	}
	
	public static ObjectTreeTableConfiguration find(ObjectManager objectManager, ORef planningViewConfigurationRef)
	{
		return (ObjectTreeTableConfiguration) objectManager.findObject(planningViewConfigurationRef);
	}
	
	public static ObjectTreeTableConfiguration find(Project project, ORef planningViewConfigurationRef)
	{
		return find(project.getObjectManager(), planningViewConfigurationRef);
	}
	
	public static final String TAG_ROW_CONFIGURATION = "TagRowConfiguration";
	public static final String TAG_COL_CONFIGURATION = "TagColConfiguration";
	public static final String TAG_DIAGRAM_DATA_INCLUSION = "TagDiagramDataInclusion";
	public static final String TAG_DIAGRAM_FILTER = "TagDiagramFilter";
	public static final String TAG_STRATEGY_OBJECTIVE_ORDER = "StrategyObjectiveOrder";
	public static final String TAG_TARGET_NODE_POSITION = "TargetNodePosition";
}
