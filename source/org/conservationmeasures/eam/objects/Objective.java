/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverride;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSet;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSetData;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()));
	}
	
	
	public Objective(BaseId id)
	{
		super(new ObjectiveId(id.asInt()));
	}
	
	public Objective(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ObjectiveId(idAsInt), json);
	}
	
	public Objective(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new ObjectiveId(idAsInt), json);
	}
	
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS))
			return getIndicatorsOnSameFactor().toString();
	
		return super.getPseudoData(fieldTag);
	}
	
	public ORefList getUpstreamNonDraftStrategies(DiagramObject diagram)
	{
		ORefList nonDraftStrategyRefs = new ORefList();
		Factor[] upstreamFactors = getUpstreamFactors(diagram);
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			if(factor.isStrategy() && !factor.isStatusDraft())
				nonDraftStrategyRefs.add(factor.getRef());
		}

		return nonDraftStrategyRefs;
	}
	
	public String getText(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getIndicatorsOnSameFactor();
		relevantOverrides.addAll(getRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(getRelevancyOverrides(defaultRelevantRefList, all , false));	
	
		return relevantOverrides.toString();
	}

	private RelevancyOverrideSet getRelevancyOverrides(ORefList refList1, ORefList refList2, boolean relevancyValue)
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList overrideRefs = ORefList.subtract(refList1, refList2);
		for (int i = 0; i < overrideRefs.size(); ++i)
		{
			RelevancyOverride thisOverride = new RelevancyOverride(overrideRefs.get(i), relevancyValue);
			relevantOverrides.add(thisOverride);
		}
		
		return relevantOverrides;
	}
	
	public ORefList getRelevantRefList() throws Exception
	{
		ORefList relevantRefList = new ORefList(getProject().getObjectData(getRef(), Objective.PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet(getProject().getObjectData(getRef(), Objective.TAG_RELEVANT_INDICATOR_SET));
		for(RelevancyOverride override : relevantOverrides)
		{
			if (override.isOverride())
				relevantRefList.add(override.getRef());
			else
				relevantRefList.remove(override.getRef());
		}
		return relevantRefList;
	}
		
	public ORefList getRelevantIndicatorRefs()
	{
		return getIndicatorsOnSameFactor();
	}
	
	public static Objective find(ObjectManager objectManager, ORef objectiveRef)
	{
		return (Objective) objectManager.findObject(objectiveRef);
	}
	
	public static Objective find(Project project, ORef objectiveRef)
	{
		return find(project.getObjectManager(), objectiveRef);
	}
	
	public void clear()
	{
		super.clear();
		relevantIndicators = new RelevancyOverrideSetData();
		defaultRelevantIndicatorRefs = new PseudoORefListData(PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS);
		
		addField(TAG_RELEVANT_INDICATOR_SET, relevantIndicators);
		addField(PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS, defaultRelevantIndicatorRefs);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS = "PseudoDefaultRelevantIndicatorRefs";
	
	private RelevancyOverrideSetData relevantIndicators;
	private PseudoORefListData defaultRelevantIndicatorRefs;
}
