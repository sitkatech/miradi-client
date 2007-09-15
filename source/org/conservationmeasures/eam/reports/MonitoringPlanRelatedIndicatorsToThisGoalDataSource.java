package org.conservationmeasures.eam.reports;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.monitoring.MonitoringGoalNode;
import org.conservationmeasures.eam.views.monitoring.MonitoringIndicatorNode;

public class MonitoringPlanRelatedIndicatorsToThisGoalDataSource extends CommonDataSource
{

	public MonitoringPlanRelatedIndicatorsToThisGoalDataSource(Goal goal) throws Exception
	{
		super(goal.getProject());
		FactorSet relatedNodes = new ChainManager(goal.getProject()).findAllFactorsRelatedToThisGoal(goal.getId());
		MonitoringGoalNode.createIndicatorNodes(project, relatedNodes);
		Vector indicatorVector = MonitoringGoalNode.createIndicatorNodes(project, relatedNodes);
		ORefList list = new ORefList();
		for (int i=0; i<indicatorVector.size(); ++i)
		{
			BaseObject ss = ((MonitoringIndicatorNode)indicatorVector.get(i)).getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
}
