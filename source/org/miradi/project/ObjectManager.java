/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.miradi.database.ObjectManifest;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.ChainWalker;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.AccountingCodePool;
import org.miradi.objectpools.AssignmentPool;
import org.miradi.objectpools.AudiencePool;
import org.miradi.objectpools.BudgetCategoryOnePool;
import org.miradi.objectpools.BudgetCategoryTwoPool;
import org.miradi.objectpools.CausePool;
import org.miradi.objectpools.ConceptualModelDiagramPool;
import org.miradi.objectpools.CostAllocationRulePool;
import org.miradi.objectpools.DashboardPool;
import org.miradi.objectpools.DiagramLinkPool;
import org.miradi.objectpools.DiagramFactorPool;
import org.miradi.objectpools.EAMNormalObjectPool;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objectpools.ExpensePool;
import org.miradi.objectpools.FactorLinkPool;
import org.miradi.objectpools.FosProjectDataPool;
import org.miradi.objectpools.FundingSourcePool;
import org.miradi.objectpools.GoalPool;
import org.miradi.objectpools.GroupBoxPool;
import org.miradi.objectpools.HumanWelfareTargetPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objectpools.IntermediateResultPool;
import org.miradi.objectpools.IucnRedlistSpeciesPool;
import org.miradi.objectpools.KeyEcologicalAttributePool;
import org.miradi.objectpools.MeasurementPool;
import org.miradi.objectpools.ObjectTreeTableConfigurationPool;
import org.miradi.objectpools.ObjectivePool;
import org.miradi.objectpools.OrganizationPool;
import org.miradi.objectpools.OtherNotableSpeciesPool;
import org.miradi.objectpools.PoolWithIdAssigner;
import org.miradi.objectpools.ProgressPercentPool;
import org.miradi.objectpools.ProgressReportPool;
import org.miradi.objectpools.ProjectMetadataPool;
import org.miradi.objectpools.RareProjectDataPool;
import org.miradi.objectpools.RatingCriterionPool;
import org.miradi.objectpools.ReportTemplatePool;
import org.miradi.objectpools.ResourcePool;
import org.miradi.objectpools.ResultsChainDiagramPool;
import org.miradi.objectpools.ScopeBoxPool;
import org.miradi.objectpools.StrategyPool;
import org.miradi.objectpools.StressPool;
import org.miradi.objectpools.SubTargetPool;
import org.miradi.objectpools.TableSettingsPool;
import org.miradi.objectpools.TaggedObjectSetPool;
import org.miradi.objectpools.TargetPool;
import org.miradi.objectpools.TaskPool;
import org.miradi.objectpools.TextBoxPool;
import org.miradi.objectpools.ThreatRatingCommentsDataPool;
import org.miradi.objectpools.ThreatReductionResultPool;
import org.miradi.objectpools.ThreatStressRatingPool;
import org.miradi.objectpools.TncProjectDataPool;
import org.miradi.objectpools.ValueOptionPool;
import org.miradi.objectpools.ViewPool;
import org.miradi.objectpools.WcpaProjectDataPool;
import org.miradi.objectpools.WcsProjectDataPool;
import org.miradi.objectpools.WwfProjectDataPool;
import org.miradi.objectpools.XenodataPool;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Audience;
import org.miradi.objects.BaseObject;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.CostAllocationRule;
import org.miradi.objects.Dashboard;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FactorLink;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.IucnRedlistSpecies;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.Objective;
import org.miradi.objects.Organization;
import org.miradi.objects.OtherNotableSpecies;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ReportTemplate;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.TableSettings;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.ValueOption;
import org.miradi.objects.ViewData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.utils.EnhancedJsonObject;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;
		diagramChainWalker = new ChainWalker();
		referrerCache = new HashMap<ORef, ORefSet>();

		pools = new HashMap<Integer, BaseObjectInformation>();
		IdAssigner ida = getNormalIdAssigner();
		addNormalPool(new FactorLinkPool(ida), FactorLink.OBJECT_NAME);
		addNormalPool(new RatingCriterionPool(ida), RatingCriterion.OBJECT_NAME);
		addNormalPool(new ValueOptionPool(ida), ValueOption.OBJECT_NAME);
		addNormalPool(new TaskPool(ida), Task.OBJECT_NAME);
		addNormalPool(new ViewPool(ida), ViewData.OBJECT_NAME);
		addNormalPool(new ResourcePool(ida), ProjectResource.OBJECT_NAME);
		addNormalPool(new IndicatorPool(ida), Indicator.OBJECT_NAME);
		addNormalPool(new ObjectivePool(ida), Objective.OBJECT_NAME);
		addNormalPool(new GoalPool(ida), Goal.OBJECT_NAME);
		addNormalPool(new ProjectMetadataPool(ida), ProjectMetadata.OBJECT_NAME);
		addNormalPool(new DiagramLinkPool(ida), DiagramLink.OBJECT_NAME);
		addNormalPool(new AssignmentPool(ida), ResourceAssignment.OBJECT_NAME);
		addNormalPool(new AccountingCodePool(ida), AccountingCode.OBJECT_NAME);
		addNormalPool(new FundingSourcePool(ida), FundingSource.OBJECT_NAME);
		addNormalPool(new KeyEcologicalAttributePool(ida), KeyEcologicalAttribute.OBJECT_NAME);
		addNormalPool(new DiagramFactorPool(ida), DiagramFactor.OBJECT_NAME);
		addNormalPool(new CausePool(ida), Cause.OBJECT_NAME);
		addNormalPool(new StrategyPool(ida), Strategy.OBJECT_NAME);
		addNormalPool(new TargetPool(ida), Target.OBJECT_NAME);
		addNormalPool(new IntermediateResultPool(ida), IntermediateResult.OBJECT_NAME);
		addNormalPool(new ResultsChainDiagramPool(ida), ResultsChainDiagram.OBJECT_NAME);
		addNormalPool(new ConceptualModelDiagramPool(ida), ConceptualModelDiagram.OBJECT_NAME);
		addNormalPool(new ThreatReductionResultPool(ida), ThreatReductionResult.OBJECT_NAME);
		addNormalPool(new TextBoxPool(ida), TextBox.OBJECT_NAME);
		addNormalPool(new ObjectTreeTableConfigurationPool(ida), ObjectTreeTableConfiguration.OBJECT_NAME);
		addNormalPool(new WwfProjectDataPool(ida), WwfProjectData.OBJECT_NAME);
		addNormalPool(new CostAllocationRulePool(ida), CostAllocationRule.OBJECT_NAME);
		addNormalPool(new MeasurementPool(ida), Measurement.OBJECT_NAME);
		addNormalPool(new StressPool(ida), Stress.OBJECT_NAME);
		addNormalPool(new ThreatStressRatingPool(ida), ThreatStressRating.OBJECT_NAME);
		addNormalPool(new GroupBoxPool(ida), GroupBox.OBJECT_NAME);
		addNormalPool(new SubTargetPool(ida), SubTarget.OBJECT_NAME);
		addNormalPool(new ProgressReportPool(ida), ProgressReport.OBJECT_NAME);
		addNormalPool(new RareProjectDataPool(ida), RareProjectData.OBJECT_NAME);
		addNormalPool(new WcsProjectDataPool(ida), WcsProjectData.OBJECT_NAME);
		addNormalPool(new TncProjectDataPool(ida), TncProjectData.OBJECT_NAME);
		addNormalPool(new FosProjectDataPool(ida), FosProjectData.OBJECT_NAME);
		addNormalPool(new OrganizationPool(ida), Organization.OBJECT_NAME);
		addNormalPool(new WcpaProjectDataPool(ida), WcpaProjectData.OBJECT_NAME);
		addNormalPool(new XenodataPool(ida), Xenodata.OBJECT_NAME);
		addNormalPool(new ProgressPercentPool(ida), ProgressPercent.OBJECT_NAME);
		addNormalPool(new ReportTemplatePool(ida), ReportTemplate.OBJECT_NAME);
		addNormalPool(new TaggedObjectSetPool(ida), TaggedObjectSet.OBJECT_NAME);
		addNormalPool(new TableSettingsPool(ida), TableSettings.OBJECT_NAME);
		addNormalPool(new ThreatRatingCommentsDataPool(ida), ThreatRatingCommentsData.OBJECT_NAME);
		addNormalPool(new ScopeBoxPool(ida), ScopeBox.OBJECT_NAME);
		addNormalPool(new ExpensePool(ida), ExpenseAssignment.OBJECT_NAME);
		addNormalPool(new HumanWelfareTargetPool(ida), HumanWelfareTarget.OBJECT_NAME);
		addNormalPool(new IucnRedlistSpeciesPool(ida), IucnRedlistSpecies.OBJECT_NAME);
		addNormalPool(new OtherNotableSpeciesPool(ida), OtherNotableSpecies.OBJECT_NAME);
		addNormalPool(new AudiencePool(ida), Audience.OBJECT_NAME);
		addNormalPool(new BudgetCategoryOnePool(ida), BudgetCategoryOne.OBJECT_NAME);
		addNormalPool(new BudgetCategoryTwoPool(ida), BudgetCategoryTwo.OBJECT_NAME);
		addNormalPool(new DashboardPool(ida), Dashboard.OBJECT_NAME);
	}
	
	public ChainWalker getDiagramChainWalker()
	{
		return diagramChainWalker;
	}

	private void addNormalPool(PoolWithIdAssigner pool, String objectName)
	{
		BaseObjectInformation baseObjectInformation = new BaseObjectInformation(pool, objectName);
		pools.put(new Integer(pool.getObjectType()), baseObjectInformation);
	}

	private IdAssigner getNormalIdAssigner()
	{
		return getProject().getNormalIdAssigner();
	}

	public EAMObjectPool getPool(int objectType)
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

	
	public FundingSourcePool getFundingSourcePool()
	{
		return (FundingSourcePool)getPool(ObjectType.FUNDING_SOURCE);
	}

	public SubTargetPool getSubTargetPool()
	{
		return (SubTargetPool) getPool(SubTarget.getObjectType());
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
		return (AssignmentPool)getPool(ObjectType.RESOURCE_ASSIGNMENT);
	}
	
	public ExpensePool getExpenseAssignmentPool()
	{
		return (ExpensePool)getPool(ExpenseAssignment.getObjectType());
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
		return (ObjectTreeTableConfigurationPool) getPool(ObjectTreeTableConfiguration.getObjectType());
	}

	public BaseId createObject(int objectType, BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId createdId = BaseId.INVALID;
		switch(objectType)
		{
			case ObjectType.FACTOR_LINK:
			{
				CreateFactorLinkParameter parameter = (CreateFactorLinkParameter)extraInfo;
				FactorLinkId realId = getProject().obtainRealLinkageId(objectId);
				FactorLink cmLinkage = new FactorLink(this, realId, parameter.getFromRef(), parameter.getToRef());
				getDatabase().writeObject(cmLinkage);
				EAMObjectPool pool = getPool(objectType);
				pool.put(realId, cmLinkage);
				getDatabase().writeObjectManifest(getFileName(), objectType, createManifest(pool));
				createdId = cmLinkage.getId();
				break;
			}
			default:
			{
				EAMNormalObjectPool pool = (EAMNormalObjectPool)getPool(objectType);
				if(pool == null)
					throw new RuntimeException("No pool for " + objectType);
				BaseObject created = pool.createObject(this, objectId, extraInfo);
				getDatabase().writeObject(created);
				getDatabase().writeObjectManifest(getFileName(), objectType, createManifest(pool));
				createdId = created.getId();
				break;
			}

		}

		return createdId;
	}

	private ObjectManifest createManifest(EAMObjectPool pool)
	{
		return new ObjectManifest(pool);
	}

	public void deleteObject(BaseObject object) throws Exception
	{
		ORef objectRef = object.getRef();
		removeFromReferrerCache(objectRef);
		int objectType = object.getType();
		BaseId objectId = object.getId();
		EAMObjectPool pool = getPool(objectType);
		if(pool.findObject(objectId) == null)
			throw new RuntimeException("Attempted to delete missing object: " + objectType + ":" + objectId);
		pool.remove(objectId);
		getDatabase().deleteObject(objectType, objectId);
		getDatabase().writeObjectManifest(getFileName(), objectType, createManifest(pool));
	}

	public void setObjectData(ORef objectRef, String fieldTag, String dataValue) throws Exception
	{
		BaseObject object = findObject(objectRef);
		object.setData(fieldTag, dataValue);
		getDatabase().writeObject(object);
	}

	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		setObjectData(new ORef(objectType, objectId), fieldTag, dataValue);
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

	//NOTE: Can't just iterate through all the pools because 
	// they have to be loaded in a specific sequence
	public void loadFromDatabase() throws Exception
	{
		Map<Integer, String> manifests = getDatabase().readAllManifestFiles();
		
		int[] types = {
			ObjectType.CAUSE,
			ObjectType.STRATEGY,
			ObjectType.TARGET,
			ObjectType.FACTOR_LINK,
			ObjectType.TASK,
			ObjectType.VIEW_DATA,
			ObjectType.PROJECT_RESOURCE,
			ObjectType.INDICATOR,
			ObjectType.OBJECTIVE,
			ObjectType.GOAL,
			ObjectType.RATING_CRITERION,
			ObjectType.VALUE_OPTION,
			ObjectType.PROJECT_METADATA,
			ObjectType.DIAGRAM_LINK,
			ObjectType.RESOURCE_ASSIGNMENT,
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
			ObjectType.THREAT_RATING_COMMENTS_DATA,
			ObjectType.SCOPE_BOX,
			ObjectType.EXPENSE_ASSIGNMENT,
			ObjectType.HUMAN_WELFARE_TARGET,
			ObjectType.IUCN_REDLIST_SPECIES,
			ObjectType.OTHER_NOTABLE_SPECIES,
			ObjectType.AUDIENCE,
			ObjectType.BUDGET_CATEGORY_ONE,
			ObjectType.BUDGET_CATEGORY_TWO,
			ObjectType.DASHBOARD,
		};
		for(int type : types)
			loadPool(type, extractManifest(manifests, type));
	}

	private ObjectManifest extractManifest(Map<Integer, String> manifests, int type) throws Exception
	{
		String manifestString = manifests.get(type);
		if(manifestString == null)
			return new ObjectManifest();
		
		return new ObjectManifest(new EnhancedJsonObject(manifestString));
	}

	private void loadPool(int type, ObjectManifest manifest) throws IOException, ParseException, Exception
	{
		BaseId[] ids = manifest.getAllKeys();
		HashMap<Integer, BaseObject> map = getDatabase().readObjects(this, type, ids);
		for(int id : map.keySet())
		{
			if(id == BaseId.INVALID.asInt())
			{
				EAM.logWarning("loadPool skipping invalid id in " + type);
				continue;
			}
			getPool(type).put(map.get(id));
		}
	}

	public Project getProject()
	{
		return project;
	}

	SimpleThreatRatingFramework getThreatRatingFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}

	ProjectServer getDatabase()
	{
		return getProject().getDatabase();
	}

	//TODO: there shold be a better way to get to the project file name then having to expose it here
	public String getFileName()
	{
		return getProject().getFilename();
	}
	
	public BaseObject[] findObjects(ORefList refList)
	{
		return findObjectsAsVector(refList).toArray(new BaseObject[0]);
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
		private BaseObjectInformation(PoolWithIdAssigner poolToUse, String objectNameToUse)
		{
			pool = poolToUse;
			objectName = objectNameToUse;
		}
		
		private PoolWithIdAssigner getPool()
		{
			return pool;
		}
		
		private String getObjectName()
		{
			return objectName;
		}
		
		private PoolWithIdAssigner pool;
		private String objectName;
	}
	
	private Project project;
	private ChainWalker diagramChainWalker;
	private HashMap<Integer, BaseObjectInformation> pools;
	private HashMap<ORef, ORefSet> referrerCache;
}
