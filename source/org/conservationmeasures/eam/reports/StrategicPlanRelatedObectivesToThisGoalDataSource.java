package org.conservationmeasures.eam.reports;

import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.strategicplan.StratPlanGoal;
import org.conservationmeasures.eam.views.strategicplan.StratPlanObjective;

public class StrategicPlanRelatedObectivesToThisGoalDataSource extends CommonDataSource
{
	public StrategicPlanRelatedObectivesToThisGoalDataSource(Goal goal) throws Exception
	{
		super(goal.getProject());
		ORefList list = new ORefList();
		Vector objectiveVector = StratPlanGoal.getObjectiveNodes(goal);
		for (int i=0; i<objectiveVector.size(); ++i)
		{
			BaseObject ss = ((StratPlanObjective)objectiveVector.get(i)).getObject();
			list.add(ss.getRef());
		}

		setObjectList(list);
	}
	
	public JRDataSource getStrategiesContainingObjectiveInChainDataSource()
	{
		return new StrategicPlanStrategiesContainingObjectiveInChainDataSource((Objective)getCurrentObject());
	}
} 
