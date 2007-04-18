package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.strategicplan.StratPlanObjective;
import org.conservationmeasures.eam.views.strategicplan.StratPlanStrategy;

public class StrategiesContainingObjectiveInChainDataSource extends CommonDataSource
{
	public StrategiesContainingObjectiveInChainDataSource(Objective objective)
	{
		super(objective.getObjectManager().getProject());
		try
		{
			ORefList list = new ORefList();
			StratPlanStrategy[] objectiveVector = StratPlanObjective.getStrategyNodes(project, objective);
			for (int i=0; i<objectiveVector.length; ++i)
			{
				BaseObject ss = objectiveVector[i].getObject();
				list.add(ss.getRef());
			}

			setObjectList(list);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
}
