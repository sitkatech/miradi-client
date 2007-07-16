package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanMonitoringIndicator;
import org.conservationmeasures.eam.views.workplan.WorkPlanMonitoringRoot;

public class BudgetMonitoringDataSource extends CommonDataSource
{
	public BudgetMonitoringDataSource(Project project)
	{
		super(project);
		WorkPlanMonitoringIndicator[] nodes =  WorkPlanMonitoringRoot.getWorkPlanIndicators(project);
		ORefList list = new ORefList();
		for (int i=0; i<nodes.length; ++i)
		{
			BaseObject object = nodes[i].getObject();
			list.add(object.getRef());
		}

		setObjectList(list);
	}
	
	public JRDataSource getBudgetMontiroingIndicatorDataSource() throws Exception
	{
		return new BudgetMontiroingIndicatorDataSource((Indicator)getCurrentObject());
	}
} 

