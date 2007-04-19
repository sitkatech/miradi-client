package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.workplan.WorkPlanMonitoringIndicator;
import org.conservationmeasures.eam.views.workplan.WorkPlanTaskNode;

public class BugetMontiroingIndicatorDataSource extends CommonDataSource
{
	public BugetMontiroingIndicatorDataSource(Indicator indicator)
	{
		super(indicator.getObjectManager().getProject());
		WorkPlanTaskNode[] nodes =  WorkPlanMonitoringIndicator.getWorkPlanIndicatorTask(indicator);
		ORefList list = new ORefList();
		for (int i=0; i<nodes.length; ++i)
		{
			BaseObject object = nodes[i].getObject();
			list.add(object.getRef());
		}

		setObjectList(list);
	}
}
