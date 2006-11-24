/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMNormalObjectPool;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.ProjectMetadataPool;
import org.conservationmeasures.eam.objectpools.RatingCriterionPool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.ValueOptionPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;

		IdAssigner ida = getAnnotationIdAssigner();
		pools = new HashMap();
		pools.put(new Integer(ObjectType.MODEL_NODE), new FactorPool());
		pools.put(new Integer(ObjectType.MODEL_LINKAGE), new FactorLinkPool(new LinkageMonitor()));
		addNormalPool(new RatingCriterionPool(ida));
		addNormalPool(new ValueOptionPool(ida));
		addNormalPool(new TaskPool(ida));
		addNormalPool(new ViewPool(ida));
		addNormalPool(new ResourcePool(ida));
		addNormalPool(new IndicatorPool(ida));
		addNormalPool(new ObjectivePool(ida));
		addNormalPool(new GoalPool(ida));
		addNormalPool(new ProjectMetadataPool(ida));

	}

	private void addNormalPool(EAMNormalObjectPool pool)
	{
		pools.put(new Integer(pool.getObjectType()), pool);
	}

	private IdAssigner getAnnotationIdAssigner()
	{
		return getProject().getAnnotationIdAssigner();
	}

	public EAMObjectPool getPool(int objectType)
	{
		return (EAMObjectPool)pools.get(new Integer(objectType));
	}

	public FactorPool getNodePool()
	{
		return (FactorPool)getPool(ObjectType.MODEL_NODE);
	}

	public FactorLinkPool getLinkagePool()
	{
		return (FactorLinkPool)getPool(ObjectType.MODEL_LINKAGE);
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
				CreateFactorParameter parameter = (CreateFactorParameter)extraInfo;
				FactorId nodeId = new FactorId(getProject().obtainRealNodeId(objectId).asInt());
				Factor node = Factor.createConceptualModelObject(nodeId, parameter);
				getNodePool().put(node);
				getDatabase().writeObject(node);
				createdId = node.getId();
				break;
			}
			case ObjectType.MODEL_LINKAGE:
			{
				CreateFactorLinkParameter parameter = (CreateFactorLinkParameter)extraInfo;
				FactorLinkId realId = getProject().obtainRealLinkageId(objectId);
				FactorLink cmLinkage = new FactorLink(realId, parameter.getFromId(), parameter.getToId());
				getDatabase().writeObject(cmLinkage);
				EAMObjectPool pool = getPool(objectType);
				pool.put(realId, cmLinkage);
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
		EAMObjectPool pool = getPool(objectType);
		if(pool.findObject(objectId) == null)
			throw new RuntimeException("Attempted to delete missing object: " + objectType + ":" + objectId);
		pool.remove(objectId);
		getDatabase().deleteObject(objectType, objectId);
	}

	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		EAMObject object = getPool(objectType).findObject(objectId);
		object.setData(fieldTag, dataValue);
		getDatabase().writeObject(object);
	}

	public boolean isPseudoTag(String fieldTag)
	{
		if (fieldTag.startsWith("PseudoTag"))
			return true;

		return false;
	}

	public String getPseudoField(int objectType, BaseId objectId, String fieldTag)
	{
		switch (objectType)
		{
			case ObjectType.GOAL:			
				return getGoalPseudoField(objectType, objectId, fieldTag);
			case ObjectType.OBJECTIVE:
				return getObjectivePseudoField(objectType, objectId, fieldTag);
			case ObjectType.INDICATOR:
				return getIndicatorPseudoField(objectType, objectId, fieldTag);
		}
		throw new RuntimeException();	
	}
	
	private String getIndicatorPseudoField(int annotationType, BaseId annotationId, String fieldTag)
	{
		try
		{
			if (fieldTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return getAnnotationFactorLabel(annotationType, annotationId);

			if (fieldTag.equals(Indicator.PSEUDO_TAG_TARGETS))
				return getRelatedFactorLabelsAsMultiLine(Factor.TYPE_TARGET, annotationType, annotationId, fieldTag);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_STRATEGIES))
				return getRelatedFactorLabelsAsMultiLine(Factor.TYPE_INTERVENTION, annotationType, annotationId, fieldTag);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(Factor.TYPE_CAUSE, annotationId, annotationType, fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}

	private String getObjectivePseudoField(int objectType, BaseId objectId, String fieldTag)
	{
		try
		{
			if (fieldTag.equals(Desire.PSEUDO_TAG_FACTOR))
				return getAnnotationFactorLabel(objectType, objectId);

			if (fieldTag.equals(Desire.PSEUDO_TAG_TARGETS))
				return getRelatedFactorLabelsAsMultiLine(Factor.TYPE_TARGET, objectType, objectId, fieldTag);
			if (fieldTag.equals(Desire.PSEUDO_TAG_STRATEGIES))
				return getRelatedFactorLabelsAsMultiLine(Factor.TYPE_INTERVENTION, objectType, objectId, fieldTag);
			if (fieldTag.equals(Desire.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(Factor.TYPE_CAUSE, objectId, objectType, fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}

	private String getGoalPseudoField(int objectType, BaseId objectId, String fieldTag)
	{
		try
		{
			if (fieldTag.equals(Desire.PSEUDO_TAG_FACTOR))
				return getAnnotationFactorLabel(objectType, objectId);

			if (fieldTag.equals(Desire.PSEUDO_TAG_STRATEGIES))
				return getRelatedFactorLabelsAsMultiLine(Factor.TYPE_INTERVENTION, objectType, objectId, fieldTag);
			if (fieldTag.equals(Desire.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(Factor.TYPE_CAUSE, objectId, objectType, fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}

	private String getRelatedFactorLabelsAsMultiLine(FactorType nodeType, int annotationType, BaseId annotationId, String fieldTag) throws Exception
	{
		String label ="";
		Factor[] cmNodes = getNodesRelatedToAnnotation(annotationType, annotationId).toNodeArray();
		boolean isFirst = true;
		for (int i = 0; i < cmNodes.length; i++)
		{
			Factor cmNode = cmNodes[i];
			if (cmNode.getNodeType().equals(nodeType))
			{
				if (!isFirst)
					label += "\n";
				label += cmNode.getLabel();
				
				isFirst = false;
			}
		}
		return label;
	}

	private String getRelatedDirectThreatLabelsAsMultiLine(FactorType nodeType, BaseId annotationId, int annotationType, String fieldTag) throws Exception
	{
		String label ="";
		Factor[] cmNodes = getNodesRelatedToAnnotation(annotationType, annotationId).toNodeArray();
		boolean isFirst = true;
		for (int i = 0; i < cmNodes.length; i++)
		{
			Factor cmNode = cmNodes[i];
			if (cmNode.isDirectThreat() && cmNode.getNodeType().equals(nodeType))
			{
				if (!isFirst)
					label += "\n"; 
				label += cmNode.getLabel();
				
				isFirst = false;
			}
		}
		return label;
	}
	
	private FactorSet getNodesRelatedToAnnotation(int annotationType, BaseId annotationId) throws Exception
	{
		ChainManager chainManager = new ChainManager(project);
		if (annotationType == ObjectType.GOAL)
			return chainManager.findAllNodesRelatedToThisGoal(annotationId);
		if (annotationType == ObjectType.OBJECTIVE)
			return  chainManager.findAllNodesRelatedToThisObjective(annotationId);
		if (annotationType == ObjectType.INDICATOR)
			return chainManager.findAllNodesRelatedToThisIndicator(annotationId);
		
		return new FactorSet();
	}

	public String getAnnotationFactorLabel(int objectType, BaseId objectId)
	{
		try
		{
			ChainManager chainManager = new ChainManager(project);
			FactorSet cmNodeSet = chainManager.findNodesThatUseThisAnnotation(objectType, objectId); 
			Iterator iterator = cmNodeSet.iterator();
			if (!iterator.hasNext())
				return ""; 

			return ((Factor)iterator.next()).getLabel();
		}
		catch( Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		if (isPseudoTag(fieldTag))
			return getPseudoField(objectType, objectId, fieldTag);

		EAMObject object = getPool(objectType).findObject(objectId);
		if(object == null)
			EAM.logDebug("getObjectData no such object: " + objectType + ":" + objectId);
		return object.getData(fieldTag);
	}

	public void loadFromDatabase() throws Exception
	{
		loadPool(ObjectType.MODEL_NODE);
		loadPool(ObjectType.MODEL_LINKAGE);

		loadPool(ObjectType.TASK);
		loadPool(ObjectType.VIEW_DATA);
		loadPool(ObjectType.PROJECT_RESOURCE);
		loadPool(ObjectType.INDICATOR);
		loadPool(ObjectType.OBJECTIVE);
		loadPool(ObjectType.GOAL);
		loadPool(ObjectType.RATING_CRITERION);
		loadPool(ObjectType.VALUE_OPTION);
		loadPool(ObjectType.PROJECT_METADATA);
	}

	private void loadPool(int type) throws IOException, ParseException, Exception
	{
		ObjectManifest manifest = getDatabase().readObjectManifest(type);
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			if(id.isInvalid())
			{
				EAM.logWarning("Ignoring invalid id of type " + type);
				continue;
			}
			EAMObject object = getDatabase().readObject(type, id);
			getPool(type).put(object.getId(), object);
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

	class LinkageMonitor implements FactorLinkListener
	{
		public void linkageWasCreated(FactorId linkFromId, FactorId linkToId)
		{
			Factor from = getNodePool().find(linkFromId); 
			Factor to = getNodePool().find(linkToId);
			if(from.isCause() && to.isTarget())
				((Cause)from).increaseTargetCount();
		}

		public void linkageWasDeleted(FactorId linkFromId, FactorId linkToId)
		{
			Factor from = getNodePool().find(linkFromId);
			Factor to = getNodePool().find(linkToId);
			if(from.isCause() && to.isTarget())
				((Cause)from).decreaseTargetCount();
		}		
	}

	Project project;

	HashMap pools;
}
