package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.workplan.WorkPlanMonitoringIndicator;
import org.conservationmeasures.eam.views.workplan.WorkPlanTaskNode;

public class BudgetMontiroingIndicatorDataSource extends CommonDataSource
{
	public BudgetMontiroingIndicatorDataSource(Indicator indicator)
	{
		super(indicator.getProject());
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
