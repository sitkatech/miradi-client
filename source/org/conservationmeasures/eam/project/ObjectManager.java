/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.database.ObjectManifest;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramChainObject;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ORefSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.AccountingCodePool;
import org.conservationmeasures.eam.objectpools.AssignmentPool;
import org.conservationmeasures.eam.objectpools.CausePool;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objectpools.CostAllocationRulePool;
import org.conservationmeasures.eam.objectpools.DiagramFactorLinkPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorPool;
import org.conservationmeasures.eam.objectpools.EAMNormalObjectPool;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.FosProjectDataPool;
import org.conservationmeasures.eam.objectpools.FundingSourcePool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.GroupBoxPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.IntermediateResultPool;
import org.conservationmeasures.eam.objectpools.KeyEcologicalAttributePool;
import org.conservationmeasures.eam.objectpools.MeasurementPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.PlanningViewConfigurationPool;
import org.conservationmeasures.eam.objectpools.ProgressReportPool;
import org.conservationmeasures.eam.objectpools.ProjectMetadataPool;
import org.conservationmeasures.eam.objectpools.RareProjectDataPool;
import org.conservationmeasures.eam.objectpools.RatingCriterionPool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.ResultsChainDiagramPool;
import org.conservationmeasures.eam.objectpools.SlidePool;
import org.conservationmeasures.eam.objectpools.SlideShowPool;
import org.conservationmeasures.eam.objectpools.StrategyPool;
import org.conservationmeasures.eam.objectpools.StressPool;
import org.conservationmeasures.eam.objectpools.SubTargetPool;
import org.conservationmeasures.eam.objectpools.TargetPool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.TextBoxPool;
import org.conservationmeasures.eam.objectpools.ThreatReductionResultPool;
import org.conservationmeasures.eam.objectpools.ThreatStressRatingPool;
import org.conservationmeasures.eam.objectpools.TncProjectDataPool;
import org.conservationmeasures.eam.objectpools.ValueOptionPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objectpools.WcsProjectDataPool;
import org.conservationmeasures.eam.objectpools.WwfProjectDataPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.martus.util.UnicodeWriter;

public class ObjectManager
{
	public ObjectManager(Project projectToUse)
	{
		project = projectToUse;
		projectChainBuilder = new ProjectChainObject();
		diagramChainBuilder = new DiagramChainObject();
		referrerCache = new HashMap<ORef, ORefSet>();

		pools = new HashMap();
		IdAssigner factorAndLinkIdAssigner = project.getNodeIdAssigner();
		pools.put(new Integer(ObjectType.FACTOR_LINK), new FactorLinkPool(factorAndLinkIdAssigner));

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
		addNormalPool(new CausePool(ida));
		addNormalPool(new StrategyPool(ida));
		addNormalPool(new TargetPool(ida));
		addNormalPool(new IntermediateResultPool(ida));
		addNormalPool(new ResultsChainDiagramPool(ida));
		addNormalPool(new ConceptualModelDiagramPool(ida));
		addNormalPool(new ThreatReductionResultPool(ida));
		addNormalPool(new TextBoxPool(ida));
		addNormalPool(new SlidePool(ida));
		addNormalPool(new SlideShowPool(ida));
		addNormalPool(new PlanningViewConfigurationPool(ida));
		addNormalPool(new WwfProjectDataPool(ida));
		addNormalPool(new CostAllocationRulePool(ida));
		addNormalPool(new MeasurementPool(ida));
		addNormalPool(new StressPool(ida));
		addNormalPool(new ThreatStressRatingPool(ida));
		addNormalPool(new GroupBoxPool(ida));
		addNormalPool(new SubTargetPool(ida));
		addNormalPool(new ProgressReportPool(ida));
		addNormalPool(new RareProjectDataPool(ida));
		addNormalPool(new WcsProjectDataPool(ida));
		addNormalPool(new TncProjectDataPool(ida));
		addNormalPool(new FosProjectDataPool(ida));
	}
	
	public ProjectChainObject getProjectChainBuilder()
	{
		return projectChainBuilder;
	}
	
	public DiagramChainObject getDiagramChainBuilder()
	{
		return diagramChainBuilder;
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
	
	public ConceptualModelDiagramPool getConceptualModelDiagramPool()
	{
		return (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
	
	public ResultsChainDiagramPool getResultsChainDiagramPool()
	{
		return (ResultsChainDiagramPool) getPool(ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public SlidePool getSlidePool()
	{
		return (SlidePool)getPool(ObjectType.SLIDE);
	}
	
	public SlideShowPool getSlideShowPool()
	{
		return (SlideShowPool)getPool(ObjectType.SLIDESHOW);
	}
	
	public PlanningViewConfigurationPool getPlanningConfigurationPool()
	{
		return (PlanningViewConfigurationPool) getPool(PlanningViewConfiguration.getObjectType());
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
				createdId = cmLinkage.getId();
				getProjectChainBuilder().clearCaches();
				break;
			}
			default:
			{
				EAMNormalObjectPool pool = (EAMNormalObjectPool)getPool(objectType);
				if(pool == null)
					throw new RuntimeException("No pool for " + objectType);
				BaseObject created = pool.createObject(this, objectId, extraInfo);
				getDatabase().writeObject(created);
				createdId = created.getId();
				break;
			}

		}

		return createdId;
	}

	public void deleteObject(BaseObject object) throws IOException, ParseException
	{
		removeFromReferrerCache(object.getRef());
		int objectType = object.getType();
		BaseId objectId = object.getId();
		EAMObjectPool pool = getPool(objectType);
		if(pool.findObject(objectId) == null)
			throw new RuntimeException("Attempted to delete missing object: " + objectType + ":" + objectId);
		pool.remove(objectId);
		getDatabase().deleteObject(objectType, objectId);
		if(objectType == FactorLink.getObjectType())
			getProjectChainBuilder().clearCaches();
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
		if (Factor.isFactor(objectType))
		{
			BaseObject cause = getPool(Cause.getObjectType()).findObject(objectId);
			if (cause != null)
				return cause;
			
			BaseObject strategy = getPool(Strategy.getObjectType()).findObject(objectId);
			if (strategy != null)
				return strategy;

			BaseObject target = getPool(Target.getObjectType()).findObject(objectId);
			if (target != null)
				return target;
			
			BaseObject threatReduction = getPool(ThreatReductionResult.getObjectType()).findObject(objectId);
			if (threatReduction != null)
				return threatReduction;
			
			BaseObject intermediateResult = getPool(IntermediateResult.getObjectType()).findObject(objectId);
			if (intermediateResult != null)
				return intermediateResult;
			
			BaseObject textBox = getPool(TextBox.getObjectType()).findObject(objectId);
			if (textBox != null)
				return textBox;

			BaseObject groupBox = getPool(GroupBox.getObjectType()).findObject(objectId);
			if (groupBox != null)
				return groupBox;
			
			return null;
		}
		
		EAMObjectPool pool = getPool(objectType);
		if(pool == null)
			throw new RuntimeException("Attempted to find object of unknown type: " + objectType);
		return pool.findObject(objectId);
	}
	
	public Factor findNode(FactorId id)
	{
		return (Factor)findObject(new ORef(ObjectType.FACTOR, id));
	}
	
	public Factor findFactor(ORef ref)
	{
		return (Factor) findObject(ref);
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
		loadPool(ObjectType.CAUSE);
		loadPool(ObjectType.STRATEGY);
		loadPool(ObjectType.TARGET);
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
		loadPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		loadPool(ObjectType.RESULTS_CHAIN_DIAGRAM);
		loadPool(ObjectType.INTERMEDIATE_RESULT);
		loadPool(ObjectType.THREAT_REDUCTION_RESULT);
		loadPool(ObjectType.TEXT_BOX);
		loadPool(ObjectType.SLIDE);
		loadPool(ObjectType.SLIDESHOW);
		loadPool(ObjectType.PLANNING_VIEW_CONFIGURATION);
		loadPool(ObjectType.WWF_PROJECT_DATA);
		loadPool(ObjectType.COST_ALLOCATION_RULE);
		loadPool(ObjectType.MEASUREMENT);
		loadPool(ObjectType.STRESS);
		loadPool(ObjectType.THREAT_STRESS_RATING);
		loadPool(ObjectType.GROUP_BOX);
		loadPool(ObjectType.SUB_TARGET);
		loadPool(ObjectType.PROGRESS_REPORT);
		loadPool(ObjectType.RARE_PROJECT_DATA);
		loadPool(ObjectType.WCS_PROJECT_DATA);
		loadPool(ObjectType.TNC_PROJECT_DATA);
		loadPool(ObjectType.FOS_PROJECT_DATA);
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
			if (object.getType() != type)
				continue;
			
			getPool(type).put(object.getId(), object);
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
		return (BaseObject[])findObjectsAsVector(refList).toArray(new BaseObject[0]);
	}
	
	public Vector findObjectsAsVector(ORefList refList)
	{
		Vector foundObjects = new Vector();
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

	public void updateReferrerCache(ORef referrerRef, ORefList oldReferrals, ORefList newReferrals)
	{
		ORefList removedReferrals = ORefList.subtract(oldReferrals, newReferrals);
		for(int i = 0; i < removedReferrals.size(); ++i)
		{
			ORef referredRef = removedReferrals.get(i);
			getReferrerRefsSet(referredRef).remove(referrerRef);
		}

		ORefList addedReferrals = ORefList.subtract(newReferrals, oldReferrals);
		for(int i = 0; i < addedReferrals.size(); ++i)
		{
			ORef referredRef = addedReferrals.get(i);
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
		ORefList referencedObjectRefs = object.getAllReferncedObjects();
		for(int i = 0; i < referencedObjectRefs.size(); ++i)
		{
			ORef referencedRef = referencedObjectRefs.get(i);
			getReferrerRefsSet(referencedRef).remove(refToRemove);
		}
		referrerCache.remove(refToRemove);
	}

	public void toXml(UnicodeWriter out) throws Exception
	{
		out.writeln("<ObjectPools>");
		Iterator iter = pools.keySet().iterator();
		while(iter.hasNext())
		{
			EAMObjectPool pool = (EAMObjectPool)pools.get(iter.next());
			pool.toXml(out);
		}
		out.writeln("</ObjectPools>");
		
	}
	
	private Project project;
	private ProjectChainObject projectChainBuilder;
	private DiagramChainObject diagramChainBuilder;
	private HashMap pools;
	private HashMap<ORef, ORefSet> referrerCache;
}
