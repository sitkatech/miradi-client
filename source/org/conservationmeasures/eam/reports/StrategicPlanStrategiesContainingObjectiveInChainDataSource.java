package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;

public class StrategicPlanStrategiesContainingObjectiveInChainDataSource extends CommonDataSource
{
	public StrategicPlanStrategiesContainingObjectiveInChainDataSource(Project projectToUse, Objective objective)
	{
		super(projectToUse);

		ObjectManager objectManager = projectToUse.getObjectManager();
		ORefList strategyRefs = objectManager.getStrategyRefsUpstreamOfObjective(objective.getRef());
		setObjectList(strategyRefs);
	}
}
