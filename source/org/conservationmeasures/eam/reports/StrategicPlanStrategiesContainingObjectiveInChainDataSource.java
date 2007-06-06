package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.strategicplan.StratPlanObjective;
import org.conservationmeasures.eam.views.strategicplan.StratPlanStrategy;

public class StrategicPlanStrategiesContainingObjectiveInChainDataSource extends CommonDataSource
{
	public StrategicPlanStrategiesContainingObjectiveInChainDataSource(Objective objective)
	{
		super(objective.getProject());
		ORefList list = new ORefList();
		StratPlanStrategy[] objectiveVector = StratPlanObjective.getStrategyNodes(objective);
		for (int i=0; i<objectiveVector.length; ++i)
		{
			BaseObject ss = objectiveVector[i].getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
}
