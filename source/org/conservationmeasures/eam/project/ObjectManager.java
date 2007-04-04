/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;

import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.AccountingCodePool;
import org.conservationmeasures.eam.objectpools.AssignmentPool;
import org.conservationmeasures.eam.objectpools.DiagramContentsPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorLinkPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorPool;
import org.conservationmeasures.eam.objectpools.EAMNormalObjectPool;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objectpools.FundingSourcePool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.KeyEcologicalAttributePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.ProjectMetadataPool;
import org.conservationmeasures.eam.objectpools.RatingCriterionPool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.ValueOptionPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.budget.BudgetTotalsCalculator;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;

		pools = new HashMap();
		IdAssigner factorAndLinkIdAssigner = project.getNodeIdAssigner();
		pools.put(new Integer(ObjectType.FACTOR), new FactorPool(factorAndLinkIdAssigner));
		pools.put(new Integer(ObjectType.FACTOR_LINK), new FactorLinkPool(factorAndLinkIdAssigner, new FactorLinkMonitor()));

		IdAssigner ida = getAnnotationIdAssigner();
		addNormalPool(new RatingCriterionPool(ida));
		addNormalPool(new ValueOptionPool(ida));
		addNormalPool(new TaskPool(ida));
		addNormalPool(new ViewPool(ida));
		addNormalPool(new ResourcePool(ida));
		addNormalPool(new IndicatorPool(ida));
		addNormalPool(new ObjectivePool(ida));
		addNormalPool(new GoalPool(ida));
		addNormalPool(new ProjectMetadataPool(ida));
		addNormalPool(new DiagramFactorLinkPool(ida));
		addNormalPool(new AssignmentPool(ida));
		addNormalPool(new AccountingCodePool(ida));
		addNormalPool(new FundingSourcePool(ida));
		addNormalPool(new KeyEcologicalAttributePool(ida));
		addNormalPool(new DiagramFactorPool(ida));
		addNormalPool(new DiagramContentsPool(ida));
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
		return (FactorPool)getPool(ObjectType.FACTOR);
	}

	public FactorLinkPool getLinkagePool()
	{
		return (FactorLinkPool)getPool(ObjectType.FACTOR_LINK);
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
	
	public AccountingCodePool getAccountingCodePool()
	{
		return (AccountingCodePool)getPool(ObjectType.ACCOUNTING_CODE);
	}

	
	public FundingSourcePool getFundingSourcePool()
	{
		return (FundingSourcePool)getPool(ObjectType.FUNDING_SOURCE);
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

	public AssignmentPool getAssignmentPool()
	{
		return (AssignmentPool)getPool(ObjectType.ASSIGNMENT);
	}

	public KeyEcologicalAttributePool getKeyEcologicalAttributePool()
	{
		return (KeyEcologicalAttributePool)getPool(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}
	
	public DiagramFactorPool getDiagramFactorPool()
	{
		return (DiagramFactorPool) getPool(ObjectType.DIAGRAM_FACTOR);
	}
	
	public DiagramFactorLinkPool getDiagramFactorLinkPool()
	{
		return (DiagramFactorLinkPool) getPool(ObjectType.DIAGRAM_LINK);
	}
	
	public DiagramContentsPool getDiagramContentsPool()
	{
		return (DiagramContentsPool) getPool(ObjectType.DIAGRAM_CONTENTS);
	}

	public BaseId createObject(int objectType, BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId createdId = BaseId.INVALID;
		switch(objectType)
		{
			case ObjectType.FACTOR:
			{
				CreateFactorParameter parameter = (CreateFactorParameter)extraInfo;
				FactorId nodeId = new FactorId(getProject().obtainRealNodeId(objectId).asInt());
				Factor node = Factor.createConceptualModelObject(this, nodeId, parameter);
				getNodePool().put(node);
				getDatabase().writeObject(node);
				createdId = node.getId();
				break;
			}
			case ObjectType.FACTOR_LINK:
			{
				CreateFactorLinkParameter parameter = (CreateFactorLinkParameter)extraInfo;
				FactorLinkId realId = getProject().obtainRealLinkageId(objectId);
				FactorLink cmLinkage = new FactorLink(this, realId, parameter.getFromId(), parameter.getToId());
				getDatabase().writeObject(cmLinkage);
				EAMObjectPool pool = getPool(objectType);
				pool.put(realId, cmLinkage);
				createdId = cmLinkage.getId();
				break;
			}
			default:
			{
				EAMNormalObjectPool pool = (EAMNormalObjectPool)getPool(objectType);
				BaseObject created = pool.createObject(this, objectId, extraInfo);
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
		BaseObject object = getPool(objectType).findObject(objectId);
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
				return getNewPseudoField(objectType, objectId, fieldTag);
			case ObjectType.OBJECTIVE:
				return getNewPseudoField(objectType, objectId, fieldTag);
			case ObjectType.INDICATOR:
				return getNewPseudoField(objectType, objectId, fieldTag);
			case ObjectType.FACTOR:
				return getNewPseudoField(objectType, objectId, fieldTag);
			case ObjectType.TASK:
				return getTaskPseudoField(objectId, fieldTag);
			case ObjectType.PROJECT_METADATA:
				return getProjectMetadataPseudoField(objectId, fieldTag);
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return getNewPseudoField(objectType, objectId, fieldTag);
		}
		throw new RuntimeException("Unknown PseudoTag: " + fieldTag + " for type " + objectType);	
	}
	
	public BaseObject findObject(ORef ref)
	{
		return findObject(ref.getObjectType(), ref.getObjectId());
	}
	
	public BaseObject findObject(int objectType, BaseId objectId)
	{
		return getPool(objectType).findObject(objectId);
	}
	

	private String getNewPseudoField(int objectType, BaseId id, String fieldTag)
	{
		BaseObject baseObject = findObject(new ORef(objectType, id));
		return baseObject.getData(fieldTag);
	}
	
	
	Factor findNode(FactorId id)
	{
		return (Factor)findObject(new ORef(ObjectType.FACTOR, id));
	}


	private String getTaskPseudoField(BaseId taskId, String fieldTag)
	{
		try
		{
			if(fieldTag.equals(Task.PSEUDO_TAG_STRATEGY_LABEL))
				return getLabelOfTaskParent(taskId);
			if(fieldTag.equals(Task.PSEUDO_TAG_INDICATOR_LABEL))
				return getLabelOfTaskParent(taskId);
			
			DecimalFormat formater = project.getCurrencyFormatter();
			if (fieldTag.equals(Task.PSEUDO_TAG_SUBTASK_TOTAL))
				return getSubtaskTotalCost(taskId, formater);
			if (fieldTag.equals(Task.PSEUDO_TAG_TASK_TOTAL))
				return getTaskTotalCost(taskId, formater);
			if (fieldTag.equals(Task.PSEUDO_TAG_TASK_COST))
				return getTaskCost(taskId, formater);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}
	
	private String getTaskCost(BaseId taskId, DecimalFormat formater) throws Exception
	{
		BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(project);
		//TODO switch to TaskId instead of BaseId to TaskId conversion
		double taskCost = calculator.getTaskCost(new TaskId(taskId.asInt()));
		
		return formater.format(taskCost);
	}

	private String getSubtaskTotalCost(BaseId taskId, DecimalFormat formater) throws Exception
	{
		BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(project);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		double subtaskTotalCost = calculator.getTotalTasksCost(task.getSubtaskIdList());
		
		return formater.format(subtaskTotalCost);
	}

	private String getTaskTotalCost(BaseId taskId, DecimalFormat formater) throws Exception
	{
		BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(project);
		//TODO switch to TaskId instead of BaseId to TaskId conversion
		double totalTaskCost = calculator.getTotalTaskCost(new TaskId(taskId.asInt()));
		
		return formater.format(totalTaskCost);
	}

	private String getProjectMetadataPseudoField(BaseId taskId, String fieldTag)
	{
		try
		{
			if(fieldTag.equals(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME))
				return getProject().getFilename();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}
	
	private String getLabelOfTaskParent(BaseId taskId) throws Exception
	{
		Task task = (Task)findObject(ObjectType.TASK, taskId);
		ORef parentRef = task.getParentRef();
		if(parentRef == null || parentRef.getObjectType() == ObjectType.FAKE)
		{
			EAM.logDebug("Task without parent: " + taskId);
			return "(none)";
		}
		BaseObject parent = findObject(parentRef);
		if(parent == null)
		{
			EAM.logDebug("Parent of task " + taskId + " not found: " + parentRef);
			return "(none)";
		}
		return parent.getData(BaseObject.TAG_LABEL);
	}

	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		if (isPseudoTag(fieldTag))
			return getPseudoField(objectType, objectId, fieldTag);

		BaseObject object = getPool(objectType).findObject(objectId);
		if(object == null)
			EAM.logDebug("getObjectData no such object: " + objectType + ":" + objectId + " fieldTag=" + fieldTag);
		return object.getData(fieldTag);
	}

	public void loadFromDatabase() throws Exception
	{
		loadPool(ObjectType.FACTOR);
		loadPool(ObjectType.FACTOR_LINK);
		loadPool(ObjectType.TASK);
		loadPool(ObjectType.VIEW_DATA);
		loadPool(ObjectType.PROJECT_RESOURCE);
		loadPool(ObjectType.INDICATOR);
		loadPool(ObjectType.OBJECTIVE);
		loadPool(ObjectType.GOAL);
		loadPool(ObjectType.RATING_CRITERION);
		loadPool(ObjectType.VALUE_OPTION);
		loadPool(ObjectType.PROJECT_METADATA);
		loadPool(ObjectType.DIAGRAM_LINK);
		loadPool(ObjectType.ASSIGNMENT);
		loadPool(ObjectType.ACCOUNTING_CODE);
		loadPool(ObjectType.FUNDING_SOURCE);
		loadPool(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		loadPool(ObjectType.DIAGRAM_FACTOR);
		loadPool(ObjectType.DIAGRAM_CONTENTS);
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
			BaseObject object = getDatabase().readObject(this, type, id);
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

	//FIXME: there shold be a better way to get to the Model then having to expose it here
	public DiagramModel getDiagramModel()
	{
		return getProject().getDiagramModel();
	}
	
	//FIXME: there shold be a better way to get to the chain manager then having to expose it here
	public ChainManager getChainManager()
	{
		return new ChainManager(getProject());
	}
	
	class FactorLinkMonitor implements FactorLinkListener
	{
		public void factorLinkWasCreated(FactorId linkFromId, FactorId linkToId)
		{
			Factor from = getNodePool().find(linkFromId); 
			Factor to = getNodePool().find(linkToId);
			if(from.isCause() && to.isTarget())
				((Cause)from).increaseTargetCount();
		}

		public void factorLinkWasDeleted(FactorId linkFromId, FactorId linkToId)
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
