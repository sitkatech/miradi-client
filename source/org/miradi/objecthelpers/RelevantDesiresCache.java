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

package org.miradi.objecthelpers;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.*;

import java.util.HashMap;

public class RelevantDesiresCache implements CommandExecutedListener
{
	public RelevantDesiresCache(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
	
	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (commandInvalidatesCache(event))
			clear();
	}

	private boolean commandInvalidatesCache(CommandExecutedEvent event)
	{
		if (event.isCreateCommandForThisType(GoalSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(GoalSchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(GoalSchema.getObjectType(), Goal.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		if (event.isCreateCommandForThisType(ObjectiveSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(ObjectiveSchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ObjectiveSchema.getObjectType(), Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		if (event.isCreateCommandForThisType(StrategySchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(StrategySchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(StrategySchema.getObjectType(), Strategy.TAG_ACTIVITY_IDS))
			return true;

		if (event.isCreateCommandForThisType(TaskSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(TaskSchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagram.TAG_DIAGRAM_FACTOR_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ResultsChainDiagramSchema.getObjectType(), ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ResultsChainDiagramSchema.getObjectType(), ResultsChainDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(DiagramLinkSchema.getObjectType(), DiagramLink.TAG_IS_BIDIRECTIONAL_LINK))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(DiagramLinkSchema.getObjectType(), DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(DiagramLinkSchema.getObjectType(), DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID))
			return true;

		return false;
	}

	private void clearAllCachedData()
	{
		allRelevantGoalsByBaseObject = new HashMap<ORef, ORefList>();
		allRelevantObjectivesByBaseObject = new HashMap<ORef, ORefList>();
		relevantGoalsByBaseObject = new HashMap<ORef, ORefList>();
		relevantObjectivesByBaseObject = new HashMap<ORef, ORefList>();
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	private Project getProject()
	{
		return project;
	}

	public ORefList getRelevantDesiresForStrategy(ORef strategyRef, final int desireType) throws Exception
	{
		switch(desireType)
		{
			case ObjectType.GOAL:
				return getRelevantGoalsForStrategy(strategyRef);

			case ObjectType.OBJECTIVE:
				return getRelevantObjectivesForStrategy(strategyRef);
		}

		throw new Exception("getRelevantDesiresForStrategy called for unknown desire type " + desireType);
	}

	private ORefList getRelevantGoalsForStrategy(ORef strategyRef) throws Exception
	{
		ORefList result = relevantGoalsByBaseObject.get(strategyRef);
		if(result == null)
		{
			result = getRelevantGoalsOrObjectivesForStrategy(strategyRef, GoalSchema.getObjectType());
			relevantGoalsByBaseObject.put(strategyRef, result);
		}

		return result;
	}

	private ORefList getRelevantObjectivesForStrategy(ORef strategyRef) throws Exception
	{
		ORefList result = relevantObjectivesByBaseObject.get(strategyRef);
		if(result == null)
		{
			result = getRelevantGoalsOrObjectivesForStrategy(strategyRef, ObjectiveSchema.getObjectType());
			relevantObjectivesByBaseObject.put(strategyRef, result);
		}

		return result;
	}

	private ORefList getRelevantGoalsOrObjectivesForStrategy(ORef strategyRef, final int desireType) throws Exception
	{
		ORefSet desireRefs = getProject().getPool(desireType).getRefSet();
		ORefList result = new ORefList();
		for(ORef desireRef: desireRefs)
		{
			Desire desire = Desire.findDesire(getProject(), desireRef);
			if(desire.getRelevantStrategyRefs().contains(strategyRef))
				result.add(desire.getRef());
		}

		return result;
	}

	public ORefList getAllRelevantDesiresForStrategyOrActivity(ORef strategyOrActivityRef, final int desireType) throws Exception
	{
		switch(desireType)
		{
			case ObjectType.GOAL:
				return getAllRelevantGoalsForStrategyOrActivity(strategyOrActivityRef);

			case ObjectType.OBJECTIVE:
				return getAllRelevantObjectivesForStrategyOrActivity(strategyOrActivityRef);
		}

		throw new Exception("getAllRelevantDesiresForStrategyOrActivity called for unknown desire type " + desireType);
	}

	private ORefList getAllRelevantGoalsForStrategyOrActivity(ORef strategyOrActivityRef) throws Exception
	{
		ORefList result = allRelevantGoalsByBaseObject.get(strategyOrActivityRef);
		if(result == null)
		{
			result = getAllRelevantGoalsOrObjectivesForStrategyOrActivity(strategyOrActivityRef, GoalSchema.getObjectType());
			allRelevantGoalsByBaseObject.put(strategyOrActivityRef, result);
		}

		return result;
	}

	private ORefList getAllRelevantObjectivesForStrategyOrActivity(ORef strategyOrActivityRef) throws Exception
	{
		ORefList result = allRelevantObjectivesByBaseObject.get(strategyOrActivityRef);
		if(result == null)
		{
			result = getAllRelevantGoalsOrObjectivesForStrategyOrActivity(strategyOrActivityRef, ObjectiveSchema.getObjectType());
			allRelevantObjectivesByBaseObject.put(strategyOrActivityRef, result);
		}

		return result;
	}

	private ORefList getAllRelevantGoalsOrObjectivesForStrategyOrActivity(ORef strategyOrActivityRef, final int desireType) throws Exception
	{
		ORefSet desireRefs = getProject().getPool(desireType).getRefSet();
		ORefList result = new ORefList();
		for(ORef desireRef: desireRefs)
		{
			Desire desire = Desire.findDesire(getProject(), desireRef);
			if(desire.getRelevantStrategyAndActivityRefs().contains(strategyOrActivityRef))
				result.add(desire.getRef());
		}

		return result;
	}

	private Project project;
	private HashMap<ORef, ORefList> allRelevantGoalsByBaseObject;
	private HashMap<ORef, ORefList> allRelevantObjectivesByBaseObject;
	private HashMap<ORef, ORefList> relevantGoalsByBaseObject;
	private HashMap<ORef, ORefList> relevantObjectivesByBaseObject;
}
