package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanStrategy;
import org.conservationmeasures.eam.views.workplan.WorkPlanStrategyRoot;

public class BudgetStrategyDataSource extends CommonDataSource
{
	public BudgetStrategyDataSource(Project project)
	{
		super(project);
		WorkPlanStrategy[] nodes = WorkPlanStrategyRoot.getWorkPlanStrategies(project);
		ORefList list = new ORefList();
		for (int i=0; i<nodes.length; ++i)
		{
			BaseObject object = nodes[i].getObject();
			list.add(object.getRef());
		}

		setObjectList(list);
	}
	
	public JRDataSource getxxxxDataSource() throws Exception
	{
		return null;
	}

} 
