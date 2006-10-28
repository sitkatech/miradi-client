package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectMetadata;

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
		fixDeletedTeamMembers();
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
				EAM.logWarning("Fixing node " + nodeId + " ghost indicatorId " + indicatorId);
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
	
	void fixDeletedTeamMembers()
	{
		ProjectMetadata metadata = project.getMetadata(); 
		IdList teamMemberIds = metadata.getTeamResourceIdList();
		for(int i = 0; i < teamMemberIds.size(); ++i)
		{
			BaseId teamMemberId = teamMemberIds.get(i);
			EAMObject resource = project.findObject(ObjectType.PROJECT_RESOURCE, teamMemberId);
			if(resource == null)
			{
				EAM.logWarning("Removing deleted team member " + teamMemberId);
				teamMemberIds.removeId(teamMemberId);
				try
				{
					project.setObjectData(metadata.getType(), metadata.getId(), metadata.TAG_TEAM_RESOURCE_IDS, teamMemberIds.toString()); 
				}
				catch(Exception e)
				{
					EAM.logError("Repair failed");
					EAM.logException(e);
				}
			}
		}
	}
	
	Project project;
}
