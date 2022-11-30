/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import org.miradi.diagram.ChainWalker;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.*;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.*;

import java.util.HashMap;
import java.util.Vector;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;
		diagramChainWalker = new ChainWalker();
		referrerCache = new HashMap<ORef, ORefSet>();

		pools = new HashMap<Integer, BaseObjectInformation>();
		IdAssigner ida = getNormalIdAssigner();
		addNormalPool(new FactorLinkPool(ida), FactorLinkSchema.OBJECT_NAME);
		addNormalPool(new RatingCriterionPool(ida), RatingCriterionSchema.OBJECT_NAME);
		addNormalPool(new ValueOptionPool(ida), ValueOptionSchema.OBJECT_NAME);
		addNormalPool(new TaskPool(ida), TaskSchema.OBJECT_NAME);
		addNormalPool(new ViewPool(ida), ViewDataSchema.OBJECT_NAME);
		addNormalPool(new ResourcePool(ida), ProjectResourceSchema.OBJECT_NAME);
		addNormalPool(new MethodPool(ida), MethodSchema.OBJECT_NAME);
		addNormalPool(new IndicatorPool(ida), IndicatorSchema.OBJECT_NAME);
		addNormalPool(new ObjectivePool(ida), ObjectiveSchema.OBJECT_NAME);
		addNormalPool(new GoalPool(ida), GoalSchema.OBJECT_NAME);
		addNormalPool(new ProjectMetadataPool(ida), ProjectMetadataSchema.OBJECT_NAME);
		addNormalPool(new DiagramLinkPool(ida), DiagramLinkSchema.OBJECT_NAME);
		addNormalPool(new TimeframePool(ida), TimeframeSchema.OBJECT_NAME);
		addNormalPool(new ResourceAssignmentPool(ida), ResourceAssignmentSchema.OBJECT_NAME);
		addNormalPool(new AccountingCodePool(ida), AccountingCodeSchema.OBJECT_NAME);
		addNormalPool(new FundingSourcePool(ida), FundingSourceSchema.OBJECT_NAME);
		addNormalPool(new KeyEcologicalAttributePool(ida), KeyEcologicalAttributeSchema.OBJECT_NAME);
		addNormalPool(new DiagramFactorPool(ida), DiagramFactorSchema.OBJECT_NAME);
		addNormalPool(new CausePool(ida), CauseSchema.OBJECT_NAME);
		addNormalPool(new StrategyPool(ida), StrategySchema.OBJECT_NAME);
		addNormalPool(new TargetPool(ida), TargetSchema.OBJECT_NAME);
		addNormalPool(new BiophysicalFactorPool(ida), BiophysicalFactorSchema.OBJECT_NAME);
		addNormalPool(new BiophysicalResultPool(ida), BiophysicalResultSchema.OBJECT_NAME);
		addNormalPool(new IntermediateResultPool(ida), IntermediateResultSchema.OBJECT_NAME);
		addNormalPool(new ResultsChainDiagramPool(ida), ResultsChainDiagramSchema.OBJECT_NAME);
		addNormalPool(new ConceptualModelDiagramPool(ida), ConceptualModelDiagramSchema.OBJECT_NAME);
		addNormalPool(new ThreatReductionResultPool(ida), ThreatReductionResultSchema.OBJECT_NAME);
		addNormalPool(new TextBoxPool(ida), TextBoxSchema.OBJECT_NAME);
		addNormalPool(new ObjectTreeTableConfigurationPool(ida), ObjectTreeTableConfigurationSchema.OBJECT_NAME);
		addNormalPool(new WwfProjectDataPool(ida), WwfProjectDataSchema.OBJECT_NAME);
		addNormalPool(new CostAllocationRulePool(ida), CostAllocationRuleSchema.OBJECT_NAME);
		addNormalPool(new MeasurementPool(ida), MeasurementSchema.OBJECT_NAME);
		addNormalPool(new StressPool(ida), StressSchema.OBJECT_NAME);
		addNormalPool(new ThreatStressRatingPool(ida), ThreatStressRatingSchema.OBJECT_NAME);
		addNormalPool(new GroupBoxPool(ida), GroupBoxSchema.OBJECT_NAME);
		addNormalPool(new SubTargetPool(ida), SubTargetSchema.OBJECT_NAME);
		addNormalPool(new ProgressReportPool(ida), ProgressReportSchema.OBJECT_NAME);
		addNormalPool(new ExtendedProgressReportPool(ida), ExtendedProgressReportSchema.OBJECT_NAME);
		addNormalPool(new ResultReportPool(ida), ResultReportSchema.OBJECT_NAME);
		addNormalPool(new RareProjectDataPool(ida), RareProjectDataSchema.OBJECT_NAME);
		addNormalPool(new WcsProjectDataPool(ida), WcsProjectDataSchema.OBJECT_NAME);
		addNormalPool(new TncProjectDataPool(ida), TncProjectDataSchema.OBJECT_NAME);
		addNormalPool(new FosProjectDataPool(ida), FosProjectDataSchema.OBJECT_NAME);
		addNormalPool(new OrganizationPool(ida), OrganizationSchema.OBJECT_NAME);
		addNormalPool(new WcpaProjectDataPool(ida), WcpaProjectDataSchema.OBJECT_NAME);
		addNormalPool(new XenodataPool(ida), XenodataSchema.OBJECT_NAME);
		addNormalPool(new ProgressPercentPool(ida), ProgressPercentSchema.OBJECT_NAME);
		addNormalPool(new ReportTemplatePool(ida), ReportTemplateSchema.OBJECT_NAME);
		addNormalPool(new TaggedObjectSetPool(ida), TaggedObjectSetSchema.OBJECT_NAME);
		addNormalPool(new TableSettingsPool(ida), TableSettingsSchema.OBJECT_NAME);
		addNormalPool(new ThreatStressRatingDataPool(ida), ThreatStressRatingDataSchema.OBJECT_NAME);
		addNormalPool(new ThreatSimpleRatingDataPool(ida), ThreatSimpleRatingDataSchema.OBJECT_NAME);
		addNormalPool(new ScopeBoxPool(ida), ScopeBoxSchema.OBJECT_NAME);
		addNormalPool(new ExpenseAssignmentPool(ida), ExpenseAssignmentSchema.OBJECT_NAME);
		addNormalPool(new HumanWelfareTargetPool(ida), HumanWelfareTargetSchema.OBJECT_NAME);
		addNormalPool(new IucnRedlistSpeciesPool(ida), IucnRedlistSpeciesSchema.OBJECT_NAME);
		addNormalPool(new OtherNotableSpeciesPool(ida), OtherNotableSpeciesSchema.OBJECT_NAME);
		addNormalPool(new AudiencePool(ida), AudienceSchema.OBJECT_NAME);
		addNormalPool(new BudgetCategoryOnePool(ida), BudgetCategoryOneSchema.OBJECT_NAME);
		addNormalPool(new BudgetCategoryTwoPool(ida), BudgetCategoryTwoSchema.OBJECT_NAME);
		addNormalPool(new DashboardPool(ida), DashboardSchema.OBJECT_NAME);
		addNormalPool(new XslTemplatePool(ida), XslTemplateSchema.OBJECT_NAME);
		addNormalPool(new MiradiShareProjectDataPool(ida), MiradiShareProjectDataSchema.OBJECT_NAME);
		addNormalPool(new MiradiShareTaxonomyPool(ida), MiradiShareTaxonomySchema.OBJECT_NAME);
		addNormalPool(new TaxonomyAssociationPool(ida), TaxonomyAssociationSchema.OBJECT_NAME);
		addNormalPool(new FutureStatusPool(ida), FutureStatusSchema.OBJECT_NAME);
		addNormalPool(new AccountingClassificationAssociationPool(ida), AccountingClassificationAssociationSchema.OBJECT_NAME);
		addNormalPool(new ResultReportPool(ida), ResultReportSchema.OBJECT_NAME);
		addNormalPool(new OutputPool(ida), OutputSchema.OBJECT_NAME);
		addNormalPool(new AnalyticalQuestionPool(ida), AnalyticalQuestionSchema.OBJECT_NAME);
		addNormalPool(new SubAssumptionPool(ida), SubAssumptionSchema.OBJECT_NAME);
	}

	public Schemas getSchemas()
	{
		if (schemas == null)
			schemas = new Schemas(project);

		return schemas;
	}

	public ChainWalker getDiagramChainWalker()
	{
		return diagramChainWalker;
	}

	private void addNormalPool(BaseObjectPool pool, String objectName)
	{
		BaseObjectInformation baseObjectInformation = new BaseObjectInformation(pool, objectName);
		pools.put(new Integer(pool.getObjectType()), baseObjectInformation);
	}

	private IdAssigner getNormalIdAssigner()
	{
		return getProject().getNormalIdAssigner();
	}

	public BaseObjectPool getPool(int objectType)
	{
		if (pools.containsKey(objectType))
		{
			BaseObjectInformation objectBundle = pools.get(new Integer(objectType));
			return objectBundle.getPool();
		}
		
		return null;
	}
	
	public String getInternalObjectTypeName(int objectType)
	{
		if (pools.containsKey(objectType))
			return pools.get(objectType).getObjectName();
		
		return null;
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

	public FutureStatusPool getFutureStatusPool()
	{
		return (FutureStatusPool) getPool(ObjectType.FUTURE_STATUS);
	}
	
	public FundingSourcePool getFundingSourcePool()
	{
		return (FundingSourcePool)getPool(ObjectType.FUNDING_SOURCE);
	}

	public SubTargetPool getSubTargetPool()
	{
		return (SubTargetPool) getPool(SubTargetSchema.getObjectType());
	}

	public IndicatorPool getIndicatorPool()
	{
		return (IndicatorPool)getPool(ObjectType.INDICATOR);
	}

	public MethodPool getMethodPool()
	{
		return (MethodPool)getPool(ObjectType.METHOD);
	}

	public ObjectivePool getObjectivePool()
	{
		return (ObjectivePool)getPool(ObjectType.OBJECTIVE);
	}

	public GoalPool getGoalPool()
	{
		return (GoalPool)getPool(ObjectType.GOAL);
	}

	public ResourceAssignmentPool getAssignmentPool()
	{
		return (ResourceAssignmentPool)getPool(ObjectType.RESOURCE_ASSIGNMENT);
	}
	
	public TimeframePool getTimeframePool()
	{
		return (TimeframePool)getPool(ObjectType.TIMEFRAME);
	}

	public ExpenseAssignmentPool getExpenseAssignmentPool()
	{
		return (ExpenseAssignmentPool)getPool(ExpenseAssignmentSchema.getObjectType());
	}

	public KeyEcologicalAttributePool getKeyEcologicalAttributePool()
	{
		return (KeyEcologicalAttributePool)getPool(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}
	
	public DiagramFactorPool getDiagramFactorPool()
	{
		return (DiagramFactorPool) getPool(ObjectType.DIAGRAM_FACTOR);
	}
	
	public DiagramLinkPool getDiagramFactorLinkPool()
	{
		return (DiagramLinkPool) getPool(ObjectType.DIAGRAM_LINK);
	}
	
	public ConceptualModelDiagramPool getConceptualModelDiagramPool()
	{
		return (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
	
	public ResultsChainDiagramPool getResultsChainDiagramPool()
	{
		return (ResultsChainDiagramPool) getPool(ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public ObjectTreeTableConfigurationPool getPlanningConfigurationPool()
	{
		return (ObjectTreeTableConfigurationPool) getPool(ObjectTreeTableConfigurationSchema.getObjectType());
	}

	public OutputPool getOutputPool()
	{
		return (OutputPool)getPool(ObjectType.OUTPUT);
	}

	public AnalyticalQuestionPool getAnalyticalQuestionPool()
	{
		return (AnalyticalQuestionPool)getPool(ObjectType.ANALYTICAL_QUESTION);
	}

	public SubAssumptionPool getSubAssumptionPool()
	{
		return (SubAssumptionPool)getPool(ObjectType.SUB_ASSUMPTION);
	}

	public BaseId createObject(int objectType, BaseId objectId) throws Exception
	{
		BaseId createdId = BaseId.INVALID;
		BaseObjectPool pool = getPool(objectType);
		if(pool == null)
			throw new RuntimeException("No pool for " + objectType);
		BaseObject created = pool.createObject(this, objectId);
		createdId = created.getId();
		return createdId;
	}

	public void deleteObject(ORef objectRef)
	{
		removeFromReferrerCache(objectRef);
		int objectType = objectRef.getObjectType();
		BaseId objectId = objectRef.getObjectId();
		EAMObjectPool pool = getPool(objectType);
		if(pool.findObject(objectId) == null)
			throw new RuntimeException("Attempted to delete missing object: " + objectType + ":" + objectId);
		pool.remove(objectId);
	}
	
	public void setObjectData(ORef objectRef, String fieldTag, String dataValue) throws Exception
	{
		BaseObject object = findObject(objectRef);
		object.setData(fieldTag, dataValue);
	}
	
	public BaseObject findObject(ORef ref)
	{
		return findObject(ref.getObjectType(), ref.getObjectId());
	}
	
	public BaseObject findObject(int objectType, BaseId objectId)
	{		
		EAMObjectPool pool = getPool(objectType);
		if(pool == null)
			throw new RuntimeException("Attempted to find object of unknown type: " + objectType);
		return pool.findObject(objectId);
	}
	
	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		return getObjectData(new ORef(objectType, objectId), fieldTag);
	}
	
	public String getObjectData(ORef ref, String fieldTag)
	{
		BaseObject object = findObject(ref);
		if(object == null)
			EAM.logDebug("getObjectData no such object: " + ref + " fieldTag=" + fieldTag);
		return object.getData(fieldTag);
	}

	public static int[] getAllObjectTypes()
	{
		//NOTE: Pools must be loaded in a specific sequence
		int[] types = {
			ObjectType.CAUSE,
			ObjectType.STRATEGY,
			ObjectType.TARGET,
            ObjectType.BIOPHYSICAL_FACTOR,
            ObjectType.BIOPHYSICAL_RESULT,
			ObjectType.FACTOR_LINK,
			ObjectType.TASK,
			ObjectType.VIEW_DATA,
			ObjectType.PROJECT_RESOURCE,
			ObjectType.INDICATOR,
			ObjectType.METHOD,
			ObjectType.OBJECTIVE,
			ObjectType.GOAL,
			ObjectType.RATING_CRITERION,
			ObjectType.VALUE_OPTION,
			ObjectType.PROJECT_METADATA,
			ObjectType.DIAGRAM_LINK,
			ObjectType.RESOURCE_ASSIGNMENT,
			ObjectType.TIMEFRAME,
			ObjectType.ACCOUNTING_CODE,
			ObjectType.FUNDING_SOURCE,
			ObjectType.KEY_ECOLOGICAL_ATTRIBUTE,
			ObjectType.DIAGRAM_FACTOR,
			ObjectType.CONCEPTUAL_MODEL_DIAGRAM,
			ObjectType.RESULTS_CHAIN_DIAGRAM,
			ObjectType.INTERMEDIATE_RESULT,
			ObjectType.THREAT_REDUCTION_RESULT,
			ObjectType.TEXT_BOX,
			ObjectType.OBJECT_TREE_TABLE_CONFIGURATION,
			ObjectType.WWF_PROJECT_DATA,
			ObjectType.COST_ALLOCATION_RULE,
			ObjectType.MEASUREMENT,
			ObjectType.STRESS,
			ObjectType.THREAT_STRESS_RATING,
			ObjectType.GROUP_BOX,
			ObjectType.SUB_TARGET,
			ObjectType.PROGRESS_REPORT,
			ObjectType.RESULT_REPORT,
			ObjectType.RARE_PROJECT_DATA,
			ObjectType.WCS_PROJECT_DATA,
			ObjectType.TNC_PROJECT_DATA,
			ObjectType.FOS_PROJECT_DATA,
			ObjectType.ORGANIZATION,
			ObjectType.WCPA_PROJECT_DATA,
			ObjectType.XENODATA,
			ObjectType.PROGRESS_PERCENT,
			ObjectType.REPORT_TEMPLATE,
			ObjectType.TAGGED_OBJECT_SET,
			ObjectType.TABLE_SETTINGS,
			ObjectType.THREAT_STRESS_RATING_DATA,
			ObjectType.THREAT_SIMPLE_RATING_DATA,
			ObjectType.SCOPE_BOX,
			ObjectType.EXPENSE_ASSIGNMENT,
			ObjectType.HUMAN_WELFARE_TARGET,
			ObjectType.IUCN_REDLIST_SPECIES,
			ObjectType.OTHER_NOTABLE_SPECIES,
			ObjectType.AUDIENCE,
			ObjectType.BUDGET_CATEGORY_ONE,
			ObjectType.BUDGET_CATEGORY_TWO,
			ObjectType.DASHBOARD,
			ObjectType.OUTPUT,
			ObjectType.ANALYTICAL_QUESTION,
			ObjectType.SUB_ASSUMPTION,
		};
		return types;
	}

	public Project getProject()
	{
		return project;
	}

	public Vector<BaseObject> findObjectsAsVector(ORefList refList)
	{
		Vector<BaseObject> foundObjects = new Vector<BaseObject>();
		for (int i = 0; i < refList.size(); ++i)
		{
			foundObjects.add(findObject(refList.get(i)));
		}
		
		return foundObjects;
	}
	
	public ORefList getAllDiagramObjectRefs()
	{
		ORefList conceptualModels = getConceptualModelDiagramPool().getORefList();
		ORefList resultsChains = getResultsChainDiagramPool().getORefList();
		
		ORefList combinedORefList = new ORefList();
		combinedORefList.addAll(conceptualModels);
		combinedORefList.addAll(resultsChains);
		
		return combinedORefList;

	}

	public void updateReferrerCache(ORef referrerRef, ORefSet oldReferrals, ORefSet newReferrals)
	{
		ORefSet removedReferrals = ORefSet.subtract(oldReferrals, newReferrals);
		for(ORef referredRef : removedReferrals)
		{
			getReferrerRefsSet(referredRef).remove(referrerRef);
		}

		ORefSet addedReferrals = ORefSet.subtract(newReferrals, oldReferrals);
		for(ORef referredRef: addedReferrals)
		{
			if(referredRef.isValid())
				getReferrerRefsSet(referredRef).add(referrerRef);
		}
	}

	public ORefSet getReferringObjects(ORef ref)
	{
		return getReferrerRefsSet(ref);
	}

	private ORefSet getReferrerRefsSet(ORef referredRef)
	{
		ORefSet referringRefs = referrerCache.get(referredRef);
		if(referringRefs == null)
		{
			referringRefs = new ORefSet();
			referrerCache.put(referredRef, referringRefs);
		}
		return referringRefs;
	}
	
	private void removeFromReferrerCache(ORef refToRemove)
	{
		BaseObject object = findObject(refToRemove);
		ORefSet referencedObjectRefs = object.getAllReferencedObjects();
		for(ORef referencedRef : referencedObjectRefs)
		{
			getReferrerRefsSet(referencedRef).remove(refToRemove);
		}
		referrerCache.remove(refToRemove);
	}

	private class BaseObjectInformation 
	{
		private BaseObjectInformation(BaseObjectPool poolToUse, String objectNameToUse)
		{
			pool = poolToUse;
			objectName = objectNameToUse;
		}
		
		private BaseObjectPool getPool()
		{
			return pool;
		}
		
		private String getObjectName()
		{
			return objectName;
		}
		
		private BaseObjectPool pool;
		private String objectName;
	}
	
	private Project project;
	private ChainWalker diagramChainWalker;
	private HashMap<Integer, BaseObjectInformation> pools;
	private HashMap<ORef, ORefSet> referrerCache;
	private Schemas schemas;
}
