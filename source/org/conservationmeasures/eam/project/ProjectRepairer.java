package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class ProjectRepairer
{
	public static void repairAnyProblems(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.repair();
	}
	
	public ProjectRepairer(Project projectToRepair)
	{
		project = projectToRepair;
	}
	
	void repair() throws Exception
	{
		fixNodeAnnotationIds();
		fixDeletedTeamMembers();
		repairUnsnappedNodes();
		deleteOrphanAnnotations();
	}
	
	void fixNodeAnnotationIds() throws Exception
	{
		FactorId[] nodeIds = project.getFactorPool().getModelNodeIds();
		for(int i = 0; i < nodeIds.length; ++i)
		{
			FactorId nodeId = nodeIds[i];
			Factor node = project.findNode(nodeId);
			fixGhostIndicatorIds(node);
			removeInvalidGoalIds(node);
			removeInvalidObjectiveIds(node);
			removeMissingObjectiveIds(node);
		}
	}
	
	private void fixGhostIndicatorIds(Factor node)
	{
		IdList newIndicatorIds = new IdList();
		IdList oldIndicatorIds = node.getIndicators();
		for(int j = 0; j < oldIndicatorIds.size(); ++j)
		{
			IndicatorId indicatorId = new IndicatorId(oldIndicatorIds.get(j).asInt());
			if(indicatorId.isInvalid())
				continue;
			EAMObject indicator = project.findObject(ObjectType.INDICATOR, indicatorId);
			if(indicator == null)
				EAM.logWarning("Fixing node " + node.getId() + " ghost indicatorId " + indicatorId);
			else
				newIndicatorIds.add(indicatorId);
		}
		if(newIndicatorIds.equals(oldIndicatorIds))
			return;
		
		try
		{
			node.setIndicators(newIndicatorIds);
			project.writeNode(node.getModelNodeId());
		}
		catch (Exception logAndContinue)
		{
			EAM.logError("Repair failed");
			logAndContinue(logAndContinue);
		}

	}
	
	private void removeInvalidGoalIds(Factor node)
	{
		IdList ids = node.getGoals();
		if(!ids.contains(BaseId.INVALID))
			return;
		
		EAM.logWarning("Removing invalid goal id for " + node.getId());
		ids.removeId(BaseId.INVALID);
		node.setGoals(ids);
		try
		{
			project.writeNode(node.getModelNodeId());
		}
		catch(Exception logAndContinue)
		{
			EAM.logError("Repair failed");
			logAndContinue(logAndContinue);
		}
	}
	
	private void removeInvalidObjectiveIds(Factor node)
	{
		IdList ids = node.getObjectives();
		if(!ids.contains(BaseId.INVALID))
			return;
		
		EAM.logWarning("Removing invalid objective id for " + node.getId());
		ids.removeId(BaseId.INVALID);
		node.setObjectives(ids);
		try
		{
			project.writeNode(node.getModelNodeId());
		}
		catch(Exception logAndContinue)
		{
			EAM.logError("Repair failed");
			logAndContinue(logAndContinue);
		}
	}
	
	private void removeMissingObjectiveIds(Factor node)
	{
		IdList newIds = new IdList();
		IdList oldIds = node.getObjectives();
		for(int i = 0; i < oldIds.size(); ++i)
		{
			BaseId id = oldIds.get(i);
			if(project.findObject(ObjectType.OBJECTIVE, id) == null)
				EAM.logWarning("Removing missing objective id " + id + " for " + node.getId());
			else
				newIds.add(id);
		}

		if(newIds.size() == oldIds.size())
			return;
		
		node.setObjectives(newIds);
		try
		{
			project.writeNode(node.getModelNodeId());
		}
		catch(Exception logAndContinue)
		{
			EAM.logError("Repair failed");
			logAndContinue(logAndContinue);
		}
	}
	
	void fixDeletedTeamMembers() throws Exception
	{
		ProjectMetadata metadata = project.getMetadata();
		if(metadata == null)
			return;
		
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
					logAndContinue(e);
				}
			}
		}
	}
	

	private void repairUnsnappedNodes()
	{
		Vector diagramNodes = project.getDiagramModel().getAllDiagramFactors();
		for (int i=0; i<diagramNodes.size(); ++i) 
			fixLocation((DiagramFactor) diagramNodes.get(i));
	}

	private void fixLocation(DiagramFactor diagramNode)
	{
		Point currentLocation = diagramNode.getLocation();
		Point expectedLocation  = project.getSnapped(currentLocation);
		int deltaX = expectedLocation.x - currentLocation.x;
		int deltaY = expectedLocation.y - currentLocation.y;

		if(deltaX == 0 && deltaY == 0)
			return;
			
		try
		{
			project.moveNodes(deltaX, deltaY, new DiagramFactorId[] { diagramNode.getDiagramFactorId() });
		}
		catch(Exception e)
		{
			logAndContinue(e);
		}
	}
	
	public void deleteOrphanAnnotations()
	{
		deleteOrphanAnnotations(ObjectType.OBJECTIVE);
		deleteOrphanAnnotations(ObjectType.GOAL);
		deleteOrphanAnnotations(ObjectType.INDICATOR);
	}

	private void deleteOrphanAnnotations(int annotationType)
	{
		IdList allIds = project.getPool(annotationType).getIdList();
		ChainManager chainManager = new ChainManager(project);
		for(int i = 0; i < allIds.size(); ++i)
		{
			BaseId annotationId = allIds.get(i);
			try
			{
				FactorSet nodes = chainManager.findNodesThatUseThisAnnotation(annotationType, annotationId);
				if(nodes.size() == 0)
				{
					EAM.logWarning("Deleting orphan " + annotationType + ":" + annotationId);
					project.deleteObject(annotationType, annotationId);
				}
			}
			catch(Exception e)
			{
				logAndContinue(e);
			}
		}
	}

	private void logAndContinue(Exception e)
	{
		EAM.logException(e);
	}	
	
	Project project;
}
