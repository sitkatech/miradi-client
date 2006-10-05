/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMNormalObjectPool;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.ThreatRatingCriterionPool;
import org.conservationmeasures.eam.objectpools.ThreatRatingValueOptionPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;
		
		IdAssigner ida = getAnnotationIdAssigner();
		pools = new HashMap();
		pools.put(new Integer(ObjectType.THREAT_RATING_CRITERION), new ThreatRatingCriterionPool(ida));
		pools.put(new Integer(ObjectType.THREAT_RATING_VALUE_OPTION), new ThreatRatingValueOptionPool(ida));
		pools.put(new Integer(ObjectType.MODEL_NODE), new NodePool());
		pools.put(new Integer(ObjectType.MODEL_LINKAGE), new LinkagePool());
		pools.put(new Integer(ObjectType.TASK), new TaskPool(ida));
		pools.put(new Integer(ObjectType.VIEW_DATA), new ViewPool(ida));
		pools.put(new Integer(ObjectType.PROJECT_RESOURCE), new ResourcePool(ida));
		pools.put(new Integer(ObjectType.INDICATOR), new IndicatorPool(ida));
		pools.put(new Integer(ObjectType.OBJECTIVE), new ObjectivePool(ida));
		pools.put(new Integer(ObjectType.GOAL), new GoalPool(ida));

		linkageListener = new LinkageMonitor();
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
		return (GoalPool)getPool(ObjectType.GOAL);
	}
	
	public BaseId createObject(int objectType, BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId createdId = BaseId.INVALID;
		switch(objectType)
		{
			case ObjectType.MODEL_NODE:
			{
				CreateModelNodeParameter parameter = (CreateModelNodeParameter)extraInfo;
				objectId = getProject().obtainRealNodeId(objectId);
				ConceptualModelNode node = ConceptualModelNode.createConceptualModelObject(objectId, parameter);
				getNodePool().put(node);
				getDatabase().writeObject(node);
				createdId = node.getId();
				break;
			}
			case ObjectType.MODEL_LINKAGE:
			{
				CreateModelLinkageParameter parameter = (CreateModelLinkageParameter)extraInfo;
				objectId = getProject().obtainRealLinkageId(objectId);
				ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(objectId, parameter.getFromId(), parameter.getToId());
				getDatabase().writeObject(cmLinkage);
				addLinkageToPool(cmLinkage);
				createdId = cmLinkage.getId();
				break;
			}
			default:
			{
				EAMNormalObjectPool pool = (EAMNormalObjectPool)getPool(objectType);
				EAMObject created = pool.createObject(objectId);
				getDatabase().writeObject(created);
				createdId = created.getId();
				break;
			}
			
		}
		
		return createdId;
	}

	public void deleteObject(int objectType, BaseId objectId) throws IOException, ParseException
	{
		switch(objectType)
		{
			case ObjectType.MODEL_NODE:
				getNodePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				ConceptualModelLinkage linkage = getLinkagePool().find(objectId);
				ModelNodeId fromId = linkage.getFromNodeId();
				ModelNodeId toId = linkage.getToNodeId();
				getLinkagePool().remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				linkageListener.linkageWasDeleted(fromId, toId);
				break;
				
			default:
				if(getPool(objectType).findObject(objectId) == null)
					throw new RuntimeException("Attempted to delete missing object: " + objectType + ":" + objectId);
				getPool(objectType).remove(objectId);
				getDatabase().deleteObject(objectType, objectId);
				break;
				
		}
	}
	
	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		switch(objectType)
		{
			case ObjectType.MODEL_NODE:
				ModelNodeId nodeId = new ModelNodeId(objectId.asInt());
				ConceptualModelNode node = getNodePool().find(nodeId);
				node.setData(fieldTag, dataValue);
				getDatabase().writeObject(node);
				break;
				
			case ObjectType.MODEL_LINKAGE:
				ConceptualModelLinkage linkage = getLinkagePool().find(objectId);
				linkage.setData(fieldTag, dataValue);
				getDatabase().writeObject(linkage);
				break;
				
			default:
				EAMObject object = getPool(objectType).findObject(objectId);
				object.setData(fieldTag, dataValue);
				getDatabase().writeObject(object);
				break;
		}
	}
	
	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		switch(objectType)
		{
			case ObjectType.MODEL_NODE:
				ModelNodeId nodeId = new ModelNodeId(objectId.asInt());
				return getNodePool().find(nodeId).getData(fieldTag);
				
			case ObjectType.MODEL_LINKAGE:
				return getLinkagePool().find(objectId).getData(fieldTag);
				
			default:
				return getPool(objectType).findObject(objectId).getData(fieldTag);
				
		}
	}
	
	public void loadFromDatabase() throws Exception
	{
		loadNodePool();
		loadLinkagePool();

		loadPool(ObjectType.TASK);
		loadPool(ObjectType.VIEW_DATA);
		loadPool(ObjectType.PROJECT_RESOURCE);
		loadPool(ObjectType.INDICATOR);
		loadPool(ObjectType.OBJECTIVE);
		loadPool(ObjectType.GOAL);
		loadPool(ObjectType.THREAT_RATING_CRITERION);
		loadPool(ObjectType.THREAT_RATING_VALUE_OPTION);
	}
	
	private void loadNodePool() throws Exception
	{
		ObjectManifest nodes = getDatabase().readObjectManifest(ObjectType.MODEL_NODE);
		BaseId[] nodeIds = nodes.getAllKeys();
		for(int i = 0; i < nodeIds.length; ++i)
		{
			ConceptualModelNode node = (ConceptualModelNode)getDatabase().readObject(ObjectType.MODEL_NODE, nodeIds[i]);
			getNodePool().put(node);
		}
	}
	
	private void loadLinkagePool() throws Exception
	{
		ObjectManifest linkages = getDatabase().readObjectManifest(ObjectType.MODEL_LINKAGE);
		BaseId[] linkageIds = linkages.getAllKeys();
		for(int i = 0; i < linkageIds.length; ++i)
		{
			ConceptualModelLinkage linkage = (ConceptualModelLinkage)getDatabase().readObject(ObjectType.MODEL_LINKAGE, linkageIds[i]);
			addLinkageToPool(linkage);
		}
	}
	
	private void loadPool(int type) throws IOException, ParseException, Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(type);
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			EAMObject object = getDatabase().readObject(type, ids[i]);
			getPool(type).put(ids[i], object);
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
	
	private void addLinkageToPool(ConceptualModelLinkage cmLinkage)
	{
		getLinkagePool().put(cmLinkage);
		linkageListener.linkageWasCreated(cmLinkage.getFromNodeId(), cmLinkage.getToNodeId());
	}
	
	class LinkageMonitor implements LinkageListener
	{
		public void linkageWasCreated(ModelNodeId linkFromId, ModelNodeId linkToId)
		{
			ConceptualModelNode from = getNodePool().find(linkFromId); 
			ConceptualModelNode to = getNodePool().find(linkToId);
			if(from.isFactor() && to.isTarget())
				((ConceptualModelFactor)from).increaseTargetCount();
		}

		public void linkageWasDeleted(ModelNodeId linkFromId, ModelNodeId linkToId)
		{
			ConceptualModelNode from = getNodePool().find(linkFromId);
			ConceptualModelNode to = getNodePool().find(linkToId);
			if(from.isFactor() && to.isTarget())
				((ConceptualModelFactor)from).decreaseTargetCount();
		}		
	}
	
	
	
	Project project;
	
	HashMap pools;
	LinkageListener linkageListener;
}
