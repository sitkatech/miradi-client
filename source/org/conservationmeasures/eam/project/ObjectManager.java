/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;
		
		pools = new HashMap();
		pools.put(new Integer(ObjectType.MODEL_NODE), new NodePool());
		pools.put(new Integer(ObjectType.MODEL_LINKAGE), new LinkagePool());
		pools.put(new Integer(ObjectType.TASK), new TaskPool());
		pools.put(new Integer(ObjectType.VIEW_DATA), new ViewPool());
		pools.put(new Integer(ObjectType.PROJECT_RESOURCE), new ResourcePool());
		pools.put(new Integer(ObjectType.INDICATOR), new IndicatorPool());
		pools.put(new Integer(ObjectType.OBJECTIVE), new ObjectivePool());

		goalPool = GoalPool.createSampleGoals(getAnnotationIdAssigner());
	}

	private IdAssigner getAnnotationIdAssigner()
	{
		return getProject().getAnnotationIdAssigner();
	}
	
	public EAMObjectPool getPool(int objectType)
	{
		return (EAMObjectPool)pools.get(new Integer(objectType));
	}
	
	public NodePool getNodePool()
	{
		return (NodePool)getPool(ObjectType.MODEL_NODE);
	}
	
	public LinkagePool getLinkagePool()
	{
		return (LinkagePool)getPool(ObjectType.MODEL_LINKAGE);
	}
	
	public TaskPool getTaskPool()
	{
		return (TaskPool)getPool(ObjectType.TASK);
	}
	
	public ViewPool getViewPool()
	{
		return (ViewPool)getPool(ObjectType.VIEW_DATA);
	}
	
	public ResourcePool getResourcePool()
	{
		return (ResourcePool)getPool(ObjectType.PROJECT_RESOURCE);
	}
	
	public IndicatorPool getIndicatorPool()
	{
		return (IndicatorPool)getPool(ObjectType.INDICATOR);
	}

	public ObjectivePool getObjectivePool()
	{
		return (ObjectivePool)getPool(ObjectType.OBJECTIVE);
	}
	
	public GoalPool getGoalPool()
	{
		return goalPool;
	}
	
	public BaseId createObject(int objectType) throws Exception
	{
		return createObject(objectType, BaseId.INVALID);
	}
	
	public BaseId createObject(int objectType, BaseId objectId) throws Exception
	{
		BaseId createdId = BaseId.INVALID;
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
			{
				createdId = getThreatRatingFramework().createCriterion(objectId);
				EAMObject newObject = getThreatRatingFramework().getCriterion(createdId);
				getDatabase().writeObject(newObject);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
			}
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
			{
				createdId = getThreatRatingFramework().createValueOption(objectId);
				EAMObject newObject = getThreatRatingFramework().getValueOption(createdId);
				getDatabase().writeObject(newObject);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
			}
			case ObjectType.TASK:
			{
				if(objectId.isInvalid())
					objectId = getAnnotationIdAssigner().takeNextId();
				Task task = new Task(objectId);
				getTaskPool().put(task);
				getDatabase().writeObject(task);
				createdId = task.getId();
				break;
			}
			
			case ObjectType.MODEL_NODE:
			{
				createdId = getProject().insertNodeAtId(new NodeTypeTarget(), objectId);
				break;
			}
			
			case ObjectType.VIEW_DATA:
			{
				if(objectId.isInvalid())
					objectId = getAnnotationIdAssigner().takeNextId();
				ViewData viewData = new ViewData(objectId);
				getViewPool().put(viewData);
				getDatabase().writeObject(viewData);
				createdId = viewData.getId();
				break;
			}
			
			case ObjectType.MODEL_LINKAGE:
			{
				objectId = getProject().obtainRealLinkageId(objectId);
				ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(objectId, BaseId.INVALID, BaseId.INVALID);
				getLinkagePool().put(cmLinkage);
				getDatabase().writeObject(cmLinkage);
				createdId = cmLinkage.getId();
				break;
			}
			
			case ObjectType.PROJECT_RESOURCE:
			{
				if(objectId.isInvalid())
					objectId = getAnnotationIdAssigner().takeNextId();
				ProjectResource resource = new ProjectResource(objectId);
				getResourcePool().put(resource);
				getDatabase().writeObject(resource);
				createdId = resource.getId();
				break;
			}
				
			case ObjectType.INDICATOR:
			{
				if(objectId.isInvalid())
					objectId = getAnnotationIdAssigner().takeNextId();
				Indicator indicator = new Indicator(objectId);
				getIndicatorPool().put(indicator);
				getDatabase().writeObject(indicator);
				createdId = indicator.getId();
				break;
			}
				
			case ObjectType.OBJECTIVE:
			{
				if(objectId.isInvalid())
					objectId = getAnnotationIdAssigner().takeNextId();
				Objective objective = new Objective(objectId);
				getObjectivePool().put(objective);
				getDatabase().writeObject(objective);
				createdId = objective.getId();
				break;
			}
				
			default:
				throw new RuntimeException("Attempted to create unknown object type: " + objectType);
		}
		
		return createdId;
	}
	
	public void deleteObject(int objectType, BaseId objectId) throws IOException, ParseException
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				getThreatRatingFramework().deleteCriterion(objectId);
				getDatabase().deleteObject(objectType, objectId);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				getThreatRatingFramework().deleteValueOption(objectId);
				getDatabase().deleteObject(objectType, objectId);
				getDatabase().writeThreatRatingFramework(getThreatRatingFramework());
				break;
				
			case ObjectType.TASK:
				getTaskPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.MODEL_NODE:
				getNodePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.VIEW_DATA:
				getViewPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				getLinkagePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.PROJECT_RESOURCE:
				getResourcePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.INDICATOR:
				getIndicatorPool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.OBJECTIVE:
				getObjectivePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			default:
				throw new RuntimeException("Attempted to delete unknown object type: " + objectType);
		}
	}
	
	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				getThreatRatingFramework().setCriterionData(objectId, fieldTag, dataValue);
				getDatabase().writeObject(getThreatRatingFramework().getCriterion(objectId));
				break;
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				getThreatRatingFramework().setValueOptionData(objectId, fieldTag, dataValue);
				getDatabase().writeObject(getThreatRatingFramework().getValueOption(objectId));
				break;
			
			case ObjectType.TASK:
				Task task = getTaskPool().find(objectId);
				task.setData(fieldTag, dataValue);
				getDatabase().writeObject(task);
				break;
				
			case ObjectType.MODEL_NODE:
				ConceptualModelNode node = getNodePool().find(objectId);
				node.setData(fieldTag, dataValue);
				getDatabase().writeNode(node);
				break;
				
			case ObjectType.VIEW_DATA:
				ViewData viewData = getViewPool().find(objectId);
				viewData.setData(fieldTag, dataValue);
				getDatabase().writeObject(viewData);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				ConceptualModelLinkage linkage = getLinkagePool().find(objectId);
				linkage.setData(fieldTag, dataValue);
				getDatabase().writeLinkage(linkage);
				break;
				
			case ObjectType.PROJECT_RESOURCE:
				ProjectResource resource = getResourcePool().find(objectId);
				resource.setData(fieldTag, dataValue);
				getDatabase().writeObject(resource);
				break;
				
			case ObjectType.INDICATOR:
				Indicator indicator = getIndicatorPool().find(objectId);
				indicator.setData(fieldTag, dataValue);
				getDatabase().writeObject(indicator);
				break;
				
			case ObjectType.OBJECTIVE:
				Objective objective = getObjectivePool().find(objectId);
				objective.setData(fieldTag, dataValue);
				getDatabase().writeObject(objective);
				break;
				
				
			default:
				throw new RuntimeException("Attempted to set data for unknown object type: " + objectType);
		}
	}
	
	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		switch(objectType)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				return getThreatRatingFramework().getCriterionData(objectId, fieldTag);
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				return getThreatRatingFramework().getValueOptionData(objectId, fieldTag);
				
			case ObjectType.TASK:
				return getTaskPool().find(objectId).getData(fieldTag);
				
			case ObjectType.MODEL_NODE:
				return getNodePool().find(objectId).getData(fieldTag);
				
			case ObjectType.VIEW_DATA:
				return getViewPool().find(objectId).getData(fieldTag);
				
			case ObjectType.MODEL_LINKAGE:
				return getLinkagePool().find(objectId).getData(fieldTag);
				
			case ObjectType.PROJECT_RESOURCE:
				return getResourcePool().find(objectId).getData(fieldTag);
				
			case ObjectType.INDICATOR:
				return getIndicatorPool().find(objectId).getData(fieldTag);
				
			case ObjectType.OBJECTIVE:
				return getObjectivePool().find(objectId).getData(fieldTag);
				
			default:
				throw new RuntimeException("Attempted to get data for unknown object type: " + objectType);
		}
	}
	
	Project getProject()
	{
		return project;
	}
	
	ThreatRatingFramework getThreatRatingFramework()
	{
		return getProject().getThreatRatingFramework();
	}
	
	ProjectServer getDatabase()
	{
		return getProject().getDatabase();
	}
	
	Project project;
	
	HashMap pools;
	GoalPool goalPool;

}
