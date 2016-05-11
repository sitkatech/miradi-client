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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.SharedWorkPlanVisibleRowsQuestion;

public class SharedWorkPlanTreeRebuilder extends NormalTreeRebuilder
{
	public SharedWorkPlanTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}

	@Override
	protected ORefList getActivities(Strategy strategy) throws Exception
	{
		String workPlanBudgetMode = getProject().getTimePeriodCostsMapsCache().getWorkPlanBudgetMode();

		if (workPlanBudgetMode.equals(SharedWorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
			return strategy.getActivityRefs();

		if (workPlanBudgetMode.equals(SharedWorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE))
			return ORefList.subtract(strategy.getActivityRefs(), strategy.getMonitoringActivityRefs());

		if (workPlanBudgetMode.equals(SharedWorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE))
			return strategy.getMonitoringActivityRefs();

		throw new Exception("getActivities called for unknown work plan budget mode " + workPlanBudgetMode);
	}

	@Override
	protected ORefList getResourceAssignmentsForParent(AbstractPlanningTreeNode parentNode) throws Exception
	{
		BaseObject parentObject = parentNode.getObject();
		if(willThisTypeEndUpInTheTree(parentObject.getTypeName()))
			return parentObject.getResourceAssignmentRefs();

		return new ORefList(){};
	}

	@Override
	protected ORefList getExpenseAssignmentsForParent(AbstractPlanningTreeNode parentNode) throws Exception
	{
		BaseObject parentObject = parentNode.getObject();
		if(willThisTypeEndUpInTheTree(parentObject.getTypeName()))
			return parentObject.getExpenseAssignmentRefs();

		return new ORefList(){};
	}

	@Override
	protected ORefList getRelevantIndicatorsInDiagram(DiagramObject diagram, Desire desire) throws Exception
	{
		return new ORefList(){};
	}

	@Override
	protected ORefList getChildrenOfIndicator(ORef parentRef, DiagramObject diagram) throws Exception
	{
		return new ORefList(){};
	}

	@Override
	protected ORefList getIndicatorsForTarget(AbstractTarget target)
	{
		return new ORefList(){};
	}

	@Override
	protected ORefList getRelevantIndicatorsForStrategy(Strategy strategy)
	{
		return new ORefList(){};
	}
}
