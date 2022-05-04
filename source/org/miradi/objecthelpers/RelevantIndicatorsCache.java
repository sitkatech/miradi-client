/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

public class RelevantIndicatorsCache implements CommandExecutedListener
{
	public RelevantIndicatorsCache(Project projectToUse)
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
		if (event.isCreateCommandForThisType(IndicatorSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(IndicatorSchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(IndicatorSchema.getObjectType(), Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
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
		allRelevantIndicatorsByBaseObject = new HashMap<ORef, ORefList>();
		relevantIndicatorsByBaseObject = new HashMap<ORef, ORefList>();
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

	public ORefList getRelevantIndicatorsForStrategy(ORef strategyRef) throws Exception
	{
		ORefList result = relevantIndicatorsByBaseObject.get(strategyRef);
		if(result == null)
		{
			result = new ORefList();
			ORefSet indicatorRefs = getProject().getPool(IndicatorSchema.getObjectType()).getRefSet();
			for(ORef indicatorRef: indicatorRefs)
			{
				Indicator indicator = Indicator.find(getProject(), indicatorRef);
				if(indicator.getRelevantStrategyRefs().contains(strategyRef))
					result.add(indicator.getRef());
			}

			relevantIndicatorsByBaseObject.put(strategyRef, result);
		}

		return result;
	}

	public ORefList getAllRelevantIndicatorsForStrategyOrActivity(ORef strategyOrActivityRef) throws Exception
	{
		ORefList result = allRelevantIndicatorsByBaseObject.get(strategyOrActivityRef);
		if(result == null)
		{
			result = new ORefList();
			ORefSet indicatorRefs = getProject().getPool(IndicatorSchema.getObjectType()).getRefSet();
			for(ORef indicatorRef: indicatorRefs)
			{
				Indicator indicator = Indicator.find(getProject(), indicatorRef);
				if(indicator.getRelevantStrategyAndActivityRefs().contains(strategyOrActivityRef))
					result.add(indicator.getRef());
			}

			allRelevantIndicatorsByBaseObject.put(strategyOrActivityRef, result);
		}

		return result;
	}

	private Project project;
	private HashMap<ORef, ORefList> allRelevantIndicatorsByBaseObject;
	private HashMap<ORef, ORefList> relevantIndicatorsByBaseObject;
}
