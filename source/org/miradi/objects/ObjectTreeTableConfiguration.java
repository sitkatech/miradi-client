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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.CustomPlanningRowsQuestion;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class ObjectTreeTableConfiguration extends BaseObject implements PlanningTreeRowColumnProvider
{
	public ObjectTreeTableConfiguration(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		
		clear();
	}
	
	public ObjectTreeTableConfiguration(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
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
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.OBJECT_TREE_TABLE_CONFIGURATION;
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
	
	public String getWorkPlanBudgetMode() throws Exception
	{
		return WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE;
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
		return objectType == getObjectType();
	}
	
	public static ObjectTreeTableConfiguration find(ObjectManager objectManager, ORef planningViewConfigurationRef)
	{
		return (ObjectTreeTableConfiguration) objectManager.findObject(planningViewConfigurationRef);
	}
	
	public static ObjectTreeTableConfiguration find(Project project, ORef planningViewConfigurationRef)
	{
		return find(project.getObjectManager(), planningViewConfigurationRef);
	}
	
	@Override
	void clear()
	{
		super.clear();
		createCodeListField(TAG_ROW_CONFIGURATION, new CustomPlanningRowsQuestion(getProject()));
		createCodeListField(TAG_COL_CONFIGURATION, CustomPlanningColumnsQuestion.class);
		createChoiceField(TAG_DIAGRAM_DATA_INCLUSION, DiagramObjectDataInclusionQuestion.class);
		createChoiceField(TAG_STRATEGY_OBJECTIVE_ORDER, StrategyObjectiveTreeOrderQuestion.class);
		createChoiceField(TAG_TARGET_NODE_POSITION, PlanningTreeTargetPositionQuestion.class);
	}

	public static final String TAG_ROW_CONFIGURATION = "TagRowConfiguration";
	public static final String TAG_COL_CONFIGURATION = "TagColConfiguration";
	public static final String TAG_DIAGRAM_DATA_INCLUSION = "TagDiagramDataInclusion";
	public static final String TAG_STRATEGY_OBJECTIVE_ORDER = "StrategyObjectiveOrder";
	public static final String TAG_TARGET_NODE_POSITION = "TargetNodePosition";
	
	public static final String OBJECT_NAME = "PlanningViewConfiguration";
}
