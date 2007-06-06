package org.conservationmeasures.eam.reports;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.monitoring.MonitoringIndicatorNode;
import org.conservationmeasures.eam.views.monitoring.MonitoringObjectiveNode;

public class MonitoringPlanRUpstreamIndicatorsForObjectiveOwnerDataSource extends CommonDataSource
{
	public MonitoringPlanRUpstreamIndicatorsForObjectiveOwnerDataSource(Objective objective)
	{
		super(objective.getProject());
		Vector indicatorVector = MonitoringObjectiveNode.getAllUpstreamIndicatorsForObjectiveOwner(objective);
		ORefList list = new ORefList();
		for (int i=0; i<indicatorVector.size(); ++i)
		{
			BaseObject ss = ((MonitoringIndicatorNode)indicatorVector.get(i)).getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
		
	}

}
