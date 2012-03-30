/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import java.text.ParseException;

import org.miradi.diagram.ChainWalker;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TargetSet;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.utils.CommandVector;
import org.miradi.utils.EnhancedJsonObject;

abstract public class Factor extends BaseObject
{
	public Factor(ObjectManager objectManager, FactorId idToUse, BaseObjectSchema schemaToUse)
	{
		super(objectManager, idToUse, schemaToUse);
	}

	public Factor(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json, BaseObjectSchema schemaToUse) throws Exception
	{
		super(objectManager, idToUse, json, schemaToUse);
	}

	public FactorId getFactorId()
	{
		return new FactorId(getId().asInt());
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.INDICATOR: 
				list.addAll(new ORefList(objectType, getOnlyDirectIndicatorIds()));
				break;
		}
		return list;
	}
	
	public ORefSet getDirectOrIndirectIndicatorRefSet()
	{
		return new ORefSet(getDirectOrIndirectIndicatorRefs());
	}
	
	public ORefList getDirectOrIndirectIndicatorRefs()
	{
		return new ORefList(IndicatorSchema.getObjectType(), getDirectOrIndirectIndicators()); 
	}
	
	public IdList getDirectOrIndirectIndicators()
	{
		return getOnlyDirectIndicatorIds();
	}
	
	public String getComment()
	{
		return getStringData(TAG_COMMENTS);
	}
	
	public void setComment(String newComments) throws Exception
	{
		setData(TAG_COMMENTS, newComments);
	}
	
	public String getDetails()
	{
		return getStringData(TAG_TEXT);
	}
	
	public boolean isStatusDraft()
	{
		return false;
	}

	public IdList getOnlyDirectIndicatorIds()
	{
		IdList rawDirectIndicatorIds = getIdListData(TAG_INDICATOR_IDS);
		ORefList activeIndicatorRefs = new ORefList();
		for(int index = 0; index < rawDirectIndicatorIds.size(); ++index)
		{
			ORef indicatorRef = new ORef(IndicatorSchema.getObjectType(), rawDirectIndicatorIds.get(index));
			Indicator indicator = Indicator.find(getObjectManager(), indicatorRef);
			if(indicator.isActive())
				activeIndicatorRefs.add(indicatorRef);
		}
		return activeIndicatorRefs.convertToIdList(IndicatorSchema.getObjectType());
	}
	
	public ORefList getActiveAndInactiveDirectIndicatorRefs() throws Exception
	{
		return getRefList(TAG_INDICATOR_IDS);
	}
	
	public ORefList getOnlyDirectIndicatorRefs()
	{
		return new ORefList(IndicatorSchema.getObjectType(), getOnlyDirectIndicatorIds());
	}
	
	public ORefList getObjectiveRefs()
	{
		return new ORefList(ObjectiveSchema.getObjectType(), getObjectiveIds());
	}
	
	public IdList getObjectiveIds()
	{
		return getIdListData(TAG_OBJECTIVE_IDS);
	}

	@Override
	public String getShortLabel()
	{
		return getStringData(TAG_SHORT_LABEL);
	}
	
	public ORefList getGoalRefs()
	{
		return new ORefList();
	}
	
	public static void ensureFactor(ORef factorRef)
	{
		if (!isFactor(factorRef))
			throw new RuntimeException(factorRef + " is not a factor ref");
	}
	
	public static boolean isFactor(BaseObject baseObject)
	{
		return isFactor(baseObject.getType());
	}
	
	public static boolean isFactor(ORef refToUse)
	{
		return isFactor(refToUse.getObjectType());
	}
	
	public static boolean isFactor(int typeToUse)
	{
		if (typeToUse == ObjectType.CAUSE)
			return true;
		
		if (typeToUse == ObjectType.TARGET)
			return true;
		
		if (typeToUse == ObjectType.STRATEGY)
			return true;
		
		if (typeToUse == ObjectType.INTERMEDIATE_RESULT)
			return true;
		
		if (typeToUse == ObjectType.THREAT_REDUCTION_RESULT)
			return true;
		
		if (typeToUse == ObjectType.FACTOR)
			return true;
	
		if (typeToUse == ObjectType.TEXT_BOX)
			return true;
		
		if (typeToUse == ObjectType.GROUP_BOX)
			return true;
		
		if (typeToUse == ObjectType.STRESS)
			return true;
		
		if (typeToUse == ObjectType.TASK)
			return true;

		if (typeToUse == ObjectType.SCOPE_BOX)
			return true;
		
		if (HumanWelfareTarget.is(typeToUse))
			return true;
		
		return false;
	}

	public boolean isGroupBox()
	{
		return false;
	}
	
	public boolean isTextBox()
	{
		return false;
	}
	
	public boolean isScopeBox()
	{
		return false;
	}
	
	public boolean isThreatReductionResult()
	{
		return false;
	}
	
	public boolean isIntermediateResult()
	{
		return false;
	}
	
	public boolean isStrategy()
	{
		return false;
	}
	
	public boolean isCause()
	{
		return false;
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isHumanWelfareTarget()
	{
		return false;
	}
	
	public boolean isContributingFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isStress()
	{
		return false;
	}
	
	public boolean isActivity()
	{
		return false;
	}
	
	public boolean canHaveType(final int type)
	{
		if (Goal.is(type))
			return canHaveGoal();
		
		if (Objective.is(type))
			return canHaveObjectives();
		
		if (Indicator.is(type))
			return canHaveIndicators();
		
		return false;
	}
	
	public boolean canHaveIndicators()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return false;
	}

	public boolean canHaveGoal()
	{
		return false;
	}

	public boolean canHaveKeyEcologicalAttribures()
	{
		return false;
	}
	
	public boolean mustBeDeletedBecauseParentIsGone()
	{
		ORefList referrers = findObjectsThatReferToUs(getObjectManager(), ObjectType.DIAGRAM_FACTOR, getRef());
		
		return referrers.size() > 0;
	}
	
	@Override
	public String toString()
	{
		return getLabel();
	}
	
	
	public static Factor createConceptualModelObject(ObjectManager objectManager, FactorId idToCreate, int objectType)
	{
		return createFactor(objectManager, idToCreate, objectType);
	}

	public static Factor createFactor(ObjectManager objectManager, FactorId idToCreate, int objectType)
	{
		if(objectType == ObjectType.STRATEGY)
			return new Strategy(objectManager, idToCreate);

		else if(objectType == ObjectType.CAUSE)
			return new Cause(objectManager, idToCreate);
		
		else if(objectType == ObjectType.TARGET)
			return new Target(objectManager, idToCreate);
	
		else if (objectType == ObjectType.INTERMEDIATE_RESULT)
			return new IntermediateResult(objectManager, idToCreate);
		
		else if (objectType == ObjectType.THREAT_REDUCTION_RESULT)
			return new ThreatReductionResult(objectManager, idToCreate);
		
		else if (objectType == ObjectType.TEXT_BOX)
			return new TextBox(objectManager, idToCreate);
	
		else if (objectType == ObjectType.GROUP_BOX)
			return new GroupBox(objectManager, idToCreate);
		
		else if (objectType == ObjectType.STRESS)
			return new Stress(objectManager, idToCreate);
		
		else if (objectType == ObjectType.TASK)
			return new Task(objectManager, idToCreate);
		
		else if (objectType == ObjectType.SCOPE_BOX)
			return new ScopeBox(objectManager, idToCreate);
		
		else if (objectType == ObjectType.HUMAN_WELFARE_TARGET)
			return new HumanWelfareTarget(objectManager, idToCreate);
		
		throw new RuntimeException("Tried to create unknown node type: " + objectType);
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return IndicatorSchema.getObjectType();
		
		if (tag.equals(TAG_OBJECTIVE_IDS))
			return ObjectiveSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return true;
		
		if (tag.equals(TAG_OBJECTIVE_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		try
		{
			if(fieldTag.equals(PSEUDO_TAG_OBJECTIVES))
				return getFactorObjectivesAsMultiline();
			
			if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
				return getFactorRelatedDirectThreats();
			
			if(fieldTag.equals(PSEUDO_TAG_TARGETS))
				return getFactorRelatedTargets();
			
			if(fieldTag.equals(PSEUDO_TAG_INDICATORS))
				return getBaseObjectLabelsOnASingleLine(getDirectOrIndirectIndicatorRefs());
			
			if(fieldTag.equals(PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS))
				return DiagramObject.getDiagramRefsContainingFactor(getProject(), ConceptualModelDiagramSchema.getObjectType(), getRef()).toString();
			
			if(fieldTag.equals(PSEUDO_TAG_RESULTS_CHAIN_REFS))
				return DiagramObject.getDiagramRefsContainingFactor(getProject(), ResultsChainDiagramSchema.getObjectType(), getRef()).toString();
			
			if(fieldTag.equals(PSEUDO_TAG_REFERRING_TAG_REFS))
				return getReferringTags();
			
			return super.getPseudoData(fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	public String getReferringTags()
	{
		return findReferringTagRefs().toString();
	}
	
	public ORefList findReferringTagRefs()
	{
		return findObjectsThatReferToUs(TaggedObjectSet.getObjectType());
	}
	
	protected CommandVector buildRemoveFromRelevancyListCommands(ORef relevantObjectRefToRemove) throws Exception
	{
		CommandVector removeFromRelevancyListCommands = new CommandVector();
		removeFromRelevancyListCommands.addAll(Desire.buildRemoveObjectFromRelevancyListCommands(getProject(), ObjectiveSchema.getObjectType(), Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantObjectRefToRemove));
		removeFromRelevancyListCommands.addAll(Desire.buildRemoveObjectFromRelevancyListCommands(getProject(), GoalSchema.getObjectType(), Goal.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantObjectRefToRemove));
		
		return removeFromRelevancyListCommands;
	}

	private String getFactorRelatedDirectThreats()
	{
		ChainWalker chain = getNonDiagramChainWalker();
		FactorSet factors = chain.buildNormalChainAndGetFactors(this);
		DirectThreatSet directThreats = new DirectThreatSet(factors);
		
		return getLabelsAsMultiline(directThreats);
	}

	private String getFactorRelatedTargets()
	{
		ChainWalker chain = getNonDiagramChainWalker();
		FactorSet factors = chain.buildNormalChainAndGetFactors(this);
		TargetSet directThreats = new TargetSet(factors);
		
		return getLabelsAsMultiline(directThreats);
	}
	
	private String getFactorObjectivesAsMultiline() throws ParseException
	{
		IdList theseDesireIds = new IdList(ObjectiveSchema.getObjectType(), getData(TAG_OBJECTIVE_IDS));
		return getDesiresAsMultiline(ObjectType.OBJECTIVE, theseDesireIds);
	}
	
	protected String getDesiresAsMultiline(int desireType, IdList desireIds)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < desireIds.size(); ++i)
		{
			if(result.length() > 0)
				result.append("\n");
			
			result.append(objectManager.getObjectData(desireType, desireIds.get(i), Desire.TAG_LABEL));
		}
		
		return result.toString();
	}
	
	public static Factor findFactor(ObjectManager objectManager, ORef factorRef)
	{
		return (Factor) objectManager.findObject(factorRef);
	}
	
	public static Factor findFactor(Project project, ORef factorRef)
	{
		return findFactor(project.getObjectManager(), factorRef);
	}
	
	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_TEXT = "Text";
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	public static final String PSEUDO_TAG_OBJECTIVES = "PseudoTagObjectives";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public static final String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public static final String PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS = "PseudoTagDiagramRefs";
	public static final String PSEUDO_TAG_RESULTS_CHAIN_REFS = "PseudoTagResultsChainDiagramRefs";
	public static final String PSEUDO_TAG_REFERRING_TAG_REFS = "PseudoTagReferringTagRefs";
	public static final String PSEUDO_TAG_TAXONOMY_CODE_VALUE = "TaxonomyCodeValue";
	public static final String PSEUDO_TAG_INDICATORS = "PseudoTagIndicators";
}
