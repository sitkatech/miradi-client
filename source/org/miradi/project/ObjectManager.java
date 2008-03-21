/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.util.Iterator;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.database.ObjectManifest;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.DiagramChainObject;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
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
import org.miradi.objectpools.CausePool;
import org.miradi.objectpools.ConceptualModelDiagramPool;
import org.miradi.objectpools.CostAllocationRulePool;
import org.miradi.objectpools.DiagramFactorLinkPool;
import org.miradi.objectpools.DiagramFactorPool;
import org.miradi.objectpools.EAMNormalObjectPool;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objectpools.FactorLinkPool;
import org.miradi.objectpools.FosProjectDataPool;
import org.miradi.objectpools.FundingSourcePool;
import org.miradi.objectpools.GoalPool;
import org.miradi.objectpools.GroupBoxPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objectpools.IntermediateResultPool;
import org.miradi.objectpools.KeyEcologicalAttributePool;
import org.miradi.objectpools.MeasurementPool;
import org.miradi.objectpools.ObjectivePool;
import org.miradi.objectpools.OrganizationPool;
import org.miradi.objectpools.PlanningViewConfigurationPool;
import org.miradi.objectpools.ProgressReportPool;
import org.miradi.objectpools.ProjectMetadataPool;
import org.miradi.objectpools.RareProjectDataPool;
import org.miradi.objectpools.RatingCriterionPool;
import org.miradi.objectpools.ResourcePool;
import org.miradi.objectpools.ResultsChainDiagramPool;
import org.miradi.objectpools.SlidePool;
import org.miradi.objectpools.SlideShowPool;
import org.miradi.objectpools.StrategyPool;
import org.miradi.objectpools.StressPool;
import org.miradi.objectpools.SubTargetPool;
import org.miradi.objectpools.TargetPool;
import org.miradi.objectpools.TaskPool;
import org.miradi.objectpools.TextBoxPool;
import org.miradi.objectpools.ThreatReductionResultPool;
import org.miradi.objectpools.ThreatStressRatingPool;
import org.miradi.objectpools.TncProjectDataPool;
import org.miradi.objectpools.ValueOptionPool;
import org.miradi.objectpools.ViewPool;
import org.miradi.objectpools.WcpaProjectDataPool;
import org.miradi.objectpools.WcsProjectDataPool;
import org.miradi.objectpools.WwfProjectDataPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.Strategy;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;

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
		addNormalPool(new OrganizationPool(ida));
		addNormalPool(new WcpaProjectDataPool(ida));
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
		loadPool(ObjectType.ORGANIZATION);
		loadPool(ObjectType.WCPA_PROJECT_DATA);
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
