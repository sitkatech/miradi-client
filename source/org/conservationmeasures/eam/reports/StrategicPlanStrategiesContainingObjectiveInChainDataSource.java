package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.views.strategicplan.StratPlanStrategy;

public class StrategicPlanStrategiesContainingObjectiveInChainDataSource extends CommonDataSource
{
	//FIXME passin project to avoid having to use objective.getproject
	public StrategicPlanStrategiesContainingObjectiveInChainDataSource(Objective objective)
	{
		super(objective.getProject());
		ORefList list = new ORefList();
		ObjectManager objectManager = objective.getProject().getObjectManager();
		StratPlanStrategy[] objectiveVector = objectManager.getStrategyNodes(objective);
		for (int i=0; i<objectiveVector.length; ++i)
		{
			BaseObject ss = objectiveVector[i].getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
}
