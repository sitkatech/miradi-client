package org.conservationmeasures.eam.reports;

import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.monitoring.MonitoringGoalNode;
import org.conservationmeasures.eam.views.monitoring.MonitoringObjectiveNode;

public class MonitoringPlanRelatedObectivesToThisGoalDataSource extends CommonDataSource
{

	public MonitoringPlanRelatedObectivesToThisGoalDataSource(Goal goal) throws Exception
	{
		super(goal.getProject());
		FactorSet relatedNodes = new ChainManager(goal.getProject()).findAllFactorsRelatedToThisGoal(goal.getId());
		Vector objectiveVector = MonitoringGoalNode.createObjectiveNodes(project, relatedNodes);
		ORefList list = new ORefList();
		for (int i=0; i<objectiveVector.size(); ++i)
		{
			BaseObject ss = ((MonitoringObjectiveNode)objectiveVector.get(i)).getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
	
	public JRDataSource getUpstreamIndicatorsForObjectiveOwnerDataSource()
	{
		return new MonitoringPlanRUpstreamIndicatorsForObjectiveOwnerDataSource((Objective)getCurrentObject());
	}
}
