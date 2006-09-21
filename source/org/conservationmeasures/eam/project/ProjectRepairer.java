package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;

public class ProjectRepairer
{
	public static void repairAnyProblems(Project project)
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.repair();
	}
	
	public ProjectRepairer(Project projectToRepair)
	{
		project = projectToRepair;
	}
	
	void repair()
	{
		fixGhostIndicatorIds();
	}
	
	void fixGhostIndicatorIds()
	{
		ModelNodeId[] nodeIds = project.getNodePool().getModelNodeIds();
		for(int i = 0; i < nodeIds.length; ++i)
		{
			ModelNodeId nodeId = nodeIds[i];
			ConceptualModelNode node = project.findNode(nodeId);
			IndicatorId indicatorId = node.getIndicatorId();
			if(indicatorId.isInvalid())
				continue;
			EAMObject indicator = project.findObject(ObjectType.INDICATOR, indicatorId);
			if(indicator == null)
			{
				EAM.logDebug("Fixing node " + nodeId + " ghost indicatorId " + indicatorId);
				node.setIndicatorId(new IndicatorId(BaseId.INVALID.asInt()));
				try
				{
					project.writeNode(nodeId);
				}
				catch (Exception e)
				{
					EAM.logError("Repair failed");
					EAM.logException(e);
				}
			}
		}
	}
	
	Project project;
}
