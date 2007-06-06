package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.workplan.WorkPlanStrategyNode;
import org.conservationmeasures.eam.views.workplan.WorkPlanTaskNode;

public class BudgetStrategyActivitiesDataSource extends CommonDataSource
{
	public BudgetStrategyActivitiesDataSource(Strategy strategy)
	{
		super(strategy.getProject());
		WorkPlanTaskNode[] nodes = WorkPlanStrategyNode.getWorkPlanActivitiesTask(strategy);
		ORefList list = new ORefList();
		for (int i=0; i<nodes.length; ++i)
		{
			BaseObject object = nodes[i].getObject();
			list.add(object.getRef());
		}

		setObjectList(list);
	}
}
