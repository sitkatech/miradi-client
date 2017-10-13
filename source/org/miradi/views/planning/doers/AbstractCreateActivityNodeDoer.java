/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.planning.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.StrategyActivityRelevancyInterface;
import org.miradi.objects.Task;
import org.miradi.project.ProjectTotalCalculatorStrategy;
import org.miradi.schemas.StrategySchema;
import org.miradi.views.diagram.doers.CloneStressDoer;

abstract class AbstractCreateActivityNodeDoer extends AbstractCreateTaskNodeDoer
{
	@Override
	protected ORef getParentRef()
	{
		ORefList selectionHierarchy = getSelectionHierarchy();
		if (selectionHierarchy.isEmpty())
			return ORef.INVALID;

		ORef selectedRef = selectionHierarchy.getFirstElement();
		if (selectedRef.isInvalid())
			return ORef.INVALID;
		
		if (Strategy.is(selectedRef))
			return selectedRef;
		
		if (Task.isActivity(getProject(), selectedRef))
			return selectionHierarchy.getRefForType(StrategySchema.getObjectType());
		
		return ORef.INVALID;
	}
	
	@Override
	protected void doWork(ORefList selectionBeforeCreate, ORef newTaskRef) throws Exception
	{
		super.doWork(selectionBeforeCreate, newTaskRef);

		if (isMonitoringActivity())
		{
			CommandSetObjectData setData = new CommandSetObjectData(newTaskRef, Task.TAG_IS_MONITORING_ACTIVITY, BooleanData.BOOLEAN_TRUE);
			getProject().executeCommand(setData);

		}

		CloneStressDoer.includeInAllItsOwnersTag(getProject(), newTaskRef);
	}

	protected String getWorkPlanBudgetMode()
	{
		ProjectTotalCalculatorStrategy projectTotalCalculatorStrategy = getProject().getTimePeriodCostsMapsCache().getProjectTotalCalculator().getProjectTotalCalculatorStrategy();
		return projectTotalCalculatorStrategy.getWorkPlanBudgetMode();
	}

	protected void createActivityRelevancy(ORefList selectionHierarchy, ORef newTaskRef) throws Exception
	{
		ORef parentObjectRef = findParentObjectRef(selectionHierarchy);
		if (parentObjectRef.isInvalid())
			return;

		StrategyActivityRelevancyInterface parentObject = (StrategyActivityRelevancyInterface) getProject().findObject(parentObjectRef);
		ORefList strategyAndActivityRefs = parentObject.getRelevantStrategyAndActivityRefs();
		strategyAndActivityRefs.add(newTaskRef);
		RelevancyOverrideSet relevancySet = parentObject.getCalculatedRelevantStrategyActivityOverrides(strategyAndActivityRefs);

		CommandSetObjectData addNewActivityToRelevancyList = new CommandSetObjectData(parentObjectRef, parentObject.getRelevantStrategyActivitySetTag(), relevancySet.toString());
		getProject().executeCommand(addNewActivityToRelevancyList);
	}

	private ORef findParentObjectRef(ORefList selectionHierarchy)
	{
		for (int index = 0; index < selectionHierarchy.size(); ++index)
		{
			ORef ref = selectionHierarchy.get(index);
			BaseObject parentObject = getProject().findObject(ref);

			if (parentObject instanceof StrategyActivityRelevancyInterface)
				return ref;
		}

		return ORef.INVALID;
	}

	protected abstract boolean isMonitoringActivity();
}
