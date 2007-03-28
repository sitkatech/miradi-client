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
import java.util.Iterator;

import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.NonDraftStrategySet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
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
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.utils.CodeList;
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
				Factor node = Factor.createConceptualModelObject(nodeId, parameter);
				getNodePool().put(node);
				getDatabase().writeObject(node);
				createdId = node.getId();
				break;
			}
			case ObjectType.FACTOR_LINK:
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
				BaseObject created = pool.createObject(objectId, extraInfo);
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
				return getGoalPseudoField(objectType, objectId, fieldTag);
			case ObjectType.OBJECTIVE:
				return getObjectivePseudoField(objectType, objectId, fieldTag);
			case ObjectType.INDICATOR:
				return getIndicatorPseudoField(objectType, objectId, fieldTag);
			case ObjectType.FACTOR:
				return getFactorPseudoField(objectId, fieldTag);
			case ObjectType.TASK:
				return getTaskPseudoField(objectId, fieldTag);
			case ObjectType.PROJECT_METADATA:
				return getProjectMetadataPseudoField(objectId, fieldTag);
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return getKeyEcologicalAttributePseudoField(objectId, fieldTag);
		}
		throw new RuntimeException("Unknown PseudoTag: " + fieldTag + " for type " + objectType);	
	}
	
	BaseObject findObject(ORef ref)
	{
		return findObject(ref.getObjectType(), ref.getObjectId());
	}
	
	BaseObject findObject(int objectType, BaseId objectId)
	{
		return getPool(objectType).findObject(objectId);
	}
	
	private String getKeyEcologicalAttributePseudoField(BaseId objectId, String fieldTag)
	{
		KeyEcologicalAttribute kea = (KeyEcologicalAttribute)findObject(new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, objectId));
		try
		{
			if (fieldTag.equals(KeyEcologicalAttribute.PSUEDO_TAG_VIABILITY_STATUS))
				return computeTNCViability(kea);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		return "";
	}

	private String getIndicatorPseudoField(int annotationType, BaseId annotationId, String fieldTag)
	{
		try
		{
			if (fieldTag.equals(Indicator.PSEUDO_TAG_FACTOR))
				return getAnnotationFactorLabel(annotationType, annotationId);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_TARGETS))
				return getRelatedTargetLabelsAsMultiLine(annotationType, annotationId);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_STRATEGIES))
				return getRelatedStrategyLabelsAsMultiline(annotationType, annotationId);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(annotationType, annotationId);
			if (fieldTag.equals(Indicator.PSEUDO_TAG_METHODS))
				return getIndicatorMethodsSingleLine(annotationId);
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
				return getRelatedTargetLabelsAsMultiLine(objectType, objectId);
			if (fieldTag.equals(Desire.PSEUDO_TAG_STRATEGIES))
				return getRelatedStrategyLabelsAsMultiline(objectType, objectId);
			if (fieldTag.equals(Desire.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(objectType, objectId);
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
				return getRelatedStrategyLabelsAsMultiline(objectType, objectId);
			if (fieldTag.equals(Desire.PSEUDO_TAG_DIRECT_THREATS))
				return getRelatedDirectThreatLabelsAsMultiLine(objectType, objectId);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
	}
	
	private String getFactorPseudoField(BaseId factorId, String fieldTag)
	{
		try
		{
			if(fieldTag.equals(Factor.PSEUDO_TAG_GOALS))
				return getFactorGoals((FactorId)factorId);
			if(fieldTag.equals(Factor.PSEUDO_TAG_OBJECTIVES))
				return getFactorObjectives((FactorId)factorId);
			if(fieldTag.equals(Factor.PSEUDO_TAG_DIRECT_THREATS))
				return getFactorRelatedDirectThreats((FactorId)factorId);
			if(fieldTag.equals(Factor.PSEUDO_TAG_TARGETS))
				return getFactorRelatedTargets((FactorId)factorId);
			// TODO: Enforce isStrategy for this
			if(fieldTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
				return getStrategyRatingSummary((FactorId)factorId);
			// TODO: Enforce isTarget for this
			if(fieldTag.equals(Target.PSEUDO_TAG_TARGET_VIABILITY))
				return getTargetViability((FactorId)factorId);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
		return "";
		
	}
	
	Factor findNode(FactorId id)
	{
		return (Factor)findObject(new ORef(ObjectType.FACTOR, id));
	}
	
	private String getStrategyRatingSummary(FactorId factorId)
	{
		ChoiceItem rating = ((Strategy)findNode(factorId)).getStrategyRating();
		return rating.getCode();

	}
	
	private String getTargetViability(FactorId factorId)
	{
		Target target = (Target)findNode(factorId);
		if(target.isViabilityModeTNC())
			return computeTNCViability(target);
		return target.getBasicTargetStatus();
	}
	
	public String computeTNCViability(Target target)
	{
		HashMap categoryKeaRatings = new HashMap();
		
		IdList keas = target.getKeyEcologicalAttributes();
		for(int i = 0; i < keas.size(); ++i)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keas.get(i));
			String category = kea.getData(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
			if(category.equals(StatusQuestion.UNSPECIFIED))
				continue;
			
			CodeList codesForCategory = (CodeList)categoryKeaRatings.get(category);
			if(codesForCategory == null)
			{
				codesForCategory = new CodeList();
				categoryKeaRatings.put(category, codesForCategory);
			}

			String keaViability = computeTNCViability(kea);
			codesForCategory.add(keaViability);
		}
		
		CodeList categorySummaryRatings = new CodeList();
		Iterator iter = categoryKeaRatings.keySet().iterator();
		while(iter.hasNext())
		{
			String category = (String)iter.next();
			CodeList keaCodes = (CodeList)categoryKeaRatings.get(category);
			String categoryRating = TNCViabilityFormula.getTotalCategoryRatingCode(keaCodes);
			categorySummaryRatings.add(categoryRating);
		}
		
		return TNCViabilityFormula.getAverageRatingCode(categorySummaryRatings);
	}
	
	public String computeTNCViability(KeyEcologicalAttribute kea)
	{
		CodeList statuses = new CodeList();
		IdList indicatorIds = kea.getIndicatorIds();
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			String status = getObjectData(ObjectType.INDICATOR, indicatorIds.get(i), Indicator.TAG_MEASUREMENT_STATUS);
			statuses.add(status);
		}
		String result = TNCViabilityFormula.getAverageRatingCode(statuses);
		return result;
	}

	private String getFactorGoals(FactorId factorId) throws ParseException
	{
		return getFactorDesires(factorId, ObjectType.GOAL, Factor.TAG_GOAL_IDS);
	}

	private String getFactorObjectives(FactorId factorId) throws ParseException
	{
		return getFactorDesires(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
	}
	
	private String getFactorDesires(FactorId factorId, int desireType, String desireIdsTag) throws ParseException
	{
		ChainObject chain = new ChainObject();
		chain.buildDownstreamChain(project.getDiagramModel(), findNode(factorId));
		
		IdList allDesireIds = new IdList();
		Factor[] factors = chain.getFactorsArray();
		for(int i = 0; i < factors.length; ++i)
		{
			Factor factor = factors[i];
			IdList theseDesireIds = new IdList(factor.getData(desireIdsTag));
			addMissingIds(allDesireIds, theseDesireIds);
		}
		
		return getDesiresAsMultiline(desireType, allDesireIds);
	}
	
	private void addMissingIds(IdList destination, IdList source)
	{
		for(int i = 0; i < source.size(); ++i)
			if(!destination.contains(source.get(i)))
				destination.add(source.get(i));
	}
	
	private String getDesiresAsMultiline(int desireType, IdList desireIds)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < desireIds.size(); ++i)
		{
			if(result.length() > 0)
				result.append("\n");
			
			result.append(getObjectData(desireType, desireIds.get(i), Desire.TAG_LABEL));
		}
		
		return result.toString();
	}

	private String getFactorRelatedDirectThreats(FactorId factorId)
	{
		ChainObject chain = new ChainObject();
		chain.buildNormalChain(project.getDiagramModel(), findNode(factorId));
		DirectThreatSet directThreats = new DirectThreatSet(chain.getFactors());
		
		return getLabelsAsMultiline(directThreats);
	}

	private String getFactorRelatedTargets(FactorId factorId)
	{
		ChainObject chain = new ChainObject();
		chain.buildNormalChain(project.getDiagramModel(), findNode(factorId));
		TargetSet directThreats = new TargetSet(chain.getFactors());
		
		return getLabelsAsMultiline(directThreats);
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
	
	
	private String getLabelsAsMultiline(FactorSet factors)
	{
		StringBuffer result = new StringBuffer();
		Iterator iter = factors.iterator();
		while(iter.hasNext())
		{
			if(result.length() > 0)
				result.append("\n");
			
			Factor factor = (Factor)iter.next();
			result.append(factor.getLabel());
		}
		
		return result.toString();
	}
	
	
	private String getRelatedStrategyLabelsAsMultiline(int annotationType, BaseId annotationId) throws Exception
	{
		return getRelatedLabelsAsMultiLine(new NonDraftStrategySet(), annotationType, annotationId);
	}
	
	
	private String getRelatedTargetLabelsAsMultiLine(int annotationType, BaseId annotationId) throws Exception
	{
		return getRelatedLabelsAsMultiLine(new TargetSet(), annotationType, annotationId);
	}
	
	
	private String getRelatedDirectThreatLabelsAsMultiLine(int annotationType, BaseId annotationId) throws Exception
	{
		return getRelatedLabelsAsMultiLine(new DirectThreatSet(), annotationType, annotationId);
	}

	
	private String getRelatedLabelsAsMultiLine(FactorSet set, int annotationType, BaseId annotationId) throws Exception
	{
		Factor[] cmNodes = getFactorsRelatedToAnnotation(annotationType, annotationId).toNodeArray();
		set.attemptToAddAll(cmNodes);
		return getLabelsAsMultiline(set);
	}
	
	private FactorSet getFactorsRelatedToAnnotation(int annotationType, BaseId annotationId) throws Exception
	{
		ChainManager chainManager = new ChainManager(project);
		if (annotationType == ObjectType.GOAL)
			return chainManager.findAllFactorsRelatedToThisGoal(annotationId);
		if (annotationType == ObjectType.OBJECTIVE)
			return  chainManager.findAllFactorsRelatedToThisObjective(annotationId);
		if (annotationType == ObjectType.INDICATOR)
			return chainManager.findAllFactorsRelatedToThisIndicator(annotationId);
		
		return new FactorSet();
	}

	private String getAnnotationFactorLabel(int objectType, BaseId objectId)
	{
		try
		{
			ChainManager chainManager = new ChainManager(project);
			Factor owner = chainManager.getDirectOrIndirectOwningFactor(new ORef(objectType, objectId)); 
			if (owner == null)
				return ""; 

			return owner.getLabel();
		}
		catch( Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	private String getIndicatorMethodsSingleLine(BaseId indicatorId) throws ParseException
	{
		String methodIdsString = getObjectData(ObjectType.INDICATOR, indicatorId, Indicator.TAG_TASK_IDS);
		
		StringBuffer result = new StringBuffer();
		IdList methodIds = new IdList(methodIdsString);
		for(int i = 0; i < methodIds.size(); ++i)
		{
			if(i > 0)
				result.append("; ");
			
			BaseId methodId = methodIds.get(i);
			BaseObject method = findObject(ObjectType.TASK, methodId);
			result.append(method.getData(Task.TAG_LABEL));
		}
		
		return result.toString();
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
			BaseObject object = getDatabase().readObject(type, id);
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
