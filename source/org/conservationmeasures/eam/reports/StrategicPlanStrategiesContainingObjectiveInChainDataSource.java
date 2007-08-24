package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.strategicplan.StratPlanStrategy;

public class StrategicPlanStrategiesContainingObjectiveInChainDataSource extends CommonDataSource
{
	public StrategicPlanStrategiesContainingObjectiveInChainDataSource(Project projectToUse, Objective objective)
	{
		super(projectToUse);
		ORefList list = new ORefList();
		ObjectManager objectManager = projectToUse.getObjectManager();
		StratPlanStrategy[] objectiveVector = objectManager.getStrategyNodes(objective.getRef());
		for (int i=0; i<objectiveVector.length; ++i)
		{
			BaseObject ss = objectiveVector[i].getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
}
